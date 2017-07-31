package tk.ThePerkyTurkey.XStaff.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Utils.Messages;
import tk.ThePerkyTurkey.XStaff.Utils.NotesManager;

public class CommandRemovenote implements CommandExecutor {
	
	private Messages msg;
	private NotesManager nm;
	
	public CommandRemovenote(XStaff xs) {
		this.msg = xs.getMessages();
		this.nm = xs.getNotesManager();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(!p.hasPermission("xstaff.notes.remove")) {
				p.sendMessage(msg.get("noPerms"));
				return true;
			}
		}
		
		switch(args.length) {
		
		case 0:
			return false;
			
		case 1:
			nm.removeNote(args[0], sender, null);
			return true;
			
		case 2:
			nm.removeNote(args[0], sender, args[1]);
			return true;
		}
		
		return true;
	}

}
