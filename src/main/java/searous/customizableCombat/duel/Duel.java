package searous.customizableCombat.duel;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import searous.customizableCombat.main.CustomizableCombat;

import java.util.List;

public class Duel {
    private final Player challenger, challenged;
    private final long startTime = 0;
    private Bet challengerBet, challengedBet;
    private boolean duelStarted = false;
    
    public Duel(CustomizableCombat plugin, Player challenger, Player challenged) {
        this.challenger = challenger;
        this.challenged = challenged;
    }
    
    public Player getChallenger() {
        return challenger;
    }
    public Player getChallenged() {
        return challenged;
    }
    
    public void setChallengerBet(Bet challengerBet) {
        this.challengerBet = challengerBet;
    }
    public void setChallengedBet(Bet challengedBet) {
        this.challengedBet = challengedBet;
    }
    public Bet getChallengerBet() {
        return challengerBet;
    }
    public Bet getChallengedBet() {
        return challengedBet;
    }
    
    public boolean isDuelStarted() {
        return duelStarted;
    }
    public void startDuel() {
        duelStarted = true;
    }
}
