package searous.customizableCombat.duel;

import org.bukkit.entity.Player;
import searous.customizableCombat.main.CustomizableCombat;

import java.util.ArrayList;
import java.util.List;

public class DuelManager {
    private final CustomizableCombat plugin;
    private final List<Duel> activeDuels = new ArrayList<>();
    
    public DuelManager(CustomizableCombat plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Gets an ongoing duel between two players
     * @param player1 challenger
     * @param player2 challengee
     * @return The current duel between player1 and player2, or null if no duel exists
     */
    public Duel getDuel(Player player1, Player player2) {
        for(Duel duel : activeDuels) {
            if(duel.getChallenger().equals(player1) && duel.getChallenged().equals(player2)) {
                return duel;
            } else if(duel.getChallenger().equals(player2) && duel.getChallenged().equals(player1)) {
                return duel;
            }
        }
        return null;
    }
    
    /**
     * Gets all duels the specified player is registered in, including duels that have not started yet.
     * @param player The player to look for
     * @return An ArrayList containing every duel the specified player is a part of
     */
    public ArrayList<Duel> getDuels(Player player) {
        ArrayList<Duel> duels = new ArrayList<>();
        for(Duel duel : activeDuels) {
            try {
                if(duel.getChallenger().getUniqueId().equals((player.getUniqueId()))
                    || duel.getChallenged().getUniqueId().equals((player.getUniqueId()))) {
                    duels.add(duel);
                }
            } catch(Exception ignored) { }
        }
        return duels;
    }
    
    public Duel challenge(Player challenger, Player challenged) {
        // Check if this is a duel being accepted
        for(Duel duel : activeDuels) {
            if(duel.getChallenger().equals(challenged)) {
                acceptChallenge(duel);
                return duel;
            }
        }
        
        Duel duel = new Duel(plugin, challenger, challenged);
        activeDuels.add(duel);
        return duel;
    }
    
    private void acceptChallenge(Duel duel) {
        duel.startDuel();
    }
    
    /**
     * Check if the specified player is dueling
     * @param player
     * @return
     */
    public boolean isPlayerDueling(Player player) {
        for(Duel duel : activeDuels) {
            if(duel.isDuelStarted()) {
                if(duel.getChallenged().equals(player) || duel.getChallenger().equals(player)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void cancelDuel(Duel duel) {
        activeDuels.remove(duel);
    }
}
