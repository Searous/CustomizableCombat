package searous.customizableCombat.main;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author Katie (Searous)
 */
public class EventHandler implements Listener {
    private CustomizableCombat plugin;

    public EventHandler(CustomizableCombat plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Used here to stop incoming damage when PvP conditions are not met for a given attacker/target set
     * 
     * @param event
     */
    @org.bukkit.event.EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        // Check if both the source and the target are players
        if(event.getEntity() instanceof Player) {
            Player target   = (Player)event.getEntity();
            
            if(event.getDamager() instanceof Player) {
            	// Attack is anothe rplayer
	            Player attacker = (Player)event.getDamager();
	            
	            checkDamagable(event, target, attacker);
            } else if(event.getDamager() instanceof Projectile) {
            	// Attacker is a projectile
            	Projectile projectile = (Projectile)event.getDamager();
            	
            	if(projectile.getShooter() instanceof Player) {
            		// Attacker is a player
            		Player attacker = (Player)projectile.getShooter();
            		
            		checkDamagable(event, target, attacker);
            	}
            }
        } 
    }
    
    @org.bukkit.event.EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Object o = plugin.getPlayerPreferences().get(player.getName());

        if(o == null) {
            plugin.getServer().getConsoleSender().sendMessage("--> That's a null");

            plugin.getPlayerPreferences().set(player.getName() + "." + plugin.getSTrings().PREFERANCE_PVP, false);
            //plugin.saveConfig(plugin.filePlayers,);
        }
    }
    
    /**
     * Checks if the given attacker is able to damage the given target.
     * Sends chat messages to attacker if PvP conditions are not met, then cancels the event.
     * 
     * @param event
     * @param target
     * @param attacker
     */
    private void checkDamagable(EntityDamageByEntityEvent event, Player target, Player attacker) {
    	// If global is true, cancel if override is false
        if(plugin.getPvpEnabledGlobal()) {
            if(!plugin.getPvpEnabledOverride()) {
                // PvP Forced disabled
                event.setCancelled(true);
            }
        } else {
            if(!plugin.getPvpEnabled(target.getName())) {
                attacker.sendMessage(ChatColor.YELLOW + target.getName() + "'s PvP is disabled!");
                attacker.playSound(attacker.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                event.setCancelled(true);
            } else if(!plugin.getPvpEnabled(attacker.getName())) {
                attacker.sendMessage(ChatColor.YELLOW + "Your PvP is disabled!");
                attacker.playSound(attacker.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                event.setCancelled(true);
            }
        }
    }
}
