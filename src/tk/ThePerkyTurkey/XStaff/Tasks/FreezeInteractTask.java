package tk.ThePerkyTurkey.XStaff.Tasks;

import org.bukkit.entity.Player;

import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Utils.Messages;
import tk.ThePerkyTurkey.XStaff.Utils.PlayerManager;
import tk.ThePerkyTurkey.XStaff.Utils.TargetUtil;

public class FreezeInteractTask {
	
	public FreezeInteractTask(XStaff xs, Player p) {
		
		Messages msg = xs.getMessages();
		
		Player target = TargetUtil.getTargetPlayer(p);
		if(target == null) {
			p.sendMessage(msg.get("notLooking"));
			return;
		}
		
		if(target.hasPermission("xstaff.freeze.exempt")) {
			p.sendMessage(msg.get("freezeExempt"));
			return;
		}
		
		if(PlayerManager.isFrozen(target)) {
			p.sendMessage(msg.get("freezeDisable", target.getName()));
		} else {
			p.sendMessage(msg.get("freezeEnable", target.getName()));
		}
		
		PlayerManager.toggleFreeze(target);
		
	}
}
