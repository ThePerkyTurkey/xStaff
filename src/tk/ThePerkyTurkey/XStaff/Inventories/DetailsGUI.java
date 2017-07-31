package tk.ThePerkyTurkey.XStaff.Inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import net.milkbowl.vault.permission.Permission;
import tk.ThePerkyTurkey.XStaff.XInventory;
import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Utils.PlayerManager;

public class DetailsGUI implements XInventory{
	
	private Player p;
	private XStaff xs;
	private String name;
	private Player target;
	
	public DetailsGUI(XStaff xs, Player p, Player target) {
		this.p = p;
		this.xs = xs;
		this.target = target;
		
		setName(t("&c" + target.getName()));
	}

	@Override
	public void open() {
		p.openInventory(generateInventory());
	}

	@Override
	public Inventory generateInventory() {
		HashMap<ItemStack, Integer> items = new HashMap<ItemStack, Integer>();
		Inventory inv = xs.getServer().createInventory(null, 27, getName() + " - Details");
		
		ItemStack potionEffects = new ItemStack(Material.BREWING_STAND_ITEM);
		ItemStack saturation = new ItemStack(Material.BREAD);
		ItemStack inventory = new ItemStack(Material.DIAMOND_CHESTPLATE);
		ItemStack xp = new ItemStack(Material.EXP_BOTTLE);
		ItemStack freeze = new ItemStack(Material.ICE);
		ItemStack administration = new ItemStack(Material.WOOD_AXE);
		ItemStack teleport = new ItemStack(Material.COMPASS);
		ItemStack reports = new ItemStack(Material.PAPER);
		ItemStack notes = new ItemStack(Material.BOOK);
		ItemStack other = new ItemStack(Material.NETHER_STAR);
		ItemStack noPerms = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
		
		List<String> lore = new ArrayList<String>(); 
		
		ItemMeta pem = potionEffects.getItemMeta();
		pem.setDisplayName(t("&6&lEffects"));
		lore.clear();
		for(PotionEffect p : target.getActivePotionEffects()) {
			lore.add(t("&a" + p.getType().getName() + "&6 - Amplifier: &a" + (p.getAmplifier() + 1)));
		}
		pem.setLore(lore);
		potionEffects.setItemMeta(pem);
		items.put(potionEffects, 10);
		
		ItemMeta sm = saturation.getItemMeta();
		sm.setDisplayName(t("&6&lStatus"));
		lore.clear();
		lore.add(t("&6Hunger: &a" + target.getFoodLevel() + " &6/&a 20"));
		lore.add(t("&6Health: &a" + target.getHealth() + " &6/&a " + target.getMaxHealth()));
		sm.setLore(lore);
		saturation.setItemMeta(sm);
		items.put(saturation, 11);
		
		ItemMeta im = inventory.getItemMeta();
		im.setDisplayName(t("&6&lInventory"));
		lore.clear();
		lore.add(t("&6Click to view the player's inventory!"));
		inventory.setItemMeta(im);
		items.put(inventory, 12);
		
		ItemMeta xm = xp.getItemMeta();
		xm.setDisplayName(t("&6&lXP Levels"));
		lore.clear();
		lore.add(t("&6Level &a" + target.getExp()));
		lore.add(t("&a" + target.getExpToLevel() + "xp &6required for next level"));
		xm.setLore(lore);
		xp.setItemMeta(xm);
		items.put(xp, 14);
		
		ItemMeta fm = freeze.getItemMeta();
		fm.setDisplayName(t("&6&lFreeze"));
		lore.clear();
		lore.add(t("&6Click to &a" + (PlayerManager.isFrozen(target) ? "unfreeze" : "freeze") + " &6this player!"));
		fm.setLore(lore);
		freeze.setItemMeta(fm);
		items.put(freeze, 15);
		
		ItemMeta am = administration.getItemMeta();
		am.setDisplayName(t("&6&lPunish options"));
		lore.clear();
		lore.add(t("&6Click to view punishment options!"));
		am.setLore(lore);
		administration.setItemMeta(am);
		items.put(administration, 16);
		
		ItemMeta tm = teleport.getItemMeta();
		tm.setDisplayName(t("&6&lTeleport Options"));
		lore.clear();
		lore.add(t("&6Click to view teleportation options"));
		tm.setLore(lore);
		teleport.setItemMeta(tm);
		items.put(teleport, 21);
		
		ItemMeta rm = reports.getItemMeta();
		rm.setDisplayName(t("&6&lReports"));
		lore.clear();
		lore.add(t("&6Click to view reports for this player"));
		rm.setLore(lore);
		reports.setItemMeta(rm);
		items.put(reports, 22);
		
		ItemMeta nm = notes.getItemMeta();
		nm.setDisplayName(t("&6&lNotes"));
		lore.clear();
		lore.add(t("&6Click to view all notes and warnings"));
		nm.setLore(lore);
		notes.setItemMeta(nm);
		items.put(notes, 23);
		
		ItemMeta om = other.getItemMeta();
		om.setDisplayName(t("&6&lOther Info"));
		lore.clear();
		lore.add(t("&6IP address: &a" + target.getAddress().getAddress().toString().replaceFirst("/", "")));
		lore.add(t("&6Display name: &a" + target.getName()));
		lore.add(t("&6Op: " + (target.isOp() ? "&aTrue" : "&cFalse")));
		if(xs.getServer().getPluginManager().isPluginEnabled("Vault")) {
			Permission perm = xs.getPermissionHandler();
			lore.add(t("&6Rank: &a" + perm.getPrimaryGroup(target)));
		}
		lore.add(t("&6Vanished: " + (PlayerManager.isVanished(target) ? "&aTrue" : "&cFalse")));
		lore.add(t("&6In staff mode: " + (PlayerManager.isStaff(target) ? "&aTrue" : "&cFalse")));
		lore.add(t("&6Gamemode: &a" + target.getGameMode().toString().toLowerCase()));
		om.setLore(lore);
		other.setItemMeta(om);
		items.put(other, 13);
		
		ItemMeta npm = noPerms.getItemMeta();
		npm.setDisplayName(t("&cNo permission"));
		noPerms.setItemMeta(npm);
		
		for(int i = 0;i < 27;i++) {
			inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
		}
		
		for(Entry<ItemStack, Integer> e : items.entrySet()) {
			String[] parts = e.getKey().getItemMeta().getDisplayName().split(" ");
			String name = parts[0].replace("§6§l", "");
			if(p.hasPermission("xstaff.details." + name.toLowerCase())) {
				inv.setItem(e.getValue(), e.getKey());
			} else {
				inv.setItem(e.getValue(), noPerms);
			}
		}
		
		return inv;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	private String t(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public void openInventoryView() {
		p.openInventory(getInventoryView());
	}
	
	private Inventory getInventoryView() {
		Inventory inv = xs.getServer().createInventory(null, 54, getName() + " - Inv");
		
		for(int i = 0; i < 54; i++) {
			inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
		}
		
		for(int i = 0; i < 36;i++) {
			ItemStack is = target.getInventory().getItem(i);
			if(is == null) {
				inv.setItem(i, new ItemStack(Material.AIR));
			} else {
				inv.setItem(i, is);
			}
		}
		
		if(p.getInventory().getHelmet() != null) {
			inv.setItem(47, p.getInventory().getHelmet());
		} else {
			inv.setItem(47, new ItemStack(Material.AIR));
		}
		if(p.getInventory().getChestplate() != null) {
			inv.setItem(48, p.getInventory().getChestplate());
		} else {
			inv.setItem(48, new ItemStack(Material.AIR));
		}
		if(p.getInventory().getLeggings() != null) {
			inv.setItem(50, p.getInventory().getLeggings());
		} else {
			inv.setItem(50, new ItemStack(Material.AIR));
		}
		if(p.getInventory().getBoots() != null) {
			inv.setItem(51, p.getInventory().getBoots());
		} else {
			inv.setItem(51, new ItemStack(Material.AIR));
		}
		
		ItemStack back = new ItemStack(Material.NETHER_STAR);
		ItemMeta bm = back.getItemMeta();
		bm.setDisplayName(t("&c&lBack"));
		List<String> lore = new ArrayList<String>();
		lore.add(t("&7Click to return to the previous page"));
		bm.setLore(lore);
		back.setItemMeta(bm);
		
		inv.setItem(49, back);
		
		return inv;
	}
	
	public void openAdminMenu() {
		p.openInventory(generateAdminMenu());
	}
	
	private Inventory generateAdminMenu() {
		Inventory inv = xs.getServer().createInventory(null, 36, getName() + " - Admin");
		
		for(int i = 0;i < 36; i++) {
			inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
		}
		List<String> lore = new ArrayList<String>();
		
		ItemStack kick = new ItemStack(Material.GOLD_BOOTS);
		ItemMeta km = kick.getItemMeta();
		km.setDisplayName(t("&6&lKick Player"));
		lore.add(t("&aKick the player"));
		km.setLore(lore);
		kick.setItemMeta(km);
		
		ItemStack back = new ItemStack(Material.NETHER_STAR);
		ItemMeta bm = back.getItemMeta();
		bm.setDisplayName(t("&c&lBack"));
		lore.clear();
		lore.add(t("&7Click to return to the previous page"));
		bm.setLore(lore);
		back.setItemMeta(bm);
		
		ItemStack ban = new ItemStack(Material.WOOD_AXE);
		ItemMeta banm = kick.getItemMeta();
		banm.setDisplayName(t("&6&lBan Player"));
		lore.clear();
		lore.add(t("&aBan the player"));
		banm.setLore(lore);
		ban.setItemMeta(banm);
		
		inv.setItem(10, kick);
		inv.setItem(13, back);
		inv.setItem(16, ban);
		
		return inv;
	}
	
	public void openTeleportGUI() {
		p.openInventory(generateTpGUI());
	}
	
	private Inventory generateTpGUI() {
		Inventory inv = xs.getServer().createInventory(null, 36, getName() + " - Tp");
		List<String> lore = new ArrayList<String>();
		
		ItemStack tpTo = new ItemStack(Material.COMPASS);
		ItemMeta tpToM = tpTo.getItemMeta();
		tpToM.setDisplayName(t("&6&lTeleport to the player"));
		lore.add(t("&6Click to teleport to the player!"));
		tpToM.setLore(lore);
		tpTo.setItemMeta(tpToM);
		
		ItemStack tpHere = new ItemStack(Material.COMPASS);
		ItemMeta tpHereM = tpTo.getItemMeta();
		tpHereM.setDisplayName(t("&6&lTeleport the player to you"));
		lore.clear();
		lore.add(t("&6Click to teleport the player to you!"));
		tpHereM.setLore(lore);
		tpHere.setItemMeta(tpHereM);
		ItemStack back = new ItemStack(Material.NETHER_STAR);
		ItemMeta bm = back.getItemMeta();
		bm.setDisplayName(t("&c&lBack"));
		lore.clear();
		lore.add(t("&7Click to return to the previous page"));
		bm.setLore(lore);
		back.setItemMeta(bm);
		
		for(int i = 0; i < 36; i++) {
			inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
		}
		
		inv.setItem(10, tpTo);
		inv.setItem(13, back);
		inv.setItem(16, tpHere);
		
		return inv;
	}
	
	public void openNotesGUI() {
		p.openInventory(generateNotesGUI());
	}
	
	private Inventory generateNotesGUI() {
		Inventory inv = xs.getServer().createInventory(null, 36, getName() + " - Notes/Warns");
		List<String> lore = new ArrayList<String>();
		
		ItemStack warns = new ItemStack(Material.BOOK);
		ItemMeta wm = warns.getItemMeta();
		wm.setDisplayName(t("&6&lWarnings"));
		lore.add(t("&6Click to view this player's warnings"));
		wm.setLore(lore);
		warns.setItemMeta(wm);
		
		ItemStack back = new ItemStack(Material.NETHER_STAR);
		ItemMeta bm = back.getItemMeta();
		bm.setDisplayName(t("&c&lBack"));
		lore.clear();
		lore.add(t("&7Click to return to the previous page"));
		bm.setLore(lore);
		back.setItemMeta(bm);
		
		ItemStack notes = new ItemStack(Material.BOOK);
		ItemMeta nm = notes.getItemMeta();
		nm.setDisplayName(t("&6&lNotes"));
		lore.clear();
		lore.add(t("&6Click to view this player's notes"));
		nm.setLore(lore);
		notes.setItemMeta(nm);
		
		for(int i = 0; i < 36; i++) {
			inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
		}
		
		inv.setItem(10, warns);
		inv.setItem(13, back);
		inv.setItem(16, notes);
		
		return inv;
	}
}
