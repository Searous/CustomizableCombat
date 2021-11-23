package searous.customizableCombat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import org.bukkit.entity.Player;
import searous.customizableCombat.main.CustomizableCombat;
import searous.customizableCombat.messages.MessageContext;

import java.util.logging.Level;

@CommandAlias("test|t")
public class ACFCommandTest extends BaseCommand {
    
    private static CustomizableCombat plugin;
    
    public ACFCommandTest(CustomizableCombat p) {
        if(plugin != null)
            p.getLogger().log(Level.SEVERE, "Attempting to instantiate duplicate copy of test command");
        
        plugin = p;
    }
    
    @Default
    @CommandCompletion("path")
    public static void onDefault(Player player, String path) {
        if(path == null || path.equals("")) {
            path = "messages.test";
        }
        MessageContext context = new MessageContext(path, player);
        context.player = player;
        plugin.getMessageHandler().sendMessage(context);
    }
}
