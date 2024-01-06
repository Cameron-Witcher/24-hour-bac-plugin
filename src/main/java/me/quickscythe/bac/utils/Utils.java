package me.quickscythe.bac.utils;

import me.quickscythe.bac.BacPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Utils {

    private static BacPlugin plugin = null;
    private static FileConfiguration playerConfig = null;
    private static File playerFile = null;
    private static final Map<UUID, Long> playerStorage = new HashMap<>();
    private static final Map<UUID, Long> loginTimes = new HashMap<>();
    private static final Map<String, UUID> uuids = new HashMap<>();

    public static void init(BacPlugin plugin){
        Utils.plugin = plugin;
        if(!plugin.getConfig().isSet("max_time")){
            plugin.getConfig().set("max_time", 24);
        }
        plugin.saveConfig();
        playerFile = new File(plugin.getDataFolder() + "/players.yml");
        if(!playerFile.exists()) {
            try {
                playerFile.createNewFile();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        playerConfig = YamlConfiguration.loadConfiguration(playerFile);

        Bukkit.getScheduler().runTaskLater(plugin, new Heartbeat(), 1);
    }

    public static UUID getUUID(String playerName){
        if(uuids.containsKey(playerName))return uuids.get(playerName);
        for(String k : playerConfig.getConfigurationSection("players").getKeys(false)){
            if(playerConfig.getString("players." + k + ".last_username").equalsIgnoreCase(playerName)){
                uuids.put(playerName, UUID.fromString(k));
                return getUUID(playerName);
            }

        }
        return null;
    }
    public static void saveUUID(Player player){
        if(uuids.containsValue(player.getUniqueId())){
            String old_name = "";
            for(Map.Entry<String, UUID> e : uuids.entrySet()){
                if(e.getValue().equals(player.getUniqueId())){
                    old_name = e.getKey();
                    break;
                }
            }
            uuids.remove(old_name);
        }
        uuids.put(player.getName(), player.getUniqueId());
    }

    public static long getPlayTime(UUID uid){
        if(playerStorage.containsKey(uid))
            return playerStorage.get(uid);
        if(playerConfig.isSet("players." + uid.toString() + ".time")){
            playerStorage.put(uid, playerConfig.getLong("players." + uid.toString() + ".time"));
            return getPlayTime(uid);
        }
        playerStorage.put(uid, 0L);
        return getPlayTime(uid);
    }

    public static void login(UUID uid){
        loginTimes.put(uid, new Date().getTime());
    }

    public static void logout(UUID uid){
        playerStorage.put(uid, getPlayTime(uid) + getLoggedInTime(uid));
        loginTimes.remove(uid);
    }

    public static long getLoggedInTime(UUID uid){
        return loginTimes.containsKey(uid) ? new Date().getTime() - loginTimes.get(uid) : 0L;
    }

    public static long getCurrentPlaytime(UUID uid){
        return getPlayTime(uid) + getLoggedInTime(uid);
    }

    public static void end(){
        for(Map.Entry<UUID, Long> e : playerStorage.entrySet()){
            playerConfig.set("players." + e.getKey().toString() + ".time", e.getValue());
        }
        for(Map.Entry<String, UUID> e : uuids.entrySet()){
            playerConfig.set("players." + e.getValue().toString() + ".last_username", e.getKey());
        }
       savePlayerConfig();
    }

    public static BacPlugin getPlugin() {
        return plugin;
    }

    public static void resetPlaytime(UUID uid) {
        playerStorage.put(uid,0L);
        playerConfig.set("players." + uid.toString() + ".time", 0L);
        if(Bukkit.getPlayer(uid)!= null){
            Player player = Bukkit.getPlayer(uid);
            if(loginTimes.containsKey(uid))
                loginTimes.put(uid, new Date().getTime());
            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            player.setGameMode(GameMode.SURVIVAL);
            player.setHealth(player.getHealthScale());
        }
        savePlayerConfig();
    }

    private static void savePlayerConfig() {
        try {
            playerConfig.save(playerFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class Heartbeat implements Runnable {
        @Override
        public void run() {

            for(Player player : Bukkit.getOnlinePlayers()){
                if(player.getGameMode().equals(GameMode.SURVIVAL)){
                    long remaining = TimeUnit.MILLISECONDS.convert(plugin.getConfig().getLong("max_time"),TimeUnit.HOURS) - getCurrentPlaytime(player.getUniqueId());
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(MessageUtils.colorize("&7" + MessageUtils.formatTimeRaw(remaining))));
                    if(remaining <= 0)
                        player.setGameMode(GameMode.SPECTATOR);
                }
            }

            Bukkit.getScheduler().runTaskLater(plugin,this,1);

        }
    }
}
