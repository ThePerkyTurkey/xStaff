package tk.ThePerkyTurkey.XStaff.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import tk.ThePerkyTurkey.XStaff.XStaff;

public class ReportManager {
	
	private XStaff xs;
	private ConfigManager cm;
	private File reportFile;
	private FileConfiguration reports;
	private Messages msgs;
	
	public ReportManager(XStaff xs) {
		this.xs = xs;
		this.msgs = xs.getMessages();		
		this.cm = xs.getConfigManager();
		this.reportFile = cm.getReportsFile();
		this.reports = cm.getReports();
	}
	
	public void createReport(final Player reporter, Player reported, List<String> args) {
		if(!ReportCooldown.can(reporter)) {
			reporter.sendMessage(msgs.get("cooldown"));
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		for(String s : args) {
			sb.append(s);
		}
		String finalMessage = sb.toString();
		
		reports.set(reported.getName() + "."+ reporter.getName(), finalMessage);
		try {
			reports.save(reportFile);
		} catch (IOException e) {
			xs.getLogger().severe("An error occured whilst saving reports.yml");
			e.printStackTrace();
		}
		
		reporter.sendMessage(msgs.get("reportConfirm", reported.getName()));
		
		for(Player p : xs.getServer().getOnlinePlayers()) {
			if(p.hasPermission("xstaff.report.notify") && cm.getBoolean("report-notify")) {
				p.sendMessage(msgs.get("notifyReport", reporter.getName(), reported.getName(), finalMessage));
			}
		}
		
		ReportCooldown.add(reporter);
		
	}
	
	public boolean hasReported(String reporter, String reported) {
		return reports.getString(reported + "." + reporter) != null;
	}
	
	//Format: HashMap<Reported Player, HashMap<Reporter, Report>>
	public HashMap<String, HashMap<String, String>> getReports() {
		File reportFile = new File(xs.getDataFolder().getPath() + "/reports.yml");
		FileConfiguration reports = YamlConfiguration.loadConfiguration(reportFile);
		
		HashMap<String, HashMap<String, String>> result = new HashMap<String, HashMap<String, String>>();
		for(String key : reports.getKeys(false)) {
			ConfigurationSection cs = reports.getConfigurationSection(key);
			HashMap<String, String> report = new HashMap<String, String>();
			for(String name : cs.getKeys(false)) {			
				report.put(name, cs.getString(name));
			}
			result.put(key, report);
		}
		
		return result;
	}
	
	public HashMap<String, String> getReports(String name) {
		return getReports().get(name);
	}
	
	public void removeReport(String loc) {
		reports.set(loc, null);
		try {
			reports.save(reportFile);
		} catch (IOException e) {
			xs.getLogger().severe("An error occured whilst saving reports.yml");
			e.printStackTrace();
		}
		
		String[] parts = loc.split("\\.");
		String part = parts[0];
		
		if(part == null) {
			return;
		}
		
		if(getReports(part).entrySet().size() == 0) {
			reports.set(part, null);
			xs.getLogger().info("Null");
			try {
				reports.save(reportFile);
			} catch (IOException e) {
				xs.getLogger().severe("An error occured whilst saving reports.yml");
				e.printStackTrace();
			}
		}
	}
}
