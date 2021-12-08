package searous.customizableCombat.messages;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import searous.customizableCombat.main.CustomizableCombat;

public class MessageHandler {
    
    private CustomizableCombat plugin;
    private String noMessageFound;
    private String chatPrefix;
    
    private String wordOn, wordOff;
    
    private boolean usePlaceholderApi;
    
    public MessageHandler(CustomizableCombat plugin) {
        this.plugin = plugin;
        
        // Fetch config
        reload();
    }
    
    public void reload() {
        FileConfiguration messages = plugin.getMessagesConfig();
        // Messages
        noMessageFound = messages.getString("message-not-found", "No message found: %key%");
        chatPrefix = messages.getString("chat-prefix", "");
        
        // Words
        wordOn = messages.getString("words.on", "on");
        wordOff = messages.getString("words.off", "off");
        
        // Flags
        // NOTE: This may need to be adapted to check if placeholder api is enabled in the future
        usePlaceholderApi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null &&
            messages.getBoolean("use-placeholder-api", false);
    }
    
    public void sendMessage(MessageContext context) {
        Message message = getMessage(context);
        if(message == null)
            return;
        
        context.getSender().sendMessage(message.text);
        if(message.playSound && context.getPlayer() != null) {
            context.getPlayer().playSound(context.getPlayer().getLocation(), message.sound, message.volume, message.pitch);
        }
    }
    
    public Message getMessage(final MessageContext context) {
        Message output = readMessage(context.getMessagePath());
        
        // Chat prefix
        output.text = chatPrefix + output.text;
        
        // Parse proprietary placeholders
        output.text = parsePlaceholders(context, output.text);
    
        // Parse chat colors
        output.text = ChatColor.translateAlternateColorCodes('&', output.text);
        
        // Parse placeholder api placeholders
        if(usePlaceholderApi) {
            output.text = PlaceholderAPI.setPlaceholders(context.getPlayer(), output.text);
        }
        
        // Return
        return output;
    }
    
    private String parsePlaceholders(MessageContext context, String text) {
        String output = text;
        
        output = output.replace("%value%", context.isValue() ? wordOn : wordOff);
        if(context.getTarget() != null)
            output = output.replace("%target%", context.getTarget().getDisplayName());
        if(context.getPlayer() != null)
            output = output.replace("%player%", context.getPlayer().getDisplayName());
        output = output.replace("%key%", context.getMessagePath());
        
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
