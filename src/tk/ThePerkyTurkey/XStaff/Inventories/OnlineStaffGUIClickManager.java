package tk.ThePerkyTurkey.XStaff.Inventories;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import tk.ThePerkyTurkey.XStaff.XClickManager;
import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Utils.ConfigManager;

public class OnlineStaffGUIClickManager implements Listener,XClickManager {
	
	private XStaff xs;
	private Player p;
	private InventoryClickEvent e;
	private ConfigManager cm;
	private OnlineStaffGUI osGUI;
	
	public OnlineStaffGUIClickManager(XStaff xs) {
		this.xs = xs;
		this.cm = xs.getConfigManager();
	}
	
	@Override
	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) {
			return;
		}
		
		String name = e.getInventory().getTitle();
		if(!name.equals(ChatColor.translateAlternateColorCodes('&', "&cOnline Staff"))) {
			return;
		}
		
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) {
			return;
		}
		
		if(!e.getInventory().getName().equals(ChatColor.translateAlternateColorCodes('&', cm.getString("online-staff-title")))) {
			return;
		}
		
		e.setCancelled(true);
		
		if(e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
			return;
		}
		Player p = (Player) e.getWhoClicked();
		
		this.p = p;
		this.e = e;
		this.osGUI = new OnlineStaffGUI(xs, p);
		manage();
	}

	@Override
	public void manage() {
		interfaceClick();
	}
	
	private void interfaceClick() {
		String[] parts = e.getInventory().getItem(49).getItemMeta().getDisplayName().split(": §6§l");
		int pageNo = Integer.parseInt(parts[1]);
		ItemStack is = e.getCurrentItem();
		String name = is.getItemMeta().getDisplayName();
		if(is.getType() == Material.PAPER) {
			if(name.contains("Next")) {
				osGUI.openNextPage(pageNo);
			} else if(name.contains("Previous")) {
				osGUI.openPreviousPage(pageNo);
			}
		}
	}

}
