package tk.ThePerkyTurkey.XStaff;

import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import net.milkbowl.vault.permission.Permission;
import tk.ThePerkyTurkey.XStaff.Commands.CommandAddnote;
import tk.ThePerkyTurkey.XStaff.Commands.CommandDetails;
import tk.ThePerkyTurkey.XStaff.Commands.CommandFreeze;
import tk.ThePerkyTurkey.XStaff.Commands.CommandNotes;
import tk.ThePerkyTurkey.XStaff.Commands.CommandRemovenote;
import tk.ThePerkyTurkey.XStaff.Commands.CommandReport;
import tk.ThePerkyTurkey.XStaff.Commands.CommandReports;
import tk.ThePerkyTurkey.XStaff.Commands.CommandStaff;
import tk.ThePerkyTurkey.XStaff.Commands.CommandStaffchat;
import tk.ThePerkyTurkey.XStaff.Commands.CommandUnwarn;
import tk.ThePerkyTurkey.XStaff.Commands.CommandVanish;
import tk.ThePerkyTurkey.XStaff.Commands.CommandWarn;
import tk.ThePerkyTurkey.XStaff.Commands.CommandWarns;
import tk.ThePerkyTurkey.XStaff.Inventories.DetailsGUIClickManager;
import tk.ThePerkyTurkey.XStaff.Inventories.NotesGUIClickManager;
import tk.ThePerkyTurkey.XStaff.Inventories.OnlineStaffGUIClickManager;
import tk.ThePerkyTurkey.XStaff.Inventories.ReportGUIClickManager;
import tk.ThePerkyTurkey.XStaff.Inventories.StaffInventory;
import tk.ThePerkyTurkey.XStaff.Inventories.WarnsGUIClickManager;
import tk.ThePerkyTurkey.XStaff.Utils.ConfigManager;
import tk.ThePerkyTurkey.XStaff.Utils.Messages;
import tk.ThePerkyTurkey.XStaff.Utils.NotesManager;
import tk.ThePerkyTurkey.XStaff.Utils.PlayerManager;
import tk.ThePerkyTurkey.XStaff.Utils.ReportCooldown;
import tk.ThePerkyTurkey.XStaff.Utils.ReportManager;
import tk.ThePerkyTurkey.XStaff.Utils.WarnManager;

public class XStaff extends JavaPlugin {
	
	public XStaff XStaff;
	public Logger logger;
	public ConfigManager cm;
	public ReportCooldown rc;
	public ReportManager rm;
	public Messages msg;
	public PlayerManager pm;
	public StaffInventory si;
	public NotesManager nm;
	public WarnManager wm;
	
	public DetailsGUIClickManager dGUI;
	
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
		this.nm = new NotesManager(XStaff);
		this.wm = new WarnManager(XStaff);
		this.dGUI = new DetailsGUIClickManager(XStaff);
		XStaff.getServer().getPluginManager().registerEvents(new XStaffListener(XStaff), XStaff);
		XStaff.getServer().getPluginManager().registerEvents(new ReportGUIClickManager(XStaff), XStaff);
		XStaff.getServer().getPluginManager().registerEvents(new OnlineStaffGUIClickManager(XStaff), XStaff);
		XStaff.getServer().getPluginManager().registerEvents(dGUI, XStaff);
		XStaff.getServer().getPluginManager().registerEvents(new WarnsGUIClickManager(XStaff), XStaff);
		XStaff.getServer().getPluginManager().registerEvents(new NotesGUIClickManager(XStaff), XStaff);
		rc.runTaskTimer(XStaff, 20, 20);
		setCommands();
		
		if(XStaff.getServer().getPluginManager().isPluginEnabled("Vault")) {
			getPermissionHandler();
		}
	}
	
	@Override
	public void onDisable() {
		for(Player p : PlayerManager.getStaffPlayers()) {
			p.sendMessage(msg.get("staffReload"));
			try {
				si.restoreInventory(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for(Player p : PlayerManager.getVanishedPlayers()) {
			p.sendMessage(msg.get("vanishReload"));
			p.removePotionEffect(PotionEffectType.INVISIBILITY);
		}
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
	
	public WarnManager getWarnManager() {
		return wm;
	}
	
	public NotesManager getNotesManager() {
		return nm;
	}
	
	private void setCommands() {
		getCommand("report").setExecutor(new CommandReport(XStaff));
		getCommand("staff").setExecutor(new CommandStaff(XStaff));
		getCommand("vanish").setExecutor(new CommandVanish(XStaff));
		getCommand("reports").setExecutor(new CommandReports(XStaff));
		getCommand("freeze").setExecutor(new CommandFreeze(XStaff));
		getCommand("details").setExecutor(new CommandDetails(XStaff));
		getCommand("warn").setExecutor(new CommandWarn(XStaff));
		getCommand("warns").setExecutor(new CommandWarns(XStaff));
		getCommand("unwarn").setExecutor(new CommandUnwarn(XStaff));
		getCommand("addnote").setExecutor(new CommandAddnote(XStaff));
		getCommand("removenote").setExecutor(new CommandRemovenote(XStaff));
		getCommand("notes").setExecutor(new CommandNotes(XStaff));
		getCommand("staffchat").setExecutor(new CommandStaffchat(XStaff));
	}
	
	public Permission getPermissionHandler() {
		Permission permission = null;
		RegisteredServiceProvider<Permission> permissionProvider = XStaff.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if(permissionProvider != null) permission = permissionProvider.getProvider();
		
		return permission;
	}
	
	private boolean isLatest() {
		String currentVersion = XStaff.getDescription().getVersion();
		return true;
	}
}
