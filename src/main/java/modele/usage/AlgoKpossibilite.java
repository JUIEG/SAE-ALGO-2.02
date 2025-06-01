package modele.usage;

import modele.DistanceMap;
import modele.Vente;

import java.util.*;

public class AlgoKpossibilite {

    /**
     * Calcule les k meilleurs parcours valides.
     *
     * @param ventes     liste des ventes (ville vendeur → ville acheteur)
     * @param distances  distances entre villes
     * @param k          nombre de parcours à conserver
     * @return les k meilleurs parcours
     */
    public static List<List<String>> trouverKParcours(List<Vente> ventes, DistanceMap distances, int k) {
        Set<String> villes = new HashSet<>();
        for (Vente v : ventes) {
            villes.add(v.getVilleVendeur());
            villes.add(v.getVilleAcheteur());
        }
        villes.add("Velizy");

        PriorityQueue<Trajet> meilleurs = new PriorityQueue<>(Comparator.comparingInt(t -> -t.distance));
        dfs("Velizy", new ArrayList<>(List.of("Velizy")), villes, ventes, new HashSet<>(Set.of("Velizy")), 0, distances, meilleurs, k);

        List<List<String>> resultats = new ArrayList<>();
        if (meilleurs.isEmpty()) {System.out.println("Meilleurs est Vide");}
        while (!meilleurs.isEmpty()) {
            resultats.add(0, meilleurs.poll().chemin);
        }

        if (resultats.isEmpty()) {
            System.out.println("❌ Aucun parcours valide trouvé. (Algo)");
        } else {
            System.out.println("✅ " + resultats.size() + " parcours valides trouvés.");
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

        if (visitees.size() == toutesVilles.size()) {
            int retour = distances.getDistance(villeActuelle, "Velizy");
            if (retour == Integer.MAX_VALUE) return;

            List<String> complet = new ArrayList<>(chemin);
            complet.add("Velizy");
            int total = distanceActuelle + retour;

            if (respecteContraintes(complet, contraintes)) {
                meilleurs.add(new Trajet(complet, total));
                if (meilleurs.size() > k) meilleurs.poll();
            }
            return;
        }

        for (String ville : toutesVilles) {
            if (!ville.equals(villeActuelle) && !visitees.contains(ville)) {
                int d = distances.getDistance(villeActuelle, ville);
                if (d == Integer.MAX_VALUE) continue;

                int estimationTotale = distanceActuelle + d + 100;
                if (meilleurs.size() >= k && estimationTotale >= meilleurs.peek().distance) {
                    continue;
                }

                visitees.add(ville);
                chemin.add(ville);

                dfs(ville, chemin, toutesVilles, contraintes, visitees, distanceActuelle + d, distances, meilleurs, k);

                chemin.remove(chemin.size() - 1);
                visitees.remove(ville);
            }
        }
    }

    private static boolean respecteContraintes(List<String> chemin, List<Vente> contraintes) {
        Map<String, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < chemin.size(); i++) {
            indexMap.put(chemin.get(i), i);
        }

        for (Vente v : contraintes) {
            Integer idxVendeur = indexMap.get(v.getVilleVendeur());
            Integer idxAcheteur = indexMap.get(v.getVilleAcheteur());
            if (idxVendeur == null || idxAcheteur == null || idxVendeur > idxAcheteur) {
                return false;
            }
        }
        return true;
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