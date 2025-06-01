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

public class ControleurAppli implements EventHandler<ActionEvent> {

    private final MenuAlgoScenario menu;
    private final VBoxParcours parcours;
    private final HBoxResultat resultat;
    private final AffichageChemin chemin;
    public ControleurAppli(MenuAlgoScenario menu, VBoxParcours parcours,
                           HBoxResultat resultat, AffichageChemin chemin) {
        this.menu = menu;
        this.parcours = parcours;
        this.resultat = resultat;
        this.chemin = chemin;
    }

    public void execute() {
        handle(new ActionEvent());
    }

    @Override
    public void handle(ActionEvent event) {

        try {
            // Chargement des données
            Map<String, String> pseudoToVille = Membre.chargerDepuisFichier();
            List<Vente> ventes = Vente.traduireVilles(
                    Vente.chargerDepuisFichier("scenarios/" + menu.getScenario() + ".txt"),
                    pseudoToVille
            );
            DistanceMap distances = DistanceMap.chargerDepuisFichier();

            List<String> meilleurParcours = new ArrayList<>();
            int distance = 0;

            String algo = menu.getAlgo();

            switch (algo) {
                case "Algo de base":
                    meilleurParcours = AlgoBase.calculerItineraire(ventes);
                    break;

                case "Algo heuristique":
                    String methodeStr = menu.getMethodeGreedy();
                    if (methodeStr == null || methodeStr.isEmpty()) {
                        System.out.println("❌ Veuillez sélectionner une méthode heuristique.");
                        return;
                    }
                    int methode = Integer.parseInt(methodeStr.substring(0, 1));
                    meilleurParcours = AlgoHeuristique.calculerItineraire(ventes, distances, methode);
                    break;

                case "K possibilités":
                    String kStr = menu.getK();
                    if (kStr == null || kStr.isEmpty()) {
                        System.out.println("❌ Veuillez renseigner une valeur pour k.");
                        return;
                    }
                    int k = Integer.parseInt(kStr);

                    // Traduction pseudos -> villes avant l'algo
                    List<Vente> ventesEnVilles = Vente.traduireVilles(
                            Vente.chargerDepuisFichier("scenarios/" + menu.getScenario() + ".txt"),
                            pseudoToVille
                    );

                    List<List<String>> parcoursList = AlgoKpossibilite.trouverKParcours(ventesEnVilles, distances, k);

                    if (!parcoursList.isEmpty()) {
                        meilleurParcours = parcoursList.get(0);

                        parcours.setParcoursMultiple(parcoursList);
                        distance = DistanceMap.calculerDistance(meilleurParcours, distances);
                        parcours.setDistance(distance);

                        // Met à jour l'affichage du tableau avec le meilleur
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
                        System.out.println("❌ Aucun parcours valide trouvé.");
                        return;
                    }


                default:
                    System.out.println("❌ Algorithme non reconnu.");
                    return;
            }

            // Calcul de la distance totale
            distance = DistanceMap.calculerDistance(meilleurParcours, distances);
            parcours.setParcours(meilleurParcours);
            parcours.setDistance(distance);

            // Mise à jour du tableau d'affichage
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
            ex.printStackTrace();
            System.out.println("❌ Erreur lors du traitement : " + ex.getMessage());
        }
    }
}
