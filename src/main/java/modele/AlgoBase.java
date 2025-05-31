package modele;

import java.util.*;

public class AlgoBase {

    public static List<String> calculerItineraire(List<Vente> ventes) {
        Set<String> villes = new HashSet<>();
        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        for (Vente v : ventes) {
            villes.add(v.getVilleVendeur());
            villes.add(v.getVilleAcheteur());
        }

        for (String ville : villes) {
            String collecte = ville + "+";
            String livraison = ville + "-";
            addEdge(graph, collecte, livraison);
            addEdge(graph, "Velizy+", collecte);
            addEdge(graph, livraison, "Velizy-");

            inDegree.putIfAbsent(collecte, 0);
            inDegree.putIfAbsent(livraison, 0);
        }

        for (Vente v : ventes) {
            String from = v.getVilleVendeur() + "+";
            String to = v.getVilleAcheteur() + "-";
            addEdge(graph, from, to);
        }

        for (String from : graph.keySet()) {
            for (String to : graph.get(from)) {
                inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
            }
        }

        Queue<String> queue = new PriorityQueue<>();
        queue.add("Velizy+");

        List<String> ordre = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (visited.contains(current)) continue;
            visited.add(current);
            ordre.add(current);

            for (String next : graph.getOrDefault(current, Set.of())) {
                inDegree.put(next, inDegree.get(next) - 1);
                if (inDegree.get(next) == 0 && !visited.contains(next)) {
                    queue.add(next);
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
