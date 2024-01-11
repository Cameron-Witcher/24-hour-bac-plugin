package me.quickscythe.lt.utils;

import me.quickscythe.lt.LimitedTime;
import me.quickscythe.lt.utils.holograms.ClassicHologram;
import me.quickscythe.lt.utils.holograms.HologramManager;
import me.quickscythe.lt.utils.placeholder.PlaceholderUtils;
import me.quickscythe.lt.utils.placeholder.Symbols;
import me.quickscythe.lt.utils.players.EventPlayer;
import me.quickscythe.lt.utils.players.PlayerManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Utils {

    private static final PlayerManager playerManager = new PlayerManager();
    private static final HologramManager hologramManager = new HologramManager();
    private static final List<Runnable> palpitations = new ArrayList<>();
    private static final Map<Integer, UUID> rankings = new HashMap<>();
    private static final Map<UUID, Long> cached_times = new HashMap<>();
    private static LimitedTime plugin = null;
    private static long duration = 24;


//    private static final Map<UUID, Long> playerStorage = new HashMap<>();
//    private static final Map<UUID, Long> loginTimes = new HashMap<>();
//    private static final Map<String, UUID> uuids = new HashMap<>();

    public static void init(LimitedTime plugin) {
        Utils.plugin = plugin;
        registerConfig();
        PlaceholderUtils.registerPlaceholders();
        playerManager.init();
        hologramManager.init();
        Bukkit.getScheduler().runTaskLater(plugin, new Heartbeat(), 1);

        registerPalpitations();

    }

    private static void registerConfig() {
        if (!plugin.getConfig().isSet("max_time")) {
            plugin.getConfig().set("max_time", "24h");
        } else duration = convertTime(plugin.getConfig().getString("max_time"));
        plugin.saveConfig();
    }

    private static void registerPalpitations() {
        addPalpitation(() -> {
            for (ClassicHologram holo : getHologramManager().getClassicHolograms()) {
                holo.update();
            }
            for(EventPlayer player : playerManager.getPlayerMap().values()){
                if(!player.hasStarted()){
                    if(new Date().getTime() - player.getOriginalJoin() >= TimeUnit.MILLISECONDS.convert(10, TimeUnit.MINUTES))
                        player.setStarted(true);
                }
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getGameMode().equals(GameMode.SURVIVAL) && !player.hasPermission("lt.admin.not_playing")) {
                    long played = playerManager.getPlayer(player).getCurrentTime();
                    long remaining = duration - played;
                    cached_times.put(player.getUniqueId(), played);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(MessageUtils.colorize("&a" + Symbols.HOURGLASS_1 + " &7" + MessageUtils.formatTimeRaw(remaining))));
                    if (remaining <= 0) getPlayerManager().getPlayer(player).end();
                }
            }
        });
        addPalpitation(new Runnable() {
            long last_check = 0;
            long now = new Date().getTime();

            @Override
            public void run() {
                now = new Date().getTime();
                if (now - last_check >= TimeUnit.MILLISECONDS.convert(30, TimeUnit.SECONDS)) {
                    last_check = now;
                    sortRankings();
                }
            }
        });
    }


    private static LinkedHashMap<UUID, Long> sortRankings() {
        List<Map.Entry<UUID, Long>> list = new LinkedList<Map.Entry<UUID, Long>>(cached_times.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<UUID, Long>>() {

            @Override
            public int compare(Map.Entry<UUID, Long> o1, Map.Entry<UUID, Long> o2) {
                // TODO Auto-generated method stub
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        LinkedHashMap<UUID, Long> temp = new LinkedHashMap<>();
        int i = 1;
        for (Map.Entry<UUID, Long> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
            rankings.put(i, aa.getKey());
            i = i + 1;
        }

        return temp;

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
        hologramManager.end();
    }

    public static LimitedTime getPlugin() {
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

    public static void cacheTime(EventPlayer player) {
        cached_times.put(player.getUUID(), player.getCurrentTime());
    }

    public static UUID getRankings(int rank) {
        return rankings.get(rank);
    }

    public static long getCachedTime(UUID uid) {
        return cached_times.get(uid);
    }

    public static boolean downloadFile(String url, String filename, String... auth) {

        boolean success = true;
        InputStream in = null;
        FileOutputStream out = null;

        try {

            URL myUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
            conn.setDoOutput(true);
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(30000);
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestMethod("GET");

            if (auth != null && auth.length >= 2) {
                String userCredentials = auth[0].trim() + ":" + auth[1].trim();
                String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
                conn.setRequestProperty("Authorization", basicAuth);
            }
            in = conn.getInputStream();
            out = new FileOutputStream(filename);
            int c;
            byte[] b = new byte[1024];
            while ((c = in.read(b)) != -1) out.write(b, 0, c);

        } catch (Exception ex) {
            ex.printStackTrace();
            success = false;
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (out != null) try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return success;
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
