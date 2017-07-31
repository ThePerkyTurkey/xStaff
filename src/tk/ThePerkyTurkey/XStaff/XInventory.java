package tk.ThePerkyTurkey.XStaff;

import org.bukkit.inventory.Inventory;

public interface XInventory {
	
	/**
	 * Opens the inventory
	 */
	public void open();
	
	/**
	 * Generates the main inventory interface
	 * 
	 * @return The inventory created
	 */
	public Inventory generateInventory();
	
	/**
	 * Gets the displayed name of the inventory
	 * 
	 * @return The name of the inventory
	 */
	public String getName();
	
	/**
	 * Sets the name of the inventory
	 * 
	 * @param name The new name of the inventory
	 */
	public void setName(String name);
	
}
