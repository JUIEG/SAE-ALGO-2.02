package modele;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Membre {
    public static final String FICHIER_MEMBRES = "ressources_appli/membres_APPLI.txt";
    private String pseudo;
    private String ville;

    public Membre(String pseudo, String ville) {
        this.pseudo = pseudo;
        this.ville = ville;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getVille() {
        return ville;
    }

    @Override
    public String toString() {
        return pseudo + " (" + ville + ")";
    }

    public static Map<String, String> chargerDepuisFichier() throws IOException {
        Map<String, String> map = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(FICHIER_MEMBRES));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
            map.put(parts[0], parts[1]);
        }
        reader.close();
        return map;
    }

}