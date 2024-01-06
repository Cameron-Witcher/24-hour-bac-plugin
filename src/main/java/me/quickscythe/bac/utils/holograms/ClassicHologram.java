package me.quickscythe.bac.utils.holograms;


import me.quickscythe.bac.utils.MessageUtils;
import me.quickscythe.bac.utils.misc.UID;
import me.quickscythe.bac.utils.placeholder.PlaceholderUtils;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.HashMap;
import java.util.Map;

public class ClassicHologram {
    Map<Integer, String> lines = new HashMap<>();
    Map<Integer, ArmorStand> stands = new HashMap<>();
//    LinkedList<ArmorStand> lines = new LinkedList<>();

    Location loc;
    UID uid;

    protected ClassicHologram(UID uid, Location loc) {
        this.loc = loc;
        this.uid = uid;
    }

    public void setLine(int line, String message) {
        ArmorStand stand;
        if(lines.containsKey(line)) stand = stands.get(line);
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
        stands.put(line,stand);
        update();
    }

    public UID getUID() {
        return uid;
    }

    public String getLine(int i) {
        return lines.get(i);
    }

    public void update() {
        for(Map.Entry<Integer, String> e : lines.entrySet()){
            ArmorStand stand = stands.get(e.getKey());
            stand.setCustomName(MessageUtils.colorize(PlaceholderUtils.replace(null, e.getValue())));
            stand.teleport(loc.clone().add(0, -0.26 * e.getKey(), 0));
        }
    }
}
