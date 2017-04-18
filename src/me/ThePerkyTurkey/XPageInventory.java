package me.ThePerkyTurkey;

import java.util.List;

import org.bukkit.inventory.Inventory;

public interface XPageInventory{
	
	/**
	 * Checks if the player is currently viewing the inventory
	 *
	 * @return True if the player is currently viewing the inventory
	 */
	public boolean isViewing();
	
	/**Sets whether the player is viewing the inventory
	 * 
	 * @param isViewing True if player is viewing the inventory
	 */
	public void setViewing(boolean isViewing);
	
	/**
	 * Opens a specific page of the inventory
	 * 
	 * @param pageNo The page number of the inventory to open (0 = first page)
	 */
	public void open(int pageNo);
	
	/**
	 * Generates the main pages of the inventory
	 * 
	 * @return A list containing the Inventories
	 */
	
	public List<Inventory> generatePages();
	
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
		
	/**
	 * Gets the final page number of the inventory
	 * 
	 * @return The final page number of the inventory
	 */
	public int getFinalPage();
	
	/**
	 * Adds a page footer to the bottom of the page
	 * 
	 * @return The inventory with the footer
	 */
	public Inventory formatPageFooter(Inventory inv, int pageNo);
}
