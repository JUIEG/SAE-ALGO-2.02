package modele;

import java.util.*;

public class AlgoBase {

    public static List<String> calculerItineraire(List<Vente> ventes, Map<String, String> pseudoToVille) {
        Set<String> villes = new HashSet<>();
        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        for (Vente v : ventes) {
            String vendeur = pseudoToVille.get(v.getVendeur());
            String acheteur = pseudoToVille.get(v.getAcheteur());
            villes.add(vendeur);
            villes.add(acheteur);
        }

        for (String ville : villes) {
            String collect = ville + "+";
            String deliver = ville + "-";

            addEdge(graph, collect, deliver);
            addEdge(graph, "Velizy+", collect);
            addEdge(graph, deliver, "Velizy-");

            inDegree.putIfAbsent(collect, 0);
            inDegree.putIfAbsent(deliver, 0);
        }

        for (Vente v : ventes) {
            String vendeur = pseudoToVille.get(v.getVendeur()) + "+";
            String acheteur = pseudoToVille.get(v.getAcheteur()) + "-";
            addEdge(graph, vendeur, acheteur);
        }

        for (String from : graph.keySet()) {
            for (String to : graph.get(from)) {
                inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
            }
        }

        List<String> ordre = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> enFile = new HashSet<>();

        PriorityQueue<String> queue = new PriorityQueue<>();
        queue.add("Velizy+");
        enFile.add("Velizy+");

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (visited.contains(current)) continue;
            visited.add(current);
            ordre.add(current);

            for (String next : graph.getOrDefault(current, Set.of())) {
                inDegree.put(next, inDegree.get(next) - 1);
                if (inDegree.get(next) == 0 && !enFile.contains(next)) {
                    queue.add(next);
                    enFile.add(next);
                }
            }
        }

        ordre.add("Velizy-");

        List<String> parcours = new ArrayList<>();
        String lastVille = null;

        for (String action : ordre) {
            String ville = action.replace("+", "").replace("-", "");
            if (!ville.equals(lastVille)) {
                parcours.add(ville);
                lastVille = ville;
            }
        }

        return parcours;
    }

    private static void addEdge(Map<String, Set<String>> graph, String from, String to) {
        graph.computeIfAbsent(from, k -> new HashSet<>()).add(to);
    }
}