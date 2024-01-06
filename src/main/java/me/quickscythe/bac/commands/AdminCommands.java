package me.quickscythe.bac.commands;

import me.quickscythe.bac.BacPlugin;
import me.quickscythe.bac.commands.listeners.AdminCommandTabCompleter;
import me.quickscythe.bac.utils.MessageUtils;
import me.quickscythe.bac.utils.Utils;
import me.quickscythe.bac.utils.holograms.ClassicHologram;
import me.quickscythe.bac.utils.players.EventPlayer;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import java.util.Objects;

public class AdminCommands implements CommandExecutor {
    public AdminCommands(BacPlugin plugin, String... cmds) {
        for (String cmd : cmds) {
            PluginCommand com = plugin.getCommand(cmd);
            assert com != null;
            com.setExecutor(this);
            com.setTabCompleter(new AdminCommandTabCompleter());
        }

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("admin.cmd")) {
            if (cmd.getName().equalsIgnoreCase("hologram")) {
                if(!(sender instanceof Player)){
                    sender.sendMessage(MessageUtils.prefixes("hologram") + "Sorry, you must be a player to run this command.");
                    return true;
                }
                Player player = (Player) sender;
                // /hologram create
                // /hologram edit <UID> move [(here)|<x> <y> <z>]
                // /hologram edit <UID> setline <line> <text>
                // /hologram edit <UID> removeline <line>

                if(args.length == 0){
//                    player.sendMessage(MessageUtils);
                    return false;
                }
                if (args[0].equalsIgnoreCase("list")) {
                    for (ClassicHologram holo : Utils.getHologramManager().getClassicHolograms()) {
                        sender.sendMessage("persistent=" + holo.isPersistent() + ": " + holo.getUID());
                    }
                }
                if(args[0].equalsIgnoreCase("create")){
                    ClassicHologram holo = Utils.getHologramManager().createClassicHologram(player.getLocation());
                    holo.setPersistent(true);
                    holo.setLine(0,"Change this with /holo edit " + holo.getUID());
                    sender.sendMessage(MessageUtils.prefixes("hologram") + "Created hologram with UID: " + holo.getUID());
                }
                if(args[0].equalsIgnoreCase("edit")){
                    if(args.length < 2){
                        return true;
                    }
                    if(args.length == 2){
                        sender.sendMessage(MessageUtils.prefixes("hologram") + "Here are some commands you can run:");
                        String key = "&f  /" + label + " edit " + args[1] + " ";
                        sender.sendMessage(MessageUtils.colorize(key + "move [(here)|<x> <y> <z>]"));
                        sender.sendMessage(MessageUtils.colorize(key + "setline <line> <text>"));
                        sender.sendMessage(MessageUtils.colorize(key + "removeline <line>"));
                        return true;
                    }
                    ClassicHologram holo = Utils.getHologramManager().getClassicHologram(args[1]);
                    if(args[2].equalsIgnoreCase("move")){
                        Location loc = player.getLocation();
                        if(args.length > 4){
                            loc.setX(Double.parseDouble(args[3]));
                            loc.setY(Double.parseDouble(args[4]));
                            loc.setZ(Double.parseDouble(args[5]));
                        }
                        holo.move(loc);
                        return true;
                    }
                    if(args[2].equalsIgnoreCase("setline")){
                        String s = "";
                        for(int i=4;i!=args.length;i++)
                            s = Objects.equals(s, "") ? args[i] : s + " " + args[i];
                        holo.setLine(Integer.parseInt(args[3]), s);
                    }
                }

            }
            if (cmd.getName().equalsIgnoreCase("playtime")) {
                if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                    sender.sendMessage(MessageUtils.prefixes("playtime") + "Here is a list of commands you can run:");
                    sender.sendMessage(MessageUtils.colorize("&f /" + label + " help &7- Display this list"));
                    sender.sendMessage(MessageUtils.colorize("&f /" + label + " get <player> &7- Gets a player's current play-time in a human readable format"));
                    sender.sendMessage(MessageUtils.colorize("&f /" + label + " reset <player|@a> &7- Resets a player's current playtime"));
                    sender.sendMessage(MessageUtils.colorize("&f /" + label + " <set|add> <player> <integer>[(h)[ours]|m[inutes]|s[econds]] &7- Sets a player's current play-time (not working yet)"));
                    return true;
                }
                if (args[0].equalsIgnoreCase("pause")) {
                    if (Utils.getPlayerManager().isPaused()) Utils.getPlayerManager().pause();
                    else Utils.getPlayerManager().unpause();
                }
                if (args.length < 2) {
                    sender.sendMessage(MessageUtils.prefixes("playtime") + "Try /" + label + " help");
                    return true;
                }
                if (args[0].equalsIgnoreCase("get")) {
                    if (Utils.getPlayerManager().searchPlayer(args[1]) == null) {
                        sender.sendMessage(MessageUtils.prefixes("playtime") + "Sorry, that player wasn't found in the database.");
                        return true;
                    }
                    EventPlayer eplayer = Utils.getPlayerManager().searchPlayer(args[1]);
                    sender.sendMessage(MessageUtils.prefixes("playtime") + "Current playtime for " + eplayer.getUsername() + " is " + MessageUtils.formatTimeRaw(eplayer.getCurrentTime()));
                    return true;
                }
                if (args[0].equalsIgnoreCase("reset")) {
                    if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("@a")) {
                        for (EventPlayer player : Utils.getPlayerManager().getPlayerMap().values())
                            player.reset();

                        return true;
                    }
                    if (Utils.getPlayerManager().searchPlayer(args[1]) == null) {
                        sender.sendMessage(MessageUtils.prefixes("playtime") + "Sorry, that player wasn't found in the database.");
                        return true;
                    }
                    EventPlayer eplayer = Utils.getPlayerManager().searchPlayer(args[1]);
                    eplayer.reset();
                    sender.sendMessage(MessageUtils.prefixes("playtime") + "Current playtime for " + eplayer.getUsername() + " is " + MessageUtils.formatTimeRaw(eplayer.getCurrentTime()));
                    return true;
                }

