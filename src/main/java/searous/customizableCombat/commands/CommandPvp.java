package searous.customizableCombat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import searous.customizableCombat.main.CustomizableCombat;
import searous.customizableCombat.messages.MessageContext;

import java.util.logging.Level;

@CommandAlias("pvp")
@CommandPermission("customizable-combat.pvp")
public class CommandPvp extends BaseCommand {
    
    private static CustomizableCombat plugin;
    
    public CommandPvp(CustomizableCombat p) {
        if(plugin != null)
            p.getLogger().log(Level.SEVERE, "Attempting to instantiate duplicate copy of PVP command");
        
        plugin = p;
    }
    
    @Default
    @CommandCompletion("on|true|off|false")
    public static void onSetSelf(Player player, boolean value) {
        plugin.setPvpEnabled(player.getUniqueId(), value);
        
        MessageContext context = new MessageContext("messages.pvp.command.set-self", player)
            .setValue(value);
        plugin.getMessageHandler().sendMessage(context);
    }
    @Subcommand("set|s")
    @CommandCompletion("@players on|true|off|false")
    @CommandPermission("customizable-combat.pvp.set")
    public static void onSet(CommandSender sender, String target, boolean value) {
        Player player = plugin.getServer().getPlayer(target);
        if(player == null) {
            MessageContext context = new MessageContext("messages.cannot-find-target", sender)
                .setSpecial(target);
            plugin.getMessageHandler().sendMessage(context);
            return;
        }
        
        MessageContext context = new MessageContext("messages.pvp.command.set-other", sender)
            .setValue(value)
            .setTarget(player);
        plugin.getMessageHandler().sendMessage(context);
    }
    
    @Subcommand("check|c")
    @CommandCompletion("@players")
    @CommandPermission("customizable-combat.pvp.check")
    public static void onCheck(CommandSender sender, String target) {
        Player other = plugin.getServer().getPlayer(target);
        if(other == null) {
            MessageContext context = new MessageContext("messages.cannot-find-target", sender)
                .setSpecial(target);
            plugin.getMessageHandler().sendMessage(context);
            return;
        }
        
        boolean value = plugin.getPvpEnabled(other.getUniqueId());
        MessageContext context = new MessageContext("messages.pvp.command.check", sender)
            .setValue(value)
            .setTarget(other);
        plugin.getMessageHandler().sendMessage(context);
    }
    
    @Subcommand("override set")
    @CommandCompletion("on|ture|off|false")
    @CommandPermission("customizable-combat.pvp.override.set")
    public static void onOverrideSet(CommandSender sender, boolean value) {
        plugin.setPvpEnabledGlobal(plugin.getPvpEnabledGlobal(), value);
    
        boolean override = plugin.getPvpEnabledOverride();
        MessageContext context = new MessageContext("messages.pvp.command.override.set", sender)
            .setValue(override);
        plugin.getMessageHandler().sendMessage(context);
    }
    @Subcommand("override enabled")
    @CommandCompletion("on|ture|off|false")
    @CommandPermission("customizable-combat.pvp.override.set")
    public static void onOverrideEnabledSet(CommandSender sender, boolean value) {
        plugin.setPvpEnabledGlobal(value, plugin.getPvpEnabledOverride());
        
        boolean override = plugin.getPvpEnabledGlobal();
        MessageContext context = new MessageContext("messages.pvp.command.override.enabled", sender)
            .setValue(override);
        plugin.getMessageHandler().sendMessage(context);
    }
    
    @Subcommand("override check")
    @CommandPermission("customizable-combat.pvp.override.check")
    public static void onOverrideCheck(CommandSender sender) {
        boolean override = plugin.getPvpEnabledOverride();
        MessageContext context = new MessageContext("messages.pvp.command.override.check", sender)
            .setValue(override);
        plugin.getMessageHandler().sendMessage(context);
    }
    @Subcommand("override enabled check")
    @CommandPermission("customizable-combat.pvp.override.check")
    public static void onOverrideEnabledCheck(CommandSender sender) {
        boolean override = plugin.getPvpEnabledGlobal();
        MessageContext context = new MessageContext("messages.pvp.command.override.enabled-check", sender)
            .setValue(override);
        plugin.getMessageHandler().sendMessage(context);
    }
}
