package modele;

import java.util.HashMap;
import java.util.Map;

public class DistanceMap {
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
}
