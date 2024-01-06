package me.quickscythe.bac.utils.holograms;

import me.quickscythe.bac.utils.Utils;
import me.quickscythe.bac.utils.misc.UID;
import org.bukkit.Location;
import org.json2.JSONArray;
import org.json2.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HologramManager {

    private final Map<UID, Hologram> holograms = new HashMap<>();
    private final Map<UID, ClassicHologram> classicHolograms = new HashMap<>();
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
                ClassicHologram holo = createClassicHologram(Utils.decryptLocation(holoData.getString("location")));
                holo.setLines(holoData.getJSONArray("lines"));
            }
        }
    }

    public Hologram createHologram(Location loc) {
        Hologram holo = new Hologram(new UID(), loc);
        holograms.put(holo.getUID(), holo);
        return holo;
    }

    public Hologram getHologram(UID uid) {
        return holograms.get(uid);
    }

    public ClassicHologram createClassicHologram(Location loc) {
        ClassicHologram holo = new ClassicHologram(new UID(), loc);
        classicHolograms.put(holo.getUID(), holo);
        return holo;
    }

    public ClassicHologram getClassicHologram(UID uid) {
        return classicHolograms.get(uid);
    }


    public void end() {
        hdata = new JSONObject("{}");
        JSONArray holos = new JSONArray();
        for (Map.Entry<UID, ClassicHologram> e : classicHolograms.entrySet()) {
            if (e.getValue().isPersistent()) {
                JSONObject holo = new JSONObject("{}");
                holo.put("location", Utils.encryptLocation(e.getValue().getLocation()));
                holo.put("lines", new JSONArray());
                for (Map.Entry<Integer, String> line : e.getValue().getLines().entrySet()) {
                    holo.getJSONArray("lines").put(new JSONObject().put("text", e.getValue()).put("line", e.getKey()));
                }

                holos.put(holo);
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


}
