package tk.ThePerkyTurkey.XStaff.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Utils.Messages;
import tk.ThePerkyTurkey.XStaff.Utils.NotesManager;

public class CommandAddnote implements CommandExecutor{
	
	private Messages msg;
	private NotesManager nm;
	
	public CommandAddnote(XStaff xs) {
		this.msg = xs.getMessages();
		this.nm = xs.getNotesManager();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(!p.hasPermission("xstaff.notes.add")) {
				p.sendMessage(msg.get("noPerms"));
				return true;
			}
		}
		
		switch(args.length) {
		
		case 0:
			return false;
			
		case 1:
			return false;
			
		default:
			StringBuilder sb = new StringBuilder();
			for(String s : args) {
				sb.append(s + " ");
			}
			
			String note = sb.toString().replaceFirst(args[0], "");
			nm.addNote(args[0], sender, note);
				
		}
		
		return true;
	}

}
