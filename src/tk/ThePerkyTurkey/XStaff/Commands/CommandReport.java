package tk.ThePerkyTurkey.XStaff.Commands;

import static org.bukkit.ChatColor.RED;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Utils.Messages;
import tk.ThePerkyTurkey.XStaff.Utils.PlayerManager;
import tk.ThePerkyTurkey.XStaff.Utils.ReportManager;

public class CommandReport implements CommandExecutor {

	private Messages msg;
	private ReportManager rm;
	private XStaff xs;

	public CommandReport(XStaff xs) {
		this.msg = xs.getMessages();
		this.xs = xs;
		this.rm = xs.getReportManager();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(RED + "You must be a player to execute this command!");
			return true;
		}

		Player p = (Player) sender;

		if (!p.hasPermission("xstaff.reports.report")) {
			p.sendMessage(msg.get("noPerms"));
			return true;
		}

		switch (args.length) {
		case 0:
			return false;
		case 1:
			return false;
		default:
			Player pl = Bukkit.getPlayerExact(args[0]);
			if (pl == null) {
				if (p.hasPermission("xstaff.vanish.see")) {
					p.sendMessage(msg.get("offline"));
					return true;
				}
			}
			if(PlayerManager.isVanished(pl) && PlayerManager.isVanishedFrom(pl, p)) {
				p.sendMessage(msg.get("offline"));
				return true;
			}

			if (rm.hasReported(p.getName(), pl.getName())) {
				p.sendMessage(msg.get("reportDeny"));
				return true;
			}

			if (pl.equals(p)) {
				p.sendMessage(msg.get("reportSelf"));
				return true;
			}

			List<String> report = new ArrayList<String>();
			for (int i = 1; i <= args.length - 1; i++) {
				if (!(i == args.length - 1)) {
					report.add(args[i] + " ");
				} else {
					report.add(args[i]);
				}
			}

			rm.createReport(p, pl, report);
		}

		return true;
	}

}
