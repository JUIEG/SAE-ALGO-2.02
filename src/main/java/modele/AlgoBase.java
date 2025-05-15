package modele;

import java.util.*;

public class AlgoBase {

    public static List<String> calculerItineraire(List<Vente> ventes, Map<String, String> pseudoToVille) {
        Set<String> actions = new HashSet<>();
        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Integer> inDegrees = new HashMap<>();

        Set<String> villesImpliquées = new HashSet<>();
        for (Vente v : ventes) {
            villesImpliquées.add(pseudoToVille.get(v.getVendeur()));
            villesImpliquées.add(pseudoToVille.get(v.getAcheteur()));
        }

        // Génère toutes les actions A+ et A-
        for (String ville : villesImpliquées) {
            actions.add(ville + "+");
            actions.add(ville + "-");
        }

        // Contraintes de base
        for (String ville : villesImpliquées) {
            addEdge(graph, "Velizy+", ville + "+");
            addEdge(graph, ville + "+", ville + "-");
            addEdge(graph, ville + "-", "Velizy-");
        }

        // Contraintes liées aux ventes
        for (Vente v : ventes) {
            String vendeur = pseudoToVille.get(v.getVendeur());
            String acheteur = pseudoToVille.get(v.getAcheteur());
            addEdge(graph, vendeur + "+", acheteur + "-");
        }

        // Calcul des degrés entrants
        for (String action : graph.keySet()) {
            inDegrees.putIfAbsent(action, 0);
            for (String cible : graph.get(action)) {
                inDegrees.put(cible, inDegrees.getOrDefault(cible, 0) + 1);
            }
        }

        // Tri topologique (Kahn)
        Queue<String> file = new LinkedList<>();
        List<String> ordre = new ArrayList<>();

        for (String action : actions) {
            if (inDegrees.getOrDefault(action, 0) == 0) {
                file.add(action);
            }
        }
        file.add("Velizy+");

        Set<String> vus = new HashSet<>();
        while (!file.isEmpty()) {
            String current = file.poll();
            if (vus.contains(current)) continue;
            vus.add(current);
            ordre.add(current);

            for (String voisin : graph.getOrDefault(current, new HashSet<>())) {
                inDegrees.put(voisin, inDegrees.get(voisin) - 1);
                if (inDegrees.get(voisin) == 0) {
                    file.add(voisin);
                }
            }
        }

        // Génère la liste des villes à visiter dans l’ordre
        List<String> parcours = new ArrayList<>();
        String derniereVille = null;
        for (String action : ordre) {
            if (action.equals("Velizy+")) {
                parcours.add("Velizy");
                derniereVille = "Velizy";
            } else if (action.equals("Velizy-")) {
                if (!"Velizy".equals(derniereVille)) {
                    parcours.add("Velizy");
                }
            } else {
                String ville = action.substring(0, action.length() - 1);
                if (!ville.equals(derniereVille)) {
                    parcours.add(ville);
                    derniereVille = ville;
                }
            }
        }

        return parcours;
    }

    private static void addEdge(Map<String, Set<String>> graph, String from, String to) {
        graph.computeIfAbsent(from, k -> new HashSet<>()).add(to);
    }
}
