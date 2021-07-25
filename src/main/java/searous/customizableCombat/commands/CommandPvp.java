package searous.customizableCombat.commands;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import searous.customizableCombat.main.CustomizableCombat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Katie (Searous)
 */
public class CommandPvp implements TabExecutor {
    private CustomizableCombat plugin;
    
    public static final String
        // Command Labels
        LABEL               = "pvp",
        SUB_LABEL_CHECK     = "check",
        SUB_LABEL_LOCAL     = "players",
        SUB_LABEL_GLOBAL    = "global",
        
        // Command Words
        ON  = "on",
        OFF = "off",
        
        // Operators
        ENABLED     = "enabled",
        OVERRIDE    = "override",
        CHECK       = "check";

    public CommandPvp(CustomizableCombat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Get list of players who have played on the server
        OfflinePlayer[] op = plugin.getServer().getOfflinePlayers();
        
        // List of player names
        List<UUID> players = new ArrayList<>();
        for(OfflinePlayer offlinePlayer : op) {
            players.add(offlinePlayer.getUniqueId());
        }

        // Parse Command
        if(command.getName().equalsIgnoreCase(LABEL)) {
            if(args.length >= 1) {
                String subSubcommand = args[0];
                
                // Check for/if a sub-command is being executed
                if(subSubcommand.equalsIgnoreCase(ON)) {
                    //region Quick enable
                    if(sender.hasPermission(plugin.getStrings().PERM_PVP)) {
                        if(sender instanceof Player) {
                            plugin.setPvpEnabled(((Player) sender).getUniqueId(), true);
                            sender.sendMessage(ChatColor.YELLOW + "Your PvP was updated to: " + ChatColor.AQUA + ON);
                        } else {
                            // Sender is not a player
                            sender.sendMessage(ChatColor.RED + "Cannot set PvP state for " + sender.getName() + "; must be a player");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You do not have permission to use this command. Contact an admin or moderator if this is an error.");
                    }

                    //endregion
                } else if(subSubcommand.equalsIgnoreCase(OFF)) {
                    //region Quick disable
    
                    if(sender.hasPermission(plugin.getStrings().PERM_PVP)) {
                        if(sender instanceof Player) {
                            plugin.setPvpEnabled(((Player) sender).getUniqueId(), false);
                            sender.sendMessage(ChatColor.YELLOW + "Your PvP was updated to: " + ChatColor.AQUA + OFF);
                        } else {
                            // Sender is not a player
                            sender.sendMessage(ChatColor.RED + "Cannot set PvP state for " + sender.getName() + "; must be a player");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You do not have permission to use this command. Contact an admin or moderator if this is an error.");
                    }

                    //endregion
                } else if(subSubcommand.equalsIgnoreCase(SUB_LABEL_CHECK)) {
                    //region Sub-Command Check

                    // Syntax: /pvp check [player]
                    
                    if(sender.hasPermission(plugin.getStrings().PERM_PVP_CHECK)) {
                        if(args.length == 2) {
                            // Player Specified
                            // Check if player has played
                            if(plugin.getPlayerPreferences().get(args[1]) != null) {
                                // Player has played before
                                if(plugin.getPvpEnabled(plugin.getServer().getPlayer(args[1]).getUniqueId())) {
                                    sender.sendMessage(ChatColor.YELLOW + args[1] + "'s PvP is: " + ChatColor.AQUA + ON);
                                } else {
                                    sender.sendMessage(ChatColor.YELLOW + args[1] + "'s PvP is: " + ChatColor.AQUA + OFF);
                                }
                            } else {
                                // Player has not played before
                                sender.sendMessage(ChatColor.RED + "Unable to find player " + args[1]);
                            }
                        } else {
                            // Player Not Specified
                            if(sender instanceof Player) {
                                // Sender is a player, tell them their PvP state
                                if(plugin.getPvpEnabled(((Player) sender).getUniqueId())) {
                                    sender.sendMessage(ChatColor.YELLOW + "Your PvP is: " + ChatColor.AQUA + ON);
                                } else {
                                    sender.sendMessage(ChatColor.YELLOW + "Your PvP is: " + ChatColor.AQUA + OFF);
                                }
                            } else {
                                // Sender is not a player
                                sender.sendMessage(ChatColor.RED + "You must be or specify a player");
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You do not have permission to use this command. Contact an admin or moderator if this is an error.");
                    }

                    //endregion
                } else if(subSubcommand.equalsIgnoreCase(SUB_LABEL_LOCAL)) {
                    //region Sub-Command Local

                    // Syntax: /pvp players <player> <on/off>
                    
                    if(sender.hasPermission(plugin.getStrings().PERM_PVP_PLAYERS)) {
                        // Check if the correct number of arguments was provided
                        if(args.length == 3) {
                            // Correct argument count
        
                            // Name and state to use
                            String name  = args[1];
                            String state = args[2];
                            UUID uuid = ((Player)sender).getUniqueId();
                            
                            // Check if player has joined before
                            if(players.contains(uuid)) {
                                // Player has joined
            
                                // Check if setting to on or off
                                if(state.equalsIgnoreCase(ON)) {
                                    // Set to on
                                    plugin.setPvpEnabled(uuid, true);
                
                                    // Tell the sender the given player's PvP was updated
                                    if(!name.equals(sender.getName())) {
                                        sender.sendMessage(ChatColor.YELLOW + name + "'s PvP was changed to: " + ChatColor.AQUA + ON);
                                    }
                
                                    // Tell the player their PvP was updated
                                    Player player = plugin.getServer().getPlayer(name);
                                    if(player != null) {
                                        if(player.isOnline()) {
                                            player.sendMessage(ChatColor.YELLOW + "Your PvP has been changed to: " + ChatColor.AQUA + ON);
                                        }
                                    }
                                } else if(state.equalsIgnoreCase(OFF)) {
                                    // Set of off
                                    plugin.setPvpEnabled(uuid, false);
                
                                    // Tell the sender the given player's PvP was updated
                                    if(!name.equals(sender.getName())) {
                                        sender.sendMessage(ChatColor.YELLOW + name + "'s PvP was changed to: " + ChatColor.AQUA + OFF);
                                    }
                
                                    // Tell the player their PvP was updated
                                    Player player = plugin.getServer().getPlayer(name);
                                    if(player != null) {
                                        if(player.isOnline()) {
                                            player.sendMessage(ChatColor.YELLOW + "Your PvP has been changed to: " + ChatColor.AQUA + OFF);
                                        }
                                    }
                                } else {
                                    // Not correct format
                                    sender.sendMessage(ChatColor.RED + "Usage: /pvp players <player> <on/off>");
                                }
                            } else {
                                // Player has not joined
                                sender.sendMessage(ChatColor.RED + "Unable to find player " + name);
                            }
                        } else {
                            // Invalid argument count
                            sender.sendMessage(ChatColor.RED + "Usage: /pvp players <player> <on/off>");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You do not have permission to use this command. Contact an admin or moderator if this is an error.");
                    }
                    
                    //endregion
                } else if(subSubcommand.equalsIgnoreCase(SUB_LABEL_GLOBAL)) {
                    //region Sub-Command Global

                    // Syntax: /pvp global <global/override> <on/off>
                    
                    if(sender.hasPermission(plugin.getStrings().PERM_PVP_GLOBAL)) {
                        // Check if enough arguments were provided
                        if(args.length == 3) {
                            // Arguments
                            String type = args[1];
                            String bool = args[2];
        
                            // Check which value is being set
                            if(type.equalsIgnoreCase(ENABLED)) {
                                // Setting global
                                boolean pvpGlobal   = plugin.getWorldsConfig().getBoolean("global." + plugin.getStrings().CONFIG_PVP_GLOBAL_ENABLED);
                                boolean pvpOverride = plugin.getWorldsConfig().getBoolean("global." + plugin.getStrings().CONFIG_PVP_GLOBAL_OVERRIDE);
            
                                if(bool.equalsIgnoreCase(ON)) {
                                    // Setting global to on
                
                                    // Update global PvP setting
                                    plugin.setPvpEnabledGlobal(true, pvpOverride);
                
                                    // Tell players what has changed
                                    for(Player player : plugin.getServer().getOnlinePlayers()) {
                                        if(!player.equals(sender)) {
                                            player.sendMessage(ChatColor.YELLOW + "Global PvP override was enabled, PvP is now forced " + (pvpOverride ? ON : OFF));
                                        }
                                    }
                
                                    // Log this change to the console, and tell the sender what happened
                                    sender.sendMessage(ChatColor.YELLOW + "Global set to: " + ChatColor.AQUA + ON);
                                    plugin.getServer().getConsoleSender().sendMessage(sender.getName() + " updated global PvP to: " + (pvpOverride ? ON : OFF));
                                } else if(bool.equalsIgnoreCase(OFF)) {
                                    // Setting global to off
                
                                    // Update global PvP setting
                                    plugin.setPvpEnabledGlobal(false, pvpOverride);
                
                                    // Tell players what has changed
                                    for(Player player : plugin.getServer().getOnlinePlayers()) {
                                        if(!player.equals(sender)) {
                                            player.sendMessage(ChatColor.YELLOW + "Global PvP override was disabled, PvP is no longer forced");
                                        }
                                    }
                
                                    // Log this change to the console, and tell the sender what happened
                                    sender.sendMessage(ChatColor.YELLOW + "Global set to: " + ChatColor.AQUA + OFF);
                                    plugin.getServer().getConsoleSender().sendMessage(sender.getName() + " updated global PvP to: " + (pvpOverride ? ON : OFF));
                                } else if(bool.equalsIgnoreCase(CHECK)) {
                                    // Checking global
                                    sender.sendMessage(ChatColor.YELLOW + "Global PvP override is: " + ChatColor.AQUA + (pvpGlobal ? ON : OFF));
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Usage: /pvp global <global/override> <on/off>");
                                }
                            } else if(type.equalsIgnoreCase(OVERRIDE)) {
                                // Setting override
                                boolean pvpGlobal   = plugin.getWorldsConfig().getBoolean("global." + plugin.getStrings().CONFIG_PVP_GLOBAL_ENABLED);
                                boolean pvpOverride = plugin.getWorldsConfig().getBoolean("global." + plugin.getStrings().CONFIG_PVP_GLOBAL_OVERRIDE);
            
                                if(bool.equalsIgnoreCase(ON)) {
                                    // Setting override to on
                
                                    // Update global PvP setting
                                    plugin.setPvpEnabledGlobal(pvpGlobal, true);
                
                                    // Tell players what has changed
                                    if(pvpGlobal) {
                                        for(Player player : plugin.getServer().getOnlinePlayers()) {
                                            if(!player.equals(sender)) {
                                                player.sendMessage(ChatColor.YELLOW + "Global PvP override was enabled, PvP is now forced " + ON);
                                            }
                                        }
                                    }
                
                                    // Log this change to the console, and tell the sender what happened
                                    sender.sendMessage(ChatColor.YELLOW + "Override set to: " + ChatColor.AQUA + ON);
                                    plugin.getServer().getConsoleSender().sendMessage(sender.getName() + " updated global PvP to: " + ON);
                                } else if(bool.equalsIgnoreCase(OFF)) {
                                    // Setting override to off
                
                                    // Update global PvP setting
                                    plugin.setPvpEnabledGlobal(pvpGlobal, false);
                
                                    // Tell players what has changed
                                    if(pvpGlobal) {
                                        for(Player player : plugin.getServer().getOnlinePlayers()) {
                                            if(!player.equals(sender)) {
                                                player.sendMessage(ChatColor.YELLOW + "Global PvP override was enabled, PvP is now forced " + OFF);
                                            }
                                        }
                                    }
                
                                    // Log this change to the console, and tell the sender what happened
                                    sender.sendMessage(ChatColor.YELLOW + "Override set to: " + ChatColor.AQUA + OFF);
                                    plugin.getServer().getConsoleSender().sendMessage(sender.getName() + " updated global PvP to: " + OFF);
                                } else if(bool.equalsIgnoreCase(CHECK)) {
                                    // Checking override
                                    sender.sendMessage(ChatColor.YELLOW + "PvP will be overridden with: " + ChatColor.AQUA + (pvpOverride ? ON : OFF));
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Usage: /pvp global <global/override> <on/off/check>");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "Usage: /pvp global <global/override> <on/off/check>");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Usage: /pvp global <global/override> <on/off/check>");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You do not have permission to use this command. Contact an admin or moderator if this is an error.");
                    }

                    //endregion
                }
            } else {
                // Not enough args, show usage prompt
                sender.sendMessage(ChatColor.RED + "Usage: /pvp <on/off>");
            }
        }

        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase(LABEL)) {
            List<String> list = Lists.newArrayList();
            int argIndex = 0;

            if(args.length <= 1) { // On/Off
                if(sender.hasPermission(plugin.getStrings().PERM_PVP)) {
                    list.add(ON);
                    list.add(OFF);
                }
                if(sender.hasPermission(plugin.getStrings().PERM_PVP_PLAYERS)) list.add(SUB_LABEL_LOCAL);
                if(sender.hasPermission(plugin.getStrings().PERM_PVP_GLOBAL)) list.add(SUB_LABEL_GLOBAL);
                if(sender.hasPermission(plugin.getStrings().PERM_PVP_CHECK)) list.add(SUB_LABEL_CHECK);
                
                argIndex = 0;
            } else {
                if(args[0].equalsIgnoreCase(SUB_LABEL_LOCAL) && sender.hasPermission(plugin.getStrings().PERM_PVP_PLAYERS)) { // Local
                    if(args.length == 3) {
                        list.add(ON);
                        list.add(OFF);
                        list.add(CHECK);

                        argIndex = 2;
                    } else {
                        //list = null;

                        argIndex = 1;
                    }
                } else if(args[0].equalsIgnoreCase(SUB_LABEL_GLOBAL) && sender.hasPermission(plugin.getStrings().PERM_PVP_GLOBAL)) { // Global
                    if(args.length == 2) {
                        list.add(ENABLED);
                        list.add(OVERRIDE);

                        argIndex = 1;
                    } else if(args.length == 3) {
                        if(args[1].equalsIgnoreCase(ENABLED)) {
                            list.add(ON);
                            list.add(OFF);
                            list.add(CHECK);
                        } else if(args[1].equalsIgnoreCase(OVERRIDE)) {
                            list.add(ON);
                            list.add(OFF);
                            list.add(CHECK);
                        }

                        argIndex = 2;
                    }
                } else if(args[0].equalsIgnoreCase(SUB_LABEL_CHECK) && sender.hasPermission(plugin.getStrings().PERM_PVP_CHECK)) { // Check
                    if(args.length == 2) {
                        //list = null;

                        argIndex = 1;
                    }
                }
            }

            // If list is empty, add names of all online players
            if(list.size() == 0) {
                for(Player player : plugin.getServer().getOnlinePlayers()) {
                    list.add(player.getName());
                }
            }

            // Search tab list
            List<String> newList = Lists.newArrayList();
            if(args[argIndex].length() >= 1) {
                for(String option : list) {
                    if(option.length() >= args[argIndex].length()) {
                        String subString = option.substring(0, args[argIndex].length());

                        if(subString.equalsIgnoreCase(args[argIndex])) {
                            newList.add(option);
                        }
                    }
                }
            } else newList = list;

            return newList;
        } else {
            return null;
        }
    }
}