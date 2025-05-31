package modele;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Membre {

    public static final String CHEMIN_PAR_DEFAUT = "ressources_appli/membres_APPLI.txt";

    /**
     * Charge le fichier de membres et retourne une map pseudo → ville.
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
     * Variante avec chemin par défaut.
     */
    public static Map<String, String> chargerDepuisFichier() throws IOException {
        return chargerDepuisFichier(CHEMIN_PAR_DEFAUT);
    }
}