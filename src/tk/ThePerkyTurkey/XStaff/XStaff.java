package tk.ThePerkyTurkey.XStaff;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import tk.ThePerkyTurkey.XStaff.Commands.CommandFreeze;
import tk.ThePerkyTurkey.XStaff.Commands.CommandReport;
import tk.ThePerkyTurkey.XStaff.Commands.CommandReports;
import tk.ThePerkyTurkey.XStaff.Commands.CommandStaff;
import tk.ThePerkyTurkey.XStaff.Commands.CommandVanish;
import tk.ThePerkyTurkey.XStaff.Inventories.OnlineStaffGUIClickManager;
import tk.ThePerkyTurkey.XStaff.Inventories.ReportGUIClickManager;
import tk.ThePerkyTurkey.XStaff.Inventories.StaffInventory;
import tk.ThePerkyTurkey.XStaff.Utils.ConfigManager;
import tk.ThePerkyTurkey.XStaff.Utils.Messages;
import tk.ThePerkyTurkey.XStaff.Utils.PlayerManager;
import tk.ThePerkyTurkey.XStaff.Utils.ReportCooldown;
import tk.ThePerkyTurkey.XStaff.Utils.ReportManager;

public class XStaff extends JavaPlugin {
	
	public XStaff XStaff;
	public Logger logger;
	public ConfigManager cm;
	public ReportCooldown rc;
	public ReportManager rm;
	public Messages msg;
	public PlayerManager pm;
	public StaffInventory si;
	
	@Override
	public void onEnable() {
		this.XStaff = this;
		this.logger = XStaff.getLogger();
		this.cm = new ConfigManager(XStaff);
		this.msg = new Messages(XStaff);
		this.pm = new PlayerManager(XStaff);
		this.rc = new ReportCooldown(XStaff);
		this.rm = new ReportManager(XStaff);
		this.si = new StaffInventory(XStaff);
		XStaff.getServer().getPluginManager().registerEvents(new XStaffPlayerListener(XStaff), XStaff);
		XStaff.getServer().getPluginManager().registerEvents(new ReportGUIClickManager(XStaff), XStaff);
		XStaff.getServer().getPluginManager().registerEvents(new OnlineStaffGUIClickManager(XStaff), XStaff);
		rc.runTaskTimer(XStaff, 20, 20);
		setCommands();
	}
	
	public XStaff getXStaff() {
		return XStaff;
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
		getCommand("report").setExecutor(new CommandReport(XStaff));
		getCommand("staff").setExecutor(new CommandStaff(XStaff));
		getCommand("vanish").setExecutor(new CommandVanish(XStaff));
		getCommand("reports").setExecutor(new CommandReports(XStaff));
		getCommand("freeze").setExecutor(new CommandFreeze(XStaff));
	}
}
