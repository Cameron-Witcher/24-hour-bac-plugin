package me.quickscythe.bac.utils;

import me.quickscythe.bac.BacPlugin;
import me.quickscythe.bac.utils.players.PlayerManager;
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
    private static final PlayerManager playerManager = new PlayerManager();
    private static long duration = 24;

//    private static final Map<UUID, Long> playerStorage = new HashMap<>();
//    private static final Map<UUID, Long> loginTimes = new HashMap<>();
//    private static final Map<String, UUID> uuids = new HashMap<>();

    public static void init(BacPlugin plugin){
        Utils.plugin = plugin;

        if(!plugin.getConfig().isSet("max_time")){
            plugin.getConfig().set("max_time", duration);
        } else duration = convertTime(plugin.getConfig().getString("max_time"));
        plugin.saveConfig();
        playerManager.init();

        Bukkit.getScheduler().runTaskLater(plugin, new Heartbeat(), 1);
    }

    public static long convertTime(String maxTime) {
        TimeUnit unit = TimeUnit.HOURS;
        if(maxTime.endsWith("h") || maxTime.endsWith("hours") || maxTime.endsWith("hour")){
            maxTime = maxTime.replace("hours", "");
            maxTime = maxTime.replace("hour", "");
            maxTime = maxTime.replace("h","");
        }
        if(maxTime.endsWith("m") || maxTime.endsWith("minutes") || maxTime.endsWith("minute")){
            unit = TimeUnit.MINUTES;
            maxTime = maxTime.replace("minutes", "");
            maxTime = maxTime.replace("minute", "");
            maxTime = maxTime.replace("m","");
        }
        if(maxTime.endsWith("s") || maxTime.endsWith("seconds") || maxTime.endsWith("second")){
            unit = TimeUnit.SECONDS;
            maxTime = maxTime.replace("seconds", "");
            maxTime = maxTime.replace("second", "");
            maxTime = maxTime.replace("s","");
        }
        return TimeUnit.MILLISECONDS.convert(Long.parseLong(maxTime),unit);
    }

    public static PlayerManager getPlayerManager(){
        return playerManager;
    }


    public static void end(){
        playerManager.end();

    }

    public static BacPlugin getPlugin() {
        return plugin;
    }

    private static class Heartbeat implements Runnable {
        @Override
        public void run() {

            for(Player player : Bukkit.getOnlinePlayers()){
                if(player.getGameMode().equals(GameMode.SURVIVAL)){
                    long remaining = duration - playerManager.getPlayer(player).getCurrentTime();
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(MessageUtils.colorize("&7" + MessageUtils.formatTimeRaw(remaining))));
                    if(remaining <= 0)
                        player.setGameMode(GameMode.SPECTATOR);
                }
            }

            Bukkit.getScheduler().runTaskLater(plugin,this,1);

        }
    }
}
