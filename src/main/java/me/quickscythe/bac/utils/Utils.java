package me.quickscythe.bac.utils;

import me.quickscythe.bac.BacPlugin;
import me.quickscythe.bac.utils.holograms.HologramManager;
import me.quickscythe.bac.utils.players.PlayerManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Utils {

    private static BacPlugin plugin = null;
    private static final PlayerManager playerManager = new PlayerManager();
    private static final HologramManager hologramManager = new HologramManager();
    private static final List<Runnable> palpitations = new ArrayList<>();
    private static long duration = 24;


//    private static final Map<UUID, Long> playerStorage = new HashMap<>();
//    private static final Map<UUID, Long> loginTimes = new HashMap<>();
//    private static final Map<String, UUID> uuids = new HashMap<>();

    public static void init(BacPlugin plugin) {
        Utils.plugin = plugin;
        registerConfig();
        playerManager.init();
        hologramManager.init();
        Bukkit.getScheduler().runTaskLater(plugin, new Heartbeat(), 1);
        registerPalpitations();
    }

    private static void registerConfig() {
        if (!plugin.getConfig().isSet("max_time")) {
            plugin.getConfig().set("max_time", duration);
        } else duration = convertTime(plugin.getConfig().getString("max_time"));
        plugin.saveConfig();
    }

    private static void registerPalpitations() {
        addPalpitation(() -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getGameMode().equals(GameMode.SURVIVAL)) {
                    long remaining = duration - playerManager.getPlayer(player).getCurrentTime();
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(MessageUtils.colorize("&7" + MessageUtils.formatTimeRaw(remaining))));
                    if (remaining <= 0) player.setGameMode(GameMode.SPECTATOR);
                }
            }
        });
    }

    public static HologramManager getHologramManager() {
        return hologramManager;
    }

    public static long convertTime(String maxTime) {
        TimeUnit unit = TimeUnit.HOURS;
        if (maxTime.endsWith("h") || maxTime.endsWith("hours") || maxTime.endsWith("hour")) {
            maxTime = maxTime.replace("hours", "");
            maxTime = maxTime.replace("hour", "");
            maxTime = maxTime.replace("h", "");
        }
        if (maxTime.endsWith("m") || maxTime.endsWith("minutes") || maxTime.endsWith("minute")) {
            unit = TimeUnit.MINUTES;
            maxTime = maxTime.replace("minutes", "");
            maxTime = maxTime.replace("minute", "");
            maxTime = maxTime.replace("m", "");
        }
        if (maxTime.endsWith("s") || maxTime.endsWith("seconds") || maxTime.endsWith("second")) {
            unit = TimeUnit.SECONDS;
            maxTime = maxTime.replace("seconds", "");
            maxTime = maxTime.replace("second", "");
            maxTime = maxTime.replace("s", "");
        }
        return TimeUnit.MILLISECONDS.convert(Long.parseLong(maxTime), unit);
    }

    public static PlayerManager getPlayerManager() {
        return playerManager;
    }


    public static void end() {
        playerManager.end();

    }

    public static BacPlugin getPlugin() {
        return plugin;
    }

    public static String encryptLocation(Location loc) {
        String r = loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getPitch() + ":" + loc.getYaw();
        r = r.replaceAll("\\.", ",");
        r = "location:" + r;
        return r;
    }

    public static Location decryptLocation(String s) {
        if (s.startsWith("location:")) s = s.replaceAll("location:", "");
        if (s.contains(",")) s = s.replaceAll(",", ".");
        String[] args = s.split(":");
        Location r = new Location(Bukkit.getWorld(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
        if (args.length >= 5) {
            r.setPitch(Float.parseFloat(args[4]));
            r.setYaw(Float.parseFloat(args[5]));
        }
        return r;
    }

    public static void addPalpitation(Runnable runnable) {
        palpitations.add(runnable);
    }


    public static List<Runnable> getPalpitations() {
        return palpitations;
    }

    private static class Heartbeat implements Runnable {
        @Override
        public void run() {

            for (Runnable palp : Utils.getPalpitations()) {
                Bukkit.getScheduler().runTaskLater(plugin, palp, 0);
            }
            Bukkit.getScheduler().runTaskLater(plugin, this, 1);


        }
    }
}
