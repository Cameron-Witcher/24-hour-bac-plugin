package me.quickscythe.lt.utils.players;

import me.quickscythe.lt.utils.Utils;
import org.bukkit.Bukkit;

import java.util.Date;
import java.util.UUID;

public class EventPlayer {
    long playtime;
    long logintime = 0;
    UUID uid;
    String username;

    public EventPlayer(String name, UUID uid) {
        this(name, uid, 0);
    }

    public EventPlayer(String name, UUID uid, long playtime) {
        this.username = name;
        this.uid = uid;
        this.playtime = playtime;
    }

    public String getUsername() {
        return username;
    }

    public long getCurrentTime() {
        return getPlaytime() + getLoginTime();
    }

    public long getLoginTime() {
        return logintime != 0 ? (new Date().getTime() - logintime) : 0;
    }

    public long getPlaytime() {
        return playtime;
    }

    public void setPlaytime(long l) {
        playtime = l;
        if (logintime != 0) {
            logintime = new Date().getTime();
        }
    }

    public void login() {
        if (!Utils.getPlayerManager().isPaused())
            logintime = new Date().getTime();
    }

    public void logout() {
        playtime = getCurrentTime();
        logintime = 0;
    }

    public void reset() {
        logintime = 0;
        playtime = 0;
        if (Bukkit.getPlayer(uid) != null) {
            logintime = new Date().getTime();
        }

    }

    public void addPlaytime(long l) {
        setPlaytime(getCurrentTime() + l);
//        playtime = playtime + l;

    }

    public UUID getUUID() {
        return uid;
    }
}
