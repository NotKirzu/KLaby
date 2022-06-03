package ga.kirzu.klaby.commands;

import ga.kirzu.klaby.KLaby;
import ga.kirzu.klaby.PlayersCache;
import ga.kirzu.klaby.hooks.PlaceholderAPIHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ListCommand implements CommandExecutor {
    private final static PlayersCache PLAYERS_CACHE
            = KLaby.getPlayersCache();
    private final static PlaceholderAPIHook PAPI_HOOK
            = KLaby.getPAPI();

    public boolean onCommand(CommandSender sender, Command cmd, final String label, final String[] args) {
        int size = PLAYERS_CACHE.size();
        if (size > 0) {
            String onlinePlayers = String.format(
                    "&fThere are &b%s &fplayers%s using &b&lLabyMod&f.&r",
                    size,
                    (size == 1) ? "" : "s"
            );
            onlinePlayers = ChatColor.translateAlternateColorCodes('&', onlinePlayers);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&l&m--------------------------------------&r"));
            sender.sendMessage(onlinePlayers);
            sender.sendMessage(" ");
            sender.sendMessage(getPlayers());
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&l&m--------------------------------------"));
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNobody is using &b&lLabyMod &cright now."));
        }
        return true;
    }

    private String getPlayers() {
        List<String> names = new ArrayList<>();
        for (UUID uuid : PLAYERS_CACHE.getAsList()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                PLAYERS_CACHE.remove(uuid);
                continue;
            }

            String name = PAPI_HOOK.isEnabled() ? PAPI_HOOK.format(player, "%vault_prefix%%player_name%") : player.getName();
            names.add(ChatColor.GRAY + name);
        }

        return ChatColor.GRAY + String.join(ChatColor.WHITE + ", ", names) + ChatColor.WHITE;
    }
}
