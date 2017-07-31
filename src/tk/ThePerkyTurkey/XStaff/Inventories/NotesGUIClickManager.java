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
import tk.ThePerkyTurkey.XStaff.Utils.NotesManager;

public class NotesGUIClickManager implements XClickManager, Listener{
	
	private XStaff xs;
	private Player p;
	private InventoryClickEvent e;
	private String target;
	private NotesGUI nGUI;
	
	public NotesGUIClickManager(XStaff xs) {
		this.xs = xs;
	}
	
	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) {
			return;
		}
		
		if(e.getInventory().getName().contains(" - Note")) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) {
			return;
		}
		
		if(!e.getInventory().getName().contains(" - Note")) {
			return;
		}
		
		if(e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
			e.setCancelled(true);
			return;
		}
		
		String[] parts = e.getInventory().getName().split(" - ");
		String name = parts[0].replaceAll("§c", "");
		
		e.setCancelled(true);
		this.target = name;
		this.p = (Player) e.getWhoClicked();
		this.e = e;
		this.nGUI = new NotesGUI(xs, p, target);
		manage();
	}

	@Override
	public void manage() {
		if(e.getInventory().getName().contains(" - Notes")) {
			mainInterfaceClick();
		} else if(e.getInventory().getName().contains(" - NoteManager")){
			notesManagerInterfaceClick();
		}
	}
	
	private void mainInterfaceClick() {
		ItemStack is = e.getCurrentItem();
		
		switch(is.getType()) {
		
		case SKULL_ITEM:
			if(!p.hasPermission("xstaff.notes.manage")) {
				p.closeInventory();
				p.sendMessage(xs.getMessages().get("noPerms"));
				return;
			}
			String[] nameParts = is.getItemMeta().getDisplayName().split("§a");
			String name = nameParts[1];
			String[] idParts = is.getItemMeta().getLore().get(0).split("§a");
			String id = idParts[1];
			
			nGUI.openNotesManagerGUI(name, Integer.valueOf(id));
			
		case PAPER:
			String[] parts = e.getInventory().getItem(49).getItemMeta().getDisplayName().split(": §6§l");
			int pageNo = Integer.valueOf(parts[1]);
			if(is.getItemMeta().getDisplayName().contains("Next")) {
				nGUI.openNextPage(pageNo);
			} else {
				nGUI.openPreviousPage(pageNo);
			}
		default:
			break;
		}
	}
	
	private void notesManagerInterfaceClick() {
		ItemStack is = e.getCurrentItem();
		String[] idParts = e.getInventory().getItem(16).getItemMeta().getLore().get(1).split("§a");
		String id = idParts[1];
		switch(is.getType()) {
		
		case EMERALD_BLOCK:
			NotesManager nm = xs.getNotesManager();
			nm.removeNote(target, p, id);
			break;
			
		case NETHER_STAR:
			nGUI.open(1);
			break;
		
		default:
			break;
		}
	}


}
