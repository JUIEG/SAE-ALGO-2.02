package modele;

import java.util.*;

public class AlgoBase {

    /**
     * Calcule un itinéraire de base (pas forcément optimal),
     * respectant simplement les contraintes vendeur → acheteur.
     */
    public static List<String> calculerItineraire(List<Vente> ventes,
                                                  Map<String, String> pseudoToVille) {

        Set<String> dejaVisitees = new HashSet<>();
        List<String> parcours = new ArrayList<>();

        parcours.add("Vélizy");
        dejaVisitees.add("Vélizy");

        for (Vente v : ventes) {
            String villeVendeur = pseudoToVille.get(v.getVendeur());
            String villeAcheteur = pseudoToVille.get(v.getAcheteur());

            if (!dejaVisitees.contains(villeVendeur)) {
                parcours.add(villeVendeur);
                dejaVisitees.add(villeVendeur);
            }
            if (!dejaVisitees.contains(villeAcheteur)) {
                parcours.add(villeAcheteur);
                dejaVisitees.add(villeAcheteur);
            }
        }

        parcours.add("Vélizy");
        return parcours;
    }
}
