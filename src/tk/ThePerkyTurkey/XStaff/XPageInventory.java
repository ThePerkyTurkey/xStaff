package tk.ThePerkyTurkey.XStaff;

import java.util.List;

import org.bukkit.inventory.Inventory;

public interface XPageInventory{
	
	/**
	 * Opens a specific page of the inventory
	 * 
	 * @param pageNo The page number of the inventory to open
	 */
	public void open(int pageNo);
	
	/**
	 * Checks if there is another page, and opens it if there is
	 * 
	 * @param currentPageNo The current page number that is being viewed
	 */
	
	public void openNextPage(int currentPageNo);
	
	/**
	 * Checks if there is a previous page, and opens it if there is
	 * 
	 * @param currentPageNo The current page number that is being viewed
	 */
	
	public void openPreviousPage(int currentPageNo);
	
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
