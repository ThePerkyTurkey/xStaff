package tk.ThePerkyTurkey.XStaff.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Inventories.WarnsGUI;
import tk.ThePerkyTurkey.XStaff.Utils.Messages;

public class CommandWarns implements CommandExecutor{
	
	private XStaff xs;
	private Messages msg;
	
	public CommandWarns(XStaff xs) {
		this.xs = xs;
		this.msg = xs.getMessages();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("You must be a player to execute this command!");
			return true;
		}
		
		Player p = (Player) sender;
		
		if(!p.hasPermission("xstaff.warn.view")) {
			p.sendMessage(msg.get("noPerms"));
			return true;
		}
		
		switch(args.length) {
		case 0:
			return false;
			
		case 1:
			if(xs.getWarnManager().getWarnings(args[0]) == null) {
				p.sendMessage(msg.get("noWarns"));
				return true;
			}
			WarnsGUI wGUI = new WarnsGUI(xs, p, args[0]);
			wGUI.open(1);
			return true;
		}
		
		return true;
	}

}
