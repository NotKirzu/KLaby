package ga.kirzu.klaby.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LabyPlayerJoinEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final String playerData;

    public LabyPlayerJoinEvent(Player player, String playerData) {
        this.player = player;
        this.playerData = playerData;
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getPlayerData() {
        return this.playerData;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
