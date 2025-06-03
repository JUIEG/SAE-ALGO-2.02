package modele.usage;

import modele.DistanceMap;
import modele.Vente;

import java.util.*;

public class AlgoKpossibilite {

    public static List<List<String>> trouverKParcours(List<Vente> ventes, DistanceMap distances, int k) {
        Set<String> villes = new HashSet<>();
        for (Vente v : ventes) {
            villes.add(v.getVilleVendeur());
            villes.add(v.getVilleAcheteur());
        }

        PriorityQueue<Trajet> meilleurs = new PriorityQueue<>(Comparator.comparingInt(t -> -t.distance));
        List<String> chemin = new ArrayList<>();
        chemin.add("Velizy");

        Set<String> visitees = new HashSet<>();
        visitees.add("Velizy");

        dfs("Velizy", chemin, villes, visitees, 0, distances, meilleurs, k);

        List<List<String>> resultats = new ArrayList<>();
        while (!meilleurs.isEmpty()) {
            resultats.add(0, meilleurs.poll().chemin);
        }

        if (resultats.isEmpty()) {
            System.out.println("Aucun parcours valide trouvé.");
        } else {
            System.out.println(resultats.size() + " parcours valides trouvés.");
        }

        return resultats;
    }

    private static void dfs(String villeActuelle,
                            List<String> chemin,
                            Set<String> villesAVisiter,
                            Set<String> visitees,
                            int distanceActuelle,
                            DistanceMap distances,
                            PriorityQueue<Trajet> meilleurs,
                            int k) {

        if (visitees.containsAll(villesAVisiter)) {
            int retour = distances.getDistance(villeActuelle, "Velizy");
            if (retour == Integer.MAX_VALUE) return;

            List<String> complet = new ArrayList<>(chemin);
            complet.add("Velizy");
            int total = distanceActuelle + retour;

            meilleurs.add(new Trajet(complet, total));
            if (meilleurs.size() > k) meilleurs.poll(); // supprime le plus long
            return;
        }

        for (String ville : villesAVisiter) {
            if (!visitees.contains(ville)) {
                int d = distances.getDistance(villeActuelle, ville);
                if (d == Integer.MAX_VALUE) continue;

                int nouvelleDistance = distanceActuelle + d;

                // Élagage si déjà trop long
                if (meilleurs.size() >= k && nouvelleDistance >= meilleurs.peek().distance) {
                    continue;
                }

                chemin.add(ville);
                visitees.add(ville);

                dfs(ville, chemin, villesAVisiter, visitees, nouvelleDistance, distances, meilleurs, k);

                chemin.remove(chemin.size() - 1);
                visitees.remove(ville);
            }
        }
    }

    private static class Trajet {
        List<String> chemin;
        int distance;

        Trajet(List<String> chemin, int distance) {
            this.chemin = chemin;
            this.distance = distance;
        }
    }
}