package tk.ThePerkyTurkey.XStaff.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import tk.ThePerkyTurkey.XStaff.XStaff;

public class StaffItemManager {
	
	private ConfigManager cm;
	private String name;
	
	private String nameLoc = "name";
	private String ItemIDLoc = "itemID";
	private String enabledLoc = "enabled";
	private String slotNoLoc = "slotNo";
	private String vanishEnName = "vanish-enabled-name";
	private String vanishEnItemID = "vanish-enabled-itemID";
	private String vanishDiName = "vanish-disabled-name";
	private String vanishDiItemID = "vanish-disabled-itemID";
	
	public StaffItemManager(String name, XStaff xs) {
		this.cm = xs.getConfigManager();
		this.name = name;
	}
	
	public String getName() {
		return ChatColor.translateAlternateColorCodes('&', cm.getString(format(nameLoc)));
	}
	
	public boolean isEnabled() {
		return cm.getBoolean(format(enabledLoc));
	}
	
	public String getItemID() {
		return cm.getString(format(ItemIDLoc));
	}
	
	public int getSlot() {
		return cm.getInt(format(slotNoLoc)) - 1;
	}
	
	private String format(String loc) {
		return name + "." + loc;
	}
	
	public String getVanishEnName() {
		return ChatColor.translateAlternateColorCodes('&', cm.getString(format(vanishEnName)));
	}
	
	public String getVanishDiName() {
		return ChatColor.translateAlternateColorCodes('&', cm.getString(format(vanishDiName)));
	}
	
	public String getVanishEnID() {
		return cm.getString(format(vanishEnItemID));
	}
	
	public String getVanishDiID() {
		return cm.getString(format(vanishDiItemID));
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack createItem() {
		ItemStack is = null;
		String[] parts = getItemID().split(":");
		int matID = Integer.parseInt(parts[0]);
		if(parts.length == 2) {
			Short data = Short.parseShort(parts[1]);
			is = new ItemStack(Material.getMaterial(matID), 1, data);
		} else {
			is = new ItemStack(Material.getMaterial(matID), 1);
		}
		
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(getName());
		is.setItemMeta(im);
		return is;
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack createVanishItems(boolean enabled) {		
		if(enabled) {
			ItemStack is;
			String[] parts = getVanishEnID().split(":");
			int matID = Integer.parseInt(parts[0]);
			if(parts.length == 2) {
				Short data = Short.parseShort(parts[1]);
				is = new ItemStack(Material.getMaterial(matID), 1, data);
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(getVanishEnName());
				is.setItemMeta(im);
				return is;
			} else {
				is = new ItemStack(Material.getMaterial(matID), 1);
				ItemMeta im = is.getItemMeta();
				is.setItemMeta(im);
				return is;
			}
		} else {
			ItemStack is;
			String[] parts = getVanishDiID().split(":");
			int matID = Integer.parseInt(parts[0]);
			if(parts.length == 2) {
				Short data = Short.parseShort(parts[1]);
				is = new ItemStack(Material.getMaterial(matID), 1, data);
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(getVanishDiName());
				is.setItemMeta(im);
				return is;
			} else {
				is = new ItemStack(Material.getMaterial(matID), 1);
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(getVanishDiName());
				is.setItemMeta(im);
				return is;
			}
		}
	}
}
