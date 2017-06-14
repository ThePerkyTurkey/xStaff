package tk.ThePerkyTurkey.XStaff.Tasks;

import org.bukkit.entity.Player;

import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Inventories.ReportGUI;

public class ReportsInteractTask {
	
	public ReportsInteractTask(XStaff xs, Player p) {
		ReportGUI rGUI = new ReportGUI(xs, p);
		
		rGUI.open(1);
	}
}
