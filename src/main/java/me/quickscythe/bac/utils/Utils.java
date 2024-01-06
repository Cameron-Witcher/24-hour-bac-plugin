package me.quickscythe.bac.utils;

import me.quickscythe.bac.BacPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Utils {

    private static BacPlugin plugin = null;
    private static FileConfiguration fileConfiguration = null;
    private static File playerFile = null;
    private static final Map<UUID, Long> playerStorage = new HashMap<>();
    private static final Map<UUID, Long> loginTimes = new HashMap<>();

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
        fileConfiguration = YamlConfiguration.loadConfiguration(playerFile);

        Bukkit.getScheduler().runTaskLater(plugin, new Heartbeat(), 1);
    }

    public static long getPlayTime(UUID uid){
        if(playerStorage.containsKey(uid))
            return playerStorage.get(uid);
        if(fileConfiguration.isSet("players." + uid.toString())){
            playerStorage.put(uid, fileConfiguration.getLong("player." + uid.toString()));
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
        return loginTimes.containsKey(uid) ? new Date().getTime() - loginTimes.get(uid) : 0;
    }

    public static long getCurrentPlaytime(UUID uid){
        return getPlayTime(uid) + getLoggedInTime(uid);
    }

    public static void end(){
        for(Map.Entry<UUID, Long> e : playerStorage.entrySet()){
            fileConfiguration.set("players." + e.getKey().toString(), e.getValue());
        }
        try {
            fileConfiguration.save(playerFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BacPlugin getPlugin() {
        return plugin;
    }

    private static class Heartbeat implements Runnable {
        @Override
        public void run() {

            for(Player player : Bukkit.getOnlinePlayers()){
                if(player.getGameMode().equals(GameMode.SURVIVAL)){
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(MessageUtils.colorize("&3" + MessageUtils.formatTimeRaw(getCurrentPlaytime(player.getUniqueId())))));
                }
            }

            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin,this,1);

        }
    }
}
