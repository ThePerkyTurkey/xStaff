package tk.ThePerkyTurkey.XStaff.Commands;

import static org.bukkit.ChatColor.RED;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Utils.Messages;
import tk.ThePerkyTurkey.XStaff.Utils.PlayerManager;

public class CommandFreeze implements CommandExecutor {
	
	private XStaff xs;
	private Messages msg;

	public CommandFreeze(XStaff xs) {
		this.xs = xs;
		this.msg = xs.getMessages();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(RED + "You must be a player to execute this command!");
			return true;
		}
		
		Player p = (Player) sender;
		
		if(!p.hasPermission("xstaff.freeze")) {
			p.sendMessage(msg.get("noPerms"));
			return true;
		}
		
		switch(args.length) {
		case 0:
			return false;
			
		case 1:
			Player target = xs.getServer().getPlayerExact(args[0]);
			if(target == null) {
				p.sendMessage(msg.get("offline"));
				return true;
			}
			
			if(PlayerManager.isFrozen(target)) {
				p.sendMessage(msg.get("freezeDisable", target.getName()));
			} else {
				p.sendMessage(msg.get("freezeEnable", target.getName()));
			}
			
			PlayerManager.toggleFreeze(target);
		}
		
		return true;
	}
}
