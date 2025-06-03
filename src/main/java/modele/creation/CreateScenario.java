package modele.creation;

import modele.Vente;

import java.io.*;
import java.nio.file.*;
import java.util.List;

public class CreateScenario {

    private static final String DOSSIER_CIBLE = "scenarios/créé";

    public static void enregistrerScenario(List<Vente> ventes) throws IOException {
        Files.createDirectories(Paths.get(DOSSIER_CIBLE));

        int numero = getProchainNumero();
        String nomFichier = String.format("scenario_créé_%03d.txt", numero);
        File fichier = new File(DOSSIER_CIBLE, nomFichier);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichier))) {
            for (Vente v : ventes) {
                writer.write(v.getVendeur() + " -> " + v.getAcheteur());
                writer.newLine();
            }
        }

        System.out.println("✅ Fichier créé : " + fichier.getAbsolutePath());
    }

    private static int getProchainNumero() {
        File dossier = new File(DOSSIER_CIBLE);
        File[] fichiers = dossier.listFiles((dir, name) -> name.matches("scenario_créé_\\d+\\.txt"));

        int max = 0;
        if (fichiers != null) {
            for (File f : fichiers) {
                String nom = f.getName().replaceAll("[^0-9]", "");
                try {
                    int val = Integer.parseInt(nom);
                    if (val > max) max = val;
                } catch (NumberFormatException ignored) {}
            }
        }

        return max + 1;
    }
}
