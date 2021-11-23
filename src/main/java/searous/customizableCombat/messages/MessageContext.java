package searous.customizableCombat.messages;

import org.bukkit.entity.Player;

public class MessageContext {
    public final String messagePath;
    public Player player, target;
    public boolean value;
    
    public MessageContext(String messagePath, Player player) {
        this.messagePath = messagePath;
        this.player = player;
    }
}
