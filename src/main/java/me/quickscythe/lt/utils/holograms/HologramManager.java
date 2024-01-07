package me.quickscythe.lt.utils.holograms;

import me.quickscythe.lt.utils.Utils;
import me.quickscythe.lt.utils.misc.UID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.json2.JSONArray;
import org.json2.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HologramManager {

    private final Map<String, Hologram> holograms = new HashMap<>();
    private final Map<String, ClassicHologram> classicHolograms = new HashMap<>();
    private JSONObject hdata = new JSONObject("{}");
    private File hologramFile = null;

    public void init() {
        hologramFile = new File(Utils.getPlugin().getDataFolder() + "/holograms.info");
        if (!hologramFile.exists()) {
            try {
                hologramFile.createNewFile();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            Scanner scanner = new Scanner(hologramFile);
            while (scanner.hasNextLine()) this.hdata = new JSONObject(scanner.nextLine());
            scanner.close();
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        if (hdata.has("classic_holograms")) {
            JSONArray holos = hdata.getJSONArray("classic_holograms");
            for (int i = 0; i < holos.length(); i++) {
                JSONObject holoData = holos.getJSONObject(i);
                ClassicHologram holo;
                Location loc = Utils.decryptLocation(holoData.getString("location"));
                if (holoData.has("uid")) {
                    holo = new ClassicHologram(UID.from(holoData.getString("uid")), loc);
                    classicHolograms.put(holo.getUID().toString(), holo);
                } else holo = createClassicHologram(loc);
                holo.setLines(holoData.getJSONArray("lines"));
                holo.setPersistent(true);

            }
        }
    }

    public Hologram createHologram(Location loc) {
        Hologram holo = new Hologram(new UID(), loc);
        holograms.put(holo.getUID().toString(), holo);
        return holo;
    }

    public Hologram getHologram(String uid) {
        return holograms.get(uid);
    }

    public ClassicHologram createClassicHologram(Location loc) {
        ClassicHologram holo = new ClassicHologram(new UID(), loc);
        classicHolograms.put(holo.getUID().toString(), holo);
        return holo;
    }

    public ClassicHologram getClassicHologram(String uid) {
        return classicHolograms.get(uid);
    }


    public void end() {
        hdata = new JSONObject("{}");
        JSONArray holos = new JSONArray();
        for (Map.Entry<String, ClassicHologram> e1 : classicHolograms.entrySet()) {
            if (e1.getValue().isPersistent()) {
                JSONObject holo = new JSONObject("{}");
                holo.put("location", Utils.encryptLocation(e1.getValue().getLocation()));
                JSONArray lines = new JSONArray();

                for (Map.Entry<Integer, String> e : e1.getValue().getLines().entrySet()) {
                    JSONObject line = new JSONObject("{}");
                    line.put("text", e.getValue());
                    line.put("line", e.getKey());
                    lines.put(line);
                }
                holo.put("lines", lines);
                holo.put("uid", e1.getValue().getUID().toString());

                holos.put(holo);
                e1.getValue().kill();
                Bukkit.broadcastMessage(holo.toString());
            }
        }
        hdata.put("classic_holograms", holos);


        try {

            FileWriter f2 = new FileWriter(hologramFile, false);
            f2.write(hdata.toString());
            f2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Collection<ClassicHologram> getClassicHolograms() {
        return classicHolograms.values();
    }

    public void removeHologram(ClassicHologram holo) {
        classicHolograms.remove(holo.getUID().toString());
        holo.kill();
    }
}
