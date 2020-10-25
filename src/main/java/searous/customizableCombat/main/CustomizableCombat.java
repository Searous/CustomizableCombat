package searous.customizableCombat.main;

import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import searous.customizableCombat.commands.CommandPvp;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Main class of Customizable Combat
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
    private CommandPvp commandPvp = null;
    
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
    
    @Override
    public void onEnable() {
        // Create event handler
        eventHandler = new EventHandler(this);
        
        // Register commands
        commandPvp = new CommandPvp(this);
        this.getCommand(CommandPvp.LABEL).setExecutor(commandPvp);
        
        // Register Events
        this.getServer().getPluginManager().registerEvents(eventHandler, this);
        
        // Config
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        filePlayers = new File(this.getDataFolder(), "players.yml");
        players = YamlConfiguration.loadConfiguration(filePlayers);
        players.options().copyDefaults(true);
        savePlayersConfig();
        
        //fileWorlds = new File(this.getDataFolder(), "worlds.yml");
        //worlds = YamlConfiguration.loadConfiguration(fileWorlds);
        //saveWorldsConfig();
        
        // Strings
        strings = new Strings(this);
        
        // Enabling finished
        this.getServer().getConsoleSender().sendMessage(strings.LOG_HEADER + "PvP Plugin Loaded!");
    }
    
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.saveConfig();
        this.getLogger().log(Level.INFO, strings.LOG_HEADER + "Config Saved");
        
        savePlayersConfig();
        this.getLogger().log(Level.INFO, strings.LOG_HEADER + "Player preferences saved");

        // Plugin disable complete
        this.getLogger().log(Level.INFO, strings.LOG_HEADER + "Plugin disabling complete");
    }
    
    // Getters
    /**
     * Gets the plugin's event handler.
     *
     * @return Instance of {@link EventHandler}
     */
    public Listener getEventHandler() { return eventHandler; }
    /**
     * Gets the {@link CommandExecutor} associated with the "/pvp" command
     *
     * @return Instance of {@link CommandPvp}
     */
    public CommandExecutor getCommandPvp() { return commandPvp; }
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
            this.getLogger().log(Level.SEVERE, strings.LOG_HEADER + "Could not save worlds config to " + filePlayers, ex);
        }
    }
    
    
    /**
     * Gets weather or not the given player has PvP enabled
     *
     * @param name The name of the player to check
     * @return Given player's PvP preference as shown in the player preferences file players.yml
     */
    public boolean getPvpEnabled(String name) {
        return players.getBoolean(name + "." + strings.PREFERANCE_PVP);
    }
    
    /**
     * Gets if global PvP is enabled
     */
    public boolean getPvpEnabledGlobal(World world) {
        return this.getConfig().getBoolean(strings.CONFIG_PVP_GLOBAL_ENABLED);
    }
    
    /**
     * Gets the global override setting
     *
     * @return PvP Override setting
     */
    public boolean getPvpEnabledOverride(World world) {
        return this.getConfig().getBoolean(strings.CONFIG_PVP_GLOBAL_OVERRIDE);
    }
    
    /**
     * Sets the given player's PvP state
     *
     * @param name Name of the player
     * @param enabled New PvP state
     */
    public void setPvpEnabled(String name, boolean enabled) {
        players.set(name + "." + strings.PREFERANCE_PVP, enabled);
    }
    
    /**
     * Sets both global, and global override settings. May change in the future
     *
     * @param global New global setting
     * @param override New global override setting
     */
    public void setPvpEnabledGlobal(boolean global, boolean override) {
        this.getConfig().set(strings.CONFIG_PVP_GLOBAL_ENABLED, global);
        this.getConfig().set(strings.CONFIG_PVP_GLOBAL_OVERRIDE, override);
    }
}
