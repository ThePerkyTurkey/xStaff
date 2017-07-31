package tk.ThePerkyTurkey.XStaff.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Inventories.DetailsGUI;
import tk.ThePerkyTurkey.XStaff.Utils.Messages;
import tk.ThePerkyTurkey.XStaff.Utils.PlayerManager;

public class CommandDetails implements CommandExecutor {
	
	private XStaff xs;
	private Messages msg;
	
	public CommandDetails(XStaff xs) {
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
		
		if(!p.hasPermission("xstaff.details.command")) {
			p.sendMessage(msg.get("noPerms"));
			return true;
		}
		
		switch(args.length) {
		case 0:
			return false;
			
		case 1:
			Player target = xs.getServer().getPlayer(args[0]);
			if(target == null) {
				p.sendMessage(msg.get("offline"));
				return true;
			}
			if(PlayerManager.isVanished(target) && PlayerManager.isVanishedFrom(target, p)) {
				p.sendMessage(msg.get("offline"));
			}
			
			DetailsGUI dGUI = new DetailsGUI(xs, p, target);
			dGUI.open();
		}
		
		return true;
	}

}
