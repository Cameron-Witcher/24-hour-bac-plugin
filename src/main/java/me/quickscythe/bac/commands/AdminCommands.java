package me.quickscythe.bac.commands;

import me.quickscythe.bac.BacPlugin;
import me.quickscythe.bac.commands.listeners.AdminCommandTabCompleter;
import me.quickscythe.bac.utils.MessageUtils;
import me.quickscythe.bac.utils.Utils;
import me.quickscythe.bac.utils.players.EventPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import java.util.UUID;

public class AdminCommands implements CommandExecutor {
    public AdminCommands(BacPlugin plugin, String... cmds) {
        for(String cmd : cmds){
            PluginCommand com = plugin.getCommand(cmd);
            assert com != null;
            com.setExecutor(this);
            com.setTabCompleter(new AdminCommandTabCompleter());
        }

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender.hasPermission("admin.cmd")){
            if(cmd.getName().equalsIgnoreCase("playtime")){
                if(args.length == 0 || args[0].equalsIgnoreCase("help")){
                    sender.sendMessage(MessageUtils.prefixes("playtime") + "Here is a list of commands you can run:");
                    sender.sendMessage(MessageUtils.colorize("&f /" + label + " help &7- Display this list"));
                    sender.sendMessage(MessageUtils.colorize("&f /" + label + " get <player> &7- Gets a player's current play-time in a human readable format"));
                    sender.sendMessage(MessageUtils.colorize("&f /" + label + " reset <player|@a> &7- Resets a player's current playtime"));
                    sender.sendMessage(MessageUtils.colorize("&f /" + label + " set <player> <time> &7- Sets a player's current play-time (not working yet)"));
                    return true;
                }
                if(args.length < 2){
                    sender.sendMessage(MessageUtils.prefixes("playtime") + "Try /" + label + " help");
                    return true;
                }
                if(args[0].equalsIgnoreCase("get")){
                    if(Utils.getPlayerManager().searchPlayer(args[1]) == null){
                        sender.sendMessage(MessageUtils.prefixes("playtime") + "Sorry, that player wasn't found in the database.");
                        return true;
                    }
                    EventPlayer eplayer = Utils.getPlayerManager().searchPlayer(args[1]);
                    sender.sendMessage(MessageUtils.prefixes("playtime") + "Current playtime for " + eplayer.getUsername() + " is " + MessageUtils.formatTimeRaw(eplayer.getCurrentTime()));
                    return true;
                }
                if(args[0].equalsIgnoreCase("reset")){
                    if(args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("@a")){
                        for(EventPlayer player : Utils.getPlayerManager().getPlayerMap().values())
                            player.reset();

                        return true;
                    }
                    if(Utils.getPlayerManager().searchPlayer(args[1]) == null){
                        sender.sendMessage(MessageUtils.prefixes("playtime") + "Sorry, that player wasn't found in the database.");
                        return true;
                    }
                    EventPlayer eplayer = Utils.getPlayerManager().searchPlayer(args[1]);
                    eplayer.reset();
                    sender.sendMessage(MessageUtils.prefixes("playtime") + "Current playtime for " + eplayer.getUsername() + " is " + MessageUtils.formatTimeRaw(eplayer.getCurrentTime()));
                    return true;
                }

                if(args[0].equalsIgnoreCase("set")){
                    if(args.length < 3){
                        sender.sendMessage(MessageUtils.prefixes("playtime") + "Usage: /" + label + " set <player> <time>");
                        return true;
                    }
                    if(args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("@a")) {
                        for (EventPlayer player : Utils.getPlayerManager().getPlayerMap().values())
                            player.setPlaytime(Utils.convertTime(args[2]));

                        return true;
                    }
                    if(Utils.getPlayerManager().searchPlayer(args[1]) == null){
                        sender.sendMessage(MessageUtils.prefixes("playtime") + "Sorry, that player wasn't found in the database.");
                        return true;
                    }
                    EventPlayer eplayer = Utils.getPlayerManager().searchPlayer(args[1]);
                    eplayer.setPlaytime(Utils.convertTime(args[2]));
                    sender.sendMessage(MessageUtils.prefixes("playtime") + "Current playtime for " + eplayer.getUsername() + " is " + MessageUtils.formatTimeRaw(eplayer.getCurrentTime()));
                    return true;
                }
                if(args[0].equalsIgnoreCase("add")){
                    if(args.length < 3){
                        sender.sendMessage(MessageUtils.prefixes("playtime") + "Usage: /" + label + " set <player> <time>");
                        return true;
                    }
                    if(args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("@a")){
                        for(EventPlayer player : Utils.getPlayerManager().getPlayerMap().values())
                            player.addPlaytime(Utils.convertTime(args[2]));
                        return true;
                    }
                    if(Utils.getPlayerManager().searchPlayer(args[1]) == null){
                        sender.sendMessage(MessageUtils.prefixes("playtime") + "Sorry, that player wasn't found in the database.");
                        return true;
                    }
                    EventPlayer eplayer = Utils.getPlayerManager().searchPlayer(args[1]);
                    eplayer.addPlaytime(Utils.convertTime(args[2]));
                    sender.sendMessage(MessageUtils.prefixes("playtime") + "Current playtime for " + eplayer.getUsername() + " is " + MessageUtils.formatTimeRaw(eplayer.getCurrentTime()));
                    return true;
                }

            }
        } else {
            sender.sendMessage(MessageUtils.prefixes("admin") + "Sorry that is an admin command.");
        }
        return true;
    }
}
