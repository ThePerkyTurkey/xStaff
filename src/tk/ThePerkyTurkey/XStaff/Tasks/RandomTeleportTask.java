package tk.ThePerkyTurkey.XStaff.Tasks;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Utils.ConfigManager;
import tk.ThePerkyTurkey.XStaff.Utils.Messages;
import tk.ThePerkyTurkey.XStaff.Utils.PlayerManager;

public class RandomTeleportTask {
	
	public RandomTeleportTask(XStaff xs, Player p) {
		
		Messages msg = xs.getMessages();
		ConfigManager cm = xs.getConfigManager();
		
		int randIndex = new Random().nextInt(xs.getServer().getOnlinePlayers().length);
		int currentIndex = 0;
		
		for(Player target : Bukkit.getServer().getOnlinePlayers()) {
			
			if(Bukkit.getServer().getOnlinePlayers().length == 1) {
				p.sendMessage(msg.get("randomTeleportError"));
				return;
			}
			if(currentIndex++ == randIndex) {
				
				if(target.equals(p)) {
					currentIndex--;
					continue;
				}
				
				if(!PlayerManager.isStaff(target)) {
					p.teleport(target);
					p.sendMessage(msg.get("randomTeleportUse", target.getName()));
					return;
				}
				
				if(PlayerManager.isStaff(target) && cm.getBoolean("inlcude-staff")) {
					p.teleport(target);
					p.sendMessage(msg.get("randomTeleportUse", target.getName()));
					return;
				}
				p.sendMessage(msg.get("randomTeleportError"));
				}
			}
		
	}

}
