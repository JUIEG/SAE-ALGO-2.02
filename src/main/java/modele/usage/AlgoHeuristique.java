package modele.usage;

import modele.DistanceMap;
import modele.Vente;

import java.util.*;


/**
 * Implémente un algorithme heuristique de construction d’un itinéraire de livraison,
 * basé sur des règles de priorité ("greedy") définies par l’utilisateur.
 *
 * Le graphe encode les actions de collecte (+) et de livraison (-),
 * avec des contraintes logiques entre elles, et utilise une file de priorité
 * pour guider l’ordre d’exécution selon la méthode choisie.
 */
public class AlgoHeuristique {

    /**
     * Calcule un itinéraire respectant les contraintes de livraison,
     * guidé par une méthode heuristique.
     *
     * @param ventes         Liste des ventes à effectuer
     * @param distances      Carte des distances entre les villes
     * @param methodeGreedy  Heuristique choisie (1 à 5)
     * @return Liste ordonnée de villes à parcourir
     */
    public static List<String> calculerItineraire(List<Vente> ventes, DistanceMap distances, int methodeGreedy) {
        Set<String> villes = new HashSet<>();
        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Map<String, Integer> dependants = new HashMap<>();   // Nombre de livraisons dépendantes d’une ville
        Map<String, Integer> historique = new HashMap<>();   // Nombre de fois où une ville a été visitée

        // Extraction des villes et initialisation des dépendances
        for (Vente v : ventes) {
            villes.add(v.getVilleVendeur());
            villes.add(v.getVilleAcheteur());

            dependants.put(v.getVilleVendeur(), dependants.getOrDefault(v.getVilleVendeur(), 0) + 1);
            historique.putIfAbsent(v.getVilleVendeur(), 0);
            historique.putIfAbsent(v.getVilleAcheteur(), 0);
        }

        // Construction du graphe d’actions (collecte, livraison, Velizy)
        for (String ville : villes) {
            String plus = ville + "+";
            String moins = ville + "-";

            addEdge(graph, plus, moins);               // collecte -> livraison
            addEdge(graph, "Velizy+", plus);           // départ depuis Velizy
            addEdge(graph, moins, "Velizy-");          // retour à Velizy

            inDegree.putIfAbsent(plus, 0);
            inDegree.putIfAbsent(moins, 0);
        }

        // Contraintes entre les ventes (vendeur -> acheteur)
        for (Vente v : ventes) {
            String from = v.getVilleVendeur() + "+";
            String to = v.getVilleAcheteur() + "-";
            addEdge(graph, from, to);
        }

        // Calcul des degrés d’entrée (préparation au tri topologique)
        for (String from : graph.keySet()) {
            for (String to : graph.get(from)) {
                inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
            }
        }

        // Tri topologique guidé par une heuristique
        List<String> ordre = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        PriorityQueue<String> queue = new PriorityQueue<>();
        queue.add("Velizy+");

        boolean phaseLivraison = false;
        String villeActuelle = "Velizy";

        while (!queue.isEmpty()) {
            // On filtre les actions disponibles selon la phase (collecte/livraison)
            List<String> disponibles = new ArrayList<>();
            for (String action : queue) {
                if (visited.contains(action)) continue;
                if (!phaseLivraison && action.endsWith("+")) {
                    disponibles.add(action);
                } else if (phaseLivraison && action.endsWith("-")) {
                    disponibles.add(action);
                }
            }

            // Si plus rien à collecter, on passe à la phase livraison
            if (disponibles.isEmpty() && !phaseLivraison) {
                phaseLivraison = true;
                continue;
            }

            // Choix de la meilleure action selon la méthode greedy
            String next = choisirAction(disponibles, villeActuelle, distances, methodeGreedy, dependants, historique);
            if (next == null) break;

            queue.remove(next);
            visited.add(next);
            ordre.add(next);

            villeActuelle = next.replace("+", "").replace("-", "");
            historique.put(villeActuelle, historique.getOrDefault(villeActuelle, 0) + 1);

            // Ajout des successeurs accessibles
            for (String succ : graph.getOrDefault(next, Set.of())) {
                inDegree.put(succ, inDegree.get(succ) - 1);
                if (inDegree.get(succ) == 0 && !visited.contains(succ)) {
                    queue.add(succ);
                }
            }
        }

        // Ajout du point final
        ordre.add("Velizy-");

        // Conversion des actions vers une liste de villes sans répétition inutile
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
     * Choisit la meilleure action parmi celles disponibles selon une méthode heuristique.
     *
     * @param actions      Liste d’actions possibles (collectes ou livraisons)
     * @param depuisVille  Ville actuelle
     * @param distances    Carte des distances
     * @param methode      Numéro de l’heuristique
     * @param dependants   Carte du nombre de dépendants pour chaque ville
     * @param historique   Historique de visite des villes
     * @return Action sélectionnée
     */
    private static String choisirAction(List<String> actions, String depuisVille, DistanceMap distances, int methode,
                                        Map<String, Integer> dependants, Map<String, Integer> historique) {

        return actions.stream().min((a1, a2) -> {
            String v1 = a1.replace("+", "").replace("-", "");
            String v2 = a2.replace("+", "").replace("-", "");

            switch (methode) {
                case 1: // Distance minimale
                    return Integer.compare(distances.getDistance(depuisVille, v1), distances.getDistance(depuisVille, v2));
                case 2: // Distance maximale
                    return Integer.compare(distances.getDistance(depuisVille, v2), distances.getDistance(depuisVille, v1));
                case 3: // Ordre alphabétique
                    return v1.compareTo(v2);
                case 4: // Ville avec le plus de dépendants
                    return Integer.compare(dependants.getOrDefault(v2, 0), dependants.getOrDefault(v1, 0));
                case 5: // Ville la moins visitée
                    return Integer.compare(historique.getOrDefault(v1, 0), historique.getOrDefault(v2, 0));
                default: // Par défaut : distance minimale
                    return Integer.compare(distances.getDistance(depuisVille, v1), distances.getDistance(depuisVille, v2));
            }
        }).orElse(null);
    }

    /**
     * Ajoute une arête orientée dans le graphe.
     *
     * @param graph Graphe de dépendance
     * @param from  Sommet de départ
     * @param to    Sommet d’arrivée
     */
    private static void addEdge(Map<String, Set<String>> graph, String from, String to) {
        graph.computeIfAbsent(from, k -> new HashSet<>()).add(to);
    }
}
