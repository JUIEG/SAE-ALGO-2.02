package console;

import modele.Vente;
import modele.DistanceMap;

import java.io.*;
import java.util.*;

public class ConsoleLauncher {

    private static final String REPERTOIRE_SCENARIOS = "scenarios/";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Liste les scénarios disponibles
        File dossierScenarios = new File(REPERTOIRE_SCENARIOS);
        File[] fichiers = dossierScenarios.listFiles((dir, name) -> name.endsWith(".txt"));
        if (fichiers == null || fichiers.length == 0) {
            System.out.println("Aucun scénario trouvé.");
            return;
        }

        System.out.println("Scénarios disponibles :");
        for (int i = 0; i < fichiers.length; i++) {
            System.out.println(i + " - " + fichiers[i].getName());
        }

        System.out.print("Entrez le numéro du scénario à charger : ");
        int choix = scanner.nextInt();
        String nomFichier = fichiers[choix].getName();

        Map<String, String> pseudoToVille = chargerMembres();
        List<Vente> ventes = chargerVentes(REPERTOIRE_SCENARIOS + nomFichier);
        DistanceMap distances = chargerDistances();

        List<String> parcours = new ArrayList<>();
        Set<String> dejaVisitees = new HashSet<>();

        parcours.add("Vélizy");
        dejaVisitees.add("Vélizy");

        for (Vente v : ventes) {
            String villeVendeur = pseudoToVille.get(v.getVendeur());
            String villeAcheteur = pseudoToVille.get(v.getAcheteur());

            if (!dejaVisitees.contains(villeVendeur)) {
                parcours.add(villeVendeur);
                dejaVisitees.add(villeVendeur);
            }
            if (!dejaVisitees.contains(villeAcheteur)) {
                parcours.add(villeAcheteur);
                dejaVisitees.add(villeAcheteur);
            }
        }

        parcours.add("Vélizy");

        int total = 0;
        for (int i = 0; i < parcours.size() - 1; i++) {
            String from = parcours.get(i);
            String to = parcours.get(i + 1);
            total += distances.getDistance(from, to);
        }

        System.out.println("\nItinéraire généré :");
        for (String ville : parcours) {
            System.out.println(" - " + ville);
        }

        System.out.println("\nDistance totale : " + total + " km");
    }

    private static Map<String, String> chargerMembres() throws IOException {
        Map<String, String> map = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader("ressources_appli/membres_APPLI.txt"));
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

    private static DistanceMap chargerDistances() throws IOException {
        DistanceMap distMap = new DistanceMap();
        List<String> lignes = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("ressources_appli/distances.txt"));
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
