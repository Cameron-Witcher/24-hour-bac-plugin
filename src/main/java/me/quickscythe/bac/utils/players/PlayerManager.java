package me.quickscythe.bac.utils.players;

import me.quickscythe.bac.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json2.JSONArray;
import org.json2.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class PlayerManager {

    private final Map<UUID, EventPlayer> playerMap = new HashMap<>();
    private JSONObject data = new JSONObject("{}");
    private File playerFile = null;
    boolean paused = true;

    public void init() {
        playerFile = new File(Utils.getPlugin().getDataFolder() + "/players.info");
        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            Scanner scanner = new Scanner(playerFile);
            while (scanner.hasNextLine()) this.data = new JSONObject(scanner.nextLine());
            scanner.close();
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        if (data.has("players")) {
            JSONArray players = data.getJSONArray("players");
            for (int i = 0; i < players.length(); i++) {
                JSONObject playerData = players.getJSONObject(i);
                UUID uid = UUID.fromString(playerData.getString("uuid"));
                EventPlayer player = new EventPlayer(playerData.getString("username"), uid, playerData.getLong("current_time"));
                playerMap.put(uid, player);
                Utils.cacheTime(player);
            }
        }
    }

    public void pause(){
        paused = true;
        for(Player player : Bukkit.getOnlinePlayers()){
            getPlayer(player).logout();
        }
    }

    public void unpause(){
        paused = false;
        for(Player player : Bukkit.getOnlinePlayers())
            getPlayer(player).login();
    }

    public void end() {
        data = new JSONObject("{}");
        JSONArray players = new JSONArray();
        for (Map.Entry<UUID, EventPlayer> e : playerMap.entrySet()) {
            JSONObject player = new JSONObject("{}");
            player.put("uuid", e.getKey());
            player.put("username", e.getValue().getUsername());
            player.put("current_time", e.getValue().getCurrentTime());
            players.put(player);
        }
        data.put("players", players);


        try {

            FileWriter f2 = new FileWriter(playerFile, false); // important part
            f2.write(data.toString());
            f2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public EventPlayer getPlayer(Player player) {
        if (!playerMap.containsKey(player.getUniqueId()))
            playerMap.put(player.getUniqueId(), new EventPlayer(player.getName(), player.getUniqueId()));
        return playerMap.get(player.getUniqueId());
    }

    public EventPlayer searchPlayer(UUID uid) {
        return playerMap.getOrDefault(uid, null);
    }

    public EventPlayer searchPlayer(String name) {
        for (EventPlayer player : playerMap.values()) {
            if (player.getUsername().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }

    public Map<UUID, EventPlayer> getPlayerMap() {
        return playerMap;
    }
}
