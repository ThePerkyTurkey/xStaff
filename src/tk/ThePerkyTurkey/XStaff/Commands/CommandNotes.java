package tk.ThePerkyTurkey.XStaff.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Inventories.NotesGUI;
import tk.ThePerkyTurkey.XStaff.Utils.Messages;

public class CommandNotes implements CommandExecutor{
	
	private XStaff xs;
	private Messages msg;
	
	public CommandNotes(XStaff xs) {
		this.xs = xs;
		this.msg = xs.getMessages();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a player to execute this command!");
			return true;
		}
		
		Player p = (Player) sender;
		
		if(!p.hasPermission("xstaff.notes.view")) {
			p.sendMessage(msg.get("noPerms"));
			return true;
		}
		
		switch(args.length) {
		
		case 0:
			return false;
			
		case 1:
			NotesGUI nGUI = new NotesGUI(xs, p, args[0]);
			
			nGUI.open(1);
			return true;
			
		}
		return true;
	}
	
	

}
