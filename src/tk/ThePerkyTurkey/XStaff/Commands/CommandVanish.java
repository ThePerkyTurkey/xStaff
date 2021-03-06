package tk.ThePerkyTurkey.XStaff.Commands;

import static org.bukkit.ChatColor.RED;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Utils.Messages;
import tk.ThePerkyTurkey.XStaff.Utils.PlayerManager;

public class CommandVanish implements CommandExecutor{
	
	private XStaff xs;
	private Messages msg;
	
	public CommandVanish(XStaff xs) {
		this.xs = xs;
		this.msg = xs.getMessages();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		switch(args.length) {
		case 0:
			
			if(!(sender instanceof Player)) {
				sender.sendMessage(RED + "On console, use /vanish <player> !");
				return true;
			}
			
			Player p = (Player) sender;
			
			if(!p.hasPermission("xstaff.vanish.toggle")) {
				p.sendMessage(msg.get("noPerms"));
				return true;
			}
			PlayerManager.toggleVanish(p);
			
			return true;
		default:
			
			Player target = xs.getServer().getPlayerExact(args[0]);
			if(target == null) {
				sender.sendMessage(msg.get("offline"));
				return true;
			}
			
			if(sender instanceof ConsoleCommandSender) {
				if(PlayerManager.isVanished(target)) {
					sender.sendMessage(msg.get("vanishToggleDisable", target.getName()));
				} else {
					sender.sendMessage(msg.get("vanishToggleEnable", target.getName()));
				}
				PlayerManager.toggleVanish(target);
				return true;
			}
			
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(!player.hasPermission("xstaff.vanish.toggle.other")) {
					player.sendMessage(msg.get("noPerms"));
					return true;
				}
				if(PlayerManager.isVanished(target) && PlayerManager.isVanishedFrom(target, player)) {
					player.sendMessage(msg.get("offline"));
					return true;
				}
				if(PlayerManager.isVanished(target)) {
					player.sendMessage(msg.get("vanishToggleDisable", target.getName()));
				} else {
					player.sendMessage(msg.get("vanishToggleEnable", target.getName()));
				}
				PlayerManager.toggleVanish(target);
				return true;
			}
			
		}
		return true;
	}

}
