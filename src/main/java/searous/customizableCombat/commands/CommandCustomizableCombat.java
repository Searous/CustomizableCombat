package searous.customizableCombat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import searous.customizableCombat.main.CustomizableCombat;

import java.util.logging.Level;

@CommandAlias("customizablecombat")
public class CommandCustomizableCombat extends BaseCommand {
    private static CustomizableCombat plugin;
    
    public CommandCustomizableCombat(CustomizableCombat p) {
        if(plugin != null)
            p.getLogger().log(Level.SEVERE, "Attempting to instantiate duplicate copy of Duel command");
        
        plugin = p;
    }
    
    @Subcommand("reload")
    public static void onReload(CommandSender sender) {
        plugin.reloadConfig();
        plugin.getMessageHandler().reload();
        
        String message = "All configs reloaded!";
        plugin.getLogger().log(Level.INFO, plugin.getStrings().LOG_HEADER + message);
        
        // Send to sender if sender was not the console
        if(!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(message);
        }
        
    }
}
