package ga.kirzu.klaby.hooks;

import ga.kirzu.klaby.KLaby;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class PlaceholderAPIHook {
    private final static Logger LOGGER
            = Logger.getLogger(PlaceholderAPIHook.class.getName());

    private boolean isEnabled = false;

    public PlaceholderAPIHook() {
        KLaby instance = KLaby.getInstance();

        if (instance.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            isEnabled = true;
            LOGGER.info("PlaceholderAPI found. Enabling hook.");
        }
    }

    public String format(Player player, String message) {
        if (isEnabled) {
            return PlaceholderAPI.setPlaceholders(player, message);
        }

        return message.replace("%player_name%", player.getName());
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }
}
