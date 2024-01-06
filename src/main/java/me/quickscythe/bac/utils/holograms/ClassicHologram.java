package me.quickscythe.bac.utils.holograms;


import me.quickscythe.bac.utils.MessageUtils;
import me.quickscythe.bac.utils.misc.UID;
import me.quickscythe.bac.utils.placeholder.PlaceholderUtils;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.json2.JSONArray;
import org.json2.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ClassicHologram extends Hologram {
    Map<Integer, String> lines = new HashMap<>();
    Map<Integer, ArmorStand> stands = new HashMap<>();

    protected ClassicHologram(UID uid, Location loc) {
        super(uid, loc);
    }

    public void setLine(int line, String message) {
        ArmorStand stand;
        if (lines.containsKey(line)) stand = stands.get(line);
        else {
            stand = loc.getWorld().spawn(loc, ArmorStand.class);
            stand.setInvisible(true);
            stand.setGravity(false);
            stand.setInvulnerable(true);
            stand.setCustomNameVisible(true);
            stand.setSmall(true);
            stand.setCollidable(false);
        }
        lines.put(line, message);
        stand.setCustomName(MessageUtils.colorize(PlaceholderUtils.replace(null, message)));
        stands.put(line, stand);
        update();
    }

    public String getLine(int i) {
        return lines.get(i);
    }

    @Override
    public void update() {
        for (Map.Entry<Integer, String> e : lines.entrySet()) {
            ArmorStand stand = stands.get(e.getKey());
            stand.setCustomName(MessageUtils.colorize(PlaceholderUtils.replace(null, e.getValue())));
            stand.teleport(loc.clone().add(0, -0.26 * e.getKey(), 0));
        }
    }

    public Map<Integer, String> getLines() {
        return lines;
    }

    public void setLines(JSONArray lines) {
        for (int i = 0; i < lines.length(); i++) {
            JSONObject lineData = lines.getJSONObject(i);
            setLine(lineData.getInt("line"), lineData.getString("text"));
        }
    }


}
