package tk.ThePerkyTurkey.XStaff.Inventories;

import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.RED;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import tk.ThePerkyTurkey.XStaff.XPageInventory;
import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Utils.SmallClasses.StaffMember;
import tk.ThePerkyTurkey.XStaff.Utils.WarnManager;

public class WarnsGUI implements XPageInventory{
	
	private XStaff xs;
	private Player p;
	private String target;
	private WarnManager wm;
	
	private String name;
	
	public WarnsGUI(XStaff xs, Player p, String target) {
		this.xs = xs;
		this.p = p;
		this.target = target;
		this.wm = xs.getWarnManager();
		
		setName(t("&c" + target));
	}

	@Override
	public void open(int pageNo) {
		List<Inventory> pages = generatePages();
		p.openInventory(pages.get(pageNo - 1));
	}

	@Override
	public void openNextPage(int currentPageNo) {
		if(currentPageNo < getFinalPage()) {
			open(currentPageNo + 1);
		}
	}

	@Override
	public void openPreviousPage(int currentPageNo) {
		if(currentPageNo > 1) {
			open(currentPageNo - 1);
		}
	}

	@Override
	public List<Inventory> generatePages() {
		List<Inventory> invs = new ArrayList<Inventory>();
		LinkedHashMap<StaffMember, String> warns = wm.getWarnings(target);
		Inventory inv = xs.getServer().createInventory(null, 54, getName() + " - Warns");
		if(warns == null) {
			invs.add(formatPageFooter(inv, 1));
			return invs;
		}
		
		int slotNo = 0;
		int pageNo = 0;
		int entryNo = 1;
		for(Entry<StaffMember, String> e : warns.entrySet()) {
			if(slotNo == 45) {
				pageNo++;
				slotNo = 0;
				invs.add(formatPageFooter(inv, pageNo));
				inv = xs.getServer().createInventory(null, 54, getName() + " - Warns");
			}
			
			List<String> lore = new ArrayList<String>();
			lore.add(0, t("&6ID: &a" + entryNo));
			lore.add(1, t("&6Reason:"));
			lore.addAll(splitLore(e.getValue(), 20));
			ItemStack skull = makeSkull(e.getKey().getName(), "&6Staff: &a" + e.getKey().getName(), lore);
			inv.setItem(slotNo, skull);
			slotNo++;
			entryNo++;
		}
		
		pageNo++;
		invs.add(formatPageFooter(inv, pageNo));
		return invs;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getFinalPage() {
		return generatePages().size();
	}

	@Override
	public Inventory formatPageFooter(Inventory inv, int pageNo) {
		ItemStack previousPage = new ItemStack(Material.PAPER, 1);
		ItemMeta ppim = previousPage.getItemMeta();
		ppim.setDisplayName(RED + "" + BOLD + "<<Previous Page");
		previousPage.setItemMeta(ppim);
		
		ItemStack page = new ItemStack(Material.NETHER_STAR, 1);
		ItemMeta pim = page.getItemMeta();
		pim.setDisplayName(RED + "" + BOLD + "Page: " + GOLD + "" + BOLD + pageNo);
		page.setItemMeta(pim);
		
		ItemStack nextPage = new ItemStack(Material.PAPER, 1);
		ItemMeta npim = nextPage.getItemMeta();
		npim.setDisplayName(RED + "" + BOLD + "Next Page>>");
		nextPage.setItemMeta(npim);
		
		inv.setItem(48, previousPage);
		inv.setItem(49, page);
		inv.setItem(50, nextPage);
		
		return inv;
	}
	
	private ItemStack makeSkull(String playerName, String name, List<String> lore) {
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
		SkullMeta sm = (SkullMeta) skull.getItemMeta();
		sm.setOwner(playerName);
		sm.setDisplayName(t(name));
		if(lore != null) {
			sm.setLore(lore);
		}
		skull.setItemMeta(sm);
		
		return skull;
	}
	
	private List<String> splitLore(String text, int characters) {
	    List<String> lore = new ArrayList<>();
	    String[] words = text.split(" ");
	    int wordsUsed = 0;
	    while (wordsUsed < words.length) {
	        String line = "§a";
	        for (int i = wordsUsed; i < words.length; i++) {
	            if (line.length() + words[i].length() >= characters) { 
	                line += words[i];
	                wordsUsed++;
	                break;
	            } else {
	                line += words[i] + " ";
	                wordsUsed++;
	            }
	        }
	        lore.add(line);
	    }
	    return lore;
	}
	
	private String t(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public void openWarnManagerGUI(String target, int ID) {
		p.openInventory(generateWarnManagerGUI(target, ID));
	}
	
	private Inventory generateWarnManagerGUI(String target, int ID) {
		LinkedHashMap<StaffMember, String> warnings = wm.getWarnings(target);
		String staff = (new ArrayList<StaffMember>(warnings.keySet())).get(ID - 1).getName();
		String reason = (new ArrayList<String>(warnings.values())).get(ID - 1);
		Inventory inv = xs.getServer().createInventory(null, 36, getName() + " - WarnManager");
		List<String> lore = new ArrayList<String>();
		
		ItemStack resolve = new ItemStack(Material.EMERALD_BLOCK);
		ItemMeta rm = resolve.getItemMeta();
		rm.setDisplayName(t("&6&lRemove this warning"));
		lore.add(t("&6Click to remove this warning"));
		rm.setLore(lore);
		resolve.setItemMeta(rm);
		
		ItemStack info = new ItemStack(Material.PAPER);
		ItemMeta im = info.getItemMeta();
		im.setDisplayName(t("&6Staff: &a" + staff));
		lore.clear();
		lore.add(t("&6Player: &a" + target));
		lore.add(t("&6ID: &a" + ID));
		lore.add(t("&6Reason:"));
		lore.addAll(splitLore(reason, 20));
		im.setLore(lore);
		info.setItemMeta(im);
		
		ItemStack back = new ItemStack(Material.NETHER_STAR, 1);
		ItemMeta bm = back.getItemMeta();
		bm.setDisplayName(t("&6&lBack"));
		lore.clear();
		lore.add(t("&a&lClick to return"));
		lore.add(t("&a&lto the main page!"));
		bm.setLore(lore);
		back.setItemMeta(bm);
		
		for(int i = 0; i < 36; i++) {
			inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
		}
		inv.setItem(10, resolve);
		inv.setItem(13, back);
		inv.setItem(16, info);
		
		
		return inv;
	}

}
