package me.quickscythe.bac.utils.holograms;

import me.quickscythe.bac.utils.misc.UID;
import org.bukkit.Location;
import org.bukkit.entity.Display;

import java.util.LinkedList;

public class Hologram {
    LinkedList<Display> lines = new LinkedList<>();
    Location loc;
    UID uid;

    protected Hologram(UID uid, Location loc) {
        this.loc = loc;
        this.uid = uid;
    }

    public void setLine(int line, Display content) {
        if (lines.size() > line) lines.set(line, content);
        else lines.add(line, content);
        update();
    }

    public UID getUID() {
        return uid;
    }

    public Display getLine(int i) {
        return lines.get(i);
    }

    public void update() {

        for (int i = 0; i != lines.size(); i++) {
            Display disp = lines.get(i);

            disp.teleport(loc.clone().add(0, -0.26 * i, 0));
        }
    }
}
