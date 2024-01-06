package me.quickscythe.bac.commands.listeners;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminCommandTabCompleter implements TabCompleter {

    Map<String, List<String>> cmds = new HashMap<>();

    public AdminCommandTabCompleter() {
        cmds.put("reset", new ArrayList<>());
        cmds.put("add", new ArrayList<>());
        cmds.put("set", new ArrayList<>());
        cmds.put("get", new ArrayList<>());
        cmds.put("help", new ArrayList<>(cmds.keySet()));


    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("playtime")) {
            if (args.length == 1)
                StringUtil.copyPartialMatches(args[0], new ArrayList<>(cmds.keySet()), completions);

            if (args.length == 2)
                StringUtil.copyPartialMatches(args[1], getOnlinePlayers(), completions);

        }


        return completions;

    }

    public List<String> getOnlinePlayers() {
        List<String> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            players.add(player.getName());
        }
        return players;
    }

}
