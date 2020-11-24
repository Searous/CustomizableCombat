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
