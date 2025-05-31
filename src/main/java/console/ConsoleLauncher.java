package console;

import modele.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ConsoleLauncher {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choisissez l'algorithme à exécuter :");
        System.out.println("1 - Algo de base");
        System.out.println("2 - Algo heuristique");
        int algo = scanner.nextInt();

        // Liste des scénarios
        File dossier = new File("scenarios");
        File[] scenarios = dossier.listFiles((d, name) -> name.endsWith(".txt"));
        for (int i = 0; i < scenarios.length; i++) {
            System.out.println(i + " - " + scenarios[i].getName());
        }
        System.out.print("Choix du scénario : ");
        int choix = scanner.nextInt();
        Vente.setScenario(scenarios[choix].getPath());

        List<Vente> ventes = null;

        try {
            ventes = Vente.chargerDepuisFichier();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, String> mapMembres = null;

        try {
            mapMembres = Membre.chargerDepuisFichier();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Vente v : ventes) v.traduireVilles(mapMembres);
        DistanceMap distances = null;

        try {
            distances = DistanceMap.chargerDepuisFichier();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<String> parcours = null;

        if (algo == 1) {
            parcours = AlgoBase.calculerItineraire(ventes);
        } else {
            System.out.println("Méthodes greedy disponibles :");
            System.out.println("1 - Ville la plus proche");
            System.out.println("2 - Ville la plus éloignée");
            System.out.println("3 - Ordre alphabétique");
            System.out.println("4 - Ville débloquant le plus de dépendances");
            System.out.println("5 - Ville la moins visitée historiquement");
            System.out.print("Votre choix : ");
            int methode = scanner.nextInt();
            parcours = AlgoHeuristique.calculerItineraire(ventes, distances, methode);
        }

        int total = 0;
        for (int i = 0; i < parcours.size() - 1; i++) {
            total += distances.getDistance(parcours.get(i), parcours.get(i + 1));
        }

        System.out.println("Itinéraire généré :");
        for (String ville : parcours) {
            System.out.println(" - " + ville);
        }
        System.out.println("Distance totale : " + total + " km");
    }
}
