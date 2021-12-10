package searous.customizableCombat.main;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InternalPAPIExpansion extends PlaceholderExpansion {
    private final CustomizableCombat plugin;
    
    protected InternalPAPIExpansion(CustomizableCombat plugin) {
        this.plugin = plugin;
    }
    
    // Set our placeholders
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        String output;
        if(params.equalsIgnoreCase("customizablecombat_pvp_preference")) {
            output = getOnOff(plugin.getPvpEnabled(player.getUniqueId()));
            return ChatColor.stripColor(output);
        }
        
        return null;
    }
    
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if(params.equalsIgnoreCase("customizablecombat_pvp_override_enabled")) {
            return getOnOff(plugin.getPvpEnabledGlobal());
        }
        
        if(params.equalsIgnoreCase("customizablecombat_pvp_override_setting")) {
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
    public @NotNull String getIdentifier() {
        return "CustomizableCombat";
    }
    
    @Override
    public @NotNull String getAuthor() {
        return "Katie (Searous)";
    }
    
    @Override
    public @NotNull String getVersion() {
        return "v1.0";
    }
    
    // Private utility
    private String getOnOff(boolean value) {
        if(value)
            return plugin.getMessagesConfig().getString("words.true");
        else
            return plugin.getMessagesConfig().getString("words.false");
    }
}
