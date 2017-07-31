package tk.ThePerkyTurkey.XStaff;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public interface XClickManager {
	
	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e);
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e);
	
	public void manage();
	

}
