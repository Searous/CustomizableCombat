package searous.customizableCombat.messages;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import searous.customizableCombat.main.CustomizableCombat;

import java.util.logging.Level;

public class Message {
    public String text;
    public boolean playSound;
    public Sound sound;
    public float volume, pitch;
    
    public Message(CustomizableCombat plugin, String path, String def) {
        FileConfiguration messages = plugin.getMessagesConfig();
        
        text = messages.getString(path + ".value", def);
        playSound = messages.getBoolean(path + ".sound.enabled");
        
        try {
            sound = Sound.valueOf(messages.getString(path + ".sound.sound"));
        } catch(IllegalArgumentException e) {
            Bukkit.getServer().getLogger().log(Level.SEVERE, "");
        }
        
        volume = messages.getInt(path + ".sound.volume");
        pitch = messages.getInt(path + ".sound.pitch");
    }
    public Message(String text) {
        this.text = text;
    }
}
