package me.ThePerkyTurkey;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import me.ThePerkyTurkey.Commands.CommandReport;
import me.ThePerkyTurkey.Commands.CommandReports;
import me.ThePerkyTurkey.Commands.CommandStaff;
import me.ThePerkyTurkey.Commands.CommandVanish;
import me.ThePerkyTurkey.Inventories.ReportGUIClickManager;
import me.ThePerkyTurkey.Inventories.StaffInventory;
import me.ThePerkyTurkey.Utils.ConfigManager;
import me.ThePerkyTurkey.Utils.Messages;
import me.ThePerkyTurkey.Utils.PlayerManager;
import me.ThePerkyTurkey.Utils.ReportCooldown;
import me.ThePerkyTurkey.Utils.ReportManager;

public class XStaff extends JavaPlugin {
	
	public XStaff xStaff;
	public Logger logger;
	public ConfigManager cm;
	public ReportCooldown rc;
	public ReportManager rm;
	public Messages msg;
	public PlayerManager pm;
	public StaffInventory si;
	
	@Override
	public void onEnable() {
		this.xStaff = this;
		this.logger = xStaff.getLogger();
		this.cm = new ConfigManager(xStaff);
		this.msg = new Messages(xStaff);
		this.pm = new PlayerManager(xStaff);
		this.rc = new ReportCooldown(xStaff);
		this.rm = new ReportManager(xStaff);
		this.si = new StaffInventory(xStaff);
		xStaff.getServer().getPluginManager().registerEvents(new XStaffPlayerListener(xStaff), xStaff);
		xStaff.getServer().getPluginManager().registerEvents(new ReportGUIClickManager(xStaff), xStaff);
		rc.runTaskTimer(xStaff, 20, 20);
		setCommands();
	}
	
	public XStaff getxStaff() {
		return xStaff;
	}

	public ConfigManager getConfigManager() {
		return cm;
	}
	
	public Messages getMessages() {
		return msg;
	}
	
	public PlayerManager getPlayerManager() {
		return pm;
	}
	
	public ReportManager getReportManager() {
		return rm;
	}
	
	public StaffInventory getStaffInventory() {
		return si;
	}
	
	private void setCommands() {
		getCommand("report").setExecutor(new CommandReport(xStaff));
		getCommand("staff").setExecutor(new CommandStaff(xStaff));
		getCommand("vanish").setExecutor(new CommandVanish(xStaff));
		getCommand("reports").setExecutor(new CommandReports(xStaff));
	}
}
