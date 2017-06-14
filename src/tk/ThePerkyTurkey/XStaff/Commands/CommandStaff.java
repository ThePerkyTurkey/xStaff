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

public class CommandStaff implements CommandExecutor {
	
	private Messages msg;
	private XStaff xs;
	
	public CommandStaff(XStaff xs) {
		this.msg = new Messages(xs);
		this.xs = xs;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
				
		switch(args.length) {
		case 0:
			if(!(sender instanceof Player)) {
				sender.sendMessage(RED + "On console, use /staff <player>!");
				return true;
			}
			
			Player p = (Player) sender;
			
			if(!p.hasPermission("xstaff.toggle.self")) {
				p.sendMessage(msg.get("noPerms"));
				return true;
			}
			
			PlayerManager.toggleStaffMode(p);
			
			return true;
			
		default:
			
			Player pl = xs.getServer().getPlayerExact(args[0]);
			
			if(pl == null) {
				sender.sendMessage(msg.get("offline"));
				return true;
			}
			if(sender instanceof ConsoleCommandSender) {
				if(PlayerManager.isStaff(pl)) {
					sender.sendMessage(msg.get("staffToggleDisable", pl.getName()));
				} else {
					sender.sendMessage(msg.get("staffToggleEnable", pl.getName()));
				}
				PlayerManager.toggleStaffMode(pl);
				return true;
			}
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(PlayerManager.isVanished(pl) && PlayerManager.isVanishedFrom(pl, player)) {
					player.sendMessage(msg.get("offline"));
					return true;
				}
				if(!player.hasPermission("xstaff.toggle.others")) {
					player.sendMessage(msg.get("noPerms"));
					return true;
				}
				if(PlayerManager.isStaff(pl)) {
					sender.sendMessage(msg.get("staffToggleDisable", pl.getName()));
				} else {
					sender.sendMessage(msg.get("staffToggleEnable", pl.getName()));
				}
				PlayerManager.toggleStaffMode(pl);
				return true;
			}
			return true;
			
		}
	}

}
