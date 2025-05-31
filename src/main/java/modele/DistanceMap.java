package modele;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistanceMap {
    public static final String FICHIER_DISTANCES = "ressources_appli/distances.txt";
    private Map<String, Map<String, Integer>> distances = new HashMap<>();

    public void addDistance(String from, String to, int distance) {
        distances.computeIfAbsent(from, k -> new HashMap<>()).put(to, distance);
    }

    public int getDistance(String from, String to) {
        if (!distances.containsKey(from) || !distances.get(from).containsKey(to)) {
            System.err.println("Distance inconnue entre " + from + " et " + to);
            return Integer.MAX_VALUE;
        }
        return distances.get(from).get(to);
    }

    public Map<String, Integer> getNeighbours(String from) {
        return distances.getOrDefault(from, new HashMap<>());
    }

    public boolean hasCity(String city) {
        return distances.containsKey(city);
    }

    public static DistanceMap chargerDepuisFichier() throws IOException {
        DistanceMap distMap = new DistanceMap();
        List<String> lignes = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(FICHIER_DISTANCES));
        String line;
        while ((line = reader.readLine()) != null) {
            lignes.add(line);
        }
        reader.close();

        List<String> villes = new ArrayList<>();
        for (String l : lignes) {
            villes.add(l.split("\\s+")[0]);
        }

        for (int i = 0; i < lignes.size(); i++) {
            String[] parts = lignes.get(i).trim().split("\\s+");
            String villeA = parts[0];
            for (int j = 1; j < parts.length; j++) {
                String villeB = villes.get(j - 1);
                int d = Integer.parseInt(parts[j]);
                distMap.addDistance(villeA, villeB, d);
            }
        }

        return distMap;
    }

}
