package ga.kirzu.klaby.factory;

import ga.kirzu.klaby.nms.LabyModChannel;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;

public class LabyChannelProvider {

    private final static String SERVER_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);

    private static final LabyModChannel LABY_MOD_CHANNEL;

    static {
        try {
            LABY_MOD_CHANNEL = (LabyModChannel) Class.forName("ga.kirzu.v" + SERVER_VERSION + ".LabyChannel").getConstructor().newInstance();
        } catch (NoSuchMethodException | ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("Your server version isn't supported by KLaby.\nServer version '" + SERVER_VERSION + "'.\nAvailable versions: 1_12_R1 - 1_16_R3 - 1_17_R1 - 1_18_R1 - 1_18_R2\nIf you think this is wrong, you may contact the author (Kiirzu).");
        }
    }

    public static LabyModChannel getLabyModChannel() {
        return LABY_MOD_CHANNEL;
    }
}
