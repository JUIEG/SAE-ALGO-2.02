package modele;

import java.io.*;
import java.util.*;

public class Util {

    public static Map<String, String> chargerMembres(String chemin) throws IOException {
        Map<String, String> map = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(chemin));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
            map.put(parts[0].trim(), parts[1].trim());
        }
        reader.close();
        return map;
    }

    public static List<Vente> chargerVentes(String chemin) throws IOException {
        List<Vente> list = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(chemin));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("->");
            if (parts.length == 2) {
                list.add(new Vente(parts[0].trim(), parts[1].trim()));
            }
        }
        reader.close();
        return list;
    }

    public static DistanceMap chargerDistances(String chemin) throws IOException {
        DistanceMap distMap = new DistanceMap();
        List<String> lignes = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(chemin));
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

    public static int calculerDistance(List<String> parcours, DistanceMap distances) {
        int total = 0;
        for (int i = 0; i < parcours.size() - 1; i++) {
            total += distances.getDistance(parcours.get(i), parcours.get(i + 1));
        }
        return total;
    }
}
