package tk.ThePerkyTurkey.XStaff.Inventories;

import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.RED;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import tk.ThePerkyTurkey.XLibrary.XPageInventory;
import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Utils.PlayerManager;

public class OnlineStaffGUI implements XPageInventory{
	
	private XStaff xs;
	private Player p;
	
	private String name;
	
	private static HashMap<Player, OnlineStaffGUI> instances = new HashMap<Player, OnlineStaffGUI>();
	
	public OnlineStaffGUI(XStaff xs, Player p) {
		this.xs = xs;
		this.p = p;
		
		instances.put(p, this);
		
		setName(xs.getConfigManager().getString("online-staff-title"));
	}

	@Override
	public void open(int pageNo) {
		List<Inventory> pages = generatePages();
		if(!p.getOpenInventory().getTitle().equals(name)) {
			p.openInventory(pages.get(pageNo - 1));
		} else {
			p.getOpenInventory().getTopInventory().setContents(pages.get(pageNo - 1).getContents());
		}
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
		List<Inventory> invs = new ArrayList<>();
		List<Player> players = new ArrayList<Player>();
		if(xs.getConfigManager().getBoolean("only-in-staff-mode")) {
			xs.getPlayerManager();
			players = PlayerManager.inStaffMode;
		} else {
			for(Player p : xs.getServer().getOnlinePlayers()) {
				if(p.hasPermission("xstaff.toggle.self") || PlayerManager.isStaff(p)) {
					players.add(p);
				}
			}
		}
		
		int slotNo = 0;
		int pageNo = 0;
		Inventory inv = xs.getServer().createInventory(null, 54, name);
		for(Player p : players) {
			if(slotNo == 45) {
				pageNo++;
				slotNo = 0;
				invs.add(formatPageFooter(inv, pageNo));
				inv = xs.getServer().createInventory(null, 54, name);
			}
			
			String name = p.getName();
			ItemStack skull = makeSkull(name, name, null);
			inv.setItem(slotNo, skull);
			slotNo++;
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
		this.name = ChatColor.translateAlternateColorCodes('&', name);
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
		sm.setDisplayName(name);
		if(lore != null) {
			sm.setLore(lore);
		}
		skull.setItemMeta(sm);
		
		return skull;
	}
}
