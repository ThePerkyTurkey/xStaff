package tk.ThePerkyTurkey.XStaff.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Inventories.StaffInventory;

public class PlayerManager {
	
	private static Messages msg;
	private static ConfigManager cm;
	private static XStaff xs;
	private static HashMap<Player, List<Player>> vanishedFrom = new HashMap<Player, List<Player>>();
	public static List<Player> inStaffMode = new ArrayList<>();
	private static List<Player> vanished = new ArrayList<>();
	private static List<Player> inStaffChat = new ArrayList<>();
	private static List<Player> frozen = new ArrayList<>();
	private static List<String> frozenMessage;
	private static HashMap<Player, Boolean> flightData = new HashMap<Player, Boolean>();
	private static HashMap<Player, GameMode> gamemodeData = new HashMap<Player, GameMode>();
	private static Team Staff;
	
	public PlayerManager(XStaff xs) {
		PlayerManager.xs = xs;
		PlayerManager.msg = xs.getMessages();
		PlayerManager.cm = xs.getConfigManager();
		Scoreboard board = xs.getServer().getScoreboardManager().getMainScoreboard();
		Team Staff = board.getTeam("Staff");
		
		if(Staff == null) {
			Staff = board.registerNewTeam("Staff");
		}
		
		Staff.setCanSeeFriendlyInvisibles(true);
		
		PlayerManager.Staff = Staff;
		xs.getStaffInventory();
		PlayerManager.frozenMessage = cm.getList("frozen-message", true);
		sendFrozenMessage();
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
		return !check.canSee(vanished);
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
			gamemodeData.put(p, p.getGameMode());
			p.setGameMode(GameMode.CREATIVE);
			inStaffMode.add(p);
			p.sendMessage(msg.get("staffEnable"));
			xs.getStaffInventory().setStaffInventory(p);
			addToStaff(p);
			if(!isVanished(p)) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
			}
		} else {
			p.setGameMode(gamemodeData.get(p));
			gamemodeData.remove(p);
			inStaffMode.remove(p);
			p.sendMessage(msg.get("staffDisable"));
			if(p.isOnline()) {
				try {
					xs.getStaffInventory().restoreInventory(p);
				} catch (IOException e) {
					e.printStackTrace();
					xs.getLogger().severe("An error occured whilst restoring " + p.getName() + "'s inventory");
				}
			}
			removeStaff(p);
			if(!isVanished(p)) {
				p.removePotionEffect(PotionEffectType.NIGHT_VISION);
			}
		}
	}
	
	public static void setVanished(Player p, boolean set) {
		if(set) {
			if(cm.getBoolean("vanished-fly")) {
				flightData.put(p, p.getAllowFlight());
				p.setAllowFlight(true);
			}
			vanished.add(p);
			if(PlayerManager.isStaff(p)) {
				StaffInventory.updateVanishItem(p);
			}
			setGhost(p);
			p.sendMessage(msg.get("vanishEnable"));
			List<Player> hidden = new ArrayList<Player>();
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				vanish(p,player);
				hidden.add(player);
			}
			vanishedFrom.put(p, hidden);
			if(!isStaff(p)) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
			}
			
		} else {
			if(cm.getBoolean("vanished-fly")) {
				p.setAllowFlight(flightData.get(p));
				flightData.remove(p);
			}
			vanished.remove(p);
			if(PlayerManager.isStaff(p)) {
				StaffInventory.updateVanishItem(p);
			}
			p.sendMessage(msg.get("vanishDisable"));
			for (Player player : Bukkit.getOnlinePlayers()) {
				unvanish(p,player);
			}
			vanishedFrom.remove(p);
			unsetGhost(p);
			if(!isStaff(p)) {
				p.removePotionEffect(PotionEffectType.NIGHT_VISION);;
			}
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
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 128, false));
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128, false));
			for(String s : frozenMessage) {
				p.sendMessage(s);
			}
		} else {
			frozen.remove(p);
		    p.sendMessage(msg.get("unFreeze"));
		    p.removePotionEffect(PotionEffectType.SLOW);
		    p.removePotionEffect(PotionEffectType.JUMP);
		}
	}
	
	public static void vanish(Player hide, Player hideFrom) {
		if(!xs.getServer().getPluginManager().isPluginEnabled("Vault")) {
			if(!hideFrom.hasPermission("xstaff.vanish.see.all")) {
				hideFrom.hidePlayer(hide);
			}
		} else {
			String rank = xs.getPermissionHandler().getPrimaryGroup(hide);
			if(!hideFrom.hasPermission("xstaff.vanish.see." + rank.toLowerCase()) && !hideFrom.hasPermission("xstaff.vanish.see.all")) {
				hideFrom.hidePlayer(hide);
			}
		}
	}
	
	public static void unvanish(Player unhide, Player unhideFrom) {
		//This is just to stop any errors if the player is already shown to them
		if(!unhideFrom.canSee(unhide)) {
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
	
	private static void addToStaff(Player p) {
		p.setScoreboard(xs.getServer().getScoreboardManager().getMainScoreboard());
		if(!Staff.hasPlayer(p)) {
			Staff.addPlayer(p);
		}
	}
	
	private static void removeStaff(Player p) {
		if(Staff.hasPlayer(p)) {
			Staff.removePlayer(p);
		}
	}
	
	private static void setGhost(Player p) {
		addToStaff(p);
		p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false));
	}
	
	private static void unsetGhost(Player p) {
		p.removePotionEffect(PotionEffectType.INVISIBILITY);
		if(!isStaff(p)) {
			removeStaff(p);
		}
	}
	
	private static void sendFrozenMessage() {
		xs.getServer().getScheduler().scheduleSyncRepeatingTask(xs, new Runnable() {

			@Override
			public void run() {
				for(Player p : getFrozenPlayers()) {
					for(String s : getFrozenMessage()) {
						p.sendMessage(s);
					}
				}
			}
			
		}, 100L, 100L);
	}
}
