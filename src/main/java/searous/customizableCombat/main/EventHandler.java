package searous.customizableCombat.main;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.*;
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
        if(event.getDamager() instanceof Player) {
            // Check if both the source and the target are players
            if(plugin.getPvpEnabledGlobal(event.getEntity().getWorld())) {
                if(!plugin.getPvpEnabledOverride(event.getEntity().getWorld())) {
                    // PvP Forced disabled
                    event.setCancelled(true);
                }
            } else if(event.getEntity() instanceof Player) {
                // Player Protection
                Player target = (Player) event.getEntity();
        
                if(event.getDamager() instanceof Player) {
                    // Attack is anothe rplayer
                    Player attacker = (Player) event.getDamager();
            
                    checkDamagable(event, target, attacker);
                } else if(event.getDamager() instanceof Projectile) {
                    // Attacker is a projectile
                    Projectile projectile = (Projectile) event.getDamager();
            
                    if(projectile.getShooter() instanceof Player) {
                        // Attacker is a player
                        Player attacker = (Player) projectile.getShooter();
                
                        checkDamagable(event, target, attacker);
                    }
                }
            } else if(event.getEntity() instanceof Tameable) {
                // Pet Protection
                Tameable pet              = (Tameable) event.getEntity();
                boolean  isOwnerProtected = false;
    
                // Is Owner Protected Status
                if(pet instanceof Wolf || pet instanceof Cat || pet instanceof Parrot)
                    isOwnerProtected = true;
    
                if(isOwnerProtected && pet.isTamed() && event.getDamager() instanceof Player) {
                    AnimalTamer owner    = pet.getOwner();
                    Player      attacker = (Player) event.getDamager();
        
                    if(plugin.getPvpEnabledGlobal(event.getEntity().getWorld())) {
                        if(!plugin.getPvpEnabledOverride(event.getEntity().getWorld())) {
                            // PvP Forced disabled
                            event.setCancelled(true);
                        }
                    } else {
                        if(!plugin.getPvpEnabled(owner.getName())) {
                            attacker.sendMessage(ChatColor.YELLOW + pet.getName() + "'s owner's PvP is disabled!");
                            attacker.playSound(attacker.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                            event.setCancelled(true);
                        } else if(!plugin.getPvpEnabled(attacker.getName())) {
                            attacker.sendMessage(ChatColor.YELLOW + "Your PvP is disabled, and this pet is owned by another player!");
                            attacker.playSound(attacker.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                            event.setCancelled(true);
                        }
                    }
                }
            } else {
                // Mount Protection
                for(int i = 0; i < event.getEntity().getPassengers().size(); i++) {
                    if(event.getEntity().getPassengers().get(i) instanceof Player) {
                        // Determine protection
                        Entity mount = event.getEntity().getPassengers().get(i);
                        Player passenger = (Player) event.getEntity().getPassengers().get(i);
                        Player attacker = (Player) event.getDamager();
    
                        if(plugin.getPvpEnabledGlobal(event.getEntity().getWorld())) {
                            if(!plugin.getPvpEnabledOverride(event.getEntity().getWorld())) {
                                // PvP Forced disabled
                                event.setCancelled(true);
                            }
                        } else {
                            if(!plugin.getPvpEnabled(passenger.getName())) {
                                attacker.sendMessage(ChatColor.YELLOW + mount.getName() + "'s rider's PvP is disabled!");
                                attacker.playSound(attacker.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                                event.setCancelled(true);
                            } else if(!plugin.getPvpEnabled(attacker.getName())) {
                                attacker.sendMessage(ChatColor.YELLOW + "Your PvP is disabled, and someone is riding " + mount.getName() + "!");
                                attacker.playSound(attacker.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                                event.setCancelled(true);
                            }
                        }
                    }
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

            plugin.getPlayerPreferences().set(player.getName() + "." + plugin.getStrings().PREFERANCE_PVP, false);
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
        if(plugin.getPvpEnabledGlobal(event.getEntity().getWorld())) {
            if(!plugin.getPvpEnabledOverride(event.getEntity().getWorld())) {
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
