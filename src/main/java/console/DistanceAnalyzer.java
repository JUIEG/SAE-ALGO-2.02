package console;

import java.io.*;
import java.util.*;

public class DistanceAnalyzer {

    public static void main(String[] args) throws IOException {
        String cheminFichier = "ressources_appli/distances.txt";

        List<String> lignes = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(cheminFichier));
        String line;
        while ((line = reader.readLine()) != null) {
            lignes.add(line.trim());
        }
        reader.close();

        List<String> villes = new ArrayList<>();
        List<int[]> matrice = new ArrayList<>();

        // 1. Extraction des villes + vérification structure
        for (int i = 0; i < lignes.size(); i++) {
            String[] parts = lignes.get(i).split("\\s+");
            if (parts.length < 2) {
                System.out.println("Ligne vide ou incomplète à l’index " + i);
                continue;
            }

            String nomVille = parts[0];
            villes.add(nomVille);

            int[] distances = new int[parts.length - 1];
            for (int j = 1; j < parts.length; j++) {
                try {
                    distances[j - 1] = Integer.parseInt(parts[j]);
                } catch (NumberFormatException e) {
                    System.out.println("Erreur de conversion ligne " + i + ", colonne " + j + ": " + parts[j]);
                    distances[j - 1] = Integer.MAX_VALUE;
                }
            }

            matrice.add(distances);
        }

        System.out.println("Villes détectées : " + villes.size());
        for (int i = 0; i < villes.size(); i++) {
            System.out.println((i + 1) + ". " + villes.get(i));
        }

        // 2. Vérification des dimensions
        boolean toutVaBien = true;
        for (int i = 0; i < matrice.size(); i++) {
            if (matrice.get(i).length != villes.size()) {
                toutVaBien = false;
                System.out.println("Ligne " + villes.get(i) + " : " +
                        matrice.get(i).length + "valeurs, attendu : " + villes.size());
            }
        }

        if (toutVaBien) {
            System.out.println("\nMatrice carrée correctement formatée !");
        } else {
            System.out.println("\nLa matrice n’est pas correctement formatée.");
        }

        // 3. Analyse spéciale : Velizy
        int indexVelizy = villes.indexOf("Velizy");
        if (indexVelizy == -1) {
            System.out.println("\nVelizy n’a pas été trouvée dans la liste des villes !");
        } else {
            int[] ligneVelizy = matrice.get(indexVelizy);
            System.out.println("\nAnalyse des distances depuis Velizy :");

            for (int j = 0; j < villes.size(); j++) {
                String ville = villes.get(j);
                int d = (j < ligneVelizy.length) ? ligneVelizy[j] : Integer.MAX_VALUE;

                if (d == Integer.MAX_VALUE) {
                    System.out.println("Distance inconnue entre Velizy et " + ville);
                }
            }
        }
    }
}