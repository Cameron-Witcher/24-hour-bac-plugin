package me.quickscythe.lt.commands.listeners;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.json2.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminCommandTabCompleter implements TabCompleter {

    Map<String, List<String>> cmds = new HashMap<>();
    Map<String, List<String>> cmds2 = new HashMap<>();
    JSONObject cmds1 = new JSONObject();

    public AdminCommandTabCompleter() {
        cmds.put("reset", new ArrayList<>());
        cmds.put("add", new ArrayList<>());
        cmds.put("set", new ArrayList<>());
        cmds.put("get", new ArrayList<>());
        cmds.put("pause", new ArrayList<>());
        cmds.put("help", new ArrayList<>(cmds.keySet()));

        cmds2.put("create", new ArrayList<>());
        cmds2.put("edit", List.of(new String[]{}));

//        cmds1.put("hologram", "{'cmds':[{'name':'playtime','cmds':[{'name':'reset'}]}]}")


    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();
//        for (int i = 0; i < cmds1.getJSONArray("cmds").length(); i++) {
//            JSONObject cmdData = cmds1.getJSONArray("cmds").getJSONObject(i);
//            if(cmdData.getString("name").equalsIgnoreCase(cmd.getName())){
//                List<String> s = new ArrayList<>();
//                if(cmdData.has("cmds")){
//                    for (int i1 = 0; i1 < cmdData.getJSONArray("cmds").length(); i1++) {
//                        JSONObject subCmdData = cmdData.getJSONArray("cmds").getJSONObject(i1);
//                        s.add(subCmdData.getString("name"));
//                }
//                StringUtil.copyPartialMatches(args[args.length-1], new ArrayList<>(s), completions);
//            }
//        }


        if (cmd.getName().equalsIgnoreCase("playtime")) {
            if (args.length == 1) StringUtil.copyPartialMatches(args[0], new ArrayList<>(cmds.keySet()), completions);

            if (args.length == 2) StringUtil.copyPartialMatches(args[1], getOnlinePlayers(), completions);

        }
//        if (cmd.getName().equalsIgnoreCase("hologram")) {
//            if(args.length){
//
//            }
//        }


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
