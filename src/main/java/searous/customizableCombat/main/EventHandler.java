package searous.customizableCombat.main;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import searous.customizableCombat.duel.Duel;

import java.util.*;
import java.util.logging.Level;

/**
 *
 * @author Katie (Searous)
 */
public class EventHandler implements Listener {
    private final CustomizableCombat plugin;
    private final List<String> protectedPets;
    private final List<String> protectedMounts;
    
    public EventHandler(CustomizableCombat plugin) {
        // Set local plugin variable
        this.plugin = plugin;
        
        // Grab entity name lists for easy access
        protectedPets = plugin.getConfig().getStringList(plugin.getStrings().CONFIG_PROTECTED_PETS);
        protectedMounts = plugin.getConfig().getStringList(plugin.getStrings().CONFIG_PROTECTED_MOUNTS);
        
        // Debug
        /*String str = protectedPets.get(0);
        for(int i = 1; i < protectedPets.size(); i++) {
            str += "," + protectedPets.get(i);
        }
        plugin.getServer().getConsoleSender().sendMessage("Protected Pets: " + str);
        
        str = protectedMounts.get(0);
        for(int i = 1; i < protectedMounts.size(); i++) {
            str += "," + protectedMounts.get(i);
        }
        plugin.getServer().getConsoleSender().sendMessage("Protected Mounts: " + str);*/
    }
    
    /**
     * Used here to stop incoming damage when PvP conditions are not met for a given attacker/target set
     * 
     * @param event The event being passed in by Bukkit
     */
    @org.bukkit.event.EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        boolean isProtected;
        Entity attacker = event.getDamager(),
        defender = event.getEntity();
        
        // Duel ending
        if(attacker instanceof Player && defender instanceof Player) {
            Duel duel = plugin.getDuelManager().getDuel((Player) attacker, (Player) defender);
            if(duel != null) {
                if(duel.isDuelStarted()) {
                    if(((Player) defender).getHealth() - event.getDamage() <= 0d) {
                        plugin.getDuelManager().cancelDuel(duel);
                        ((Player) attacker).setHealth(((Player) attacker).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                        attacker.sendMessage(ChatColor.YELLOW + "You won the duel!");
                        
                        ((Player) defender).setHealth(((Player) defender).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                        defender.sendMessage(ChatColor.YELLOW + "You lost the duel!");
                        
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
        
        // Check which entity is attacking, then check defenders
        
        // Determine if event.getEntity() should be protected
        if(attacker instanceof Player) {
            // Attacker is a player
            Player attackingPlayer = (Player)attacker;
            
            // Check if this player can attack the other
            isProtected = checkDefender(event, attackingPlayer, defender);
        } else if(event.getDamager() instanceof Projectile) {
            // Attacker is a projectile
            Projectile projectile = (Projectile) event.getDamager();
            
            // Check if this projectile's owner could attack this player
            if(projectile.getShooter() instanceof Player) {
                Player attackingPlayer = (Player)projectile.getShooter();
    
                isProtected = checkDefender(event, attackingPlayer, defender);
            }
        } else if(event.getDamager() instanceof Tameable) {
            // Attacker is a pet
            Tameable attackingPet = (Tameable) event.getDamager();
            
            // Check if this pet's owner could attack this player
            if(attackingPet.isTamed()) {
                if(attackingPet.getOwner() instanceof Player) {
                    Player attackingPlayer = (Player)attackingPet.getOwner();
                    
                    isProtected = checkDefender(event, attackingPlayer, defender);
                    if(isProtected) {
                        if(attackingPet instanceof Wolf) {
                            // Cancel wolf anger
                            Wolf wolf = (Wolf)attackingPet;
                            wolf.setSitting(true);
                            wolf.setAngry(false);
                        }
                    }
                }
            }
        }
    }
    
    @org.bukkit.event.EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Object o = plugin.getPlayerPreferences().get(player.getUniqueId().toString());
        
        if(o == null) {
            plugin.getLogger().log(Level.INFO, "Setting default player preferences for new player");

            plugin.getPlayerPreferences().set(player.getUniqueId().toString() + "." + plugin.getStrings().PREFERANCE_PVP, plugin.getWorldsConfig().getBoolean("global.pvp-default"));
            //plugin.saveConfig(plugin.filePlayers,);
        }
    }
    
    /**
     * To be called in onDamage. Determines if the defender is eligible for protection, then checks if it should be protected.
     * @param event The event this was called in
     * @param attackingPlayer The player attempting to attack the defender
     * @param defender The defending entity
     */
    private boolean checkDefender(EntityDamageByEntityEvent event, Player attackingPlayer, Entity defender) {
        if(defender instanceof Player) {
            // Defender is a player
            return checkDamageable(event, attackingPlayer, (Player)defender);
        } else if(protectedPets.contains(event.getEntity().getType().toString())) {
            // Defender is a pet
            if(event.getEntity() instanceof Tameable) {
                Tameable pet = (Tameable)defender;
            
                if(pet.getOwner() instanceof Player && pet.getOwner() != attackingPlayer) {
                    Player owner = (Player) pet.getOwner();
                    
                    return checkDamageable(event, attackingPlayer, owner);
                }
            }
        } else if(protectedMounts.contains(event.getEntity().getType().toString())) {
            // Defender is a mount
            List<Entity> passengers = event.getEntity().getPassengers();
            if(passengers.size() > 0) {
                for(int i = 0; i < passengers.size(); i++) {
                    if(passengers.get(i) instanceof Player) {
                        Player rider = (Player)passengers.get(i);
    
                        return checkDamageable(event, attackingPlayer, rider);
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * Checks if the given attacker is able to damage the given target.
     * Sends chat messages to attacker if PvP conditions are not met, then cancels the event.
     * 
     * @param event The event this was being called from.
     * @param defender The target (defending) player.
     * @param attacker The attacking player
     */
    private boolean checkDamageable(EntityDamageByEntityEvent event, Player attacker, Player defender) {
    	// If global is true, cancel if override is false
        if(plugin.getDuelManager().isPlayerDueling(attacker) || plugin.getDuelManager().isPlayerDueling(defender)) {
            Duel duel = plugin.getDuelManager().getDuel(attacker, defender);
            if(duel != null && duel.isDuelStarted()) {
                return false;
            } else {
                attacker.sendMessage(ChatColor.YELLOW + "You or " + defender.getName() + " are in a duel!");
                attacker.playSound(attacker.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                event.setCancelled(true);
                return true;
            }
        } else if(plugin.getPvpEnabledGlobal()) {
            if(!plugin.getPvpEnabledOverride()) {
                // PvP Forced disabled
                attacker.sendMessage(ChatColor.YELLOW + "PvP Is Disabled");
                attacker.playSound(attacker.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                event.setCancelled(true);
                return true;
            }
            
            return false;
        } else {
            if(!plugin.getPvpEnabled(defender.getUniqueId())) {
                attacker.sendMessage(ChatColor.YELLOW + defender.getName() + "'s PvP is disabled!");
                attacker.playSound(attacker.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                event.setCancelled(true);
                return true;
            } else if(!plugin.getPvpEnabled(attacker.getUniqueId())) {
                attacker.sendMessage(ChatColor.YELLOW + "Your PvP is disabled!");
                attacker.playSound(attacker.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                event.setCancelled(true);
                return true;
            }
            
            return false;
        }
    }
}
