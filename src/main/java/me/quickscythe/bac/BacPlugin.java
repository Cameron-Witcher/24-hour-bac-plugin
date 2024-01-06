package me.quickscythe.bac;


import me.quickscythe.bac.commands.AdminCommands;
import me.quickscythe.bac.listeners.ServerListener;
import me.quickscythe.bac.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BacPlugin extends JavaPlugin {

    public void onEnable() {


        new ServerListener(this);

        new AdminCommands(this, "playtime", "hologram");

        Utils.init(this);


        //For reloads
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("admin.not_playing")) Utils.getPlayerManager().getPlayer(player).login();
        }
    }

    public void onDisable() {

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("admin.not_playing")) Utils.getPlayerManager().getPlayer(player).logout();
        }
        Utils.end();
    }

}
