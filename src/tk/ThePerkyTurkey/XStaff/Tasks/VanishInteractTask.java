package tk.ThePerkyTurkey.XStaff.Tasks;

import org.bukkit.entity.Player;

import tk.ThePerkyTurkey.XStaff.Utils.PlayerManager;

public class VanishInteractTask {
	
	public VanishInteractTask(Player p) {
		PlayerManager.toggleVanish(p);
	}

}
