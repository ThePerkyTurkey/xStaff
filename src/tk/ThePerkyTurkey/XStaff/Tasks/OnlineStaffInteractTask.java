package tk.ThePerkyTurkey.XStaff.Tasks;

import org.bukkit.entity.Player;

import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Inventories.OnlineStaffGUI;

public class OnlineStaffInteractTask {
	
	public OnlineStaffInteractTask(XStaff xs, Player p) {
		OnlineStaffGUI olsGUI = new OnlineStaffGUI(xs, p);
		
		olsGUI.open(1);
	}

}
