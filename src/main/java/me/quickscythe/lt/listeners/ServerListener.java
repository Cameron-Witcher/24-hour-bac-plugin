package me.quickscythe.lt.listeners;

import me.quickscythe.lt.LimitedTime;
import me.quickscythe.lt.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ServerListener implements Listener {
    public ServerListener(LimitedTime plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().hasPermission("admin.not_playing"))
            Utils.getPlayerManager().getPlayer(e.getPlayer()).login();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        if (!e.getPlayer().hasPermission("admin.not_playing"))
            Utils.getPlayerManager().getPlayer(e.getPlayer()).logout();
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent e){
        if(!e.getPlayer().hasPermission("lt.admin.not_playing")){
            if(Utils.getPlayerManager().getPlayer(e.getPlayer()).isFinished() && !e.getNewGameMode().equals(GameMode.SPECTATOR)){
                e.setCancelled(true);
                e.getPlayer().setGameMode(GameMode.SPECTATOR);
            }
        }
    }
}
