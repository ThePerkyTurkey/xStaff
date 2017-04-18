package me.ThePerkyTurkey.Inventories;

import static org.bukkit.ChatColor.BLUE;
import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.translateAlternateColorCodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.ThePerkyTurkey.XPageInventory;
import me.ThePerkyTurkey.XStaff;
import me.ThePerkyTurkey.Utils.ReportManager;

public class ReportGUI implements XPageInventory{
	
	private XStaff xs;
	private Player p;
	private ReportManager rm;
	
	private boolean isViewing;
	private String name;
	
	private static HashMap<Player, ReportGUI> instances = new HashMap<Player, ReportGUI>();
	
	
	public ReportGUI(XStaff xs, Player p) {
		if(xs.getConfigManager().getString("report-gui-title") == null) {
			this.name = translateAlternateColorCodes('&', "&cReports");
		} else {
			this.name = translateAlternateColorCodes('&', xs.getConfigManager().getString("report-gui-title"));
		}
		this.xs = xs;
		this.p = p;
		this.rm = xs.getReportManager();
		instances.put(p, this);
	}
	
	public static ReportGUI getInstance(Player p) {
		return instances.get(p);
	}


	@Override
	public boolean isViewing() {
		return isViewing;
	}


	@Override
	public void setViewing(boolean isViewing) {
		this.isViewing = isViewing;
		
	}


	@Override
	public void open(int pageNo) {
		List<Inventory> pages = generatePages();
		if(!p.getOpenInventory().getTitle().contains("\u00A7cReports")) {
			p.openInventory(pages.get(pageNo - 1));
		} else {
			p.getOpenInventory().getTopInventory().setContents(pages.get(pageNo - 1).getContents());
		}
		setViewing(true);
	}

