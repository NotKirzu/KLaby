package ga.kirzu.klaby.hooks;

import com.google.gson.JsonObject;
import ga.kirzu.klaby.KLaby;
import ga.kirzu.klaby.PlayersCache;
import ga.kirzu.klaby.factory.LabyChannelProvider;
import ga.kirzu.klaby.nms.LabyModChannel;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.logging.Logger;

public class EconomyManager implements Listener {

    private final static Logger LOGGER
            = Logger.getLogger(PlaceholderAPIHook.class.getName());
    private final static LabyModChannel LABY_MOD_CHANNEL
            = LabyChannelProvider.getLabyModChannel();
    private final static PlayersCache PLAYERS_CACHE
            = KLaby.getPlayersCache();
    private final static KLaby INSTANCE
            = KLaby.getInstance();

    private final Map<UUID, Integer> playerQueue = new HashMap<>();

    private boolean enabled = false;
    private Economy economy = null;

    public EconomyManager() {
        if (setupEconomy()) {
            enabled = true;

            LOGGER.info("Vault found. Enabling hook.");
        }
    }

    private boolean setupEconomy() {
        if (INSTANCE.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = INSTANCE.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        economy = rsp.getProvider();

        return economy != null;
    }

    public void addPlayerToQueue(Player player) {
        if (!enabled) {
            return;
        }

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!playerQueue.containsKey(player.getUniqueId())) {
                    this.cancel();
                    return;
                }
                updatePlayerEconomy(player);
            }
        }.runTaskTimer(INSTANCE, 0L, 200L);

        playerQueue.put(player.getUniqueId(), task.getTaskId());
    }

    public void removePlayerFromQueue(Player player) {
        if (!enabled || !playerQueue.containsKey(player.getUniqueId())) {
            return;
        }

        int task = playerQueue.get(player.getUniqueId());
        Bukkit.getScheduler().cancelTask(task);
        playerQueue.remove(player.getUniqueId());
    }

    public void updatePlayerEconomy(Player player) {
        FileConfiguration config = INSTANCE.getConfig();

        if (enabled && config.getBoolean("economy.enabled") && PLAYERS_CACHE.has(player.getUniqueId())) {
            int money = (int) (economy.getBalance(player) * 1000);

            JsonObject economyObject = new JsonObject();
            JsonObject cashObject = new JsonObject();

            cashObject.addProperty("visible", true);
            cashObject.addProperty("balance", money);

            ConfigurationSection icon = config.getConfigurationSection("economy.icon");
            if (icon.getBoolean("enabled")) {
                cashObject.addProperty("icon", icon.getString("url"));
            }

            JsonObject decimalObject = new JsonObject();
            decimalObject.addProperty("format", "##.##");
            decimalObject.addProperty("divisor", 1000);
            cashObject.add("decimal", decimalObject);

            economyObject.add("cash", cashObject);

            LABY_MOD_CHANNEL.sendLabyMessage(player, "economy", economyObject);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }
}