package modele.creation;

import modele.Vente;

import java.io.*;
import java.nio.file.*;
import java.util.List;

public class CreateScenario {

    //private static final String DOSSIER_CIBLE = "scenarios/créé";
    private static final String DOSSIER_CIBLE = "scenarios";


    public static void enregistrerScenario(List<Vente> ventes) throws IOException {
        Files.createDirectories(Paths.get(DOSSIER_CIBLE));

        int numero = getProchainNumero();
        String nomFichier = String.format("scenario_%d.txt", numero);
        File fichier = new File(DOSSIER_CIBLE, nomFichier);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichier))) {
            for (Vente v : ventes) {
                writer.write(v.getVendeur() + " -> " + v.getAcheteur());
                writer.newLine();
            }
        }

        System.out.println("Fichier créé : " + fichier.getAbsolutePath());
    }

    private static int getProchainNumero() {
        File dossier = new File(DOSSIER_CIBLE);
        File[] fichiers = dossier.listFiles((dir, name) -> name.matches("scenario_\\d+\\.txt"));

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
    public static void modifierScenario(int numeroScenario, List<Vente> nouvellesVentes) throws IOException {
        String nomFichier = String.format("scenario_%d.txt", numeroScenario);
        File fichier = new File(DOSSIER_CIBLE, nomFichier);

        if (!fichier.exists()) {
            System.out.println("⚠ Le fichier " + fichier.getName() + " n'existe pas.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichier))) {
            for (Vente v : nouvellesVentes) {
                writer.write(v.getVendeur() + " -> " + v.getAcheteur());
                writer.newLine();
            }
        }

        System.out.println("✅ Scénario modifié : " + fichier.getAbsolutePath());
    }

}
