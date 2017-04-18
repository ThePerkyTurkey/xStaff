package me.ThePerkyTurkey;

import org.bukkit.inventory.Inventory;

public interface XInventory {

	/**
	 * Checks if the player is currently viewing the inventory
	 *
	 * @return True if the player is currently viewing the inventory
	 */
	public boolean isViewing();
	
	/**
	 * Sets whether the player is viewing the inventory
	 * 
	 * @param isViewing True if player is viewing the inventory
	 */
	public void setViewing(boolean isViewing);
	
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
