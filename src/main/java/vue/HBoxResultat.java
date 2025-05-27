package vue;

import javafx.scene.layout.HBox;

public class HBoxResultat extends HBox {

    public HBoxResultat() {
        this.setSpacing(10);
        this.getChildren().addAll(new AffichageChemin(), new VBoxParcours());
    }
}
