package searous.customizableCombat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import searous.customizableCombat.main.CustomizableCombat;

import java.util.logging.Level;

@CommandAlias("pvp")
@CommandPermission("customizable-combat.pvp")
public class ACFCommandPvp extends BaseCommand {
    
    private static CustomizableCombat plugin;
    
    public ACFCommandPvp(CustomizableCombat p) {
        if(plugin != null)
            p.getLogger().log(Level.SEVERE, "Attempting to instantiate duplicate copy of PVP command");
        
        plugin = p;
    }
    
    @Default
    @CommandCompletion("on|true|off|false")
    public static void onSetSelf(Player player, boolean value) {
        plugin.setPvpEnabled(player.getUniqueId(), value);
    
        if(value)
            player.sendMessage(ChatColor.YELLOW + "Your PvP has been set to: " + ChatColor.AQUA + "on");
        else
            player.sendMessage(ChatColor.YELLOW + "Your PvP has been set to: " + ChatColor.AQUA + "off");
    }
    @Subcommand("set|s")
    @CommandCompletion("@players on|true|off|false")
    @CommandPermission("customizable-combat.pvp.set")
    public static void onSet(CommandSender sender, String target, boolean value) {
        Player player = plugin.getServer().getPlayer(target);
        if(player == null) {
            sender.sendMessage(ChatColor.RED + "Unable to locate player '" + target + "'");
            return;
        }
        
        plugin.setPvpEnabled(player.getUniqueId(), value);
        if(value)
            player.sendMessage(ChatColor.YELLOW + "Your PvP has been set to: " + ChatColor.AQUA + "on");
        else
            player.sendMessage(ChatColor.YELLOW + "Your PvP has been set to: " + ChatColor.AQUA + "off");
    }
    
    @Subcommand("check|c")
    @CommandCompletion("@players")
    @CommandPermission("customizable-combat.pvp.check")
    public static void onCheck(CommandSender sender, String target) {
        Player player = plugin.getServer().getPlayer(target);
        if(player == null) {
            if(sender instanceof Player) {
                player = (Player) sender;
            } else {
                sender.sendMessage(ChatColor.RED + "Unable to locate player '" + target + "'");
                return;
            }
        }
        
        boolean value = plugin.getPvpEnabled(player.getUniqueId());
        if(value)
            sender.sendMessage(ChatColor.YELLOW + target+ "'s PvP is " + ChatColor.AQUA + "on");
        else
            sender.sendMessage(ChatColor.YELLOW + target+ "'s PvP is " + ChatColor.AQUA + "off");
    }
    
    @Subcommand("override set")
    @CommandCompletion("on|ture|off|false")
    @CommandPermission("customizable-combat.pvp.override.set")
    public static void onOverrideSet(CommandSender sender, boolean value) {
        plugin.setPvpEnabledGlobal(plugin.getPvpEnabledGlobal(), value);
    }
    @Subcommand("override enabled")
    @CommandCompletion("on|ture|off|false")
    @CommandPermission("customizable-combat.pvp.override.set")
    public static void onOverrideEnabledSet(CommandSender sender, boolean value) {
        plugin.setPvpEnabledGlobal(value, plugin.getPvpEnabledOverride());
    }
    
    @Subcommand("override check")
    @CommandPermission("customizable-combat.pvp.override.check")
    public static void onOverrideCheck(CommandSender sender) {
        boolean override = plugin.getPvpEnabledOverride();
        if(override)
            sender.sendMessage(ChatColor.YELLOW + "PvP override is set to " + ChatColor.AQUA + "on");
        else
            sender.sendMessage(ChatColor.YELLOW + "PvP override is set to " + ChatColor.AQUA + "off");
    }
    @Subcommand("override enabled check")
    @CommandPermission("customizable-combat.pvp.override.check")
    public static void onOverrideEnabledCheck(CommandSender sender) {
        boolean override = plugin.getPvpEnabledGlobal();
        if(override)
            sender.sendMessage(ChatColor.YELLOW + "PvP override is " + ChatColor.AQUA + "enabled");
        else
            sender.sendMessage(ChatColor.YELLOW + "PvP override is " + ChatColor.AQUA + "disabled");
    }
}
