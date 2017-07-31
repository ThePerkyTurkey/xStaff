package tk.ThePerkyTurkey.XStaff.Inventories;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import tk.ThePerkyTurkey.XStaff.XClickManager;
import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Utils.Messages;
import tk.ThePerkyTurkey.XStaff.Utils.ReportManager;

public class ReportGUIClickManager implements Listener,XClickManager{

	private XStaff xs;
	private InventoryClickEvent e;
	private ReportGUI rGUI;
	private ReportManager rm;
	private Messages msg;
	
	private Player p;
	
	private int previousPageNo = -1;
	private int secondaryPageNo = -1;
	private String secondaryName = null;
	
	public ReportGUIClickManager(XStaff xs) {
		this.xs = xs;
		this.rm = xs.getReportManager();
		this.msg = xs.getMessages();
	}
	
	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) {
			return;
		}
		
		String name = e.getInventory().getTitle();
		if(!name.contains("Report")) {
			return;
		}
		
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) {
			return;
		}
		
		if(!e.getInventory().getName().contains("Report")) {
			return;
		}
		
		e.setCancelled(true);
		
		if(e.getCurrentItem() == null || e.getCurrentItem().equals(Material.AIR)) {
			return;
		}
		
		Player p = (Player) e.getWhoClicked();
		
		this.p = p;
		this.e = e;
		this.rGUI = new ReportGUI(xs, p);
		e.setCancelled(true);
		manage();
	}
	
	@Override
	public void manage() {
		String invName = e.getInventory().getName();
		if(invName.equals("\u00A7cReports")) {
			mainInterfaceClick();
		} else if (invName.contains("'s")){
			secondaryInterfaceClick();
		} else if(invName.contains("Manager")) {
			reportManagerInterfaceClick();
		}
		
	}
	
	private void mainInterfaceClick() {
		String[] parts = e.getInventory().getItem(49).getItemMeta().getDisplayName().split(": §6§l");
		int pageNo = Integer.parseInt(parts[1]);
		ItemStack is = e.getCurrentItem();
		switch(is.getType()) {
		case PAPER:
			String name = is.getItemMeta().getDisplayName();
			if(name.contains("Next")) {
				rGUI.openNextPage(pageNo);
			} else if(name.contains("Previous")) {
				rGUI.openPreviousPage(pageNo);
			}
			break;
		case SKULL_ITEM:
			String playerName = is.getItemMeta().getDisplayName();
			if(xs.getReportManager().getReports(playerName) == null) {
				ItemStack resolved = new ItemStack(Material.REDSTONE_BLOCK);
				ItemMeta im = resolved.getItemMeta();
				im.setDisplayName(ChatColor.RED + "All reports for this player have already been resolved!");
				resolved.setItemMeta(im);
				e.getInventory().setItem(e.getSlot(), resolved);
			} else {
				this.previousPageNo = pageNo;
				rGUI.openPlayerReportGUI(playerName, 1);
			}
		default:
			return;
		}
	}
	
	private void secondaryInterfaceClick() {
		String[] parts = e.getInventory().getItem(49).getItemMeta().getDisplayName().split(": §6§l");
		int pageNo = Integer.parseInt(parts[1]);
		ItemStack is = e.getCurrentItem();
		String[] nameParts = e.getInventory().getTitle().split("'s");
		String name = nameParts[0].replaceAll("§c", "");
		switch(is.getType()) {
		case NETHER_STAR:
			if(previousPageNo == -1) {
				rGUI.open(1);
			} else {
				rGUI.open(previousPageNo);
			}
		case PAPER:
			String paperName = is.getItemMeta().getDisplayName();
			if(paperName.contains("Next")) {
				rGUI.openNextPlayerReportGUIPage(pageNo, name);
			}  else if(paperName.contains("Previous")) {
				rGUI.openPreviousPlayerReportGUIPage(pageNo, name);
			}
			break;
		case SKULL_ITEM:
			if(!p.hasPermission("xstaff.reports.manage")) {
				p.closeInventory();
				p.sendMessage(msg.get("noPerms"));
				return;
			}
			String playerName = is.getItemMeta().getDisplayName();
			this.secondaryPageNo = pageNo;
			this.secondaryName = name;
			rGUI.openReportDetailGUI(name, playerName);
		default:
			break;
		}
	}
	
	private void reportManagerInterfaceClick() {
		ItemStack is = e.getCurrentItem();
		switch(is.getType()) {
		case REDSTONE_BLOCK:
			if(secondaryPageNo == -1 || secondaryName == null) {
				rGUI.open(1);
			} else {
				rGUI.openPlayerReportGUI(secondaryName, secondaryPageNo);
			}
			break;
		case EMERALD_BLOCK:
			String[] reportedNameParts = e.getInventory().getItem(38).getItemMeta().getDisplayName().split("to ");
			String[] reporterNameParts = e.getInventory().getItem(42).getItemMeta().getDisplayName().split("to ");
			String reportedName = reportedNameParts[1];
			String reporterName = reporterNameParts[1];
			
			String loc = reportedName + "." + reporterName;
			
			//To check if that player is still reported. Another player may have already resolved that report
			if(rm.hasReported(reporterName, reportedName)) {
				rm.removeReport(loc);
				p.sendMessage(msg.get("reportRemoveSuccess"));
			} else {
				p.sendMessage(msg.get("reportRemoveFailure"));
			}

			if(secondaryPageNo == -1 || secondaryName == null) {
				rGUI.open(1);
			} else {
				rGUI.openPlayerReportGUI(secondaryName, secondaryPageNo);
			}
			break;
		case COMPASS:
			String[] nameParts = is.getItemMeta().getDisplayName().split("to ");
			Player target = xs.getServer().getPlayerExact(nameParts[1]);
			if(target == null) {
				p.sendMessage(msg.get("offline"));
				break;
			} else {
				p.sendMessage(msg.get("teleport", target.getName()));
				p.teleport(target);
			}
		default:
			break;
			
		}
	}
}
