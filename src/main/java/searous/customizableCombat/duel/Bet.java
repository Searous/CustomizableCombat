package searous.customizableCombat.duel;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Bet {
    private final Player bettingPlayer;
    private List<ItemStack> itemBet;
    private double currancyBet;
    private float xpBet;
    
    public Bet(Player bettingPlayer) {
        this.bettingPlayer = bettingPlayer;
    }
    
    public Bet(Player bettingPlayer, List<ItemStack> itemBet) {
        this(bettingPlayer);
        this.itemBet = itemBet;
    }
    
    public Bet(Player bettingPlayer, List<ItemStack> itemBet, double currancyBet, float xpBet) {
        this(bettingPlayer, itemBet);
        this.currancyBet = currancyBet;
        this.xpBet = xpBet;
    }
}
