package tk.ThePerkyTurkey.XStaff.Tasks;

import org.bukkit.entity.Player;

import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Inventories.DetailsGUI;
import tk.ThePerkyTurkey.XStaff.Utils.TargetUtil;

public class DetailsInteractTask {
	
	public DetailsInteractTask(Player p, XStaff xs) {
		
		Player target = TargetUtil.getTargetPlayer(p);
		if(target == null) {
			p.sendMessage(xs.getMessages().get("notLooking"));
			return;
		}
		
		DetailsGUI dGUI = new DetailsGUI(xs, p, target);
		dGUI.open();
	}

}
