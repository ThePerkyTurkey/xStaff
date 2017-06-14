package tk.ThePerkyTurkey.XStaff.Commands;

import static org.bukkit.ChatColor.RED;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Inventories.ReportGUI;
import tk.ThePerkyTurkey.XStaff.Utils.Messages;
import tk.ThePerkyTurkey.XStaff.Utils.ReportManager;

public class CommandReports implements CommandExecutor {
	
	private XStaff xs;
	private Messages msg;
	
	public CommandReports(XStaff xs) {
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
		
		if(!p.hasPermission("xstaff.reports")) {
			p.sendMessage(msg.get("noPerms"));
			return true;
		}
		
		switch(args.length) {
		case 0:
			ReportGUI rGUI = new ReportGUI(xs, p);
			rGUI.open(1);
			break;
			
		case 1:
			ReportManager rm = xs.getReportManager();
			String target = args[0];
			
			if(rm.getReports(target) == null) {
				p.sendMessage(msg.get("noReports"));
				return true;
			}
			
			ReportGUI reportGUI = new ReportGUI(xs, p);
			reportGUI.openPlayerReportGUI(target, 1);
			
		}
		
		return true;
	}
	
	

}
