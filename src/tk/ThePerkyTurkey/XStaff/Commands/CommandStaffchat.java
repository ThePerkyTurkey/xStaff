package tk.ThePerkyTurkey.XStaff.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Tasks.StaffChatTask;
import tk.ThePerkyTurkey.XStaff.Utils.Messages;
import tk.ThePerkyTurkey.XStaff.Utils.PlayerManager;

public class CommandStaffchat implements CommandExecutor {

	private Messages msg;
	private XStaff xs;

	public CommandStaffchat(XStaff xs) {
		this.msg = xs.getMessages();
		this.xs = xs;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (!p.hasPermission("xstaff.chat.send")) {
				p.sendMessage(msg.get("noPerms"));
				return true;
			}
		}

		switch (args.length) {

		case 0:
			if (sender instanceof ConsoleCommandSender) {
				sender.sendMessage(ChatColor.RED + "On console, use /schat <message>!");
				return true;
			}

			if (sender instanceof Player) {
				Player p = (Player) sender;
				PlayerManager.toggleStaffChat(p);
				return true;
			}

		default:
			StringBuilder sb = new StringBuilder();
			for (String s : args) {
				sb.append(s + " ");
			}

			new StaffChatTask(xs, sender, sb.toString());
		}

		return true;
	}

}
