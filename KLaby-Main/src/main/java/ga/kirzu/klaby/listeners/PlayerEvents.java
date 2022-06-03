package ga.kirzu.klaby.listeners;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ga.kirzu.klaby.KLaby;
import ga.kirzu.klaby.PlayersCache;
import ga.kirzu.klaby.events.LabyPlayerJoinEvent;
import ga.kirzu.klaby.factory.LabyChannelProvider;
import ga.kirzu.klaby.hooks.EconomyManager;
import ga.kirzu.klaby.hooks.PlaceholderAPIHook;
import ga.kirzu.klaby.nms.LabyModChannel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener {

    private final static KLaby INSTANCE
            = KLaby.getInstance();
    private final static PlayersCache PLAYERS_CACHE
            = KLaby.getPlayersCache();
    private final static PlaceholderAPIHook PAPI_HOOK
            = KLaby.getPAPI();
    private final static LabyModChannel LABY_MOD_CHANNEL
            = LabyChannelProvider.getLabyModChannel();
    private final static EconomyManager ECONOMY
            = KLaby.getEconomy();

    @EventHandler
    public void onLabyPlayerJoin(LabyPlayerJoinEvent event) {
        Player player = event.getPlayer();

        PLAYERS_CACHE.set(player.getUniqueId(), event.getPlayerData());

        ECONOMY.addPlayerToQueue(player);
        sendDiscordServer(player);
        sendFriendServer(player);
        sendNotification(player);
        sendSubtitle(player);
        sendButtons(player);
        sendBanner(player);
    }

    @EventHandler
    public void onLabyPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (PLAYERS_CACHE.has(player.getUniqueId())) {
            PLAYERS_CACHE.remove(player.getUniqueId());
            ECONOMY.removePlayerFromQueue(player);
        }
    }

    private void sendSubtitle(Player player) {
        FileConfiguration config = INSTANCE.getConfig();

        if (config.getBoolean("subtitle.enabled")) {
            JsonArray array = new JsonArray();

            ConfigurationSection subtitles = config.getConfigurationSection("subtitle.subtitles");
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                JsonObject subtitleJson = new JsonObject();

                String subtitle = subtitles.getKeys(false).stream()
                        .filter(st -> onlinePlayer.hasPermission(subtitles.getString(st + ".permission")))
                        .findFirst()
                        .orElse(null);

                if (subtitle == null) {
                    continue;
                }

                ConfigurationSection title = config.getConfigurationSection("subtitle.subtitles." + subtitle);
                subtitleJson.addProperty("uuid", onlinePlayer.toString());
                subtitleJson.addProperty("size", title.getDouble("size"));
                subtitleJson.addProperty("value", title.getString("text"));

                array.add(subtitleJson);
            }

            LABY_MOD_CHANNEL.sendLabyMessage(player, "account_subtitle", array);
        }
    }

    private void sendDiscordServer(Player player) {
        FileConfiguration config = INSTANCE.getConfig();

        if (config.getBoolean("server-name.discord.enabled")) {
            String server = config.getString("server-name.discord.name");

            JsonObject object = new JsonObject();
            object.addProperty("hasGame", true);
            object.addProperty("game_mode", server);
            object.addProperty("game_startTime", System.currentTimeMillis());
            object.addProperty("game_endTime", 0);

            LABY_MOD_CHANNEL.sendLabyMessage(player, "discord_rpc", object);
        }
    }

    private void sendFriendServer(final Player player) {
        FileConfiguration config = INSTANCE.getConfig();

        if (config.getBoolean("server-name.friends.enabled")) {
            String server = config.getString("server-name.friends.name");
            server = ChatColor.translateAlternateColorCodes('&', server);
            JsonObject object = new JsonObject();
            object.addProperty("show_gamemode", true);
            object.addProperty("gamemode_name", server);

            LABY_MOD_CHANNEL.sendLabyMessage(player, "server_gamemode", object);
        }
    }

    private void sendNotification(Player player) {
        FileConfiguration config = INSTANCE.getConfig();

        if (config.getBoolean("join-message.enabled")) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.hasPermission(config.getString("join-message.permission"))) {
                    continue;
                }

                String message = config.getString("join-message.message");
                message = PAPI_HOOK.format(player, message);
                message = ChatColor.translateAlternateColorCodes('&', message);

                onlinePlayer.sendMessage(message);
            }
        }
    }

    private void sendBanner(Player player) {
        FileConfiguration config = INSTANCE.getConfig();

        if (config.getBoolean("banner.enabled")) {
            JsonObject object = new JsonObject();
            object.addProperty("url", config.getString("banner.link"));
            LABY_MOD_CHANNEL.sendLabyMessage(player, "server_banner", object);
        }
    }

    private void sendButtons(Player player) {
        FileConfiguration config = INSTANCE.getConfig();

        JsonArray buttons = new JsonArray();
        ConfigurationSection section = config.getConfigurationSection("menu");
        for (String options : section.getKeys(false)) {
            ConfigurationSection option = section.getConfigurationSection(options);

            if (option.contains("permission") && !player.hasPermission(option.getString("permission"))) {
                continue;
            }

            JsonObject button = new JsonObject();
            button.addProperty("displayName", option.getString("name"));
            button.addProperty("type", option.getString("type"));
            button.addProperty("value", option.getString("value"));
            buttons.add(button);
        }

        if (buttons.size() > 0) {
            LABY_MOD_CHANNEL.sendLabyMessage(player, "user_menu_actions", buttons);
        }
    }

}
