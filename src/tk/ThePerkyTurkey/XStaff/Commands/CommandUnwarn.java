package tk.ThePerkyTurkey.XStaff.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Utils.Messages;
import tk.ThePerkyTurkey.XStaff.Utils.WarnManager;

public class CommandUnwarn implements CommandExecutor {
	
	private Messages msg;
	private WarnManager wm;
	
	public CommandUnwarn(XStaff xs) {
		this.msg = xs.getMessages();
		this.wm = xs.getWarnManager();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(!p.hasPermission("xstaff.warn.unwarn")) {
				p.sendMessage(msg.get("noPerms"));
				return true;
			}
		}
		
		switch(args.length) {
		
		case 0:
			return false;
			
		case 1:
			wm.unWarn(args[0], sender, null);
			return true;
			
		case 2:
			wm.unWarn(args[0], sender, args[1]);
			return true;
		}
		
		return true;
	}
	
	

}
