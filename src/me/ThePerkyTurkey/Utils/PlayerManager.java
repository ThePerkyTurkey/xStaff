package me.ThePerkyTurkey.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.ThePerkyTurkey.XStaff;
import me.ThePerkyTurkey.Inventories.StaffInventory;

public class PlayerManager {
	
	private static Messages msg;
	private static ConfigManager cm;
	private static HashMap<Player, List<Player>> vanishedFrom = new HashMap<Player, List<Player>>();
	private static List<Player> inStaffMode = new ArrayList<>();
	private static List<Player> vanished = new ArrayList<>();
	private static List<Player> inStaffChat = new ArrayList<>();
	private static List<Player> frozen = new ArrayList<>();
	private static List<String> frozenMessage;
	
	public PlayerManager(XStaff xs) {
		PlayerManager.msg = xs.getMessages();
		PlayerManager.cm = xs.getConfigManager();
		xs.getStaffInventory();
		PlayerManager.frozenMessage = cm.getList("frozen-message", true);
	}
	
	public static List<String> getFrozenMessage() {
		return frozenMessage;
	}
	
	public static boolean isStaff(Player p) {
		return inStaffMode.contains(p);
	}
	
	public static boolean isVanished(Player p) {
		return vanished.contains(p);
	}
	
	public static boolean isVanishedFrom(Player vanished, Player check) {
		return vanishedFrom.get(vanished).contains(check);
	}
	
	public static boolean isInStaffChat(Player p) {
		return inStaffChat.contains(p);
	}
	
	public static boolean isFrozen(Player p) {
		return frozen.contains(p);
	}
	
	public static List<Player> getStaffPlayers() {
		return inStaffMode;
	}
	
	public static List<Player> getFrozenPlayers() {
		return frozen;
	}
	public static List<Player> getStaffChatPlayers() {
		return inStaffChat;
	}
	public static List<Player> getVanishedPlayers() {
		return vanished;
	}
	
	public static void setStaff(Player p, boolean set) {
		if(set) {
			inStaffMode.add(p);
			p.sendMessage(msg.get("staffEnable"));
			StaffInventory.setStaffInventory(p);
		} else {
			inStaffMode.remove(p);
			p.sendMessage(msg.get("staffDisable"));
			StaffInventory.restoreInventory(p);
		}
	}
	
	public static void setVanished(Player p, boolean set) {
		if(set) {
			vanished.add(p);
			if(PlayerManager.isStaff(p)) {
				StaffInventory.updateVanishItem(p);
			}
			p.sendMessage(msg.get("vanishEnable"));
			List<Player> hidden = new ArrayList<Player>();
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				vanish(p,player);
				hidden.add(player);
			}
			vanishedFrom.put(p, hidden);
		} else {
			vanished.remove(p);
			if(PlayerManager.isStaff(p)) {
				StaffInventory.updateVanishItem(p);
			}
			p.sendMessage(msg.get("vanishDisable"));
			for (Player player : Bukkit.getOnlinePlayers()) {
				unvanish(p,player);
			}
			vanishedFrom.remove(p);
		}
	}
	
	public static void setStaffChat(Player p, boolean set) {
		if(set) {
			inStaffChat.add(p);
			p.sendMessage(msg.get("staffChatEnable"));
		} else {
			inStaffChat.remove(p);
			p.sendMessage(msg.get("staffChatDisable"));
		}
	}
	
	public static void setFrozen(Player p, boolean set) {
		if(set) {
			frozen.add(p);
			for(String s : frozenMessage) {
				p.sendMessage(s);
			}
		} else {
		 frozen.remove(p);
		 p.sendMessage(msg.get("unFreeze"));
		}
	}
	
	public static void vanish(Player hide, Player hideFrom) {
		if(!hideFrom.hasPermission("xstaff.vanish.see")) {
			hideFrom.hidePlayer(hide);
		}
	}
	
	public static void unvanish(Player unhide, Player unhideFrom) {
		//This is just to stop any errors if the player is already shown to them
		if(!unhideFrom.hasPermission("xstaff.vanish.see")) {
			unhideFrom.showPlayer(unhide);
		}
	}
	
	public static void toggleStaffMode(Player p) {
		if(isStaff(p)) {
			setStaff(p, false);
		} else {
			setStaff(p, true);
		}
	}
	
	public static void toggleVanish(Player p) {
		if(isVanished(p)) {
			setVanished(p, false);
		} else {
			setVanished(p, true);
		}
	}
	
	public static void toggleStaffChat(Player p) {
		if(isInStaffChat(p)) {
			setStaffChat(p, false);
		} else {
			setStaffChat(p, true);
		}
	}
	
	public static void toggleFreeze(Player p) {
		if(isFrozen(p)) {
			setFrozen(p, false);
		} else {
			setFrozen(p, true);
		}
	}
}
