package me.ThePerkyTurkey.Utils;

import java.text.MessageFormat;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import me.ThePerkyTurkey.XStaff;

public class Messages {
	
	private ConfigManager cm;
	private FileConfiguration msg;
	private String prefix;
	
	public Messages(XStaff xs) {
		this.cm = xs.getConfigManager();
		this.msg = cm.getMessages();
		setPrefix();
	}
	
	public String get(String loc) {
		return prefix + ChatColor.translateAlternateColorCodes('&', msg.getString(loc));
	}
	
	public String get(String loc, String... objects) {
		String message = msg.getString(loc);
		MessageFormat mf = new MessageFormat(message);
		String finalMessage = mf.format(objects);
		return prefix + ChatColor.translateAlternateColorCodes('&', finalMessage);
	}
	
	private void setPrefix() {
		String messageprefix = cm.getString("message-prefix");
		
		if(messageprefix == null) {
			this.prefix = ChatColor.translateAlternateColorCodes('&', "&l&4xStaff>>");
		} else {
			this.prefix = ChatColor.translateAlternateColorCodes('&', messageprefix);
		}
	}
}
