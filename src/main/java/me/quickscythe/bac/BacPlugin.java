package me.quickscythe.bac;


import me.quickscythe.bac.listeners.ServerListener;
import me.quickscythe.bac.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BacPlugin extends JavaPlugin {

    public void onEnable() {


        new ServerListener(this);

        Utils.init(this);


        //For reloads
        for (Player player : Bukkit.getOnlinePlayers()) {
            Utils.login(player.getUniqueId());
        }
    }

    public void onDisable() {

        for (Player player : Bukkit.getOnlinePlayers()) {
            Utils.logout(player.getUniqueId());
        }
        Utils.end();
    }

}
