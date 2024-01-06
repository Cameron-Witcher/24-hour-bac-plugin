package me.quickscythe.bac.utils.placeholder;

import me.quickscythe.bac.utils.MessageUtils;
import me.quickscythe.bac.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PlaceholderUtils {

    static Map<String, PlaceholderWorker> placeholders = new HashMap<>();

    public static void registerPlaceholders() {
        registerPlaceholder("name", Player::getName);
        registerPlaceholder("online", (player) -> {
            return Bukkit.getOnlinePlayers().size() + "";
        });
        registerPlaceholder("lt_1st_place_player", (player) -> {
            try {
                return Bukkit.getOfflinePlayer(Utils.getRankings(1)).getName();
            } catch (Exception ex) {
            }
            return "N/A";
        });
        registerPlaceholder("lt_1st_place_time", (player) -> {
            try {
                return MessageUtils.formatTimeRaw(Utils.getCachedTime(Utils.getRankings(1)));
            } catch (Exception ex) {
            }
            return "N/A";
        });

        registerPlaceholder("lt_2nd_place_player", (player) -> {
            try {
                return Bukkit.getOfflinePlayer(Utils.getRankings(2)).getName();
            } catch (Exception ex) {
            }
            return "N/A";
        });
        registerPlaceholder("lt_2nd_place_time", (player) -> {
            try {
                return MessageUtils.formatTimeRaw(Utils.getCachedTime(Utils.getRankings(2)));
            } catch (Exception ex) {
            }
            return "N/A";
        });

        registerPlaceholder("lt_3rd_place_player", (player) -> {
            try {
                return Bukkit.getOfflinePlayer(Utils.getRankings(3)).getName();
            } catch (Exception ex) {
            }
            return "N/A";
        });
        registerPlaceholder("lt_3rd_place_time", (player) -> {
            try {
                return MessageUtils.formatTimeRaw(Utils.getCachedTime(Utils.getRankings(3)));
            } catch (Exception ex) {
            }
            return "N/A";
        });

        registerPlaceholder("lt_4th_place_player", (player) -> {
            try {
                return Bukkit.getOfflinePlayer(Utils.getRankings(4)).getName();
            } catch (Exception ex) {
            }
            return "N/A";
        });
        registerPlaceholder("lt_4th_place_time", (player) -> {
            try {
                return MessageUtils.formatTimeRaw(Utils.getCachedTime(Utils.getRankings(4)));
            } catch (Exception ex) {
            }
            return "N/A";
        });
        registerPlaceholder("lt_5th_place_player", (player) -> {
            try {
                return Bukkit.getOfflinePlayer(Utils.getRankings(5)).getName();
            } catch (Exception ex) {
            }
            return "N/A";
        });
        registerPlaceholder("lt_5th_place_time", (player) -> {
            try {
                return MessageUtils.formatTimeRaw(Utils.getCachedTime(Utils.getRankings(5)));
            } catch (Exception ex) {
            }
            return "N/A";
        });
        registerPlaceholder("lt_6th_place_player", (player) -> {
            try {
                return Bukkit.getOfflinePlayer(Utils.getRankings(6)).getName();
            } catch (Exception ex) {
            }
            return "N/A";
        });
        registerPlaceholder("lt_6th_place_time", (player) -> {
            try {
                return MessageUtils.formatTimeRaw(Utils.getCachedTime(Utils.getRankings(6)));
            } catch (Exception ex) {
            }
            return "N/A";
        });
        registerPlaceholder("lt_7th_place_player", (player) -> {
            try {
                return Bukkit.getOfflinePlayer(Utils.getRankings(7)).getName();
            } catch (Exception ex) {
            }
            return "N/A";
        });
        registerPlaceholder("lt_7th_place_time", (player) -> {
            try {
                return MessageUtils.formatTimeRaw(Utils.getCachedTime(Utils.getRankings(7)));
            } catch (Exception ex) {
            }
            return "N/A";
        });
        registerPlaceholder("lt_8th_place_player", (player) -> {
            try {
                return Bukkit.getOfflinePlayer(Utils.getRankings(8)).getName();
            } catch (Exception ex) {
            }
            return "N/A";
        });
        registerPlaceholder("lt_8th_place_time", (player) -> {
            try {
                return MessageUtils.formatTimeRaw(Utils.getCachedTime(Utils.getRankings(8)));
            } catch (Exception ex) {
            }
            return "N/A";
        });
        registerPlaceholder("lt_9th_place_player", (player) -> {
            try {
                return Bukkit.getOfflinePlayer(Utils.getRankings(9)).getName();
            } catch (Exception ex) {
            }
            return "N/A";
        });
        registerPlaceholder("lt_9th_place_time", (player) -> {
            try {
                return MessageUtils.formatTimeRaw(Utils.getCachedTime(Utils.getRankings(9)));
            } catch (Exception ex) {
            }
            return "N/A";
        });
        registerPlaceholder("lt_10th_place_player", (player) -> {
            try {
                return Bukkit.getOfflinePlayer(Utils.getRankings(10)).getName();
            } catch (Exception ex) {
            }
            return "N/A";
        });
        registerPlaceholder("lt_10th_place_time", (player) -> {
            try {
                return MessageUtils.formatTimeRaw(Utils.getCachedTime(Utils.getRankings(10)));
            } catch (Exception ex) {
            }
            return "N/A";
        });


    }

    public static void registerPlaceholder(String key, PlaceholderWorker worker) {
        placeholders.put("%" + key + "%", worker);
    }

    public static String replace(Player player, String string) {

        for (Entry<String, PlaceholderWorker> e : placeholders.entrySet()) {
            if (string.contains(e.getKey())) {
                string = string.replaceAll(e.getKey(), e.getValue().run(player));
            }
        }

        string = emotify(string);
        return string;
    }

    public static String emotify(String string) {
        String tag = string;
        while (tag.contains("%symbol:")) {
            String icon = tag.split("ymbol:")[1].split("%")[0];
            if (Symbols.valueOf(icon.toUpperCase()) == null) {
                tag = tag.replaceAll("%symbol:" + icon + "%", Symbols.UNKNOWN.toString());
            } else {
                tag = tag.replaceAll("%symbol:" + icon + "%", Symbols.valueOf(icon.toUpperCase()).toString());
            }
        }
        return tag;
    }

    public static String markup(Player player, String string) {
        String tag = string;
        while (tag.contains("%bold:")) {
            String icon = tag.split("old:")[1].split("%")[0];
            tag = tag.replaceAll("%bold:" + icon + "%", ChatColor.BOLD + icon + ChatColor.getLastColors(tag.split("%bold")[0]));
        }
        while (tag.contains("%upper:")) {
            String icon = tag.split("pper:")[1].split("%")[0];
            tag = tag.replaceAll("%upper:" + icon + "%", icon.contains("%") ? replace(player, icon).toUpperCase() : icon.toUpperCase());
        }
        while (tag.contains("%fade:")) {
            String from = tag.split(":")[1];
            String to = tag.split(":")[2];
            String s = tag.split(":")[3].split("%")[0];

            tag = tag.replaceFirst("%fade:" + from + ":" + to + ":" + s + "%", MessageUtils.fade(from, to, s));
        }
        return tag;
    }

    @FunctionalInterface
    public interface PlaceholderWorker {

        String run(Player player);

    }

}