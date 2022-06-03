package ga.kirzu.klaby;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayersCache {
    private final HashMap<UUID, String> PLAYERS
            = new HashMap<>();

    public void set(UUID uuid, String data) {
        if (has(uuid)) {
            remove(uuid);
        }

        PLAYERS.put(uuid, data);
    }

    public void remove(UUID uuid) {
        PLAYERS.remove(uuid);
    }

    public boolean has(UUID uuid) {
        return PLAYERS.containsKey(uuid);
    }

    public List<String> getAddons(UUID uuid) {
        ArrayList<String> addons = new ArrayList<>();

        if (!has(uuid)) {
            return addons;
        }

        JSONParser parser = new JSONParser();

        try {
            JSONObject obj = (JSONObject) parser.parse(PLAYERS.get(uuid));
            for (Object data : (JSONArray) obj.get("addons")) {
                String addon = (String) ((JSONObject) data).get("name");
                addons.add(addon);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return addons;
    }

    public int size() {
        return PLAYERS.size();
    }

    public List<UUID> getAsList() {
        return new ArrayList<>(PLAYERS.keySet());
    }
}
