package modele.usage;

import modele.Vente;

import java.util.*;
/**
 * Implémentation d’un algorithme de base pour calculer un itinéraire de livraison.
 *
 * L'approche consiste à modéliser les actions de collecte et de livraison comme un graphe orienté,
 * en tenant compte des contraintes logiques (collecte avant livraison, passage par Velizy).
 * L'algorithme construit ensuite un ordre valide d’exécution à l’aide d’un tri topologique
 * (en utilisant les degrés d'entrée).
 */

public class AlgoBase {
    /**
     * Calcule un itinéraire respectant les contraintes de livraison, sans optimiser la distance.
     *
     * @param ventes Liste des ventes à honorer (chaque vente a une ville vendeur et une ville acheteur)
     * @return Liste ordonnée de villes à parcourir, en respectant l’ordre des actions
     */
    public static List<String> calculerItineraire(List<Vente> ventes) {
        Set<String> villes = new HashSet<>();
        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
                // Étape 1 : Extraction de toutes les villes impliquées
        for (Vente v : ventes) {
            villes.add(v.getVilleVendeur());
            villes.add(v.getVilleAcheteur());
        }
                // Étape 2 : Construction du graphe d'actions

        for (String ville : villes) {
            String collecte = ville + "+";
            String livraison = ville + "-";
            // Règles de base : chaque collecte précède sa livraison

            addEdge(graph, collecte, livraison);
                        // Velizy+ précède toute collecte, toute livraison précède Velizy-

            addEdge(graph, "Velizy+", collecte);
            addEdge(graph, livraison, "Velizy-");
            
            // Initialisation des degrés d’entrée

            inDegree.putIfAbsent(collecte, 0);
            inDegree.putIfAbsent(livraison, 0);
        }
        // Étape 3 : Ajout des contraintes liées aux ventes
        for (Vente v : ventes) {
            String from = v.getVilleVendeur() + "+";
            String to = v.getVilleAcheteur() + "-";
            addEdge(graph, from, to);
        }
        // Étape 4 : Calcul des degrés d’entrée pour le tri topologique
        for (String from : graph.keySet()) {
            for (String to : graph.get(from)) {
                inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
            }
        }
        // Étape 5 : Tri topologique via BFS avec file de priorité

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
         // Ajoute Velizy- comme point final

        ordre.add("Velizy-");
                // Étape 6 : Simplification de l’ordre en parcours de villes (sans répétition inutile)
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
    /**
     * Ajoute une arête orientée dans le graphe.
     *
     * @param graph Graphe représenté comme une map de sets
     * @param from  Sommet de départ
     * @param to    Sommet d’arrivée
     */

    private static void addEdge(Map<String, Set<String>> graph, String from, String to) {
        graph.computeIfAbsent(from, k -> new HashSet<>()).add(to);
    }
}
