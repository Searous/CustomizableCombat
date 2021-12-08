package searous.customizableCombat.messages;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageContext {
    private final String messagePath;
    private Player player;
    private Player target;
    private CommandSender sender;
    private String special;
    private boolean value;
    
    private MessageContext(String messagePath) {
        this.messagePath = messagePath;
    }
    public MessageContext(String messagePath, CommandSender sender) {
        this(messagePath);
        this.sender = sender;
    }
    public MessageContext(String messagePath, Player player) {
        this(messagePath, (CommandSender) player);
        this.player = player;
        target = player;
    }
    
    // Setters
    public MessageContext setTarget(Player target) {
        this.target = target;
        return this;
    }
    public MessageContext setValue(boolean value) {
        this.value = value;
        return this;
    }
    public MessageContext setSpecial(String special) {
        this.special = special;
        return this;
    }
    
    // Getters
    public String getMessagePath() {
        return messagePath;
    }
    public Player getPlayer() {
        return player;
    }
    public Player getTarget() {
        return target;
    }
    public CommandSender getSender() {
        return sender;
    }
    public String getSpecial() {
        return special;
    }
    public boolean isValue() {
        return value;
    }
}
