package searous.customizableCombat.utilities;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import searous.customizableCombat.main.CustomizableCombat;

public class MessageHandler {
    
    private CustomizableCombat plugin;
    private boolean usePlaceholderApi;
    private FileConfiguration messages;
    private String noMessageFound;
    private String chatPrefix;
    
    private String wordOn, wordOff;
    
    public MessageHandler(CustomizableCombat plugin) {
        this.plugin = plugin;
        
        // Fetch config
        messages = plugin.getMessagesConfig();
        usePlaceholderApi = messages.getBoolean("use-placeholder-api", false);
        noMessageFound = messages.getString("message-not-found", "No message found: %key%");
        chatPrefix = messages.getString("chat-prefix", "");
        
        wordOn = getMessage(new MessageContext("words.on")).text;
        wordOff = getMessage(new MessageContext("words.off")).text;
    }
    
    public void sendMessage(MessageContext context) {
        Message output = getMessage(context);
        
        // TODO: Message sending
    }
    
    public Message getMessage(final MessageContext context) {
        Message output = readMessage(context.messagePath);
        
        // Parse chat colors
        output.text = ChatColor.translateAlternateColorCodes('&', output.text);
        
        // Parse placeholder api placeholders
        if(usePlaceholderApi) {
            // TODO: Implement placeholder API
        }
        
        // Parse proprietary placeholders
        output.text = parsePlaceholders(context, output.text);
        
        // Return
        return output;
    }
    
    public String parsePlaceholders(MessageContext context, String text) {
        String output = text;
        
        if(context.player != null)
            output.replace("%target%", context.target.getDisplayName());
        if(context.target != null)
            output.replace("%player%", context.player.getDisplayName());
        output.replace("%value%", context.value ? "d" : "");
        output.replace("%key%", context.messagePath);
        
        return output;
    }
    
    private Message readMessage(String path) {
        if(messages.isString(path))
            return new Message(messages.getString(path, noMessageFound));
        else if(messages.isSet(path))
            return new Message(messages, path, noMessageFound);
        return new Message(noMessageFound);
    }
    
    public class Message {
        public String text;
        public boolean playSound;
        public String soundName;
        public int volume, pitch;
        
        public Message(FileConfiguration messages, String path, String def) {
            text = messages.getString(path + ".value", def);
            playSound = messages.getBoolean(path + ".sound.enabled");
            soundName = messages.getString(path + ".sound.sound");
            volume = messages.getInt(path + ".sound.volume");
            pitch = messages.getInt(path + ".sound.pitch");
        }
        public Message(String text) {
            this.text = text;
        }
    }
    
    public class MessageContext {
        public final String messagePath;
        public Player player, target;
        public boolean value;
        
        public MessageContext(String messagePath) {
            this.messagePath = messagePath;
        }
    }
}
