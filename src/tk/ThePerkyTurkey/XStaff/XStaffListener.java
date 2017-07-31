package tk.ThePerkyTurkey.XStaff;

import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import tk.ThePerkyTurkey.XStaff.Tasks.StaffChatTask;
import tk.ThePerkyTurkey.XStaff.Utils.ConfigManager;
import tk.ThePerkyTurkey.XStaff.Utils.ItemInteractManager;
import tk.ThePerkyTurkey.XStaff.Utils.Messages;
import tk.ThePerkyTurkey.XStaff.Utils.PlayerManager;

public class XStaffListener implements Listener {

	private XStaff xs;
	private ConfigManager cm;
	private Messages msg;

	public XStaffListener(XStaff xs) {
		this.xs = xs;
		this.cm = xs.getConfigManager();
		this.msg = xs.getMessages();
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (xs.getStaffInventory().needsRestoring(p)) {
			try {
				xs.getStaffInventory().restoreInventory(p);
			} catch (IOException ex) {
				ex.printStackTrace();
				xs.getLogger().severe("An error occured whilst saving " + p.getName() + "'s inventory");
			}
		}
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (PlayerManager.isStaff(p)) {
			PlayerManager.inStaffMode.remove(p);
			PlayerManager.setVanished(p, false);
		}
		if (PlayerManager.isFrozen(p)) {
			for (Player player : xs.getServer().getOnlinePlayers()) {
				if (player.hasPermission("xstaff.notify")) {
					player.sendMessage(msg.get("frozenLeave", p.getName()));
				}
			}
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (PlayerManager.isStaff(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			if (PlayerManager.isStaff(p)) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getWhoClicked();
		if (PlayerManager.isStaff(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		if (PlayerManager.isStaff(p)) {
			e.setCancelled(true);
		}
		if (PlayerManager.isVanished(p) && !p.hasPermission("xstaff.vanish.pickup")) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (PlayerManager.isStaff(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getItem() == null) {
			return;
		}
		if (PlayerManager.isStaff(p)) {
			if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				e.setCancelled(true);
				new ItemInteractManager(e.getItem(), e.getPlayer(), xs);
			}
		}
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if (PlayerManager.isFrozen(p)) {
			if (!cm.getBoolean("frozen-chat")) {
				e.setCancelled(true);
				p.sendMessage(msg.get("freezeInteract"));
			}
		}
		if(xs.dGUI.awaitingBanReason.containsKey(e.getPlayer())) {
			e.setCancelled(true);
			String banCommand = xs.getConfigManager().getString("ban-command")
					.replaceAll("\\{player\\}", xs.dGUI.awaitingBanReason.get(e.getPlayer()).getName())
					.replaceAll("\\{reason\\}", e.getMessage());
			xs.getServer().getScheduler().runTask(xs, new BukkitRunnable() {

				@Override
				public void run() {
					xs.getServer().dispatchCommand(e.getPlayer(), banCommand);
				}
				
			});
			xs.dGUI.awaitingBanReason.remove(e.getPlayer());
		} else
		
		if(xs.dGUI.awaitingKickReason.containsKey(e.getPlayer())) {
			e.setCancelled(true);
			String kickCommand = xs.getConfigManager().getString("kick-command")
					.replaceAll("\\{player\\}", xs.dGUI.awaitingKickReason.get(e.getPlayer()).getName())
					.replaceAll("\\{reason\\}", e.getMessage());
			xs.getServer().getScheduler().runTask(xs, new BukkitRunnable() {

				@Override
				public void run() {
					xs.getServer().dispatchCommand(e.getPlayer(), kickCommand);
				}
				
			});
			xs.dGUI.awaitingKickReason.remove(p);
		} else
		if (PlayerManager.isInStaffChat(p)) {
			e.setCancelled(true);
			new StaffChatTask(xs, p, e.getMessage());
		}
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent e) {
		if (!(e.getDamager() instanceof Player)) {
			return;
		}

		Player p = (Player) e.getDamager();
		if (PlayerManager.isFrozen(p)) {
			if (!cm.getBoolean("frozen-attack")) {
				e.setCancelled(true);
				p.sendMessage(msg.get("freezeInteract"));
				if (!(e.getEntity() instanceof Player)) {
					return;
				}

				Player player = (Player) e.getEntity();

				player.sendMessage(msg.get("frozenPrevent"));
			}
		}
		if (PlayerManager.isVanished(p) && !p.hasPermission("xstaff.vanish.pvp")) {
			e.setCancelled(true);
			p.sendMessage(msg.get("vanishPvp"));
		}
		if (PlayerManager.isStaff(p) && !p.hasPermission("xstaff.mode.pvp")) {
			e.setCancelled(true);
			p.sendMessage(msg.get("staffPvp"));
		}
	}

	@EventHandler
	public void onCommandPreProcess(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if (PlayerManager.isFrozen(p)) {
			if (!cm.getBoolean("frozen-commands")) {
				e.setCancelled(true);
				p.sendMessage(msg.get("freezeInteract"));
			}
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();

		if (PlayerManager.isFrozen(p)) {
			if (!cm.getBoolean("frozen-damage")) {
				e.setCancelled(true);
				e.setDamage(0);
				p.setFireTicks(0);
				return;
			}
		}

		if (PlayerManager.isStaff(p)) {
			e.setCancelled(true);
			e.setDamage(0);
			p.setFireTicks(0);
			return;
		}

		if (PlayerManager.isVanished(p)) {
			if (p.hasPermission("xstaff.vanish.god")) {
				e.setCancelled(true);
				e.setDamage(0);
				p.setFireTicks(0);
			}
		}
	}

	@EventHandler
	public void onEntityTarget(EntityTargetEvent e) {
		if (!(e.getTarget() instanceof Player)) {
			return;
		}

		Player p = (Player) e.getTarget();

		if (PlayerManager.isStaff(p) || PlayerManager.isVanished(p) || PlayerManager.isFrozen(p)) {
			e.setCancelled(true);
		}
	}
}
