package me.quickscythe.lt;


import me.quickscythe.lt.commands.AdminCommands;
import me.quickscythe.lt.listeners.ServerListener;
import me.quickscythe.lt.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class LimitedTime extends JavaPlugin {

    public void onEnable() {


        new ServerListener(this);

        new AdminCommands(this, "playtime", "hologram");

        Utils.init(this);


        //For reloads
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("lt.admin.not_playing")) Utils.getPlayerManager().getPlayer(player).login();
        }
    }

    public void onDisable() {

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("lt.admin.not_playing")) Utils.getPlayerManager().getPlayer(player).logout();
        }
        Utils.end();
    }

}
