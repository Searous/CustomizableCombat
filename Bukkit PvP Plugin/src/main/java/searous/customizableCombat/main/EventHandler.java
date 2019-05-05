package searous.customizableCombat.main;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
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

    @org.bukkit.event.EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        // Check if both the source and the target are players
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player target   = (Player)event.getEntity();
            Player attacker = (Player)event.getDamager();

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
}
