package searous.customizableCombat.file;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import searous.customizableCombat.main.CustomizableCombat;
import searous.customizableCombat.utilities.ConfigUpdater;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class FileHandler {
    
    private final CustomizableCombat plugin;
    
    // Configs
    private FileConfiguration config;
    
    private FileConfiguration playersConfig;
    public FileConfiguration getPlayersConfig() { return playersConfig; }
    
    private FileConfiguration messagesConfig;
    public FileConfiguration getMessagesConfig() { return messagesConfig; }
    
    private FileConfiguration worldsConfig;
    public FileConfiguration getWorldsConfig() { return worldsConfig; }
    
    public FileHandler(CustomizableCombat plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Loads or reloads all configs
     */
    public void loadConfigs() {
        // Main Config
        config = plugin.getConfig();
    
        List<String> pets = config.getStringList(plugin.getStrings().CONFIG_PROTECTED_PETS);
        for(int i = 0; i < pets.size(); i++) {
            String entry = pets.get(i);
        
            entry = entry.toUpperCase();
            entry = entry.trim();
            entry = entry.replace(' ', '_');
        
            pets.set(i, entry);
        }
        config.set(plugin.getStrings().CONFIG_PROTECTED_PETS, pets);
    
        List<String> mounts = config.getStringList(plugin.getStrings().CONFIG_PROTECTED_MOUNTS);
        for(int i = 0; i < mounts.size(); i++) {
            String entry = mounts.get(i);
        
            entry = entry.toUpperCase();
            entry = entry.trim();
            entry = entry.replace(' ', '_');
        
            mounts.set(i, entry);
        }
        config.set(plugin.getStrings().CONFIG_PROTECTED_MOUNTS, mounts);
        
        // Other Configs
        playersConfig = loadConfig("players", false);
        messagesConfig = loadConfig("messages", true);
        worldsConfig = loadConfig("worlds", false);
    }
    
    /**
     * Saves all configs
     */
    public void saveConfigs() {
        saveConfig(config, "config");
        saveConfig(playersConfig, "players");
        //saveConfig(worldsConfig);
    }
    
    private FileConfiguration loadConfig(String name, boolean allowUpdate) {
        File file = new File(plugin.getDataFolder(), name + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.options().copyDefaults(true);
        
        // Write default config if one does not exist
        if(!file.exists()) {
            try {
                config.save(file);
            } catch(IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to write default configuration file for '" + name + ".yml'", e);
            }
        }
        
        return config;
    }
    private void updateConfig(String name, List<String> ignoreSections) {
        File file = new File(plugin.getDataFolder(), name + ".yml");
        
        try {
            ConfigUpdater.update(plugin, name + ".yml", file, ignoreSections);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void saveConfig(FileConfiguration config, String name) {
        plugin.getLogger().log(Level.WARNING, name);
        
        try {
            config.save(new File(name + ".yml"));
        } catch(IOException e) {
            plugin.getLogger().log(Level.SEVERE, e.getMessage());
        }
        
        // Re-add comments to the file
        updateConfig(name, Arrays.asList());
    }
}
