package me.quickscythe.lt.utils.players;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;

public class EventPlayer {
    long playtime;
    long logintime = 0;
    UUID uid;
    String username;
    boolean finished = false;
    boolean started = false;
    long original_join = new Date().getTime();

    public EventPlayer(String name, UUID uid) {
        this(name, uid, 0);
    }

    public EventPlayer(String name, UUID uid, long playtime) {
        this.username = name;
        this.uid = uid;
        this.playtime = playtime;

//        Bukkit.getScheduler().runTaskLater(Utils.getPlugin(),()->{if(!hasStarted())setStarted(true);},5*60*20);
    }

    public long getOriginalJoin() {
        return original_join;
    }

    public void setOriginalJoin(long original_join) {
        this.original_join = original_join;
    }

    public boolean hasStarted() {
        return started;
    }

    public void setStarted(boolean b) {
        started = b;
        if (b && Bukkit.getPlayer(uid) != null && logintime == 0) login();
    }

    public String getUsername() {
        return username;
    }

    public long getCurrentTime() {
        return getPlaytime() + getLoginTime();
    }

    public long getLoginTime() {
        return started ? logintime != 0 ? (new Date().getTime() - logintime) : 0 : 0;
    }

    public long getPlaytime() {
        return playtime;
    }

    public void setPlaytime(long l) {
        playtime = l;
        if (logintime != 0 && started) {
            login();
        }
    }

    public void login() {
        if (started) logintime = new Date().getTime();
    }

    public void logout() {
        playtime = getCurrentTime();
        logintime = 0;
    }

    public void reset() {
        logintime = 0;
        playtime = 0;
        finished = false;
        started = true;
        if (Bukkit.getPlayer(uid) != null) {
            login();
            Bukkit.getPlayer(uid).setGameMode(GameMode.SURVIVAL);
        }

    }

    public void addPlaytime(long l) {
        setPlaytime(getCurrentTime() + l);
//        playtime = playtime + l;

    }

    public UUID getUUID() {
        return uid;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean b) {
        finished = b;
    }

    public void end() {
        if (!finished) {
            finished = true;
            if (Bukkit.getPlayer(uid) != null) {
                Player player = Bukkit.getPlayer(uid);
                player.getWorld().strikeLightningEffect(player.getLocation());
                player.setGameMode(GameMode.SPECTATOR);
            }
        }
    }
}
