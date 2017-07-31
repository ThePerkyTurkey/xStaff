package tk.ThePerkyTurkey.XStaff.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
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
	private File notesFile;
	private File warnsFile;
	private FileConfiguration config;

	public ConfigManager(XStaff xs) {
		this.xs = xs;
		this.logger = xs.getLogger();
		this.dataFolder = xs.getDataFolder();
		this.configFile = new File(dataFolder, "config.yml");
		this.messagesFile = new File(dataFolder, "messages.yml");
		this.reportFile = new File(dataFolder, "reports.yml");
		this.inventoryFile = new File(dataFolder, "inventories.yml");
		this.notesFile = new File(dataFolder, "notes.yml");
		this.warnsFile = new File(dataFolder, "warns.yml");
		checkFirstTime();
	}

	// This is to avoid any errors when the plugin is updated, and certain
	// fields aren't in the config files
	private void updateExistingFiles() {
		if(!config.getString("version").equals(xs.getDescription().getVersion())) {
			xs.getLogger().info("Config.yml is outdated. Updating it now!");
			HashMap<String, Object> data = new HashMap<String, Object>();
			for(String key : getKeys(config)) {
				data.put(key, config.get(key));
			}
			
			InputStream is = null;
			OutputStream os = null;
			try {
			
				is = xs.getResource("config.yml");
				os = new FileOutputStream(configFile);
				File f = new File(dataFolder, "config.yml");
				byte[] buffer = new byte[1024];
				int length;
				while((length = is.read(buffer)) > 0) {
					os.write(buffer, 0, length);
				}
				
			}catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			configFile = new File(dataFolder, "config.yml");
			config = YamlConfiguration.loadConfiguration(configFile);
			
			for(String s : getKeys(config)) {
				if(data.containsKey(s)) {
					config.set(s, data.get(s));
				}
			}
			
			config.set("version", xs.getDescription().getVersion());
			try {
				config.save(configFile);
				xs.getLogger().info("Config.yml has updated sucessfully!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		FileConfiguration messages = getMessages();
		if(!messages.getString("version").equals(xs.getDescription().getVersion())) {
			xs.getLogger().info("Messages.yml is outdated! Updating it now.");
			HashMap<String, Object> data = new HashMap<String, Object>();
			for(String s : getKeys(messages)) {
				data.put(s, messages.get(s));
			}
			
			InputStream is = null;
			OutputStream os = null;
			try {
			
				is = xs.getResource("messages.yml");
				os = new FileOutputStream(messagesFile);
				byte[] buffer = new byte[1024];
				int length;
				while((length = is.read(buffer)) > 0) {
					os.write(buffer, 0, length);
				}
				
			}catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			
			messagesFile = new File(dataFolder, "messages.yml");
			messages = YamlConfiguration.loadConfiguration(messagesFile);
			
			for(String key : getKeys(messages)) {
				if(data.containsKey(key)) {
					messages.set(key, data.get(key));
				}
			}
			
			messages.set("version", xs.getDescription().getVersion());
			try {
				messages.save(messagesFile);
				xs.getLogger().info("Messages.yml has been updated sucessfully!");
			} catch(IOException e) {
				e.printStackTrace();
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
		if (!translate) {
			return list;
		}
		List<String> result = new ArrayList<>();
		for (String s : list) {
			String translated = ChatColor.translateAlternateColorCodes('&', s);
			result.add(translated);
		}

		return result;

	}

	private void checkFirstTime() {
		if (dataFolder.exists()) {
			loadConfigs();
		} else {
			firstTime = true;
			setup();
		}
	}

	private void loadConfigs() {
		this.configFile = new File(dataFolder, "config.yml");
		if (!configFile.exists())
			xs.saveResource("config.yml", true);
		configFile = new File(dataFolder.getPath() + "/config.yml");
		this.config = YamlConfiguration.loadConfiguration(configFile);
		updateExistingFiles();
		getMessageFile();
		getReportsFile();
		getInventoryFile();
		getWarnsFile();
		getNotesFile();
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

		if (!messagesFile.exists() || messagesFile == null) {
			xs.saveResource("messages.yml", true);
		}

		messagesFile = new File(dataFolder, "messages.yml");

		return messagesFile;
	}

	public File getReportsFile() {

		if (!reportFile.exists() || reportFile == null) {
			xs.saveResource("reports.yml", true);
			reportFile = new File(dataFolder.getPath() + "/reports.yml");
		}

		return reportFile;
	}

	public File getInventoryFile() {

		if (!inventoryFile.exists() || inventoryFile == null) {
			xs.saveResource("inventories.yml", true);
			inventoryFile = new File(dataFolder.getPath() + "/inventories.yml");
		}

		return inventoryFile;
	}
	
	public File getNotesFile() {

		if (!notesFile.exists() || notesFile == null) {
			xs.saveResource("notes.yml", true);
			notesFile = new File(dataFolder.getPath() + "/notes.yml");
		}

		return notesFile;
	}
	
	public File getWarnsFile() {

		if (!warnsFile.exists() || warnsFile == null) {
			xs.saveResource("warns.yml", true);
			warnsFile = new File(dataFolder.getPath() + "/warns.yml");
		}

		return warnsFile;
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
	
	public FileConfiguration getWarns() {
		return YamlConfiguration.loadConfiguration(getWarnsFile());
	}
	
	public FileConfiguration getNotes() {
		return YamlConfiguration.loadConfiguration(getNotesFile());
	}

	private List<String> getKeys(FileConfiguration fc) {
		List<String> result = new ArrayList<String>();
		for (String key : config.getKeys(false)) {
			if (config.getConfigurationSection(key) == null) {
				result.add(key);
			} else {
				for (String key2 : config.getConfigurationSection(key).getKeys(false)) {
					result.add(key + "." + key2);
				}
			}
		}
		return result;
	}

}
