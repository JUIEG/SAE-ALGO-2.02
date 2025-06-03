package vue.usage;

import javafx.scene.layout.HBox;


/**
 * Conteneur horizontal affichant côte à côte l'affichage du chemin
 * et la liste des parcours générés.
 */
public class HBoxResultat extends HBox {

    /**
     * Constructeur qui prend en paramètres les deux composants à afficher.
     * @param affichage composant d'affichage du chemin (table)
     * @param vboxParcours composant affichant la liste des parcours + distance
     */
    public HBoxResultat(AffichageChemin affichage, VBoxParcours vboxParcours) {
        this.setSpacing(10);  // Espacement horizontal entre enfants
        this.getChildren().addAll(affichage, vboxParcours);
    }
}


