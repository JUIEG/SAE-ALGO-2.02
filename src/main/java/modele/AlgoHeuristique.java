package modele;

import java.util.*;

public class AlgoHeuristique {

    /**
     * Calcule un itinéraire heuristique à partir des ventes et distances.
     * Si toutes les contraintes sont bloquées, choisit la ville atteignable la plus proche.
     *
     * @param ventes         liste des ventes (vendeur → acheteur)
     * @param pseudoToVille  map des pseudonymes vers villes
     * @param distances      matrice des distances entre villes
     * @param methodeGreedy  stratégie heuristique à utiliser (1 à 5)
     * @return liste ordonnée des villes à visiter
     */
    public static List<String> calculerItineraire(List<Vente> ventes, Map<String, String> pseudoToVille, DistanceMap distances, int methodeGreedy) {
        Set<String> villesARendre = new HashSet<>();
        Map<String, Integer> nombreDependants = new HashMap<>();
        Map<String, Integer> historique = new HashMap<>();

        for (Vente v : ventes) {
            String vendeur = pseudoToVille.get(v.getVendeur());
            String acheteur = pseudoToVille.get(v.getAcheteur());

            villesARendre.add(vendeur);
            villesARendre.add(acheteur);

            nombreDependants.put(vendeur, nombreDependants.getOrDefault(vendeur, 0) + 1);
            historique.put(vendeur, 0);
            historique.put(acheteur, 0);
        }

        Set<String> visitées = new HashSet<>();
        List<String> parcours = new ArrayList<>();
        String villeActuelle = "Velizy";

        parcours.add(villeActuelle);
        visitées.add(villeActuelle);

        while (!visitées.containsAll(villesARendre)) {
            List<String> candidates = new ArrayList<>();
            for (String ville : villesARendre) {
                if (!visitées.contains(ville)) {
                    candidates.add(ville);
                }
            }

            if (candidates.isEmpty()) break;

            String prochain = choisirVille(candidates, villeActuelle, distances, methodeGreedy, nombreDependants, historique);

            if (prochain == null) {
                System.err.println("Aucune ville admissible trouvée.");
                break;
            }

            parcours.add(prochain);
            visitées.add(prochain);
            historique.put(prochain, historique.getOrDefault(prochain, 0) + 1);
            villeActuelle = prochain;
        }

        parcours.add("Velizy");
        return parcours;
    }

    private static String choisirVille(List<String> villes, String depuis, DistanceMap distances, int methode,
                                       Map<String, Integer> dependants, Map<String, Integer> historique) {
        switch (methode) {
            case 1: // Plus proche
                return villes.stream()
                        .min(Comparator.comparingInt(v -> distances.getDistance(depuis, v)))
                        .orElse(null);

            case 2: // Plus éloignée
                return villes.stream()
                        .max(Comparator.comparingInt(v -> distances.getDistance(depuis, v)))
                        .orElse(null);

            case 3: // Alphabétique
                return villes.stream().min(String::compareTo).orElse(null);

            case 4: // Plus de dépendants
                return villes.stream()
                        .max(Comparator.comparingInt(v -> dependants.getOrDefault(v, 0)))
                        .orElse(null);

            case 5: // Moins visitée historiquement
                return villes.stream()
                        .min(Comparator.comparingInt(v -> historique.getOrDefault(v, 0)))
                        .orElse(null);

            default:
                System.err.println("Méthode inconnue, méthode 1 utilisée par défaut.");
                return villes.stream()
                        .min(Comparator.comparingInt(v -> distances.getDistance(depuis, v)))
                        .orElse(null);
        }
    }
}
