package searous.customizableCombat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import searous.customizableCombat.main.CustomizableCombat;

import java.util.logging.Level;

@CommandAlias("duel")
@CommandPermission("customizable-combat.duel")
public class CommandDuel extends BaseCommand {
    
    private static CustomizableCombat plugin;
    
    public CommandDuel(CustomizableCombat p) {
        if(plugin != null)
            p.getLogger().log(Level.SEVERE, "Attempting to instantiate duplicate copy of Duel command");
        
        plugin = p;
    }
    
    @Default
    @CommandCompletion("@players")
    public static void onDuel(Player player, String target) {
        Player targetPlayer = plugin.getServer().getPlayer(target);
        if(targetPlayer == null) {
            player.sendMessage(ChatColor.RED + "Unable to locate target '" + target + "'");
            return;
        }
        
        // Attempted self-duel
        if(targetPlayer.equals(player)) {
            player.sendMessage(ChatColor.YELLOW + "You can't duel yourself, silly!");
            return;
        }
        
        // Attempting to challenge a player already in a duel
        if(plugin.getDuelManager().isPlayerDueling(targetPlayer)) {
            player.sendMessage(ChatColor.YELLOW + targetPlayer.getName() + " is already dueling@");
        }
        
        // Challenge / Accept
        if(plugin.getDuelManager().getDuel(targetPlayer, player) != null) {
            player.sendMessage(ChatColor.YELLOW + "You accepted " + targetPlayer.getName() + "'s challenge!");
            targetPlayer.sendMessage(ChatColor.YELLOW + player.getName() + " accepted your challenge!");
        } else {
            player.sendMessage(ChatColor.YELLOW + "You challenged " + targetPlayer.getName() + " to a duel!");
            targetPlayer.sendMessage(ChatColor.YELLOW + player.getName() + " challenged you to a duel! Type '/duel " + player.getName() + "' to accept!");
        }
    
        plugin.getDuelManager().challenge(player, targetPlayer);
    }
}
