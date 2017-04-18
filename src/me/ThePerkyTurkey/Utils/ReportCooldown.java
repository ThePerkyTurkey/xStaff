package me.ThePerkyTurkey.Utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.ThePerkyTurkey.XStaff;

public class ReportCooldown extends BukkitRunnable{
	
	private static Map<Player, Long> cooldown = new HashMap<Player, Long>();
	private int cooldownTime;

	public ReportCooldown(XStaff xs) {
		this.cooldownTime = xs.getConfigManager().getInt("report-cooldown");
	}
	
	@Override
	public void run() {
		Iterator<Entry<Player, Long>> it = cooldown.entrySet().iterator();
		while(it.hasNext()) {
			Entry<Player, Long> pair = it.next();
			if(System.currentTimeMillis() - pair.getValue() >= (cooldownTime * 1000)) {
				it.remove();
			}
		}
	}
	
	public static boolean can(Player p) {
		return !cooldown.containsKey(p);
	}
	
	public static void add(Player p) {
		cooldown.put(p, System.currentTimeMillis());
	}
	
	
}
