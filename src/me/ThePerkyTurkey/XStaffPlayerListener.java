package me.ThePerkyTurkey;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import me.ThePerkyTurkey.Utils.PlayerManager;

public class XStaffPlayerListener implements Listener {
	
	public XStaffPlayerListener(XStaff xs) {
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		PlayerManager.setStaff(p, false);
		PlayerManager.setVanished(p, false);
	}
	
	@EventHandler
	public void onHandSwitch(PlayerSwapHandItemsEvent e) {
		Player p = e.getPlayer();
		if(PlayerManager.isStaff(p) && !p.hasPermission("xstaff.modify")) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(PlayerManager.isStaff(p) && !p.hasPermission("xstaff.break")) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e) {
		if(e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			if(PlayerManager.isStaff(p) && !p.hasPermission("xstaff.modify")) {
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
		if(PlayerManager.isStaff(p) && !p.hasPermission("xstaff.modify")) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		if(PlayerManager.isStaff(p) && !p.hasPermission("xstaff.modify")) {
			e.setCancelled(true);
		}
		if(PlayerManager.isVanished(p) && !p.hasPermission("xstaff.vanish.pickup")) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if(PlayerManager.isStaff(p) && !p.hasPermission("xstaff.modify")) {
			e.setCancelled(true);
		}
	}
}