	@Override
	public List<Inventory> generatePages() {
		HashMap<String, HashMap<String, String>> reports = rm.getReports();
		List<Inventory> invs = new ArrayList<>();
		HashMap<String, Integer> number = new HashMap<String, Integer>();
		for(Entry<String, HashMap<String, String>> entry : reports.entrySet()) {
			number.put(entry.getKey(), entry.getValue().size());
		}
		int itemNo = 0;
		int pageNo = 0;
		Inventory inv = xs.getServer().createInventory(null, 54, name);
		for(Entry<String, Integer> entry : number.entrySet()) {
			if(itemNo == 45) {
				pageNo++;
				itemNo = 0;
				invs.add(formatPageFooter(inv, pageNo));
				inv = xs.getServer().createInventory(null, 54, name);
			}
			String name = entry.getKey();
			List<String> lore = new ArrayList<>();
			lore.add(GRAY + "==============");
			lore.add(BLUE + "Reports: " + GREEN + entry.getValue());
			lore.add(GRAY + "==============");
			ItemStack skull = makeSkull(name, name, lore);
			inv.setItem(itemNo, skull);
			itemNo++;
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
		List<Inventory> pages = this.generatePages();
		return pages.size();
	}
	
	private ItemStack makeSkull(String playerName, String name, List<String> lore) {
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
		SkullMeta sm = (SkullMeta) skull.getItemMeta();
		sm.setOwner(playerName);
		sm.setDisplayName(name);
		sm.setLore(lore);
		skull.setItemMeta(sm);
		
		return skull;
	}
	
	public void openNextPage(int currentPageNo) {
		if(currentPageNo < getFinalPage()) {
			open(currentPageNo + 1);
		}
	}
	
	public void openPreviousPage(int currentPageNo) {
		if(currentPageNo > 1) {
			open(currentPageNo - 1);
		}
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
	
	public Inventory formatSecondaryPageFooter(Inventory inv, int pageNo) {
		ItemStack previousPage = new ItemStack(Material.PAPER, 1);
		ItemMeta ppim = previousPage.getItemMeta();
		ppim.setDisplayName(RED + "" + BOLD + "<<Previous Page");
		previousPage.setItemMeta(ppim);
		
		ItemStack page = new ItemStack(Material.NETHER_STAR, 1);
		ItemMeta pim = page.getItemMeta();
		pim.setDisplayName(RED + "" + BOLD + "Page: " + GOLD + "" + BOLD + pageNo);
		List<String> lore = new ArrayList<String>();
		lore.add(GREEN + "" + BOLD + "Click to return");
		lore.add(GREEN + "" + BOLD + "to the main page!");
		pim.setLore(lore);
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
	
	public void openPlayerReportGUI(String name, int pageNo) {
		List<Inventory> pages = getPlayerReportsGUI(name);
		if(!p.getOpenInventory().getTitle().contains("'s")) {
			p.openInventory(pages.get(pageNo - 1));
		} else {
			p.getOpenInventory().getTopInventory().setContents(pages.get(pageNo - 1).getContents());
		}
		setViewing(true);
	}
	
	public void openNextPlayerReportGUIPage(int currentPageNo, String name) {
		List<Inventory> pages = getPlayerReportsGUI(name);
		if(currentPageNo < pages.size()) {
			openPlayerReportGUI(name, currentPageNo + 1);
		}
	}
	
	public void openPreviousPlayerReportGUIPage(int currentPageNo, String name) {
		if(currentPageNo > 1) {
			openPlayerReportGUI(name, currentPageNo - 1);
		}
	}
	
	public List<Inventory> getPlayerReportsGUI(String name) {
		List<Inventory> pages = new ArrayList<Inventory>();
		
		int itemNo = 0;
		int pageNo = 0;
		Inventory inv = xs.getServer().createInventory(null, 54 , RED + name + "'s Reports");
		if(rm.getReports(name) == null) {
			pages.add(formatSecondaryPageFooter(inv, 1));
			return pages;
		}
		for(Entry<String, String> e : rm.getReports(name).entrySet()) {
			if(itemNo == 45) {
				itemNo = 0;
				pageNo++;
				pages.add(formatSecondaryPageFooter(inv, pageNo));
				inv = xs.getServer().createInventory(null, 54, RED + name + "'s Reports");
			}
			
			String playerName = e.getKey();
			List<String> lore =  splitLore(e.getValue(), 20);
			ItemStack skull = makeSkull(playerName, playerName, lore);
			inv.setItem(itemNo, skull);
			itemNo++;
		}
		
		pageNo++;
		pages.add(formatSecondaryPageFooter(inv, pageNo));
		return pages;
	}
	
	private List<String> splitLore(String text, int characters) {
	    List<String> lore = new ArrayList<>();
	    String[] words = text.split(" ");
	    int wordsUsed = 0;
	    while (wordsUsed < words.length) {
	        String line = "";
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
	
	public void openReportDetailGUI(String reportedName, String reporterName) {
		Inventory inv = getReportDetailInventory(reportedName, reporterName);
		p.openInventory(inv);
	}
	
	private Inventory getReportDetailInventory(String reportedName, String reporterName) {
		Inventory inv = xs.getServer().createInventory(null, 54, RED + "Report Manager");
		HashMap<String, String> playerReport = rm.getReports(reportedName);
		
		ItemStack report = new ItemStack(Material.PAPER);
		ItemMeta rim = report.getItemMeta();
		rim.setDisplayName(reporterName);
		rim.setLore(splitLore(playerReport.get(reporterName), 20));
		report.setItemMeta(rim);
		
		ItemStack resolve = new ItemStack(Material.EMERALD_BLOCK);
		ItemMeta reim = resolve.getItemMeta();
		reim.setDisplayName(GREEN + "Resolve Report");
		List<String> resolveLore = new ArrayList<String>();
		resolveLore.add(RED + "Click to remove the report");
		reim.setLore(resolveLore);
		resolve.setItemMeta(reim);
		
		ItemStack back = new ItemStack(Material.REDSTONE_BLOCK);
		ItemMeta bim = back.getItemMeta();
		bim.setDisplayName(GREEN + "" + BOLD + "Back");
		List<String> backLore = new ArrayList<String>();
		backLore.add(RED + "Click to return to");
		backLore.add(RED + "the previous page!");
		bim.setLore(backLore);
		back.setItemMeta(bim);
		
		ItemStack tpToReporter = new ItemStack(Material.COMPASS);
		ItemMeta reporterim = tpToReporter.getItemMeta();
		reporterim.setDisplayName(GREEN + "Teleport to " + reporterName);
		List<String> reporterLore = new ArrayList<String>();
		reporterLore.add(RED + "Click to teleport to the reporter");
		reporterim.setLore(reporterLore);
		tpToReporter.setItemMeta(reporterim);
		
		ItemStack tpToReported = new ItemStack(Material.COMPASS);
		ItemMeta reportedim = tpToReported.getItemMeta();
		reportedim.setDisplayName(GREEN + "Teleport to " + reportedName);
		List<String> reportedLore = new ArrayList<String>();
		reportedLore.add(RED + "Click to teleport to the reported Player");
		reportedim.setLore(reportedLore);
		tpToReported.setItemMeta(reportedim);
		
		for(int i = 0; i < 54; i++) {
			inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
		}
		
		inv.setItem(13, report);
		inv.setItem(24, resolve);
		inv.setItem(20, back);
		inv.setItem(38, tpToReported);
		inv.setItem(42, tpToReporter);
				
		return inv;
	}

}
