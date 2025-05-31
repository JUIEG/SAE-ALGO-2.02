package controleur;
import modele.AlgoBase;
import modele.AlgoKpossibilite;
import modele.DistanceMap;
import modele.Vente;

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

    @Override
    public void handle(ActionEvent event) {
        System.out.println("✅ Bouton cliqué : action reçue !");

        if (event.getSource() instanceof Button && ((Button) event.getSource()).getId().equals("Valider")) {
            try {
                Map<String, String> pseudoToVille = Membre.chargerDepuisFichier();
                List<Vente> ventes = Vente.traduireVilles(Vente.chargerDepuisFichier("scenarios/" + menu.getScenario() + ".txt"), pseudoToVille);
                DistanceMap distances = DistanceMap.chargerDepuisFichier();

                List<String> meilleurParcours = new ArrayList<>();
                int distance = 0;

                switch (menu.getAlgo()) {
                    case "Algo de base":
                        meilleurParcours = AlgoBase.calculerItineraire(ventes);
//                      meilleurParcours = AlgoBase.calculerItineraire(ventes, pseudoToVille);
                        break;
                    case "Algo heuristique":
                        meilleurParcours = AlgoBase.calculerItineraire(ventes);
//                      meilleurParcours = AlgoHeuristique.calculerItineraire(ventes, pseudoToVille, distances);
                        break;
                    case "K possibilités":
                        int k = Integer.parseInt(menu.getK());
                        List<List<String>> parcoursList = AlgoKpossibilite.trouverKParcours(ventes, pseudoToVille, distances, k);
                        if (!parcoursList.isEmpty()) {
                            meilleurParcours = parcoursList.get(0);
                        }
                        break;
                }

                distance = DistanceMap.calculerDistance(meilleurParcours, distances);
                parcours.setParcours(meilleurParcours);
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

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
