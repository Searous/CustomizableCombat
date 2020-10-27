package searous.customizableCombat.main;

import org.bukkit.ChatColor;

/**
 * Contains strings used by the plugin
 * The method getStrings() in CustomizableCombat returns the instance used by the plugin
 *
 * @author Katie (Searous)
 */
public final class Strings {
    // Plugin protected strings
    /**
     * Added before log outputs to distinguish them from other outputs by the server or other plugins
     */
    public final String LOG_HEADER = "[CustomizableCombat] " + ChatColor.RESET;
    /**
     * Lowercase, no-spaces version of the plugin's name. Similar to Minecraft's unlocalized names.
     */
    public final String CODE_NAME = "customizable-combat";
    
    // Config paths
    /**
     * Paths for config.yml
     */
    public final String
        CONFIG_PVP_COMMAND_LABEL    = "pvp-command-label",
        CONFIG_PVP_GLOBAL_ENABLED   = "pvp-global-enabled",
        CONFIG_PVP_GLOBAL_OVERRIDE  = "pvp-global-override",
        CONFIG_PVP_DEFAULT          = "pvp-default",
        CONFIG_PET_PROTECTION       = "pet-protection",
        CONFIG_PROTECTED_PETS       = "protected-pets",
        CONFIG_MOUNT_PROTECTION     = "mount-protection",
        CONFIG_PROTECTED_MOUNTS     = "protected-mounts"
    ;
    
    // Player preferences paths
    /**
     * Paths for each player preference in players.yml. Does not include player names.
     */
    public final String
        PREFERANCE_PVP          = "pvp",
        PREFERENCE_HEALTHBARS   = "healthbars";
    
    // Permissions
    /**
     * The permissions used by this plugin
     */
    public final String
        PERM_PVP            = CODE_NAME + ".pvp.onoff",
        PERM_PVP_GLOBAL     = CODE_NAME + ".pvp.global",
        PERM_PVP_PLAYERS    = CODE_NAME + ".pvp.players",
        PERM_PVP_CHECK      = CODE_NAME + ".pvp.check";
        
    public Strings(CustomizableCombat plugin) {
    
    }
}
