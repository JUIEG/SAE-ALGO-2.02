package vue;

import javafx.scene.layout.HBox;

public class HBoxResultat extends HBox {

    public HBoxResultat(AffichageChemin affichage, VBoxParcours vboxParcours) {
        this.setSpacing(10);
        this.getChildren().addAll(affichage, vboxParcours);
    }
}

