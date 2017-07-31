package tk.ThePerkyTurkey.XStaff.Tasks;

import org.bukkit.entity.Player;

import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Inventories.ReportGUI;
import tk.ThePerkyTurkey.XStaff.Utils.TargetUtil;

public class ReportsInteractTask {
	
	public ReportsInteractTask(XStaff xs, Player p) {
		ReportGUI rGUI = new ReportGUI(xs, p);
		
		Player target = TargetUtil.getTargetPlayer(p);
		if(target == null) {
			rGUI.open(1);
		} else {
			rGUI.openPlayerReportGUI(target.getName(), 1);
		}
	}
}