                if (args[0].equalsIgnoreCase("set")) {
                    if (args.length < 3) {
                        sender.sendMessage(MessageUtils.prefixes("playtime") + "Usage: /" + label + " <set|add> <player> <integer>[(h)[ours]|m[inutes]|s[econds]]");
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("@a")) {
                        for (EventPlayer player : Utils.getPlayerManager().getPlayerMap().values())
                            player.setPlaytime(Utils.convertTime(args[2]));

                        return true;
                    }
                    if (Utils.getPlayerManager().searchPlayer(args[1]) == null) {
                        sender.sendMessage(MessageUtils.prefixes("playtime") + "Sorry, that player wasn't found in the database.");
                        return true;
                    }
                    EventPlayer eplayer = Utils.getPlayerManager().searchPlayer(args[1]);
                    eplayer.setPlaytime(Utils.convertTime(args[2]));
                    sender.sendMessage(MessageUtils.prefixes("playtime") + "Current playtime for " + eplayer.getUsername() + " is " + MessageUtils.formatTimeRaw(eplayer.getCurrentTime()));
                    return true;
                }
                if (args[0].equalsIgnoreCase("add")) {
                    if (args.length < 3) {
                        sender.sendMessage(MessageUtils.prefixes("playtime") + "Usage: /" + label + " <set|add> <player> <integer>[(h)[ours]|m[inutes]|s[econds]]");
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("@a")) {
                        for (EventPlayer player : Utils.getPlayerManager().getPlayerMap().values())
                            player.addPlaytime(Utils.convertTime(args[2]));
                        return true;
                    }
                    if (Utils.getPlayerManager().searchPlayer(args[1]) == null) {
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
