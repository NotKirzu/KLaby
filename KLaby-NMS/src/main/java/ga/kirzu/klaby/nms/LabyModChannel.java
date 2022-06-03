package ga.kirzu.klaby.nms;

import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import org.bukkit.entity.Player;

public interface LabyModChannel {

    void sendLabyMessage(Player player, String string, JsonElement key);

    String readString(ByteBuf buf, int maxLength);

}
