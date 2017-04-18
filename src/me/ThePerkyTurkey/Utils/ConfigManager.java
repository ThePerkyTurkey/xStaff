package me.ThePerkyTurkey.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.ThePerkyTurkey.XStaff;

public class ConfigManager {
	
	private XStaff xs;
	private Logger logger;
	private boolean firstTime = false;
	private File dataFolder;
	private File configFile;
	private File messagesFile;
	private File reportFile;
	private FileConfiguration config;
	
	public ConfigManager(XStaff xs) {
		this.xs = xs;
		this.logger = xs.getLogger();
		this.dataFolder = xs.getDataFolder();
		this.messagesFile = new File(dataFolder, "messages.yml");
		this.reportFile = new File(dataFolder, "reports.yml");
		checkFirstTime();
	}
	
	private void loadDefaultConfigs() {
			xs.saveResource("config.yml", true);
			xs.saveResource("reports.yml", true);
			xs.saveResource("messages.yml", true);
	}
	
	public boolean getBoolean(String loc) {
		return config.getBoolean(loc);
	}
	
	public String getString(String loc) {
		return config.getString(loc);
	}
	
	public int getInt(String loc) {
		return config.getInt(loc);
	}
	
	public boolean isFirstTime() {
		return firstTime;
	}
	
	public List<String> getList(String loc, boolean translate) {
		List<String> list = config.getStringList(loc);
		if(!translate) {
			return list;
		}
		List<String> result = new ArrayList<>();
		for(String s : list) {
			String translated = ChatColor.translateAlternateColorCodes('&', s);
			result.add(translated);
		}
		
		return result;
		
	}
	
	private void checkFirstTime() {
		if(dataFolder.exists()) {
			logger.info("This is not the first time!");
			loadConfig();
		} else {
			firstTime = true;
			setup();
		}
	}
	
	private void loadConfig() {
		this.configFile = new File(dataFolder, "config.yml");
		this.config = YamlConfiguration.loadConfiguration(configFile);
	}
	
	public void saveConfig() {
		try {
			config.save(configFile);
		} catch (IOException e) {
			logger.severe("An error occured whilst saving the config file!");
			e.printStackTrace();
		}
	}
	
	private void setup() {
		logger.info("This is the first time being enabled:");
		logger.info("Initiating Setup process");
		logger.info("Created Data Folder");
		dataFolder.mkdirs();
		logger.info("Loaded default Config file");
		loadDefaultConfigs();
		logger.info("Setup Complete!");
		loadConfig();
	}
	
	public File getMessageFile() { 
		
		if(!messagesFile.exists() || messagesFile == null) {
			xs.saveResource("messages.yml", true);
			messagesFile = new File(dataFolder.getPath() + "/messages.yml");
		}

		return messagesFile;
	}
	
	public File getReportsFile() { 
		
		if(!reportFile.exists() || reportFile == null) {
			xs.saveResource("reports.yml", true);
			reportFile = new File(dataFolder.getPath() + "/reports.yml");
		}
		
		return reportFile;
	}
	
	
	public FileConfiguration getMessages() { 
		
		return YamlConfiguration.loadConfiguration(getMessageFile());
	}
	public FileConfiguration getReports() { 
		
		return YamlConfiguration.loadConfiguration(getReportsFile());
		
	}
	
}
