package controleur;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import modele.*;
import vue.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur principal de l'application.
 * Il récupère les paramètres depuis l'interface utilisateur (MenuAlgoScenario),
 * exécute l'algorithme sélectionné avec les données chargées, et met à jour l'affichage.
 */
public class ControleurAppli implements EventHandler<ActionEvent> {

    private final MenuAlgoScenario menu;
    private final VBoxParcours parcours;
    private final HBoxResultat resultat;
    private final AffichageChemin chemin;

    /**
     * Constructeur du contrôleur.
     * 
     * @param menu       Référence vers le menu de sélection des paramètres.
     * @param parcours   Vue affichant le parcours calculé.
     * @param resultat   Vue affichant les résultats (distance, etc.).
     * @param chemin     Vue détaillée du chemin (liste des villes et distances).
     */
    public ControleurAppli(MenuAlgoScenario menu, VBoxParcours parcours,
                           HBoxResultat resultat, AffichageChemin chemin) {
        this.menu = menu;
        this.parcours = parcours;
        this.resultat = resultat;
        this.chemin = chemin;
    }

    /**
     * Méthode appelée depuis l'interface pour déclencher manuellement une exécution.
     */
    public void execute() {
        handle(new ActionEvent());
    }

    /**
     * Exécution de l'algorithme choisi en fonction des paramètres utilisateur.
     * Chargement des données, traitement algorithmique, puis mise à jour de l'affichage.
     * 
     * @param event Événement déclencheur (non utilisé ici).
     */
    @Override
    public void handle(ActionEvent event) {
        try {
            // Chargement des données
            Map<String, String> pseudoToVille = Membre.chargerDepuisFichier(); // Associe pseudo -> ville
            List<Vente> ventes = Vente.traduireVilles(
                    Vente.chargerDepuisFichier("scenarios/" + menu.getScenario() + ".txt"),
                    pseudoToVille
            );
            DistanceMap distances = DistanceMap.chargerDepuisFichier();

            List<String> meilleurParcours = new ArrayList<>();
            int distance = 0;

            String algo = menu.getAlgo(); // Récupère l'algo sélectionné

            switch (algo) {
                case "Algo de base":
                    // Appelle l'algo simple (vendeur avant acheteur, sans heuristique)
                    meilleurParcours = AlgoBase.calculerItineraire(ventes);
                    break;

                case "Algo heuristique":
                    // Appelle une heuristique selon la méthode choisie par l'utilisateur
                    String methodeStr = menu.getMethodeGreedy();
                    if (methodeStr == null || methodeStr.isEmpty()) {
                        System.out.println(" Veuillez sélectionner une méthode heuristique.");
                        return;
                    }
                    int methode = Integer.parseInt(methodeStr.substring(0, 1)); // ex: "1 - ..." → 1
                    meilleurParcours = AlgoHeuristique.calculerItineraire(ventes, distances, methode);
                    break;

                case "K possibilités":
                    // Appelle un algo qui teste k chemins valides et garde les meilleurs
                    String kStr = menu.getK();
                    if (kStr == null || kStr.isEmpty()) {
                        System.out.println(" Veuillez renseigner une valeur pour k.");
                        return;
                    }
                    int k = Integer.parseInt(kStr);

                    // Re-traduction (sécurité : on pourrait aussi réutiliser `ventes`)
                    List<Vente> ventesEnVilles = Vente.traduireVilles(
                            Vente.chargerDepuisFichier("scenarios/" + menu.getScenario() + ".txt"),
                            pseudoToVille
                    );

                    List<List<String>> parcoursList = AlgoKpossibilite.trouverKParcours(ventesEnVilles, distances, k);

                    if (!parcoursList.isEmpty()) {
                        // Sélectionne le meilleur chemin et met à jour toutes les vues
                        meilleurParcours = parcoursList.get(0);

                        parcours.setParcoursMultiple(parcoursList);
                        distance = DistanceMap.calculerDistance(meilleurParcours, distances);
                        parcours.setDistance(distance);

                        List<AffichageChemin.Ligne> lignes = new ArrayList<>();
                        for (int i = 0; i < meilleurParcours.size(); i++) {
                            String ville = meilleurParcours.get(i);
                            int dist = (i < meilleurParcours.size() - 1)
                                    ? distances.getDistance(ville, meilleurParcours.get(i + 1))
                                    : 0;
                            lignes.add(new AffichageChemin.Ligne(i + 1, ville, dist));
                        }
                        chemin.setChemin(lignes);
                    } else {
                        System.out.println(" Aucun parcours valide trouvé.");
                        return;
                    }
                    break;

                default:
                    System.out.println("Algorithme non reconnu.");
                    return;
            }

            // Calcul final de la distance
            distance = DistanceMap.calculerDistance(meilleurParcours, distances);
            parcours.setParcours(meilleurParcours);
            parcours.setDistance(distance);

            // Mise à jour de l'affichage détaillé du chemin
            List<AffichageChemin.Ligne> lignes = new ArrayList<>();
            for (int i = 0; i < meilleurParcours.size(); i++) {
                String ville = meilleurParcours.get(i);
                int dist = (i < meilleurParcours.size() - 1)
                        ? distances.getDistance(ville, meilleurParcours.get(i + 1))
                        : 0;
                lignes.add(new AffichageChemin.Ligne(i + 1, ville, dist));
            }
            chemin.setChemin(lignes);

        } catch (IOException | NumberFormatException ex) {
            // Gestion des erreurs liées aux fichiers ou à la conversion des données
            ex.printStackTrace();
            System.out.println(" Erreur lors du traitement : " + ex.getMessage());
        }
    }
}
