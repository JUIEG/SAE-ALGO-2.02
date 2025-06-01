package modele;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Représente un membre de l'APPLI avec un pseudonyme et une ville.
 * Contient aussi des méthodes statiques pour charger les membres depuis un fichier.
 */
public class Membre {

    // Pseudonyme du membre
    private String pseudo;

    // Ville de résidence du membre
    private String ville;

    // Chemin par défaut vers le fichier des membres
    public static final String CHEMIN_PAR_DEFAUT = "ressources_appli/membres_APPLI.txt";

    /**
     * Constructeur de la classe Membre.
     *
     * @param pseudo Le pseudonyme du membre
     * @param ville  La ville de résidence du membre
     */
    public Membre(String pseudo, String ville) {
        this.pseudo = pseudo;
        this.ville = ville;
    }

    /**
     * Retourne le pseudonyme du membre.
     *
     * @return Le pseudo
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * Retourne la ville de résidence du membre.
     *
     * @return La ville
     */
    public String getVille() {
        return ville;
    }

    /**
     * Retourne une représentation textuelle du membre.
     *
     * @return Le pseudo et la ville entre parenthèses (ex : "Pikachu (Paris)")
     */
    @Override
    public String toString() {
        return pseudo + " (" + ville + ")";
    }

    /**
     * Charge les membres depuis un fichier texte.
     * Le fichier doit contenir une ligne par membre, avec le pseudo et la ville séparés par des espaces.
     *
     * @param chemin Le chemin vers le fichier texte
     * @return Une map contenant les couples pseudo → ville
     * @throws IOException En cas d’erreur de lecture du fichier
     */
    public static Map<String, String> chargerDepuisFichier(String chemin) throws IOException {
        Map<String, String> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(chemin))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length == 2) {
                    map.put(parts[0], parts[1]);
                }
            }
        }
        return map;
    }

    /**
     * Variante de chargement utilisant le chemin par défaut.
     *
     * @return Une map contenant les couples pseudo → ville
     * @throws IOException En cas d’erreur de lecture
     */
    public static Map<String, String> chargerDepuisFichier() throws IOException {
        return chargerDepuisFichier(CHEMIN_PAR_DEFAUT);
    }
}

