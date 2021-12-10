package searous.customizableCombat.main;

import co.aikar.commands.BukkitCommandManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import searous.customizableCombat.commands.*;
import searous.customizableCombat.duel.DuelManager;
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
    /**
     * Contains the command executor for the /pvp command
     */
    //private CommandPvp commandPvp = null;
    private MessageHandler messageHandler;
    
    private DuelManager duelManager = null;
    
    // Strings
    private Strings strings = null;
    
    // Custom Configs
    /**
     * Config file for player preferences file
     */
    private File filePlayers = null;
    /**
     * Bukkit configuration loaded/saved from players.yml
     */
    private FileConfiguration players = null;
    
    private File fileWorlds = null;
    private FileConfiguration worlds = null;
    
    private File fileMessages = null;
    private FileConfiguration messages = null;
    
    public BukkitCommandManager commandManager;
    
    @Override
    public void onEnable() {
        // Strings
        strings = new Strings(this);
        
        // Config
        this.getServer().getConsoleSender().sendMessage(strings.LOG_HEADER + "Loading configs...");
        reloadConfig();
        this.getServer().getConsoleSender().sendMessage(strings.LOG_HEADER + "All configs loaded!");
        
        // Placeholder API
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new InternalPAPIExpansion(this).register();
        }
        
        // Create event handler
        eventHandler = new EventHandler(this);
        
        // Create message handler
        messageHandler = new MessageHandler(this);
        
        // Register commands
        //commandPvp = new CommandPvp(this);
        //this.getCommand(CommandPvp.LABEL).setExecutor(commandPvp);
        //getCommand(CommandDuel.LABEL).setExecutor(new CommandDuel(this));
        commandManager = new BukkitCommandManager(this);
        commandManager.registerCommand(new CommandCustomizableCombat(this));
        commandManager.registerCommand(new CommandPvp(this));
        commandManager.registerCommand(new CommandDuel(this));
        commandManager.registerCommand(new CommandTest(this));
        
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
        this.getLogger().log(Level.INFO, strings.LOG_HEADER + "Config Saved");
        
        savePlayersConfig();
        this.getLogger().log(Level.INFO, strings.LOG_HEADER + "Player preferences saved");
        saveWorldsConfig();
        this.getLogger().log(Level.INFO, strings.LOG_HEADER + "Player preferences saved");
        
        // Plugin disable complete
        this.getLogger().log(Level.INFO, strings.LOG_HEADER + "Plugin disabling complete");
    }
    
    @Override
    public void reloadConfig() {
        this.saveDefaultConfig();
        File configFile = new File(getDataFolder(), "config.yml");
    
        try {
            ConfigUpdater.update(this, "config.yml", configFile, Arrays.asList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        super.reloadConfig();
    
        List<String> pets = getConfig().getStringList(strings.CONFIG_PROTECTED_PETS);
        for(int i = 0; i < pets.size(); i++) {
            String entry = pets.get(i);
        
            entry = entry.toUpperCase();
            entry = entry.trim();
            entry = entry.replace(' ', '_');
        
            pets.set(i, entry);
        }
        getConfig().set(strings.CONFIG_PROTECTED_PETS, pets);
    
        List<String> mounts = getConfig().getStringList(strings.CONFIG_PROTECTED_MOUNTS);
        for(int i = 0; i < mounts.size(); i++) {
            String entry = mounts.get(i);
        
            entry = entry.toUpperCase();
            entry = entry.trim();
            entry = entry.replace(' ', '_');
        
            mounts.set(i, entry);
        }
        getConfig().set(strings.CONFIG_PROTECTED_MOUNTS, mounts);
    
        filePlayers = new File(this.getDataFolder(), "players.yml");
        players = YamlConfiguration.loadConfiguration(filePlayers);
        players.options().copyDefaults(true);
        savePlayersConfig();
    
        fileWorlds = new File(this.getDataFolder(), "worlds.yml");
        worlds = YamlConfiguration.loadConfiguration(fileWorlds);
        worlds.options().copyDefaults(true);
        saveWorldsConfig();
        
        fileMessages = new File(this.getDataFolder(), "messages.yml");
        messages = YamlConfiguration.loadConfiguration(fileMessages);
        messages.options().copyDefaults(true);
        saveMessagesConfig();
    }
    
    // Getters
    /**
     * Gets the plugin's event handler.
     *
     * @return Instance of {@link EventHandler}
     */
    public Listener getEventHandler() { return eventHandler; }
    /**
     * Gets the {@link FileConfiguration} containing the player preferences config players.yml
     *
     * @return Player preferences configuration
     */
    public FileConfiguration getPlayerPreferences() { return players; }
    /**
     * Gets strings
     *
     * @return Instance of {@link Strings}
     */
    public Strings getStrings() { return strings; }
    
    // Methods
    /**
     * Saves player preferences config
     */
    public void savePlayersConfig() {
        try {
            players.save(filePlayers);
        } catch (IOException ex) {
            this.getLogger().log(Level.SEVERE, strings.LOG_HEADER + "Could not save player preferences config to " + filePlayers, ex);
        }
    }
    public void saveWorldsConfig() {
        try {
            worlds.save(fileWorlds);
        } catch (IOException ex) {
            this.getLogger().log(Level.SEVERE, strings.LOG_HEADER + "Could not save worlds config to " + fileWorlds, ex);
        }
    }
    public void saveMessagesConfig() {
        File configFile = new File(getDataFolder(), "messages.yml");
        
        try {
            ConfigUpdater.update(this, "messages.yml", configFile, Arrays.asList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Gets weather or not the given player has PvP enabled
     *
     * @param playerId The UUID of the player to check
     * @return Given player's PvP preference as shown in the player preferences file players.yml
     */
    public boolean getPvpEnabled(UUID playerId) {
        return players.getBoolean(playerId.toString() + "." + strings.PREFERANCE_PVP);
    }
    
    /**
     * Gets if global PvP is enabled
     */
    public boolean getPvpEnabledGlobal() {
        return worlds.getBoolean("global." + strings.CONFIG_PVP_GLOBAL_ENABLED);
    }
    
    /**
     * Gets the global override setting
     *
     * @return PvP Override setting
     */
    public boolean getPvpEnabledOverride() {
        return worlds.getBoolean("global." + strings.CONFIG_PVP_GLOBAL_OVERRIDE);
    }
    
    /**
     * Sets the given player's PvP state
     *
     * @param playerId The UUID of the target player
     * @param enabled New PvP state
     */
    public void setPvpEnabled(UUID playerId, boolean enabled) {
        players.set(playerId.toString() + "." + strings.PREFERANCE_PVP, enabled);
    }
    
    public FileConfiguration getWorldsConfig() {
        return worlds;
    }
    public FileConfiguration getMessagesConfig() { return messages; }
    
    /**
     * Sets both global, and global override settings. May change in the future
     *
     * @param global New global setting
     * @param override New global override setting
     */
    public void setPvpEnabledGlobal(boolean global, boolean override) {
        worlds.set("global." + strings.CONFIG_PVP_GLOBAL_ENABLED, global);
        worlds.set("global." + strings.CONFIG_PVP_GLOBAL_OVERRIDE, override);
        saveWorldsConfig();
    }
    
    public DuelManager getDuelManager() {
        return duelManager;
    }
    
    public MessageHandler getMessageHandler() {
        return messageHandler;
    }
}

