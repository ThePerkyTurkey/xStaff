package me.ThePerkyTurkey.Inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import me.ThePerkyTurkey.XStaff;
import me.ThePerkyTurkey.Utils.PlayerManager;
import me.ThePerkyTurkey.Utils.StaffItemManager;

public class StaffInventory {
	
	private XStaff xs;
	
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
		
		if(p.hasPermission("xstaff.modify")) {
			updateVanishItem(p);
			return;
		}
		
		PlayerInventory pi = p.getInventory();
		for(int i = 0; i<36; i++) {
			ItemStack is = getItemFromMap(i);
				if(pi.getItem(i) == null) {
					if(is != null) {
						pi.setItem(i, is);
					}
				} else if(!pi.getItem(i).equals(is) && is != null) {
					pi.setItem(i, is);
				} else if(!pi.getItem(i).equals(is) && is == null) {
					if(!updateVanishItem(p)) {
						pi.setItem(i, null);
					}
				}
 		}
	}
	
	public static boolean updateVanishItem(Player p) {
		if(!vanishItemEnabled) {
			return false;
		}
		
		PlayerInventory pi = p.getInventory();
		if(PlayerManager.isVanished(p)) {
			if(!p.hasPermission("xstaff.modify")) {
				if(pi.getItem(vanishSlotNo) == null) {
					pi.setItem(vanishSlotNo, vanishEnabled);
				} else if(!pi.getItem(vanishSlotNo).equals(vanishEnabled)) {
					pi.setItem(vanishSlotNo, vanishEnabled);
				}
				return true;
			} else {
				if(!pi.contains(vanishEnabled)) {
					return true;
				}
				
				if(pi.contains(vanishDisabled)) {
					pi.setItem(pi.first(vanishDisabled), vanishEnabled);
				}
				return true;
				
			}
		} else {
			if(!p.hasPermission("xstaff.modify")) {
				if(pi.getItem(vanishSlotNo) == null) {
					pi.setItem(vanishSlotNo, vanishDisabled);
				} else if(!pi.getItem(vanishSlotNo).equals(vanishDisabled)) {
					pi.setItem(vanishSlotNo, vanishDisabled);
				}
				return true;
			} else {
				if(!pi.contains(vanishDisabled)) {
					return true;
				}
				
				if(pi.contains(vanishEnabled)) {
					pi.setItem(pi.first(vanishEnabled), vanishDisabled);
				}
				return true;
			}
		}
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
