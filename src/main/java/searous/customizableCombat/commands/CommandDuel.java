package searous.customizableCombat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import searous.customizableCombat.main.CustomizableCombat;
import searous.customizableCombat.messages.MessageContext;

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
    public static void onDuel(CommandSender sender, String target) {
        Player targetPlayer = plugin.getServer().getPlayer(target);
        if(targetPlayer == null) {
            MessageContext context = new MessageContext("messages.cannot-find-target", sender)
                .setSpecial(target);
            plugin.getMessageHandler().sendMessage(context);
            return;
        }
    
        // Ensure sender is a player
        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players may use this command");
            return;
        }
        Player player = (Player)sender;
        
        // Attempted self-duel
        if(targetPlayer.equals(player)) {
            MessageContext context = new MessageContext("messages.duel.command.attempt-duel-self", player)
                .setTarget(targetPlayer);
            plugin.getMessageHandler().sendMessage(context);
            return;
        }
        
        // Attempting to challenge a player already in a duel
        if(plugin.getDuelManager().isPlayerDueling(targetPlayer)) {
            MessageContext context = new MessageContext("messages.duel.command.attempt-duel-target-already-dueling", player)
                .setTarget(targetPlayer);
            plugin.getMessageHandler().sendMessage(context);
        }
        
        // Challenge / Accept
        if(plugin.getDuelManager().getDuel(targetPlayer, player) != null) {
            // Send to player
            MessageContext context = new MessageContext("messages.duel.command.accept", player)
                .setTarget(targetPlayer);
            plugin.getMessageHandler().sendMessage(context);
            
            // Send to target
            context = new MessageContext("messages.duel.command.inform-accepted", targetPlayer)
                .setTarget(player);
            plugin.getMessageHandler().sendMessage(context);
        } else {
            // Send to player
            MessageContext context = new MessageContext("messages.duel.command.challenge", player)
                .setTarget(targetPlayer);
            plugin.getMessageHandler().sendMessage(context);
            
            // Send to target
            context = new MessageContext("messages.duel.command.inform-challenged", targetPlayer)
                .setTarget(player);
            plugin.getMessageHandler().sendMessage(context);
        }
    
        plugin.getDuelManager().challenge(player, targetPlayer);
    }
}
