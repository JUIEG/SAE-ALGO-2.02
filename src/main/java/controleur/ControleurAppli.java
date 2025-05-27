package controleur;

import modele.*;
import vue.*;

import java.io.IOException;
import java.util.*;

public class ControleurAppli {

    private final MenuAlgoScenario menu;
    private final AffichageChemin affichage;
    private final VBoxParcours vboxParcours;

    public ControleurAppli(MenuAlgoScenario menu, AffichageChemin affichage, VBoxParcours vboxParcours) {
        this.menu = menu;
        this.affichage = affichage;
        this.vboxParcours = vboxParcours;

        menu.getValiderButton().setOnAction(e -> executerAlgo());
    }

    private void executerAlgo() {
        String scenario = menu.getScenario();
        String algo = menu.getAlgo();
        int greedy = menu.getGreedyIndex();
        int k = menu.getK();

        try {
            Map<String, String> pseudoToVille = Util.chargerMembres("ressources_appli/membres_APPLI.txt");
            List<Vente> ventes = Util.chargerVentes("scenarios/" + scenario + ".txt");
            DistanceMap distances = Util.chargerDistances("ressources_appli/distances.txt");

            List<String> meilleurParcours = new ArrayList<>();
            int distance = 0;

            if ("Algo de base".equals(algo)) {
                meilleurParcours = AlgoBase.calculerItineraire(ventes, pseudoToVille);
            } else if ("Algo heuristique".equals(algo)) {
                meilleurParcours = AlgoHeuristique.calculerItineraire(ventes, pseudoToVille, distances, greedy);
            } else if ("K possibilités".equals(algo)) {
                List<List<String>> parcoursList = AlgoKpossibilite.trouverKParcours(ventes, pseudoToVille, distances, k);
                if (!parcoursList.isEmpty()) {
                    meilleurParcours = parcoursList.get(0); // top 1
                }
            }

            distance = Util.calculerDistance(meilleurParcours, distances);
            vboxParcours.setParcours(meilleurParcours);
            vboxParcours.setDistance(distance);

            // Mettre à jour le tableau
            List<AffichageChemin.Ligne> lignes = new ArrayList<>();
            for (int i = 0; i < meilleurParcours.size(); i++) {
                String ville = meilleurParcours.get(i);
                int dist = (i < meilleurParcours.size() - 1)
                        ? distances.getDistance(ville, meilleurParcours.get(i + 1))
                        : 0;
                lignes.add(new AffichageChemin.Ligne(i + 1, ville, dist));
            }
            affichage.setChemin(lignes);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}