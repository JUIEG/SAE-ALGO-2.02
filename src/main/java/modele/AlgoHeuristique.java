package modele;

import java.util.*;

public class AlgoHeuristique {

    /**
     * Calcule un itinéraire heuristique à partir des ventes et distances.
     * @param ventes liste des ventes (vendeur → acheteur)
     * @param pseudoToVille map des pseudonymes vers villes
     * @param distances matrice des distances entre villes
     * @return liste ordonnée des villes à visiter (début et fin à Vélizy)
     */
    public static List<String> calculerItineraire(List<Vente> ventes, Map<String, String> pseudoToVille, DistanceMap distances) {

        Set<String> villesARendre = new HashSet<>();
        Map<String, List<String>> dependances = new HashMap<>();
        Set<String> villesVisitees = new HashSet<>();
        List<String> parcours = new ArrayList<>();

        for (Vente v : ventes) {
            String villeVendeur = pseudoToVille.get(v.getVendeur());
            String villeAcheteur = pseudoToVille.get(v.getAcheteur());

            villesARendre.add(villeVendeur);
            villesARendre.add(villeAcheteur);

            dependances.computeIfAbsent(villeAcheteur, k -> new ArrayList<>()).add(villeVendeur);
        }

        String villeActuelle = "Vélizy";
        parcours.add(villeActuelle);
        villesVisitees.add(villeActuelle);

        while (!villesVisitees.containsAll(villesARendre)) {
            String prochain = null;
            int distanceMin = Integer.MAX_VALUE;

            for (String ville : villesARendre) {
                if (villesVisitees.contains(ville)) continue;

                boolean contrainteOK = true;
                if (dependances.containsKey(ville)) {
                    for (String vendeur : dependances.get(ville)) {
                        if (!villesVisitees.contains(vendeur)) {
                            contrainteOK = false;
                            break;
                        }
                    }
                }

                if (contrainteOK) {
                    int dist = distances.getDistance(villeActuelle, ville);
                    if (dist < distanceMin) {
                        distanceMin = dist;
                        prochain = ville;
                    }
                }
            }

            if (prochain != null) {
                parcours.add(prochain);
                villesVisitees.add(prochain);
                villeActuelle = prochain;
            } else {
                System.err.println("Erreur : blocage heuristique.");
                System.err.println("Villes restantes non atteignables :");
                for (String ville : villesARendre) {
                    if (!villesVisitees.contains(ville)) {
                        System.err.print("- " + ville);
                        if (dependances.containsKey(ville)) {
                            System.err.print(" (attend : " + dependances.get(ville) + ")");
                        }
                        System.err.println();
                    }
                }
                break;
            }

        }

        parcours.add("Vélizy");
        return parcours;
    }
}
