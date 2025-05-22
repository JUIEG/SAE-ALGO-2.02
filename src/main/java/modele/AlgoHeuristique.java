package modele;

import java.util.*;

public class AlgoHeuristique {

    /**
     * Calcule un itinéraire heuristique respectant les contraintes vendeur → acheteur
     * à l’aide d’un tri topologique guidé par une méthode greedy.
     *
     * @param ventes         liste des ventes (vendeur → acheteur)
     * @param pseudoToVille  map des pseudonymes vers villes
     * @param distances      matrice des distances entre villes
     * @param methodeGreedy  numéro de la méthode greedy (1 à 5)
     * @return liste ordonnée des villes à visiter
     */
    public static List<String> calculerItineraire(List<Vente> ventes, Map<String, String> pseudoToVille, DistanceMap distances, int methodeGreedy) {
        Set<String> villes = new HashSet<>();
        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        Map<String, Integer> dependants = new HashMap<>();
        Map<String, Integer> historique = new HashMap<>();

        // Récupération des villes impliquées
        for (Vente v : ventes) {
            String vendeur = pseudoToVille.get(v.getVendeur());
            String acheteur = pseudoToVille.get(v.getAcheteur());
            villes.add(vendeur);
            villes.add(acheteur);

            dependants.put(vendeur, dependants.getOrDefault(vendeur, 0) + 1);
            historique.putIfAbsent(vendeur, 0);
            historique.putIfAbsent(acheteur, 0);
        }

        // Construction du graphe (nœuds A+ et A-)
        for (String ville : villes) {
            String plus = ville + "+";
            String moins = ville + "-";

            addEdge(graph, plus, moins);         // Collecte avant livraison
            addEdge(graph, "Velizy+", plus);     // Tout commence après Velizy+
            addEdge(graph, moins, "Velizy-");    // Tout finit avant Velizy-

            inDegree.putIfAbsent(plus, 0);
            inDegree.putIfAbsent(moins, 0);
        }

        // Contraintes de ventes (vendeur+ → acheteur-)
        for (Vente v : ventes) {
            String vendeur = pseudoToVille.get(v.getVendeur()) + "+";
            String acheteur = pseudoToVille.get(v.getAcheteur()) + "-";
            addEdge(graph, vendeur, acheteur);
        }

        // Calcul des degrés entrants
        for (String from : graph.keySet()) {
            for (String to : graph.get(from)) {
                inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
            }
        }

        // Tri topologique avec stratégie greedy
        List<String> ordre = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> enFile = new HashSet<>();

        PriorityQueue<String> queue = new PriorityQueue<>();
        queue.add("Velizy+");
        enFile.add("Velizy+");

        String villeActuelle = "Velizy";

        while (!queue.isEmpty()) {
            // Sélection des actions admissibles
            List<String> disponibles = new ArrayList<>();
            for (String action : queue) {
                if (!visited.contains(action)) {
                    disponibles.add(action);
                }
            }

            String next = choisirAction(disponibles, villeActuelle, distances, methodeGreedy, dependants, historique);
            if (next == null) break;

            queue.remove(next);
            visited.add(next);
            ordre.add(next);

            // Mise à jour du contexte
            villeActuelle = next.replace("+", "").replace("-", "");
            historique.put(villeActuelle, historique.getOrDefault(villeActuelle, 0) + 1);

            for (String succ : graph.getOrDefault(next, Set.of())) {
                inDegree.put(succ, inDegree.get(succ) - 1);
                if (inDegree.get(succ) == 0 && !visited.contains(succ) && !enFile.contains(succ)) {
                    queue.add(succ);
                    enFile.add(succ);
                }
            }
        }

        ordre.add("Velizy-");

        // Nettoyage final du parcours : extraire les villes dans l'ordre d'apparition
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
                case 1: // Plus proche
                    return Integer.compare(distances.getDistance(depuisVille, v1), distances.getDistance(depuisVille, v2));
                case 2: // Plus éloignée
                    return Integer.compare(distances.getDistance(depuisVille, v2), distances.getDistance(depuisVille, v1));
                case 3: // Alphabétique
                    return v1.compareTo(v2);
                case 4: // Plus de dépendants
                    return Integer.compare(dependants.getOrDefault(v2, 0), dependants.getOrDefault(v1, 0));
                case 5: // Moins visitée
                    return Integer.compare(historique.getOrDefault(v1, 0), historique.getOrDefault(v2, 0));
                default: // Par défaut la plus proche est utilisé
                    return Integer.compare(distances.getDistance(depuisVille, v1), distances.getDistance(depuisVille, v2));
            }
        }).orElse(null);
    }

    private static void addEdge(Map<String, Set<String>> graph, String from, String to) {
        graph.computeIfAbsent(from, k -> new HashSet<>()).add(to);
    }
}