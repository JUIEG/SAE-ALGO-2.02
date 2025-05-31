package modele;

import java.util.*;

public class AlgoKpossibilite {

    /**
     * Calcule les k meilleurs parcours valides respectant les contraintes vendeur → acheteur.
     *
     * @param ventes         liste des ventes (vendeur → acheteur)
     * @param pseudoToVille  correspondance pseudo → ville
     * @param distances      distances entre villes
     * @param k              nombre de parcours à conserver
     * @return liste des k meilleurs parcours (les plus courts en distance)
     */
    public static List<List<String>> trouverKParcours(List<Vente> ventes, Map<String, String> pseudoToVille, DistanceMap distances, int k) {
        Set<String> villes = new HashSet<>();
        List<Vente> contraintes = new ArrayList<>();
        for (Vente v : ventes) {
            String vendeur = pseudoToVille.get(v.getVilleVendeur());
            String acheteur = pseudoToVille.get(v.getVilleAcheteur());
            villes.add(vendeur);
            villes.add(acheteur);
            contraintes.add(new Vente(vendeur, acheteur));
        }

        PriorityQueue<Trajet> meilleurs = new PriorityQueue<>(Comparator.comparingInt(t -> -t.distance)); // max-heap inversé
        Set<String> visited = new HashSet<>();

        dfs("Velizy", new ArrayList<>(List.of("Velizy")), villes, contraintes, visited, 0, distances, meilleurs, k);

        List<List<String>> resultats = new ArrayList<>();
        while (!meilleurs.isEmpty()) {
            resultats.add(0, meilleurs.poll().chemin); // insertion inverse pour obtenir du plus court au plus long
        }
        return resultats;
    }

    private static void dfs(String villeActuelle,
                            List<String> chemin,
                            Set<String> toutesVilles,
                            List<Vente> contraintes,
                            Set<String> visitees,
                            int distanceActuelle,
                            DistanceMap distances,
                            PriorityQueue<Trajet> meilleurs,
                            int k) {

        if (visitees.containsAll(toutesVilles)) {
            // Toutes les villes sont couvertes, vérifions les contraintes
            Set<String> dejaPassees = new HashSet<>(chemin);
            boolean ok = true;
            for (Vente v : contraintes) {
                if (chemin.indexOf(v.getVilleVendeur()) > chemin.indexOf(v.getVilleAcheteur())) {
                    ok = false;
                    break;
                }
            }

            if (ok) {
                // Retour à Velizy
                int retour = distances.getDistance(villeActuelle, "Velizy");
                List<String> complet = new ArrayList<>(chemin);
                complet.add("Velizy");
                int total = distanceActuelle + retour;

                meilleurs.add(new Trajet(complet, total));
                if (meilleurs.size() > k) {
                    meilleurs.poll(); // retire le plus mauvais
                }
            }
            return;
        }

        for (String ville : toutesVilles) {
            if (!ville.equals(villeActuelle)) {
                boolean admissible = true;
                for (Vente v : contraintes) {
                    if (v.getVilleAcheteur().equals(ville) && !visitees.contains(v.getVilleVendeur())) {
                        admissible = false;
                        break;
                    }
                }

                if (!visitees.contains(ville) && admissible) {
                    int d = distances.getDistance(villeActuelle, ville);
                    visitees.add(ville);
                    chemin.add(ville);

                    dfs(ville, chemin, toutesVilles, contraintes, visitees, distanceActuelle + d, distances, meilleurs, k);

                    chemin.remove(chemin.size() - 1);
                    visitees.remove(ville);
                }
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