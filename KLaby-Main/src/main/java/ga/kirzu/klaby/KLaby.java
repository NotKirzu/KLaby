package ga.kirzu.klaby;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.IEssentials;
import ga.kirzu.klaby.commands.CheckInfoCommand;
import ga.kirzu.klaby.commands.ListCommand;
import ga.kirzu.klaby.hooks.EconomyManager;
import ga.kirzu.klaby.hooks.PlaceholderAPIHook;
import ga.kirzu.klaby.listeners.LabyMessageListener;
import ga.kirzu.klaby.listeners.PlayerEvents;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class KLaby extends JavaPlugin {

    private final static Logger LOGGER
            = Logger.getLogger(KLaby.class.getName());
    
    private static EconomyManager economyManager;
    private static PlaceholderAPIHook papiHook;
    private static PlayersCache playersCache;
    private static IEssentials essentials;
    private static KLaby instance;

    @Override
    public void onEnable() {
        instance = this;
        registerConfig();

        essentials = getPlugin(Essentials.class);

        playersCache = new PlayersCache();
        papiHook = new PlaceholderAPIHook();
        economyManager = new EconomyManager();
        registerListeners();
        registerCommands();

        LOGGER.info("KLaby is enabled");
    }
    
    @Override
    public void onDisable() {
        LOGGER.info("KLaby is disabled.");
    }
    
    public static KLaby getInstance() {
        return instance;
    }

    public static EconomyManager getEconomy() {
        return economyManager;
    }

    public static IEssentials getEssentials() {
        return essentials;
    }

    public static PlayersCache getPlayersCache() {
        return playersCache;
    }

    public static PlaceholderAPIHook getPAPI() {
        return papiHook;
    }

    private void registerCommands() {
        getCommand("klist").setExecutor(new ListCommand());
        getCommand("kcheckinfo").setExecutor(new CheckInfoCommand());
    }

    private void registerListeners() {
        Server server = getServer();
        server.getMessenger().registerIncomingPluginChannel(this, "labymod3:main", new LabyMessageListener());

        PluginManager pm = server.getPluginManager();
        pm.registerEvents(new PlayerEvents(), this);
    }

    private void registerConfig() {
        File file = new File(this.getDataFolder(), "config.yml");
        if (!file.exists()) {
            this.saveDefaultConfig();
        } else {
            this.saveConfig();
        }
    }
}
