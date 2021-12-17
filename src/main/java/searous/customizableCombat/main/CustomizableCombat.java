package searous.customizableCombat.main;

import co.aikar.commands.BukkitCommandManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import searous.customizableCombat.commands.*;
import searous.customizableCombat.duel.DuelManager;
import searous.customizableCombat.file.FileHandler;
import searous.customizableCombat.messages.MessageHandler;
import searous.customizableCombat.utilities.ConfigUpdater;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Main class of Customizable Combat
 * This thing is a mess, and I plan to fix this in a future update
 *
 * @author Katie (Searous)
 */
public final class CustomizableCombat extends JavaPlugin {
    // Instances
    /**
     * Contains the event handler
     */
    private EventHandler eventHandler = null;
    private MessageHandler messageHandler;
    private DuelManager duelManager = null;
    
    // Strings
    private Strings strings = null;
    
    public BukkitCommandManager commandManager;
    private FileHandler fileHandler;
    public FileHandler getFileHandler() { return fileHandler; }
    
    @Override
    public void onEnable() {
        // Strings
        strings = new Strings(this);
        
        // File Handler
        fileHandler = new FileHandler(this);
    
        // Create message handler
        messageHandler = new MessageHandler(this);
        
        // Config
        this.getServer().getConsoleSender().sendMessage(strings.LOG_HEADER + "Loading configs...");
        relaod();
        this.getServer().getConsoleSender().sendMessage(strings.LOG_HEADER + "All configs loaded!");
        
        // Placeholder API
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new InternalPAPIExpansion(this).register();
        }
        
        // Create event handler
        eventHandler = new EventHandler(this);
        
        // Register commands
        //commandPvp = new CommandPvp(this);
        //this.getCommand(CommandPvp.LABEL).setExecutor(commandPvp);
        //getCommand(CommandDuel.LABEL).setExecutor(new CommandDuel(this));
        commandManager = new BukkitCommandManager(this);
        commandManager.registerCommand(new CommandCustomizableCombat(this));
        commandManager.registerCommand(new CommandPvp(this));
        commandManager.registerCommand(new CommandDuel(this));
        //commandManager.registerCommand(new CommandTest(this));
        
        // Register Events
        this.getServer().getPluginManager().registerEvents(eventHandler, this);
        
        // Duel Manager
        duelManager = new DuelManager(this);
        
        // Enabling finished
        this.getServer().getConsoleSender().sendMessage(strings.LOG_HEADER + "PvP Plugin Loaded!");
    }
    
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        //this.saveConfig();
        fileHandler.saveConfigs();
        this.getLogger().log(Level.INFO, strings.LOG_HEADER + "Configs Saved");
        
        // Plugin disable complete
        this.getLogger().log(Level.INFO, strings.LOG_HEADER + "Plugin disabling complete");
    }
    
    public void relaod() {
        fileHandler.loadConfigs();
        messageHandler.reload();
    }
    
    // Getters
    /**
     * Gets the plugin's event handler.
     *
     * @return Instance of {@link EventHandler}
     */
    public Listener getEventHandler() { return eventHandler; }
    /**
     * Gets strings
     *
     * @return Instance of {@link Strings}
     */
    public Strings getStrings() { return strings; }
    
    // Methods
    
    /**
     * Gets weather or not the given player has PvP enabled
     *
     * @param playerId The UUID of the player to check
     * @return Given player's PvP preference as shown in the player preferences file players.yml
     */
    public boolean getPvpEnabled(UUID playerId) {
        return fileHandler.getPlayersConfig().getBoolean(playerId.toString() + "." + strings.PREFERANCE_PVP);
    }
    
    /**
     * Gets if global PvP is enabled
     */
    public boolean getPvpEnabledGlobal() {
        return fileHandler.getWorldsConfig().getBoolean("global." + strings.CONFIG_PVP_GLOBAL_ENABLED);
    }
    
    /**
     * Gets the global override setting
     *
     * @return PvP Override setting
     */
    public boolean getPvpEnabledOverride() {
        return fileHandler.getWorldsConfig().getBoolean("global." + strings.CONFIG_PVP_GLOBAL_OVERRIDE);
    }
    
    /**
     * Sets the given player's PvP state
     *
     * @param playerId The UUID of the target player
     * @param enabled New PvP state
     */
    public void setPvpEnabled(UUID playerId, boolean enabled) {
        fileHandler.getPlayersConfig().set(playerId.toString() + "." + strings.PREFERANCE_PVP, enabled);
    }

    /**
     * Sets both global, and global override settings. May change in the future
     *
     * @param global New global setting
     * @param override New global override setting
     */
    public void setPvpEnabledGlobal(boolean global, boolean override) {
        FileConfiguration worlds = fileHandler.getWorldsConfig();
        worlds.set("global." + strings.CONFIG_PVP_GLOBAL_ENABLED, global);
        worlds.set("global." + strings.CONFIG_PVP_GLOBAL_OVERRIDE, override);
        //saveWorldsConfig();
    }
    
    public DuelManager getDuelManager() {
        return duelManager;
    }
    
    public MessageHandler getMessageHandler() {
        return messageHandler;
    }
}

