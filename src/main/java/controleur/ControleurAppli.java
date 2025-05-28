package controleur;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import modele.*;
import vue.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ControleurAppli implements EventHandler{
    @Override
    public void handle(Event event) {
        VBoxRoot vboxroot = VBoxRoot.getInstance();
        VBoxParcours parcours = VBoxRoot.getVBoxParcours();
        HBoxResultat resultat = VBoxRoot.getResultat();
        AffichageChemin chemin = VBoxRoot.getAffichage();
        MenuAlgoScenario menue = VBoxRoot.getMenu();

        if (event.getSource() instanceof Button && ((Button) event.getSource()).getId().equals("Valider")) {

            try {
                Map<String, String> pseudoToVille = Util.chargerMembres("ressources_appli/membres_APPLI.txt");
                List<Vente> ventes = Util.chargerVentes("scenarios/" + menue.getScenario() + ".txt");
                DistanceMap distances = Util.chargerDistances("ressources_appli/distances.txt");

                List<String> meilleurParcours = new ArrayList<>();
                int distance = 0;

                if ("Algo de base".equals(menue.getAlgo())) {
                    meilleurParcours = AlgoBase.calculerItineraire(ventes, pseudoToVille);
                } else if ("Algo heuristique".equals(menue.getAlgo())) {
                    String greedy = menue.getGreedyIndex();
                    String[] greedysplit = greedy.split(" ");
                    int greedyInt = Integer.parseInt(greedysplit[0]);

                    meilleurParcours = AlgoHeuristique.calculerItineraire(ventes, pseudoToVille, distances, greedyInt);
                } else if ("K possibilités".equals(menue.getAlgo())) {
                    int k = Integer.parseInt(menue.getK());
                    List<List<String>> parcoursList = AlgoKpossibilite.trouverKParcours(ventes, pseudoToVille, distances, k);
                    if (!parcoursList.isEmpty()) {
                        meilleurParcours = parcoursList.get(0);
                    }
                }

                distance = Util.calculerDistance(meilleurParcours, distances);
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