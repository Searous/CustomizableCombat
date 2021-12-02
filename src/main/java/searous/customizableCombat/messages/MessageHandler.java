package searous.customizableCombat.messages;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import searous.customizableCombat.main.CustomizableCombat;

import java.util.logging.Level;

public class MessageHandler {
    
    private CustomizableCombat plugin;
    private String noMessageFound;
    private String chatPrefix;
    
    private String wordOn, wordOff;
    
    public MessageHandler(CustomizableCombat plugin) {
        this.plugin = plugin;
        
        // Fetch config
        FileConfiguration messages = plugin.getMessagesConfig();
        noMessageFound = messages.getString("message-not-found", "No message found: %key%");
        chatPrefix = messages.getString("chat-prefix", "");
        
        wordOn = messages.getString("words.on", "on");
        wordOff = messages.getString("words.off", "off");
    }
    
    public void sendMessage(MessageContext context) {
        Message message = getMessage(context);
        if(message == null)
            return;
        
        context.player.sendMessage(message.text);
        if(message.playSound) {
            context.player.playSound(context.player.getLocation(), message.sound, message.volume, message.pitch);
        }
    }
    
    public Message getMessage(final MessageContext context) {
        Message output = readMessage(context.messagePath);
        
        // Parse chat colors
        output.text = ChatColor.translateAlternateColorCodes('&', output.text);
    
        // Parse proprietary placeholders
        output.text = parsePlaceholders(context, output.text);
        
        // Parse placeholder api placeholders
        // NOTE: This may need to be adapted to check if placeholder api is enabled in the future
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null && plugin.getMessagesConfig().getBoolean("use-placeholder-api", false)) {
            output.text = PlaceholderAPI.setPlaceholders(context.player, output.text);
        }
        
        // Return
        return output;
    }
    
    public String parsePlaceholders(MessageContext context, String text) {
        String output = text;
    
        output = output.replace("%value%", context.value ? "d" : "");
        if(context.target != null)
            output = output.replace("%target%", context.target.getDisplayName());
        output = output.replace("%player%", context.player.getDisplayName());
        output = output.replace("%key%", context.messagePath);
        
        return output;
    }
    
    private Message readMessage(String path) {
        FileConfiguration messages = plugin.getMessagesConfig();
        if(messages.isString(path))
            return new Message(messages.getString(path, noMessageFound));
        else if(messages.isSet(path))
            return new Message(plugin, path, noMessageFound);
        return new Message(noMessageFound);
    }
}
