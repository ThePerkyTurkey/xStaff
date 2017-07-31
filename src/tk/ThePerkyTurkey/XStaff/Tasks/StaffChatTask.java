package tk.ThePerkyTurkey.XStaff.Tasks;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.ChatColor;
import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Utils.ConfigManager;

public class StaffChatTask {
	
	public StaffChatTask(XStaff xs, CommandSender staff, String msg) {
		ConfigManager cm = xs.getConfigManager();
		String rawFormat = cm.getString("staff-chat-format");
		
		String format = rawFormat.replaceAll("\\{player\\}", staff.getName())
				.replaceAll("\\{msg\\}", msg);
		
		String finalFormat = format;
		if(staff instanceof Player) {
			Player p = (Player) staff;
			if(xs.getServer().getPluginManager().isPluginEnabled("Vault")) {
				finalFormat = format.replaceAll("\\{rank\\}", xs.getPermissionHandler().getPrimaryGroup(p));
			}
		} else {
			finalFormat = format.replaceAll("\\{rank\\}", "");
		}
		
		for(Player player : xs.getServer().getOnlinePlayers()) {
			if(player.hasPermission("xstaff.chat.recieve")) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', finalFormat));
			}
		}
		
		xs.getLogger().info("[StaffChat] " + ChatColor.translateAlternateColorCodes('&', finalFormat));
		
	}

}
