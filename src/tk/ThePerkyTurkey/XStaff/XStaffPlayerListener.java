package tk.ThePerkyTurkey.XStaff;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import tk.ThePerkyTurkey.XStaff.Inventories.StaffInventory;
import tk.ThePerkyTurkey.XStaff.Utils.ConfigManager;
import tk.ThePerkyTurkey.XStaff.Utils.ItemInteractManager;
import tk.ThePerkyTurkey.XStaff.Utils.Messages;
import tk.ThePerkyTurkey.XStaff.Utils.PlayerManager;

public class XStaffPlayerListener implements Listener {
	
	private XStaff xs;
	private ConfigManager cm;
	private Messages msg;
	
	public XStaffPlayerListener(XStaff xs) {
		this.xs = xs;
		this.cm = xs.getConfigManager();
		this.msg = xs.getMessages();
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(xs.getStaffInventory().needsRestoring(p)) {
			xs.getStaffInventory().getFromFile(p);
			StaffInventory.restoreInventory(p);
		}
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(PlayerManager.isStaff(p)) {
			PlayerManager.inStaffMode.remove(p);
			PlayerManager.setVanished(p, false);
			xs.getStaffInventory().writeToFile(p);
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(PlayerManager.isStaff(p)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e) {
		if(e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			if(PlayerManager.isStaff(p)) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getWhoClicked();
		if(PlayerManager.isStaff(p)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		if(PlayerManager.isStaff(p)) {
			e.setCancelled(true);
		}
		if(PlayerManager.isVanished(p) && !p.hasPermission("xstaff.vanish.pickup")) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if(PlayerManager.isStaff(p)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getItem() == null) {
			return;
		}
		if(PlayerManager.isStaff(p)) {
			if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				e.setCancelled(true);
				new ItemInteractManager(e.getItem(), e.getPlayer(), xs);
			}
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if(PlayerManager.isFrozen(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if(PlayerManager.isFrozen(p)) {
			if(!cm.getBoolean("frozen-chat")) {
				e.setCancelled(true);
				p.sendMessage(msg.get("freezeInteract"));
			}
		}
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent e) {
		if(!(e.getDamager() instanceof Player)) {
			return;
		}
		
		Player p = (Player) e.getDamager();
		if(PlayerManager.isFrozen(p)) {
			if(!cm.getBoolean("frozen-attack")) {
				e.setCancelled(true);
				p.sendMessage(msg.get("freezeInteract"));
			}
		}
	}
	
	@EventHandler
	public void onCommandPreProcess(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if(PlayerManager.isFrozen(p)) {
			if(!cm.getBoolean("frozen-commands")) {
				e.setCancelled(true);
				p.sendMessage(msg.get("freezeInteract"));
			}
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if(!(e.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();
		
		if(PlayerManager.isFrozen(p)) {
			if(!cm.getBoolean("frozen-damage")) {
				e.setCancelled(true);
				e.setDamage(0);
				p.setFireTicks(0);
				return;
			}
		}
		
		if(PlayerManager.isStaff(p)) {
			e.setCancelled(true);
			e.setDamage(0);
			p.setFireTicks(0);
			return;
		}
		
		if(PlayerManager.isVanished(p)) {
			if(cm.getBoolean("prevent-vanished-damage")) {
				e.setCancelled(true);
				e.setDamage(0);
				p.setFireTicks(0);
				return;
			}
		}
	}
}
