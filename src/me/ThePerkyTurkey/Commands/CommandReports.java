package me.ThePerkyTurkey.Commands;

import static org.bukkit.ChatColor.RED;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ThePerkyTurkey.XStaff;
import me.ThePerkyTurkey.Inventories.ReportGUI;

public class CommandReports implements CommandExecutor {
	
	private XStaff xs;
	
	public CommandReports(XStaff xs) {
		this.xs = xs;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(RED + "You must be a player to execute this command!");
			return true;
		}
		
		Player p = (Player) sender;
		ReportGUI rGUI = new ReportGUI(xs, p);
		rGUI.open(1);
		
		return true;
	}
	
	

}
