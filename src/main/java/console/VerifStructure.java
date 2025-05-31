package console;

import modele.Vente;

import java.io.*;
import java.util.*;

/**
 * Classe de debug autonome permettant d'afficher la structure des graphes
 * utilisés par les différents algorithmes.
 */
public class VerifStructure {

    private static final String FICHIER_MEMBRES = "ressources_appli/membres_APPLI.txt";
    private static final String REPERTOIRE_SCENARIOS = "scenarios/";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Vérification de structure de graphe ===");

        // Choix algo
        System.out.println("Choisissez un algorithme à analyser :");
        System.out.println("1 - Algo de base");
        System.out.println("2 - Algo heuristique");
        System.out.println("3 - Algo k possibilités");
        System.out.print("Votre choix : ");
        int choixAlgo = Integer.parseInt(scanner.nextLine());

        // Liste les scénarios disponibles
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
        int choixScenar = Integer.parseInt(scanner.nextLine());

        if (choixScenar < 0 || choixScenar >= fichiers.length) {
            System.out.println("Numéro de scénario invalide.");
            return;
        }

        // Lecture des données
        String nomFichier = fichiers[choixScenar].getName();
        List<Vente> ventes = chargerVentes(REPERTOIRE_SCENARIOS + nomFichier);
        Map<String, String> pseudoToVille = chargerMembres(FICHIER_MEMBRES);

        // Affichage du graphe
        if (choixAlgo == 1) {
            debugGrapheBase(ventes, pseudoToVille);
        } else if (choixAlgo == 2) {
            debugGrapheHeuristique(ventes, pseudoToVille);
        } else {
            debugGrapheKpossibilite(ventes, pseudoToVille);
        }
    }

    public static void debugGrapheBase(List<Vente> ventes, Map<String, String> pseudoToVille) {
        System.out.println("\n🔎 Graphe - Algo de base :");
        Map<String, Set<String>> graph = construireGrapheBase(ventes, pseudoToVille);
        afficherGraphe(graph);
    }

    public static void debugGrapheHeuristique(List<Vente> ventes, Map<String, String> pseudoToVille) {
        System.out.println("\n🔎 Graphe - Algo heuristique :");
        Map<String, Set<String>> graph = construireGrapheHeuristique(ventes, pseudoToVille);
        afficherGraphe(graph);
    }

    public static void debugGrapheKpossibilite(List<Vente> ventes, Map<String, String> pseudoToVille) {
        System.out.println("\n🔎 Graphe - Algo k possibilités :");
        Map<String, Set<String>> graph = construireGrapheKpossibilites(ventes, pseudoToVille);
        afficherGraphe(graph);
    }

    private static Map<String, Set<String>> construireGrapheBase(List<Vente> ventes, Map<String, String> pseudoToVille) {
        Map<String, Set<String>> graph = new HashMap<>();
        for (Vente v : ventes) {
            String vendeur = pseudoToVille.get(v.getVilleVendeur());
            String acheteur = pseudoToVille.get(v.getVilleAcheteur());

            addEdge(graph, "Velizy+", vendeur + "+");
            addEdge(graph, "Velizy+", acheteur + "+");

            addEdge(graph, vendeur + "+", vendeur + "-");
            addEdge(graph, acheteur + "+", acheteur + "-");

            addEdge(graph, vendeur + "+", acheteur + "-");

            addEdge(graph, vendeur + "-", "Velizy-");
            addEdge(graph, acheteur + "-", "Velizy-");
        }
        return graph;
    }

    private static Map<String, Set<String>> construireGrapheHeuristique(List<Vente> ventes, Map<String, String> pseudoToVille) {
        Map<String, Set<String>> graph = new HashMap<>();
        Set<String> villes = new HashSet<>();
        for (Vente v : ventes) {
            villes.add(pseudoToVille.get(v.getVilleVendeur()));
            villes.add(pseudoToVille.get(v.getVilleAcheteur()));
        }

        for (String ville : villes) {
            addEdge(graph, "Velizy+", ville + "+");
            addEdge(graph, ville + "+", ville + "-");
            addEdge(graph, ville + "-", "Velizy-");
        }

        for (Vente v : ventes) {
            String vendeur = pseudoToVille.get(v.getVilleVendeur()) + "+";
            String acheteur = pseudoToVille.get(v.getVilleAcheteur()) + "-";
            addEdge(graph, vendeur, acheteur);
        }

        return graph;
    }

    private static Map<String, Set<String>> construireGrapheKpossibilites(List<Vente> ventes, Map<String, String> pseudoToVille) {
        return construireGrapheBase(ventes, pseudoToVille);
    }

    private static void afficherGraphe(Map<String, Set<String>> graph) {
        List<String> cles = new ArrayList<>(graph.keySet());
        Collections.sort(cles);
        for (String from : cles) {
            List<String> succ = new ArrayList<>(graph.getOrDefault(from, Set.of()));
            Collections.sort(succ);
            System.out.println("  " + from + " -> " + succ);
        }
    }

    private static void addEdge(Map<String, Set<String>> graph, String from, String to) {
        graph.computeIfAbsent(from, k -> new LinkedHashSet<>()).add(to);
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
}
