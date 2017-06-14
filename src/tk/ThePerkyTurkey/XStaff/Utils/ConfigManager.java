package tk.ThePerkyTurkey.XStaff.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import tk.ThePerkyTurkey.XStaff.XStaff;

public class ConfigManager {
	
	private XStaff xs;
	private Logger logger;
	private boolean firstTime = false;
	private File dataFolder;
	private File configFile;
	private File messagesFile;
	private File reportFile;
	private File inventoryFile;
	private FileConfiguration config;
	
	public ConfigManager(XStaff xs) {
		this.xs = xs;
		this.logger = xs.getLogger();
		this.dataFolder = xs.getDataFolder();
		this.configFile = new File(dataFolder, "config.yml");
		this.messagesFile = new File(dataFolder, "messages.yml");
		this.reportFile = new File(dataFolder, "reports.yml");
		this.inventoryFile = new File(dataFolder, "inventories.yml");
		checkFirstTime();
	}
	
	//This is to avoid any errors when the plugin is updated, and certain fields aren't in the config files
	private void updateExistingFiles() {
		FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		if(!config.getString("version").equals(xs.getDescription().getVersion())) {
			HashMap<String, Object> saved = new HashMap<String, Object>();
			xs.getLogger().info("Config version is different to the plugin version. Updating the config file.");
			for(String key : getKeys(config)) {
				saved.put(key, config.get(key));
			}
			
			@SuppressWarnings("deprecation")
			FileConfiguration newConfig = YamlConfiguration.loadConfiguration(xs.getResource("config.yml"));
			
			for(String key : getKeys(newConfig)) {
				if(!saved.containsKey(key)) {
					config.set(key, newConfig.get(key));
				}
			}
			
			config.set("version", xs.getDescription().getVersion());
			
			try {
				config.save(configFile);
			} catch (IOException e) {
				xs.getLogger().info("An error occured whilst updating config.yml");
			}
		}
		
		FileConfiguration messages = YamlConfiguration.loadConfiguration(messagesFile);
		
		if(!messages.getString("version").equals(xs.getDescription().getVersion())) {
			HashMap<String, Object> saved = new HashMap<String, Object>();
			xs.getLogger().info("Messages file version is different to the plugin version. Updating the message file.");
			for(String key : getKeys(messages)) {
				saved.put(key, messages.get(key));
			}
			
			@SuppressWarnings("deprecation")
			FileConfiguration newConfig = YamlConfiguration.loadConfiguration(xs.getResource("config.yml"));
			
			for(String key : getKeys(newConfig)) {
				if(!saved.containsKey(key)) {
					messages.set(key, newConfig.get(key));
				}
			}
			
			messages.set("version", xs.getDescription().getVersion());
			
			try {
				messages.save(messagesFile);
			} catch (IOException e) {
				xs.getLogger().info("An error occured whilst updating messages.yml");
			}
		}
	}
	
	public boolean getBoolean(String loc) {
		return config.getBoolean(loc);
	}
	
	public void set(String loc, Object value) {
		config.set(loc, value);
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
			loadConfigs();
		} else {
			firstTime = true;
			setup();
		}
	}
	
	private void loadConfigs() {
		this.configFile = new File(dataFolder, "config.yml");
		if(!configFile.exists()) xs.saveResource("config.yml", true);
		configFile = new File(dataFolder.getPath() + "/config.yml");
		this.config = YamlConfiguration.loadConfiguration(configFile);
		getMessageFile();
		getReportsFile();
		getInventoryFile();
		updateExistingFiles();
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
		dataFolder.mkdirs();
		loadConfigs();
	}
	
	public File getMessageFile() { 
		
		if(!messagesFile.exists() || messagesFile == null) {
			xs.saveResource("messages.yml", true);
		}
				
		messagesFile = new File(dataFolder, "messages.yml");

		return messagesFile;
	}
	
	public File getReportsFile() { 
		
		if(!reportFile.exists() || reportFile == null) {
			xs.saveResource("reports.yml", true);
			reportFile = new File(dataFolder.getPath() + "/reports.yml");
		}
		
		return reportFile;
	}
	
	public File getInventoryFile() {
		
		if(!inventoryFile.exists() || inventoryFile == null) {
			xs.saveResource("inventories.yml", true);
			inventoryFile = new File(dataFolder.getPath() + "/inventories.yml");
		}
		
		return inventoryFile;
	}
	
	
	public FileConfiguration getMessages() { 
		return YamlConfiguration.loadConfiguration(getMessageFile());
	}
	public FileConfiguration getReports() { 
		return YamlConfiguration.loadConfiguration(getReportsFile());
	}
	public FileConfiguration getInventories() {
		return YamlConfiguration.loadConfiguration(getInventoryFile());
	}
	
	private List<String> getKeys(FileConfiguration fc) {
		List<String> result = new ArrayList<String>();
		for(String key : config.getKeys(false)) {
			if(config.getConfigurationSection(key) == null) {
				result.add(key);
			} else {
				for(String key2 : config.getConfigurationSection(key).getKeys(false)) {
					result.add(key + "." + key2);
				}
			}
		}
		return result;
	}
	
}
