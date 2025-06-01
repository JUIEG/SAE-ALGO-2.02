package vue;

import controleur.ControleurAppli;
import javafx.scene.layout.VBox;

public class VBoxRoot extends VBox {
    private static VBoxRoot instance;

    private final MenuAlgoScenario menu;
    private final AffichageChemin affichage;
    private final VBoxParcours vBoxParcours;
    private final HBoxResultat resultat;
    private final ControleurAppli controleur;

    public VBoxRoot() {
        instance = this;

        // Initialisation des composants
        menu = new MenuAlgoScenario();
        affichage = new AffichageChemin();
        vBoxParcours = new VBoxParcours();
        resultat = new HBoxResultat(affichage, vBoxParcours);

        controleur = new ControleurAppli(menu, vBoxParcours, resultat, affichage);

        menu.setControleur(controleur);

        // Affichage
        this.setSpacing(10);
        this.getChildren().addAll(menu, resultat);
    }

    public static VBoxRoot getInstance() {
        return instance;
    }

    public static VBoxParcours getVBoxParcours() {
        return instance.vBoxParcours;
    }

    public static HBoxResultat getResultat() {
        return instance.resultat;
    }

    public static AffichageChemin getAffichage() {
        return instance.affichage;
    }

    public static MenuAlgoScenario getMenu() {
        return instance.menu;
    }

    public static ControleurAppli getControleur() {
        return instance.controleur;
    }
}

