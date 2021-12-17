package searous.customizableCombat.main;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class InternalPAPIExpansion extends PlaceholderExpansion {
    private final CustomizableCombat plugin;
    
    protected InternalPAPIExpansion(CustomizableCombat plugin) {
        this.plugin = plugin;
    }
    
    // Set our placeholders
    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("pvp_preference")) {
            String output = getOnOff(plugin.getPvpEnabled(player.getUniqueId()));
            return ChatColor.stripColor(output);
        }
        
       if(params.equalsIgnoreCase("pvp_override_enabled")) {
           return getOnOff(plugin.getPvpEnabledGlobal());
       }
    
       if(params.equalsIgnoreCase("pvp_override_setting")) {
           return getOnOff(plugin.getPvpEnabledOverride());
       }
    
       return null;
    }
    
    //
    @Override
    public boolean persist() {
        return true;
    }
    
    // Required strings
    @Override
    public String getIdentifier() {
        return "customizablecombat";
    }
    
    @Override
    public String getAuthor() {
        return "Katie (Searous)";
    }
    
    @Override
    public String getVersion() {
        return "v1.0";
    }
    
    // Private utility
    private String getOnOff(boolean value) {
        if(value)
            return plugin.getFileHandler().getMessagesConfig().getString("words.true");
        else
            return plugin.getFileHandler().getMessagesConfig().getString("words.false");
    }
}
