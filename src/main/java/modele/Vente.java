package modele;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Vente {
    public static String DOSSIER_SCENARIOS = "scenarios/";
    private String vendeur;
    private String acheteur;

    public Vente(String vendeur, String acheteur) {
        this.vendeur = vendeur;
        this.acheteur = acheteur;
    }

    public String getVilleVendeur() {
        return vendeur;
    }

    public static void setScenario(String scenario) {DOSSIER_SCENARIOS = "scenarios/" + scenario + ".txt";}

    public String getVilleAcheteur() {
        return acheteur;
    }

    @Override
    public String toString() {
        return vendeur + " -> " + acheteur;
    }

    public static List<Vente> chargerDepuisFichier() throws IOException {
        List<Vente> ventes = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(DOSSIER_SCENARIOS));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("->");
            if (parts.length == 2) {
                ventes.add(new Vente(parts[0].trim(), parts[1].trim()));
            }
        }
        reader.close();
        return ventes;
    }

}
