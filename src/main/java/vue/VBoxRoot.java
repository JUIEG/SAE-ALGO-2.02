package vue;

import controleur.ControleurAppli;
import javafx.scene.layout.VBox;

public class VBoxRoot extends VBox {
    private static VBoxRoot instance;
    private static ControleurAppli controleur;
    private static VBoxParcours vBoxParcours;
    private static HBoxResultat resultat;
    private static AffichageChemin affichage;
    private static MenuAlgoScenario menu;


    public VBoxRoot() {
        instance = this;
        menu = new MenuAlgoScenario();
        affichage = new AffichageChemin();
        controleur = new ControleurAppli();
        vBoxParcours = new VBoxParcours();
        resultat = new HBoxResultat(affichage, vBoxParcours);

        this.getChildren().addAll(menu, resultat);
    }

    public static VBoxRoot getInstance() {return instance;}
    public static VBoxParcours getVBoxParcours() {return vBoxParcours;}
    public static HBoxResultat getResultat() {return resultat;}
    public static AffichageChemin getAffichage(){return affichage;}
    public static MenuAlgoScenario getMenu(){return menu;}
}
