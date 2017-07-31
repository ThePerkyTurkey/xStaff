package tk.ThePerkyTurkey.XStaff.Inventories;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
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
	private File rootfile;
	
	private List<String> allItems = new ArrayList<String>();
	private static HashMap<ItemStack, Integer> items = new HashMap<ItemStack, Integer>();
	
	private static boolean vanishItemEnabled;
	private static int vanishSlotNo;
	private static ItemStack vanishEnabled;
	private static ItemStack vanishDisabled;
	
	public StaffInventory(XStaff xs) {
		this.xs = xs;
		this.cm = xs.getConfigManager();
		this.file = cm.getInventories();
		this.rootfile = cm.getInventoryFile();
		allItems.add("Freeze");
		allItems.add("Reports");
		allItems.add("Details");
		allItems.add("OnlineStaff");
		allItems.add("RandomTeleport");
		allItems.add("WallPass");
		initializeItems();
		updateInvTimer();
	}
	
	private void updateInvTimer() {
		xs.getServer().getScheduler().scheduleSyncRepeatingTask(xs, new Runnable() {
    
			@Override
			public void run() {
				for(Player p : PlayerManager.getStaffPlayers()) {
					updateInventory(p);
				}	
			}
			
		}, 20L, 20L);
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
	
	public void saveInventory(Player p) throws IOException {
		file.set(p.getUniqueId().toString() + ".inventory", saveItemArray(p.getInventory().getContents()));
		file.set(p.getUniqueId().toString() + ".armour", saveItemArray(p.getInventory().getArmorContents()));
		file.set("saved." + p.getName(), true);
		file.save(rootfile);
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
	}
	
	public void restoreInventory(Player p) throws IOException {
		p.getInventory().clear();
		
		p.getInventory().setContents(loadItemArray(makeUsable(file.getMapList(p.getUniqueId().toString() + ".inventory")), new ItemStack[36]));
		p.getInventory().setArmorContents(loadItemArray(makeUsable(file.getMapList(p.getUniqueId().toString() + ".armour")), new ItemStack[4]));
		
		file.set(p.getUniqueId().toString(), null);
		file.set("saved." + p.getName(), null);
		
		file.save(rootfile);
	}
	
	public boolean needsRestoring(Player p) {
		return file.getBoolean("saved." + p.getName());
	}
	
	public void setStaffInventory(Player p) {
		try {
			saveInventory(p);
		} catch (IOException e) {
			e.printStackTrace();
			xs.getLogger().severe("An error occured whilst saving " + p.getName() + "'s inventory");
		}
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
		
		ItemStack helmet = new ItemStack(Material.CHAINMAIL_HELMET);
		helmet.addEnchantment(Enchantment.DURABILITY, 1);
		ItemStack chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		chest.addEnchantment(Enchantment.DURABILITY, 1);
		ItemStack leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
		leggings.addEnchantment(Enchantment.DURABILITY, 1);
		ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);
		boots.addEnchantment(Enchantment.DURABILITY, 1);
		pi.setHelmet(helmet);
		pi.setChestplate(chest);
		pi.setLeggings(leggings);
		pi.setBoots(boots);
	}
	
	public void updateInventory(Player p) {
		
		PlayerInventory pi = p.getInventory();
		for(int i = 0;i<36;i++) {
			ItemStack is = getItemFromMap(i);
			if(pi.getItem(i) != null && !pi.getItem(i).equals(is)) {
				if(i != vanishSlotNo) {
					pi.setItem(i, is);
				}
			} else if(pi.getItem(i) == null) {
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
		
		if(pi.getItem(vanishSlotNo) == null) {
			pi.setItem(vanishSlotNo, (PlayerManager.isVanished(p) ? vanishEnabled : vanishDisabled));
		}
	
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
	
	private List<Map<String, Object>> saveItemArray(ItemStack[] inv) {
	    List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	    for (int i = 0; i < inv.length; i++)
	    {
	      ItemStack item = inv[i];
	      if (item != null)
	      {
	        Map<String, Object> itemserialized = item.serialize();
	        itemserialized.put("id", Integer.valueOf(i));
	        result.add(itemserialized);
	      }
	    }
	    return result;
	 }
	
	private ItemStack[] loadItemArray(List<Map<String, Object>> list, ItemStack[] inv) {
	    for (Map<String, Object> itemmap : list)
	    {
	      ItemStack item = ItemStack.deserialize(itemmap);
	      inv[((Integer)itemmap.get("id")).intValue()] = item;
	    }
	    return inv;
	 }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Map<String, Object>> makeUsable(List<Map<?, ?>> iets)
	  {
	    List<Map<String, Object>> result = new ArrayList();
	    for (Map<?, ?> nu : iets)
	    {
	      Set<?> keys = nu.keySet();
	      Map<String, Object> returnmap = new HashMap();
	      for (Object key : keys) {
	        returnmap.put((String)key, nu.get(key));
	      }
	      result.add(returnmap);
	    }
	    return result;
	 }

}
