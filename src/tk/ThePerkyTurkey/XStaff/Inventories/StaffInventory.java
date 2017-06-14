package tk.ThePerkyTurkey.XStaff.Inventories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Utils.ConfigManager;
import tk.ThePerkyTurkey.XStaff.Utils.PlayerManager;
import tk.ThePerkyTurkey.XStaff.Utils.StaffItemManager;

public class StaffInventory {
	
	private XStaff xs;
	private ConfigManager cm;
	private FileConfiguration file;
	
	private List<String> allItems = new ArrayList<String>();
	private static HashMap<ItemStack, Integer> items = new HashMap<ItemStack, Integer>();
	private static HashMap<Player, ItemStack[]> inventories = new HashMap<Player, ItemStack[]>();
	private static HashMap<Player, ItemStack[]> armour = new HashMap<Player, ItemStack[]>();
	
	private static boolean vanishItemEnabled;
	private static int vanishSlotNo;
	private static ItemStack vanishEnabled;
	private static ItemStack vanishDisabled;
	
	public StaffInventory(XStaff xs) {
		this.xs = xs;
		this.cm = xs.getConfigManager();
		this.file = cm.getInventories();
		allItems.add("Freeze");
		allItems.add("Reports");
		allItems.add("Details");
		allItems.add("OnlineStaff");
		allItems.add("RandomTeleport");
		allItems.add("WallPass");
		initializeItems();
		updateInvTimer();
	}
	
	@SuppressWarnings("deprecation")
	private void updateInvTimer() {
		xs.getServer().getScheduler().scheduleSyncRepeatingTask(xs, new BukkitRunnable() {
    
			@Override
			public void run() {
				for(Player p : PlayerManager.getStaffPlayers()) {
					updateInventory(p);
				}	
			}
			
		}, 20, 20);
	}
	
	private void initializeItems() {
		for(String s : allItems) {
			StaffItemManager sim = new StaffItemManager(s, xs);
			if(sim.isEnabled()) {
				items.put(sim.createItem(), sim.getSlot());
			}
		}
		
		StaffItemManager sim = new StaffItemManager("Vanish", xs);
		vanishItemEnabled = sim.isEnabled();
		vanishSlotNo = sim.getSlot();
		vanishEnabled = sim.createVanishItems(true);
		vanishDisabled = sim.createVanishItems(false);
	}
	
	public static void saveInventory(Player p) {
		inventories.put(p, p.getInventory().getContents());
		armour.put(p, p.getInventory().getArmorContents());
		
		p.getInventory().clear();
	}
	
	public static void restoreInventory(Player p) {
		p.getInventory().clear();
		
		p.getInventory().setContents(inventories.get(p));
		p.getInventory().setArmorContents(armour.get(p));
		
		inventories.remove(p);
		armour.remove(p);
	}
	
	public void writeToFile(Player p) {
		file.set("saved." + p.getName(), true);
		file.set(p.getUniqueId().toString() + ".inventory", inventories.get(p));
		file.set(p.getUniqueId().toString() + ".armour", armour.get(p));
		
		inventories.remove(p);
		armour.remove(p);
		
		try {
			file.save(cm.getInventoryFile());
		} catch (IOException e) {
			xs.getLogger().severe("An error occured whilst saving inventories.yml");
			e.printStackTrace();
		}
	}
	
	public boolean needsRestoring(Player p) {
		return file.getBoolean("saved." + p.getName());
	}
	
	public void getFromFile(Player p) {
		file.set("saved." + p.getName(), null);
		inventories.put(p, (ItemStack[]) file.get(p.getUniqueId().toString() + ".inventory"));
		armour.put(p, (ItemStack[]) file.get(p.getUniqueId().toString() + ".armour"));
		file.set(p.getUniqueId().toString(), null);
	}
	
	public static void setStaffInventory(Player p) {
		saveInventory(p);
		PlayerInventory pi = p.getInventory();
		
		if(vanishItemEnabled) {
			if(PlayerManager.isVanished(p)) {
				pi.setItem(vanishSlotNo, vanishEnabled);
			} else {
				pi.setItem(vanishSlotNo, vanishDisabled);
			}
		}

		for(Entry<ItemStack, Integer> entry : items.entrySet()) {
			pi.setItem(entry.getValue(), entry.getKey());
		}
	}
	
	public void updateInventory(Player p) {
		
		PlayerInventory pi = p.getInventory();
		for(int i = 0;i<36;i++) {
			ItemStack is = getItemFromMap(i);
			if(pi.getItem(i) != null && !pi.getItem(i).equals(is)) {
				if(i != vanishSlotNo) {
					pi.setItem(i, is);
				}
			}
		}
		
		updateVanishItem(p);
	}
	
	public static boolean updateVanishItem(Player p) {
		if(!vanishItemEnabled) {
			return false;
		}
		
		PlayerInventory pi = p.getInventory();
		if(PlayerManager.isVanished(p)) {
			if(!pi.getItem(vanishSlotNo).equals(vanishEnabled)) {
				pi.setItem(vanishSlotNo, vanishEnabled);
			}
		} else {
			if(!pi.getItem(vanishSlotNo).equals(vanishDisabled)) {
				pi.setItem(vanishSlotNo, vanishDisabled);
			}
		}
		
		return true;
	}
	
	private ItemStack getItemFromMap(Integer value) {
		for (ItemStack i : items.keySet()) {
			if(items.get(i).equals(value)) {
				return i;
			}
		}
		return null;
	}

}
