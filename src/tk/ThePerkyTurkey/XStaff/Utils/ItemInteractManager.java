package tk.ThePerkyTurkey.XStaff.Utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Tasks.DetailsInteractTask;
import tk.ThePerkyTurkey.XStaff.Tasks.FreezeInteractTask;
import tk.ThePerkyTurkey.XStaff.Tasks.OnlineStaffInteractTask;
import tk.ThePerkyTurkey.XStaff.Tasks.RandomTeleportTask;
import tk.ThePerkyTurkey.XStaff.Tasks.ReportsInteractTask;
import tk.ThePerkyTurkey.XStaff.Tasks.TeleportInteractTask;
import tk.ThePerkyTurkey.XStaff.Tasks.VanishInteractTask;

public class ItemInteractManager {
	
	@SuppressWarnings("deprecation")
	public ItemInteractManager(ItemStack is, Player p, XStaff xs) {
		
		Messages msg = xs.getMessages();
		
		String FreezeID = new StaffItemManager("Freeze", xs).getItemID();
		String VanishEnID = new StaffItemManager("Vanish", xs).getVanishEnID();
		String VanishDiID = new StaffItemManager("Vanish", xs).getVanishDiID();
	    String ReportsID = new StaffItemManager("Reports", xs).getItemID();
		String DetailsID = new StaffItemManager("Details", xs).getItemID();
		String OnlineStaffID = new StaffItemManager("OnlineStaff", xs).getItemID();
		String RandomTeleportID = new StaffItemManager("RandomTeleport", xs).getItemID();
		String WallPassID = new StaffItemManager("WallPass", xs).getItemID();
		
		int ID = is.getTypeId();
		int Durability = is.getDurability();
		String fullID = ID + ":" + Durability;
		
		if(fullID.equals(formatID(FreezeID))) {
			if(p.hasPermission("xstaff.mode.freeze")) {
				new FreezeInteractTask(xs, p);
			} else {
				p.sendMessage(msg.get("noPerms"));
			}
		}
		
		if(fullID.equals(formatID(ReportsID))) {
			if(p.hasPermission("xstaff.mode.reports")) {
				new ReportsInteractTask(xs, p);
			} else {
				p.sendMessage(msg.get("noPerms"));
			}
		}
		
		if(fullID.equals(formatID(VanishEnID)) || fullID.equals(formatID(VanishDiID))) {
			if(p.hasPermission("xstaff.mode.vanish")) {
				new VanishInteractTask(p);
			} else {
				p.sendMessage(msg.get("noPerms"));
			}
		}
		
		if(fullID.equals(formatID(RandomTeleportID))) {
			if(p.hasPermission("xstaff.mode.randomtp")) {
				new RandomTeleportTask(xs, p);
			} else {
				p.sendMessage(msg.get("noPerms"));
			}
		}
		
		if(fullID.equals(formatID(OnlineStaffID))) {
			if(p.hasPermission("xstaff.mode.onlinestaff")) {
				new OnlineStaffInteractTask(xs, p);
			} else {
				p.sendMessage(msg.get("noPerms"));
			}
		}
		
		if(fullID.equals(formatID(WallPassID))) {
			if(p.hasPermission("xstaff.mode.tp")) {
				new TeleportInteractTask(xs, p);
			} else {
				p.sendMessage(msg.get("noPerms"));
			}
		}
		
		if(fullID.equals(formatID(DetailsID))) {
			if(p.hasPermission("xstaff.mode.details")) {
				new DetailsInteractTask(p, xs);
			} else {
				p.sendMessage(msg.get("noPerms"));
			}
		}
	}
	
	private String formatID(String s) {
		if(s.contains(":")) {
			return s;
		}
		
		return s + ":" + 0;
	}

}
