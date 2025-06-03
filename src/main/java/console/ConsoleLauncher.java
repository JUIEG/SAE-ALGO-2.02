package console;

import modele.usage.AlgoBase;
import modele.usage.AlgoHeuristique;
import modele.DistanceMap;
import modele.Vente;

import java.io.*;
import java.util.*;

public class ConsoleLauncher {

    private static final String REPERTOIRE_SCENARIOS = "scenarios/";
    private static final String FICHIER_MEMBRES = "ressources_appli/membres_APPLI.txt";
    private static final String FICHIER_DISTANCES = "ressources_appli/distances.txt";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Choix de l'algorithme
        System.out.println("Choisissez l'algorithme à exécuter :");
        System.out.println("1 - Algo de base");
        System.out.println("2 - Algo heuristique");
        System.out.print("Votre choix : ");
        int algoChoisi = scanner.nextInt();
        scanner.nextLine(); // consomme le retour à la ligne

        // Liste les scénarios disponibles
        File dossierScenarios = new File(REPERTOIRE_SCENARIOS);
        File[] fichiers = dossierScenarios.listFiles((dir, name) -> name.endsWith(".txt"));
        if (fichiers == null || fichiers.length == 0) {
            System.out.println("Aucun scénario trouvé.");
            return;
        }

        System.out.println("\nScénarios disponibles :");
        for (int i = 0; i < fichiers.length; i++) {
            System.out.println(i + " - " + fichiers[i].getName());
        }

        System.out.print("Entrez le numéro du scénario à charger : ");
        int choix = scanner.nextInt();
        scanner.nextLine();

        if (choix < 0 || choix >= fichiers.length) {
            System.out.println("Numéro de scénario invalide.");
            return;
        }

        String nomFichier = fichiers[choix].getName();

        // Chargement des données
        Map<String, String> pseudoToVille = chargerMembres(FICHIER_MEMBRES);
        List<Vente> ventes = chargerVentes(REPERTOIRE_SCENARIOS + nomFichier);
        DistanceMap distances = chargerDistances(FICHIER_DISTANCES);

        // Calcul de l’itinéraire selon l’algo choisi
        List<String> parcours;
        String nomAlgo;

        if (algoChoisi == 1) {
            parcours = AlgoBase.calculerItineraire(ventes);
            nomAlgo = "Algorithme de base";
        } else {
            int methodeGreedy = 1;
            parcours = AlgoHeuristique.calculerItineraire(ventes, distances, methodeGreedy);

            nomAlgo = "Algorithme heuristique";
        }

        // Calcul de la distance totale
        int total = 0;
        for (int i = 0; i < parcours.size() - 1; i++) {
            String from = parcours.get(i);
            String to = parcours.get(i + 1);
            total += distances.getDistance(from, to);
        }

        // Affichage du résultat
        System.out.println("\nAlgorithme utilisé : " + nomAlgo);
        System.out.println("Itinéraire généré :");
        for (String ville : parcours) {
            System.out.println(" - " + ville);
        }

        System.out.println("\nDistance totale : " + total + " km");
    }

    private static Map<String, String> chargerMembres(String chemin) throws IOException {
        Map<String, String> map = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(chemin));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
            map.put(parts[0], parts[1]);
        }
        reader.close();
        return map;
    }

    private static List<Vente> chargerVentes(String chemin) throws IOException {
        List<Vente> list = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(chemin));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("->");
            list.add(new Vente(parts[0].trim(), parts[1].trim()));
        }
        reader.close();
        return list;
    }

    private static DistanceMap chargerDistances(String chemin) throws IOException {
        DistanceMap distMap = new DistanceMap();
        List<String> lignes = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(chemin));
        String line;
        while ((line = reader.readLine()) != null) {
            lignes.add(line);
        }
        reader.close();

        List<String> villes = new ArrayList<>();
        for (String l : lignes) {
            villes.add(l.split("\\s+")[0]);
        }

        for (String ligne : lignes) {
            String[] parts = ligne.trim().split("\\s+");
            String villeA = parts[0];
            for (int j = 1; j < parts.length; j++) {
                String villeB = villes.get(j - 1);
                int d = Integer.parseInt(parts[j]);
                distMap.addDistance(villeA, villeB, d);
            }
        }

        return distMap;
    }
}