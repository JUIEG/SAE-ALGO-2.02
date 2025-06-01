package modele;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;



/**
 * Classe représentant une carte des distances entre les villes.
 * Les distances sont chargées depuis un fichier texte structuré en matrice.
 */
public class DistanceMap {

    // Chemin par défaut du fichier de distances
    public static final String CHEMIN_PAR_DEFAUT = "ressources_appli/distances.txt";

    // Structure de données principale : Map<Origine, Map<Destination, Distance>>
    private final Map<String, Map<String, Integer>> distances = new HashMap<>();

    /**
     * Ajoute une distance entre deux villes.
     *
     * @param from     Ville de départ
     * @param to       Ville d’arrivée
     * @param distance Distance entre from et to
     */
    public void addDistance(String from, String to, int distance) {
        distances.computeIfAbsent(from, k -> new HashMap<>()).put(to, distance);
    }

    /**
     * Affiche toutes les distances enregistrées dans la carte.
     */
    public void afficherDistances() {
        for (String from : distances.keySet()) {
            for (Map.Entry<String, Integer> entry : distances.get(from).entrySet()) {
                System.out.println(from + " -> " + entry.getKey() + " : " + entry.getValue());
            }
        }
    }

    /**
     * Retourne la distance entre deux villes.
     *
     * @param from Ville de départ
     * @param to   Ville d’arrivée
     * @return Distance ou Integer.MAX_VALUE si inconnue
     */
    public int getDistance(String from, String to) {
        if (!distances.containsKey(from) || !distances.get(from).containsKey(to)) {
            System.err.println(" Distance inconnue entre " + from + " et " + to);
            return Integer.MAX_VALUE;
        }
        int dist = distances.get(from).get(to);
        System.out.println("Distance entre " + from + " et " + to + " = " + dist);
        return dist;
    }

    /**
     * Vérifie si une distance est connue entre deux villes.
     *
     * @param ville1 Ville de départ
     * @param ville2 Ville d’arrivée
     * @return true si la distance est connue
     */
    public boolean contains(String ville1, String ville2) {
        return getDistance(ville1, ville2) != Integer.MAX_VALUE;
    }

    /**
     * Charge une carte des distances depuis un fichier texte structuré.
     *
     * Format attendu :
     * Chaque ligne commence par le nom d'une ville, suivi de distances vers les autres villes (dans le même ordre).
     *
     *
     * @param chemin Chemin vers le fichier
     * @return Instance de DistanceMap remplie
     * @throws IOException si erreur de lecture
     */
    public static DistanceMap chargerDepuisFichier(String chemin) throws IOException {
        DistanceMap distMap = new DistanceMap();
        List<String> lignes = new ArrayList<>();

        // Lecture du fichier ligne par ligne
        try (BufferedReader reader = new BufferedReader(new FileReader(chemin))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lignes.add(line);
            }
        }

        // Extraction des noms de villes (1re colonne)
        List<String> villes = new ArrayList<>();
        for (String l : lignes) {
            villes.add(l.split("\\s+")[0]);
        }

        // Lecture de la matrice de distances
        for (int i = 0; i < lignes.size(); i++) {
            String[] parts = lignes.get(i).trim().split("\\s+");
            String villeA = parts[0];

            for (int j = 1; j < parts.length; j++) {
                String villeB = villes.get(j - 1); // -1 car parts[0] = nom de la ville
                int d = Integer.parseInt(parts[j]);
                distMap.addDistance(villeA, villeB, d);
            }
        }

        distMap.afficherDistances(); // Affichage à la fin du chargement

        return distMap;
    }

    /**
     * Surcharge : utilise le chemin par défaut.
     *
     * @return Instance de DistanceMap remplie
     * @throws IOException si erreur de lecture
     */
    public static DistanceMap chargerDepuisFichier() throws IOException {
        return chargerDepuisFichier(CHEMIN_PAR_DEFAUT);
    }

    public Map<String, Integer> getNeighbours(String from) {
        return distances.getOrDefault(from, new HashMap<>());
    }
    public static int calculerDistance(List<String> parcours, DistanceMap distances) {
        int total = 0;
        for (int i = 0; i < parcours.size() - 1; i++) {
            total += distances.getDistance(parcours.get(i), parcours.get(i + 1));
        }
        return total;
    }
}
