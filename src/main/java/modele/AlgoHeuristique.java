package modele;

import java.util.*;

public class AlgoHeuristique {

    public static List<String> calculerItineraire(List<Vente> ventes, DistanceMap distances, int methodeGreedy) {
        Set<String> villes = new HashSet<>();
        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Map<String, Integer> dependants = new HashMap<>();
        Map<String, Integer> historique = new HashMap<>();

        for (Vente v : ventes) {
            villes.add(v.getVilleVendeur());
            villes.add(v.getVilleAcheteur());

            dependants.put(v.getVilleVendeur(), dependants.getOrDefault(v.getVilleVendeur(), 0) + 1);
            historique.putIfAbsent(v.getVilleVendeur(), 0);
            historique.putIfAbsent(v.getVilleAcheteur(), 0);
        }

        for (String ville : villes) {
            String plus = ville + "+";
            String moins = ville + "-";
            addEdge(graph, plus, moins);
            addEdge(graph, "Velizy+", plus);
            addEdge(graph, moins, "Velizy-");

            inDegree.putIfAbsent(plus, 0);
            inDegree.putIfAbsent(moins, 0);
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

        List<String> ordre = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        PriorityQueue<String> queue = new PriorityQueue<>();
        queue.add("Velizy+");

        boolean phaseLivraison = false;
        String villeActuelle = "Velizy";

        while (!queue.isEmpty()) {
            List<String> disponibles = new ArrayList<>();
            for (String action : queue) {
                if (visited.contains(action)) continue;
                if (!phaseLivraison && action.endsWith("+")) disponibles.add(action);
                else if (phaseLivraison && action.endsWith("-")) disponibles.add(action);
            }

            if (disponibles.isEmpty() && !phaseLivraison) {
                phaseLivraison = true;
                continue;
            }

            String next = choisirAction(disponibles, villeActuelle, distances, methodeGreedy, dependants, historique);
            if (next == null) break;

            queue.remove(next);
            visited.add(next);
            ordre.add(next);

            villeActuelle = next.replace("+", "").replace("-", "");
            historique.put(villeActuelle, historique.getOrDefault(villeActuelle, 0) + 1);

            for (String succ : graph.getOrDefault(next, Set.of())) {
                inDegree.put(succ, inDegree.get(succ) - 1);
                if (inDegree.get(succ) == 0 && !visited.contains(succ)) {
                    queue.add(succ);
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

    private static String choisirAction(List<String> actions, String depuisVille, DistanceMap distances, int methode,
                                        Map<String, Integer> dependants, Map<String, Integer> historique) {

        return actions.stream().min((a1, a2) -> {
            String v1 = a1.replace("+", "").replace("-", "");
            String v2 = a2.replace("+", "").replace("-", "");

            switch (methode) {
                case 1: return Integer.compare(distances.getDistance(depuisVille, v1), distances.getDistance(depuisVille, v2));
                case 2: return Integer.compare(distances.getDistance(depuisVille, v2), distances.getDistance(depuisVille, v1));
                case 3: return v1.compareTo(v2);
                case 4: return Integer.compare(dependants.getOrDefault(v2, 0), dependants.getOrDefault(v1, 0));
                case 5: return Integer.compare(historique.getOrDefault(v1, 0), historique.getOrDefault(v2, 0));
                default: return Integer.compare(distances.getDistance(depuisVille, v1), distances.getDistance(depuisVille, v2));
            }
        }).orElse(null);
    }

    private static void addEdge(Map<String, Set<String>> graph, String from, String to) {
        graph.computeIfAbsent(from, k -> new HashSet<>()).add(to);
    }
}