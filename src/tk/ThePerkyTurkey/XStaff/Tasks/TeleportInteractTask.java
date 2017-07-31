package tk.ThePerkyTurkey.XStaff.Tasks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Utils.ConfigManager;
import tk.ThePerkyTurkey.XStaff.Utils.Direction;
import tk.ThePerkyTurkey.XStaff.Utils.Messages;
import tk.ThePerkyTurkey.XStaff.Utils.TargetUtil;

public class TeleportInteractTask {
	
	public TeleportInteractTask(XStaff xs, Player p) {
		
		ConfigManager cm = xs.getConfigManager();
		Messages msg = xs.getMessages();
		
		Block target = TargetUtil.getTargetBlock(p, cm.getInt("wall-pass-range"));
		
		if(target.getType() == Material.AIR) {
			p.sendMessage(msg.get("wallPassError"));
			return;
		} else {
			
			Direction d = TargetUtil.getDirection(p);
			
			switch(d) {
			case NORTH:
					p.teleport(new Location(target.getWorld(), target.getX(), target.getY(), target.getZ() + 1));
					p.sendMessage(msg.get("wallPassSuccess"));
				return;
			case EAST:
					p.teleport(new Location(target.getWorld(), target.getX() - 1, target.getY(), target.getZ()));
					p.sendMessage(msg.get("wallPassSuccess"));
				return;
			case SOUTH:
					p.teleport(new Location(target.getWorld(), target.getX(), target.getY(), target.getZ() - 1));
					p.sendMessage(msg.get("wallPassSuccess"));
				return;
			case WEST:
					p.teleport(new Location(target.getWorld(), target.getX() + 1, target.getY(), target.getZ()));
					p.sendMessage(msg.get("wallPassSuccess"));
				return;
			}
			
			p.sendMessage(msg.get("wallPassError"));
		}
	}

}
