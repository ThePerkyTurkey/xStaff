package tk.ThePerkyTurkey.XStaff.Utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;
 
public class TargetUtil {
 
    public static Player getTargetPlayer(final Player player) {
        return getTarget(player, player.getWorld().getPlayers());
    }
 
    public static Entity getTargetEntity(final Entity entity) {
        return getTarget(entity, entity.getWorld().getEntities());
    }
 
    public static <T extends Entity> T getTarget(final Entity entity,
            final Iterable<T> entities) {
        if (entity == null)
            return null;
        T target = null;
        final double threshold = 1;
        for (final T other : entities) {
            final Vector n = other.getLocation().toVector()
                    .subtract(entity.getLocation().toVector());
            if (entity.getLocation().getDirection().normalize().crossProduct(n)
                    .lengthSquared() < threshold
                    && n.normalize().dot(
                            entity.getLocation().getDirection().normalize()) >= 0) {
                if (target == null
                        || target.getLocation().distanceSquared(
                                entity.getLocation()) > other.getLocation()
                                .distanceSquared(entity.getLocation()))
                    target = other;
            }
        }
        return target;
    }
    
    public static Block getTargetBlock(Player p, int range) {
    	BlockIterator iterator = new BlockIterator(p, range);
    	Block lastBlock = iterator.next();
    	
    	while(iterator.hasNext()) {
    		lastBlock = iterator.next();
    		if(lastBlock.getType() == Material.AIR) {
    			continue;
    		}
    		break;
    	}
    	
    	return lastBlock;
    }
    
    public static Direction getDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        
        //Left out the other directions because it would cause errors trying to
        //get block faces
        if (0 <= rotation && rotation < 45) {
            return Direction.WEST;
        } else if(45 <= rotation && rotation < 135) {
            return Direction.NORTH;
        } else if (135 <= rotation && rotation < 225) {
            return Direction.EAST;
        } else if (225 <= rotation && rotation < 315) {
            return Direction.SOUTH;
        } else if (315 <= rotation && rotation < 360.0) {
            return Direction.WEST;
        } else {
            return null;
        }
    }
}
