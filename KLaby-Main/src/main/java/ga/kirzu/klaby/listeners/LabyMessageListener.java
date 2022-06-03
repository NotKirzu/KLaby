package ga.kirzu.klaby.listeners;

import ga.kirzu.klaby.events.LabyPlayerJoinEvent;
import ga.kirzu.klaby.factory.LabyChannelProvider;
import ga.kirzu.klaby.nms.LabyModChannel;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class LabyMessageListener implements PluginMessageListener {

    private final static LabyModChannel LABY_MOD_CHANNEL = LabyChannelProvider.getLabyModChannel();

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("labymod3:main")) {
            return;
        }

        ByteBuf buf = Unpooled.wrappedBuffer(message);
        String key = LABY_MOD_CHANNEL.readString(buf, Short.MAX_VALUE);

        if (key.equals("INFO")) {
            String data = LABY_MOD_CHANNEL.readString(buf, Short.MAX_VALUE);

            Bukkit.getPluginManager().callEvent(new LabyPlayerJoinEvent(player, data));
        }
    }
}
