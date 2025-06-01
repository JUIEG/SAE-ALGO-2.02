package modele;



/**
 * Cette classe implémente un algorithme de recherche en profondeur (DFS)
 * pour trouver les K meilleurs itinéraires valides selon les contraintes
 * de livraison (un acheteur ne peut être visité avant son vendeur).
 */
public class AlgoKpossibilite {

    /**
     * Trouve les K parcours les plus courts en respectant les contraintes vendeur→acheteur.
     *
     * @param ventes   Liste des ventes à effectuer (avec contraintes vendeur→acheteur)
     * @param distances Carte des distances entre villes
     * @param k         Nombre de parcours valides à retourner
     * @return Liste des k meilleurs parcours, chacun étant une liste ordonnée de villes
     */
    public static List<List<String>> trouverKParcours(List<Vente> ventes, DistanceMap distances, int k) {
        Set<String> villes = new HashSet<>();
        villes.add("Velizy"); // point de départ et de retour

        List<Vente> contraintes = new ArrayList<>();
        for (Vente v : ventes) {
            villes.add(v.getVilleVendeur());
            villes.add(v.getVilleAcheteur());
            contraintes.add(new Vente(v.getVilleVendeur(), v.getVilleAcheteur()));
        }

        //stocke les k meilleurs trajets avec plus petite distance en dernier
        PriorityQueue<Trajet> meilleurs = new PriorityQueue<>(Comparator.comparingInt(t -> -t.distance));

        List<String> chemin = new ArrayList<>();
        chemin.add("Velizy");

        Set<String> restantes = new HashSet<>(villes);
        restantes.remove("Velizy");

        // Lancement de la DFS récursive
        dfs("Velizy", chemin, restantes, contraintes, 0, distances, meilleurs, k);

        // Récupération des résultats en ordre croissant de distance
        List<List<String>> resultats = new ArrayList<>();
        while (!meilleurs.isEmpty()) {
            resultats.add(0, (List<String>) meilleurs.poll()); // les meilleurs en tête
        }

        if (resultats.isEmpty()) {
            System.out.println(" Aucun parcours valide trouvé.");
        } else {
            System.out.println( resultats.size() + " parcours valides trouvés.");
        }

        return resultats;
    }

    /**
     * Fonction récursive de parcours DFS avec backtracking et contraintes.
     *
     * @param villeActuelle      Ville actuelle dans le parcours
     * @param chemin             Chemin actuel construit
     * @param restantes          Villes restantes à visiter
     * @param contraintes        Contraintes de type vendeur→acheteur
     * @param distanceActuelle   Distance déjà parcourue
     * @param distances          Carte des distances
     * @param meilleurs          Max-heap contenant les k meilleurs trajets
     * @param k                  Nombre maximal de trajets à conserver
     */
    private static void dfs(String villeActuelle,
                            List<String> chemin,
                            Set<String> restantes,
                            List<Vente> contraintes,
                            int distanceActuelle,
                            DistanceMap distances,
                            PriorityQueue<Trajet> meilleurs,
                            int k) {

        // Si toutes les villes ont été visitées
        if (restantes.isEmpty()) {
            int retour = distances.getDistance(villeActuelle, "Velizy");
            if (retour == Integer.MAX_VALUE) return; // pas de chemin de retour

            List<String> complet = new ArrayList<>(chemin);
            complet.add("Velizy");
            int total = distanceActuelle + retour;

            meilleurs.add(new Trajet(complet, total));
            if (meilleurs.size() > k) {
                meilleurs.poll(); // enlève le pire trajet si on dépasse k
            }

            System.out.println(" Parcours valide : " + complet + " (distance : " + total + ")");
            return;
        }

        for (String suivante : new HashSet<>(restantes)) {
            if (!distances.contains(villeActuelle, suivante)) continue;

            if (!estAdmissible(suivante, chemin, contraintes)) {
                System.out.println("Ville candidate " + suivante + " rejetée (contraintes non respectées).");
                continue;
            }

            chemin.add(suivante);
            restantes.remove(suivante);

            int nouvelleDistance = distanceActuelle + distances.getDistance(villeActuelle, suivante);
            dfs(suivante, chemin, restantes, contraintes, nouvelleDistance, distances, meilleurs, k);

            chemin.remove(chemin.size() - 1);
            restantes.add(suivante);
        }
    }

    /**
     * Vérifie si la ville candidate peut être visitée à ce moment du parcours,
     * en s’assurant que les contraintes vendeur→acheteur sont respectées.
     *
     * @param villeCandidate Ville à tester
     * @param chemin         Chemin en cours
     * @param contraintes    Liste des ventes (contraintes)
     * @return true si admissible, false sinon
     */
    private static boolean estAdmissible(String villeCandidate, List<String> chemin, List<Vente> contraintes) {
        for (Vente v : contraintes) {
            if (v.getVilleAcheteur().equals(villeCandidate)) {
                String vendeur = v.getVilleVendeur();
                if (!chemin.contains(vendeur)) {
                    return false; // vendeur pas encore visité
                }
            }
        }
        return true;
    }

    /**
     * Structure représentant un trajet avec son chemin et sa distance totale.
     */
    private static class Trajet {
        List<String> chemin;
        int distance;

        Trajet(List<String> chemin, int distance) {
            this.chemin = chemin;
            this.distance = distance;
        }
    }
}
