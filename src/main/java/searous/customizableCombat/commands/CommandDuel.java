package searous.customizableCombat.commands;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import searous.customizableCombat.main.CustomizableCombat;

import java.util.ArrayList;
import java.util.List;

public class CommandDuel implements TabExecutor {
    private CustomizableCombat plugin;
    
    // /duel <target> [bet] [<item>] [count]    | Request a duel with the target or accept one
    public static final String
        LABEL = "duel",
        BET = "bet"
    ;
    
    public CommandDuel(CustomizableCombat plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equals(LABEL) && sender.hasPermission(plugin.getStrings().PERM_DUEL)) {
            if(args.length >= 1) {
                // Challenge or accept target player
                //List<Player> players = new ArrayList<>(ImmutableList.copyOf(plugin.getServer().getOnlinePlayers()));
                Player target = plugin.getServer().getPlayer(args[0]);
                
                if(target != null) {
                    if(!sender.equals(target)) {
                        if(plugin.getDuelManager().isPlayerDueling(target)) {
                            sender.sendMessage("You accepted " + target.getName() + "'s challenge! ");
                            target.sendMessage(sender.getName() + "accepted your challenge!");
                        } else {
                            sender.sendMessage("You challenged " + target.getName() + " to a duel!");
                            target.sendMessage(sender.getName() + " challenged you to a duel! Type '/duel " + target.getName() + "' to accept!");
                            plugin.getDuelManager().challenge((Player)sender, target);
                        }
                        
                        return true;
                    } else {
                        sender.sendMessage("You can't challenge yourself to a duel, silly!");
                        return true;
                    }
                }
            } else {
                // Incorrect Usage
                sender.sendMessage("No");
                return false;
            }
        }
        return false;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
    
        List<Player> players = new ArrayList<>(ImmutableList.copyOf(plugin.getServer().getOnlinePlayers()));
        for(int i = 0; i < players.size(); i++) {
            list.add(players.get(i).getName());
        }
        
        return list;
    }
}
