package modele;

import java.util.*;public class AlgoKpossibilite {

    /**
     * Calcule les k meilleurs parcours valides respectant les contraintes vendeur → acheteur.
     *
     * @param ventes        liste des ventes (vendeur → acheteur)
     * @param distances     distances entre villes
     * @param k             nombre de parcours à conserver
     * @return liste des k meilleurs parcours (les plus courts en distance)
     */
    public static List<List<String>> trouverKParcours(List<Vente> ventes, DistanceMap distances, int k) {
        Set<String> villes = new HashSet<>();
        villes.add("Velizy");

        List<Vente> contraintes = new ArrayList<>();
        for (Vente v : ventes) {
            String vendeur = v.getVilleVendeur();
            String acheteur = v.getVilleAcheteur();
            villes.add(vendeur);
            villes.add(acheteur);
            contraintes.add(new Vente(vendeur, acheteur));
        }

        System.out.println("Toutes les villes à visiter : " + villes);

        PriorityQueue<Trajet> meilleurs = new PriorityQueue<>(Comparator.comparingInt(t -> -t.distance)); // max-heap inversé
        Set<String> visited = new HashSet<>();
        visited.add("Velizy");

        dfs("Velizy", new ArrayList<>(List.of("Velizy")), villes, contraintes, visited, 0, distances, meilleurs, k);

        List<List<String>> resultats = new ArrayList<>();
        while (!meilleurs.isEmpty()) {
            resultats.add(0, meilleurs.poll().chemin);
        }

        if (resultats.isEmpty()) {
            System.out.println(" Aucun parcours valide trouvé.");
        } else {
            System.out.println(resultats.size() + " parcours valides trouvés.");
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

        // Cas de base : toutes les villes ont été visitées
        if (visitees.size() == toutesVilles.size()) {
            boolean contraintesRespectees = true;
            for (Vente v : contraintes) {
                int vendeurIdx = chemin.indexOf(v.getVilleVendeur());
                int acheteurIdx = chemin.indexOf(v.getVilleAcheteur());

                if (vendeurIdx == -1 || acheteurIdx == -1) {
                    contraintesRespectees = false;
                    System.out.println("❌ Ville absente dans chemin : " + v);
                    break;
                }

                if (vendeurIdx > acheteurIdx) {
                    contraintesRespectees = false;
                    System.out.println("❌ Contrainte violée : " + v.getVilleVendeur() + " → " + v.getVilleAcheteur());
                    break;
                }
            }

            if (contraintesRespectees) {
                int retour = distances.getDistance(villeActuelle, "Velizy");
                if (retour == Integer.MAX_VALUE) {
                    System.out.println("❌ Pas de retour possible à Velizy depuis " + villeActuelle);
                    return;
                }

                List<String> complet = new ArrayList<>(chemin);
                complet.add("Velizy");
                int total = distanceActuelle + retour;

                System.out.println("✅ Parcours valide trouvé : " + complet + " distance totale = " + total);

                meilleurs.add(new Trajet(complet, total));
                if (meilleurs.size() > k) {
                    meilleurs.poll(); // on garde les k meilleurs
                }
            }
            return;
        }

        // 🔁 Explorer les autres villes
        for (String ville : toutesVilles) {
            if (ville != null && !ville.equals(villeActuelle) && !visitees.contains(ville)) {

                // ✅ Vérifie admissibilité (toutes les contraintes vendeur → acheteur respectées)
                boolean admissible = true;
                for (Vente v : contraintes) {
                    if (v.getVilleAcheteur().equals(ville) && !visitees.contains(v.getVilleVendeur())) {
                        System.out.println("⚠️ " + ville + " est acheteur mais vendeur " + v.getVilleVendeur() + " pas encore visité.");
                        admissible = false;
                        break;
                    }
                }

                System.out.println("Ville candidate : " + ville + ", admissible : " + admissible);

                if (admissible) {
                    int d = distances.getDistance(villeActuelle, ville);
                    if (d == Integer.MAX_VALUE) {
                        System.out.println("⚠️ Distance inconnue entre " + villeActuelle + " et " + ville + ", saut...");
                        continue;
                    }

                    System.out.println("🔁 Avancer vers : " + ville + " depuis " + villeActuelle);

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
