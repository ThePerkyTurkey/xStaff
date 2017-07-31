package tk.ThePerkyTurkey.XStaff.Inventories;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import tk.ThePerkyTurkey.XStaff.XClickManager;
import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Utils.Messages;
import tk.ThePerkyTurkey.XStaff.Utils.PlayerManager;

public class DetailsGUIClickManager implements XClickManager, Listener{
	
	private XStaff xs;
	private Messages msg;
	private Player p;
	private Player target;
	private DetailsGUI dGUI;
	private InventoryClickEvent e;
	
	public HashMap<Player, Player> awaitingKickReason = new HashMap<Player, Player>();
	public HashMap<Player, Player> awaitingBanReason = new HashMap<Player, Player>();

	public DetailsGUIClickManager(XStaff xs) {
		this.xs = xs;
		this.msg = xs.getMessages();
	}

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) {
			return;
		}
		
		String name = e.getInventory().getTitle();
		if(name.contains(" - ")) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) {
			return;
		}
		
		if(!e.getInventory().getName().toLowerCase().contains(" - ")) {
			return;
		}
		
		String[] parts = e.getInventory().getName().split(" - ");
		String name = parts[0].replaceAll("§c", "");
		
		Player target = xs.getServer().getPlayer(name);
		
		Player p = (Player) e.getWhoClicked();
		
		e.setCancelled(true);
		
		if(target == null) {
			p.closeInventory();
			return;
		}
		
		if(e.getCurrentItem() == null || e.getCurrentItem().equals(Material.AIR)) {
			return;
		}
		this.p = p;
		this.target = target;
		this.e = e;
		this.dGUI = new DetailsGUI(xs, p, target);
		manage();
	}

	@Override
	public void manage() {
		if(e.getInventory().getName().contains(" - Details")) {
			mainInterfaceClick();
		} else if(e.getInventory().getName().contains(" - Inv")) {
			invInterfaceClick();
		} else if(e.getInventory().getName().contains(" - Admin")) {
			adminInterfaceClick();
		} else if(e.getInventory().getName().contains(" - Tp")) {
			teleportInterfaceClick();
		} else if(e.getInventory().getName().contains(" - Notes/Warns")) {
			notesInterfaceClick();
		}
	}
	
	private void mainInterfaceClick() {
		ItemStack is = e.getCurrentItem();
		switch(is.getType()) {
		
		case DIAMOND_CHESTPLATE:
			dGUI.openInventoryView();
			break;
			
		case ICE:
			if(PlayerManager.isFrozen(target)) {
				p.sendMessage(msg.get("freezeDisable", target.getName()));
			} else {
				p.sendMessage(msg.get("freezeEnable", target.getName()));
			}
			PlayerManager.toggleFreeze(target);
			dGUI.open();
			break;
			
		case WOOD_AXE:
			dGUI.openAdminMenu();
			break;
			
		case PAPER:
			ReportGUI rGUI = new ReportGUI(xs, p);
			rGUI.openPlayerReportGUI(target.getName(), 1);
			break;
			
		case COMPASS:
			dGUI.openTeleportGUI();
			break;
			
		case BOOK:
			dGUI.openNotesGUI();
			break;
		default:
			break;
		}
	}
	
	private void invInterfaceClick() {
		ItemStack is = e.getCurrentItem();
		if(is.getType() == Material.NETHER_STAR && is.getItemMeta().getDisplayName().equals("§c§lBack")) {
			dGUI.open();
		}
	}
	
	private void adminInterfaceClick() {
		ItemStack is = e.getCurrentItem();
		switch(is.getType()) {
		
		case GOLD_BOOTS:
			if(!p.hasPermission("xstaff.details.punish.kick")) {
				p.closeInventory();
				p.sendMessage(msg.get("noPerms"));
				return;
			}
			awaitingKickReason.put(p, target);
			p.closeInventory();
			p.sendMessage(msg.get("inputMessage", "kick"));
			break;
			
		case WOOD_AXE:
			if(!p.hasPermission("xstaff.details.punish.ban")) {
				p.closeInventory();
				p.sendMessage(msg.get("noPerms"));
				return;
			}
			awaitingBanReason.put(p, target);
			p.closeInventory();
			p.sendMessage(msg.get("inputMessage", "ban"));
			break;

		case NETHER_STAR:
			dGUI.open();
			break;
			
		default:
			break;
		}
	}
	
	private void teleportInterfaceClick() {
		ItemStack is = e.getCurrentItem();
		switch(is.getType()) {
		
		case COMPASS:
			if(is.getItemMeta().getDisplayName().contains("to the player")) {
				if(!p.hasPermission("xstaff.details.teleport.to")) {
					p.closeInventory();
					p.sendMessage(msg.get("noPerms"));
					return;
				}
				p.teleport(target);
			} else {
				if(!p.hasPermission("xstaff.details.teleport.here")) {
					p.closeInventory();
					p.sendMessage(msg.get("noPerms"));
					return;
				}
				target.teleport(p);
			}
			p.sendMessage(msg.get("wallPassSuccess"));
			break;
			
		case NETHER_STAR:
			dGUI.open();
		
		default:
			break;
		}
	}
	
	private void notesInterfaceClick() {
		ItemStack is = e.getCurrentItem();
		switch(is.getType()) {
		
		case BOOK:
			if(is.getItemMeta().getDisplayName().contains("Warnings")) {
				if(!p.hasPermission("xstaff.details.notes.warns")) {
					p.closeInventory();
					p.sendMessage(msg.get("noPerms"));
					return;
				}
				WarnsGUI wGUI = new WarnsGUI(xs, p, target.getName());
				wGUI.open(1);
			} else if(is.getItemMeta().getDisplayName().contains("Notes")) {
				if(!p.hasPermission("xstaff.details.notes.notes")) {
					p.closeInventory();
					p.sendMessage(msg.get("noPerms"));
					return;
				}
				NotesGUI nGUI = new NotesGUI(xs, p, target.getName());
				nGUI.open(1);
			}
			break;
			
		case NETHER_STAR:
			dGUI.open();
			break;
		default:
			break;
		}
	}

}
