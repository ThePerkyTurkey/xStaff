package tk.ThePerkyTurkey.XStaff.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Utils.Messages;
import tk.ThePerkyTurkey.XStaff.Utils.WarnManager;

public class CommandWarn implements CommandExecutor {

	private Messages msg;
	private WarnManager wm;

	public CommandWarn(XStaff xs) {
		this.msg = xs.getMessages();
		this.wm = xs.getWarnManager();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (!p.hasPermission("xstaff.warn.warn")) {
				p.sendMessage(msg.get("noPerms"));
				return true;
			}
		}

		switch (args.length) {

		case 0:
			return false;

		case 1:
			return false;

		default:
			StringBuilder sb = new StringBuilder();
			for (String s : args) {
				sb.append(s + " ");
			}

			String reason = sb.toString().replaceFirst(args[0], "");

			wm.warn(args[0], sender, reason);
			return true;
		}
	}

}
