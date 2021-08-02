package searous.customizableCombat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import searous.customizableCombat.main.CustomizableCombat;

import java.util.logging.Level;

@CommandAlias("duel")
@CommandPermission("customizable-combat.duel")
public class ACFCommandDuel extends BaseCommand {
    
    private static CustomizableCombat plugin;
    
    public ACFCommandDuel(CustomizableCombat p) {
        if(plugin != null)
            p.getLogger().log(Level.SEVERE, "Attempting to instantiate duplicate copy of Duel command");
        
        plugin = p;
    }
    
    @Default
    @CommandCompletion("@players")
    public static void onDuel(Player player, String target) {
        Player t = plugin.getServer().getPlayer(target);
        if(t == null) {
            player.sendMessage(ChatColor.RED + "Unable to locate target '" + target + "'");
            return;
        }
        
        // Attempted self-duel
        if(t.equals(player)) {
            player.sendMessage(ChatColor.YELLOW + "You can't duel yourself, silly!");
            return;
        }
        
        // Challenge / Accept
        if(plugin.getDuelManager().isPlayerDueling(t)) {
            player.sendMessage(ChatColor.YELLOW + "You accepted " + t.getName() + "'s challenge! ");
            t.sendMessage(ChatColor.YELLOW + player.getName() + "accepted your challenge!");
        } else {
            player.sendMessage(ChatColor.YELLOW + "You challenged " + t.getName() + " to a duel!");
            t.sendMessage(ChatColor.YELLOW + player.getName() + " challenged you to a duel! Type '/duel " + player.getName() + "' to accept!");
        }
    
        plugin.getDuelManager().challenge(player, t);
    }
}
