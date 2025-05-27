package vue;

import controleur.ControleurAppli;
import javafx.scene.layout.VBox;

public class VBoxRoot extends VBox {

    public VBoxRoot() {
        MenuAlgoScenario menu = new MenuAlgoScenario();
        AffichageChemin affichage = new AffichageChemin();
        VBoxParcours vboxParcours = new VBoxParcours();
        HBoxResultat resultat = new HBoxResultat(affichage, vboxParcours);

        this.getChildren().addAll(menu, resultat);

        new ControleurAppli(menu, affichage, vboxParcours);
    }
}
