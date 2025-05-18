package modele;

import java.util.*;

public class AlgoBase {

    public static List<String> calculerItineraire(List<Vente> ventes, Map<String, String> pseudoToVille) {
        Set<String> villes = new HashSet<>();
        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        // Étape 1 : récupérer toutes les villes impliquées
        for (Vente v : ventes) {
            String vendeur = pseudoToVille.get(v.getVendeur());
            String acheteur = pseudoToVille.get(v.getAcheteur());
            villes.add(vendeur);
            villes.add(acheteur);
        }

        // Étape 2 : construire les nœuds A+ et A- pour chaque ville
        for (String ville : villes) {
            String collect = ville + "+";
            String deliver = ville + "-";

            addEdge(graph, collect, deliver);
            addEdge(graph, "Velizy+", collect);
            addEdge(graph, deliver, "Velizy-");

            inDegree.putIfAbsent(collect, 0);
            inDegree.putIfAbsent(deliver, 0);
        }

        // Étape 3 : ajouter les contraintes des ventes
        for (Vente v : ventes) {
            String vendeur = pseudoToVille.get(v.getVendeur()) + "+";
            String acheteur = pseudoToVille.get(v.getAcheteur()) + "-";
            addEdge(graph, vendeur, acheteur);
        }

        // Étape 4 : calcul des degrés entrants
        for (String from : graph.keySet()) {
            for (String to : graph.get(from)) {
                inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
            }
        }

        // Étape 5 : tri topologique hybride FIFO + ordre alphabétique
        List<String> ordre = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Map<String, Integer> insertionIndex = new HashMap<>();
        int index = 0;

        PriorityQueue<ActionPrioritaire> queue = new PriorityQueue<>();
        insertionIndex.put("Velizy+", index);
        queue.add(new ActionPrioritaire("Velizy+", index++));

        while (!queue.isEmpty()) {
            ActionPrioritaire currentAction = queue.poll();
            String current = currentAction.nom;

            if (visited.contains(current)) continue;
            visited.add(current);
            ordre.add(current);

            for (String next : graph.getOrDefault(current, Set.of())) {
                inDegree.put(next, inDegree.get(next) - 1);
                if (inDegree.get(next) == 0 && !insertionIndex.containsKey(next)) {
                    insertionIndex.put(next, index);
                    queue.add(new ActionPrioritaire(next, index++));
                }
            }
        }

        ordre.add("Velizy-");

        // Étape 6 : extraire la séquence de villes (sans doublons inutiles)
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

    private static class ActionPrioritaire implements Comparable<ActionPrioritaire> {
        String nom;
        int ordreArrivee;

        public ActionPrioritaire(String nom, int ordreArrivee) {
            this.nom = nom;
            this.ordreArrivee = ordreArrivee;
        }

        @Override
        public int compareTo(ActionPrioritaire other) {
            if (this.ordreArrivee != other.ordreArrivee) {
                return Integer.compare(this.ordreArrivee, other.ordreArrivee);
            }
            return this.nom.compareTo(other.nom);  // ordre alphabétique
        }
    }
}