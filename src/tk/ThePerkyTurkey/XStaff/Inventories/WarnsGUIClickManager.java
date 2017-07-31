package tk.ThePerkyTurkey.XStaff.Inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import tk.ThePerkyTurkey.XStaff.XClickManager;
import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Utils.WarnManager;

public class WarnsGUIClickManager implements XClickManager, Listener {
	
	private XStaff xs;
	private Player p;
	private InventoryClickEvent e;
	private String target;
	private WarnsGUI wGUI;
	
	public WarnsGUIClickManager(XStaff xs) {
		this.xs = xs;
	}

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) {
			return;
		}
		
		if(e.getInventory().getName().contains(" - Warn")) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) {
			return;
		}
		
		if(!e.getInventory().getName().contains(" - Warn")) {
			return;
		}
		
		e.setCancelled(true);
		if(e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
			return;
		}
		
		String[] parts = e.getInventory().getName().split(" - ");
		String name = parts[0].replaceAll("§c", "");
		
		this.target = name;
		this.p = (Player) e.getWhoClicked();
		this.e = e;
		this.wGUI = new WarnsGUI(xs, p, target);
		manage();
	}

	@Override
	public void manage() {
		if(e.getInventory().getName().contains(" - Warns")) {
			mainInterfaceClick();
		} else if(e.getInventory().getName().contains(" - WarnManager")){
			warnManagerInterfaceClick();
		}
	}
	
	private void mainInterfaceClick() {
		ItemStack is = e.getCurrentItem();
		switch(is.getType()) {
		
		case SKULL_ITEM:
			if(!p.hasPermission("xstaff.warn.manage")) {
				p.closeInventory();
				p.sendMessage(xs.getMessages().get("noPerms"));
				return;
			}
			String[] nameParts = is.getItemMeta().getDisplayName().split("§a");
			String name = nameParts[1];
			String[] idParts = is.getItemMeta().getLore().get(0).split("§a");
			String id = idParts[1];
			
			wGUI.openWarnManagerGUI(name, Integer.valueOf(id));
			break;
			
		case PAPER:
			String[] parts = e.getInventory().getItem(49).getItemMeta().getDisplayName().split(": §6§l");
			int pageNo = Integer.valueOf(parts[1]);
			if(is.getItemMeta().getDisplayName().contains("Next")) {
				wGUI.openNextPage(pageNo);
			} else {
				wGUI.openPreviousPage(pageNo);
			}
			break;
		default:
			break;
		}
	}
	
	private void warnManagerInterfaceClick() {
		ItemStack is = e.getCurrentItem();
		String[] idParts = e.getInventory().getItem(16).getItemMeta().getLore().get(1).split("§a");
		String id = idParts[1];
		switch(is.getType()) {
		
		case EMERALD_BLOCK:
			WarnManager wm = xs.getWarnManager();
			wm.unWarn(target, p, id);
			break;
			
		case NETHER_STAR:
			wGUI.open(1);
			break;
		
		default:
			break;
		}
	}

}
