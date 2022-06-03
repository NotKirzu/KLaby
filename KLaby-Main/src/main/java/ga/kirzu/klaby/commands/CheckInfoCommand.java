package ga.kirzu.klaby.commands;

import ga.kirzu.klaby.KLaby;
import ga.kirzu.klaby.PlayersCache;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckInfoCommand implements CommandExecutor {
    private final static PlayersCache PLAYERS_CACHE
            = KLaby.getPlayersCache();

    public boolean onCommand(CommandSender sender, Command cmd, final String label, final String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /" + label + " <Player>"));
        } else {
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null || !Bukkit.getOnlinePlayers().contains(player)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes(
                        '&',
                        "&cPlayer &e" + args[0] + " &cdoesn't exists or is offline."
                ));
            } else {
                if (!PLAYERS_CACHE.has(player.getUniqueId())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlayer &e" + player.getName() + " &cisn't using LabyMod."));
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d" + player.getName() + "&b's addons:&r"));
                    sender.sendMessage(" ");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes(
                            '&',
                            "&fAddons: &b" + String.join(", ", PLAYERS_CACHE.getAddons(player.getUniqueId()))
                    ));
                }
            }
        }
        return true;
    }
}
