package me.quickscythe.bac.utils.holograms;

import me.quickscythe.bac.utils.misc.UID;
import org.bukkit.Location;

public class Hologram {

    Location loc;
    UID uid;
    boolean persistent = false;

    protected Hologram(UID uid, Location loc) {
        this.loc = loc;
        this.uid = uid;
    }

    public Location getLocation() {
        return loc.clone();
    }

    public UID getUID() {
        return uid;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }
    public void update() {

    }
    public void move(Location loc) {
        this.loc = loc;
    }


}
