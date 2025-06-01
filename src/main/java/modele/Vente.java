package modele;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Vente {
    private String vendeur;
    private String acheteur;

    public Vente(String vendeur, String acheteur) {
        this.vendeur = vendeur;
        this.acheteur = acheteur;
    }

    public String getVilleVendeur() {
        return vendeur;
    }

    public String getVilleAcheteur() {
        return acheteur;
    }

    @Override
    public String toString() {
        return vendeur + " -> " + acheteur;
    }

    /**
     * Charge une liste de ventes à partir d'un fichier texte.
     *
     * @param chemin le chemin du fichier
     * @return une liste de ventes
     * @throws IOException si erreur d'accès fichier
     */
    public static List<Vente> chargerDepuisFichier(String chemin) throws IOException {
        List<Vente> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(chemin))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("->");
                if (parts.length == 2) {
                    list.add(new Vente(parts[0].trim(), parts[1].trim()));
                }
            }
        }
        return list;
    }

    /**
     * Traduit chaque vente en villes au lieu de pseudos.
     *
     * @param ventes la liste d'objets Vente avec des pseudonymes
     * @param mapPseudoVille la correspondance pseudo → ville
     * @return une nouvelle liste avec les ventes par villes
     */
    public static List<Vente> traduireVilles(List<Vente> ventes, Map<String, String> mapPseudoVille) {
        List<Vente> traduites = new ArrayList<>();
        for (Vente v : ventes) {
            String villeVendeur = mapPseudoVille.get(v.vendeur);
            String villeAcheteur = mapPseudoVille.get(v.acheteur);
            if (villeVendeur != null && villeAcheteur != null) {
                traduites.add(new Vente(villeVendeur, villeAcheteur));
            } else {
                System.err.println(" Pseudo inconnu : " + v.vendeur + " ou " + v.acheteur);
            }
        }
        return traduites;
    }
    // Getter pour le pseudo du vendeur
    public String getVendeur() {
        return vendeur;
    }

    // Getter pour le pseudo de l'acheteur
    public String getAcheteur() {
        return acheteur;
    }

}