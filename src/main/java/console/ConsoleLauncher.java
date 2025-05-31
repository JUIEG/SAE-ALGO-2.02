package console;

import modele.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ConsoleLauncher {

    private static final String REPERTOIRE_SCENARIOS = "scenarios/";
    private static final String FICHIER_MEMBRES = "ressources_appli/membres_APPLI.txt";
    private static final String FICHIER_DISTANCES = "ressources_appli/distances.txt";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Choix de l'algo
        System.out.println("Choisissez l'algorithme à exécuter :");
        System.out.println("1 - Algo de base");
        System.out.println("2 - Algo heuristique");
        System.out.println("3 - Algo k possibilités");
        System.out.print("Votre choix : ");
        int algoChoisi = scanner.nextInt();
        scanner.nextLine();

        // Liste des scénarios
        File[] fichiers = new File(REPERTOIRE_SCENARIOS).listFiles((dir, name) -> name.endsWith(".txt"));
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
        String cheminScenario = REPERTOIRE_SCENARIOS + fichiers[choix].getName();

        // Choix de méthode greedy
        int methodeGreedy = 1;
        if (algoChoisi == 2) {
            System.out.println("\nMéthodes greedy disponibles :");
            System.out.println("1 - Ville la plus proche");
            System.out.println("2 - Ville la plus éloignée");
            System.out.println("3 - Ordre alphabétique");
            System.out.println("4 - Ville débloquant le plus de dépendances");
            System.out.println("5 - Ville la moins visitée historiquement");
            System.out.print("Votre choix : ");
            methodeGreedy = scanner.nextInt();
            scanner.nextLine();
        }

        // K parcours ?
        int k = 1;
        if (algoChoisi == 3) {
            System.out.print("Combien de parcours voulez-vous générer (k) ? ");
            k = scanner.nextInt();
            scanner.nextLine();
        }

        // Chargement des données
        Map<String, String> mapMembres = Membre.chargerDepuisFichier(FICHIER_MEMBRES);
        List<Vente> ventesPseudos = Vente.chargerDepuisFichier(cheminScenario);
        List<Vente> ventes = Vente.traduireVilles(ventesPseudos, mapMembres);
        DistanceMap distances = DistanceMap.chargerDepuisFichier(FICHIER_DISTANCES);

        if (algoChoisi == 3) {
            List<List<String>> topK = AlgoKpossibilite.trouverKParcours(ventes, mapMembres, distances, k);
            System.out.println("\nAlgorithme utilisé : Algo k possibilités");
            for (int i = 0; i < topK.size(); i++) {
                List<String> parcours = topK.get(i);
                int total = calculerDistance(parcours, distances);
                System.out.println("Parcours #" + (i + 1) + " (" + total + " km) :");
                for (String ville : parcours) {
                    System.out.println(" - " + ville);
                }
                System.out.println();
            }
        } else {
            List<String> parcours;
            String nomAlgo;

            if (algoChoisi == 1) {
                parcours = AlgoBase.calculerItineraire(ventes);
                nomAlgo = "Algorithme de base";
            } else {
                parcours = AlgoHeuristique.calculerItineraire(ventes, distances, methodeGreedy);
                nomAlgo = "Algorithme heuristique";
            }

            int total = calculerDistance(parcours, distances);
            System.out.println("\nAlgorithme utilisé : " + nomAlgo);
            System.out.println("Itinéraire généré :");
            for (String ville : parcours) {
                System.out.println(" - " + ville);
            }
            System.out.println("\nDistance totale : " + total + " km");
        }
    }

    private static int calculerDistance(List<String> parcours, DistanceMap distances) {
        int total = 0;
        for (int i = 0; i < parcours.size() - 1; i++) {
            total += distances.getDistance(parcours.get(i), parcours.get(i + 1));
        }
        return total;
    }
}
