package tk.ThePerkyTurkey.XStaff.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Utils.SmallClasses.StaffMember;
import tk.ThePerkyTurkey.XStaff.Utils.SmallClasses.Trivalue;

public class WarnManager {

	private XStaff xs;
	private File baseFile;
	private FileConfiguration warns;
	private Messages msg;

	public WarnManager(XStaff xs) {
		this.xs = xs;
		this.baseFile = xs.getConfigManager().getWarnsFile();
		this.warns = xs.getConfigManager().getWarns();
		this.msg = xs.getMessages();
	}

	public void warn(String warned, CommandSender staff, String why) {

		String reason = why.replaceFirst(" ", "");

		String totalWarnsString = warns.getString(warned + ".total-warns");
		if (totalWarnsString == null) {
			warns.set(warned + ".total-warns", 0);
			totalWarnsString = "0";
		}
		int totalWarns = Integer.valueOf(totalWarnsString) + 1;

		warns.set(warned + "." + totalWarns + ".staff", staff.getName());
		warns.set(warned + "." + totalWarns + ".reason", reason);

		warns.set(warned + ".total-warns", totalWarns);
		staff.sendMessage(msg.get("warnSuccess", warned));
		Player target = xs.getServer().getPlayer(warned);
		if (target != null) {
			target.sendMessage(msg.get("warnPlayer", staff.getName(), reason));
		}
		
		for(Player player : xs.getServer().getOnlinePlayers()) {
			if(player.hasPermission("xstaff.notify")) {
				player.sendMessage(msg.get("notifyWarn", staff.getName(), warned, reason));
			}
		}

		try {
			warns.save(baseFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		checkWarnings(warned);

	}

	public void unWarn(String p, CommandSender staff, String id) {

		String totalWarnsString = warns.getString(p + ".total-warns");
		if (totalWarnsString == null || totalWarnsString.equals("0")) {
			staff.sendMessage(msg.get("noWarns"));
			return;
		}

		int totalWarns = Integer.valueOf(totalWarnsString);

		if (id == null || Integer.valueOf(id) > totalWarns) {
			warns.set(p + "." + totalWarns, null);
			warns.set(p + ".total-warns", totalWarns - 1);
			staff.sendMessage(msg.get("warnRemove", p));
		} else {
			warns.set(p + "." + id, null);
			warns.set(p + ".total-warns", totalWarns - 1);
			
			List<Trivalue> data = new ArrayList<Trivalue>();
			for(int i = Integer.valueOf(id) + 1; i < totalWarns + 1; i ++) {
				String player = warns.getString(p + "." + i + ".staff");
				String reason = warns.getString(p + "." + i + ".reason");
				Trivalue tri = new Trivalue(String.valueOf(i), player, reason);
				data.add(tri);
			}
			
			for(Trivalue tri : data) {
				warns.set(p + "." + (Integer.valueOf(tri.getA()) - 1) + ".staff" , tri.getC());
				warns.set(p + "." + (Integer.valueOf(tri.getA()) - 1) + ".reason", tri.getC());
			}
			
			warns.set(p + "." + totalWarns, null);
			staff.sendMessage(msg.get("warnRemoveId", p, id));
		}
		
		for (Player player : xs.getServer().getOnlinePlayers()) {
			if (player.hasPermission("xstaff.notify")) {
				player.sendMessage(msg.get("notifyUnwarn", staff.getName(), p));
			}
		}

		try {
			warns.save(baseFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Format : LinkedHashMap<Staff member, reason>
	public LinkedHashMap<StaffMember, String> getWarnings(String p) {
		LinkedHashMap<StaffMember, String> warnings = new LinkedHashMap<StaffMember, String>();

		String totalWarnsString = warns.getString(p + ".total-warns");
		if (totalWarnsString == null || totalWarnsString == "0") {
			return null;
		}

		int totalWarns = Integer.valueOf(totalWarnsString);

		for (int i = 1; i < totalWarns + 1; i++) {
			warnings.put(new StaffMember(warns.getString(p + "." + String.valueOf(i) + ".staff")),
					warns.getString(p + "." + String.valueOf(i) + ".reason"));
		}

		return warnings;
	}

	private void checkWarnings(String p) {
		ConfigManager cm = xs.getConfigManager();
		String totalWarnsString = warns.getString(p + ".total-warns");
		if (totalWarnsString == null) {
			return;
		}
		List<String> commands = cm.getList(totalWarnsString + ".commands", false);
		if (commands != null && !commands.isEmpty()) {
			for (String cmd : commands) {
				String command = ChatColor.translateAlternateColorCodes('&', cmd.replaceAll("\\{player\\}", p).replaceAll("\\{total\\}", totalWarnsString));

				xs.getServer().dispatchCommand(xs.getServer().getConsoleSender(), command);
			}
		}
	}

}
