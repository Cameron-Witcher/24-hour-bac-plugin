package me.quickscythe.bac.listeners;

import me.quickscythe.bac.BacPlugin;
import me.quickscythe.bac.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ServerListener implements Listener {
    public ServerListener(BacPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Utils.login(e.getPlayer().getUniqueId());
        Utils.saveUUID(e.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
       Utils.logout(e.getPlayer().getUniqueId());
    }
}
