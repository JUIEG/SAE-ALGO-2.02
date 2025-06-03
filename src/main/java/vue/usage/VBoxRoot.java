package vue.usage;

import controleur.ControleurAppli;
import javafx.scene.layout.VBox;
import vue.MenuPrincipal;
import vue.creation.VBoxCreation;

public class VBoxRoot extends VBox {

    private static VBoxRoot instance;

    private final MenuAlgoScenario menu;
    private final AffichageChemin affichage;
    private final VBoxParcours vBoxParcours;
    private final HBoxResultat resultat;
    private final ControleurAppli controleur;

    private final MenuPrincipal menuPrincipal;
    private final VBoxCreation creation;
    private final VBox usage;

    public VBoxRoot() {
        instance = this;
        this.setSpacing(10);

        // Menu général (utilisation / création)
        menuPrincipal = new MenuPrincipal();
        this.getChildren().add(menuPrincipal);

        // Partie "utilisation"
        menu = new MenuAlgoScenario();
        affichage = new AffichageChemin();
        vBoxParcours = new VBoxParcours();
        resultat = new HBoxResultat(affichage, vBoxParcours);
        controleur = new ControleurAppli(menu, vBoxParcours, resultat, affichage);
        menu.setControleur(controleur);

        usage = new VBox(menu, resultat);

        // Partie "création"
        creation = new VBoxCreation();

        // Affichage initial : mode utilisation
        this.getChildren().add(usage);

        // Gestion du changement de mode
        menuPrincipal.getItemUtilisation().setOnAction(e -> basculerVersUtilisation());
        menuPrincipal.getItemCreation().setOnAction(e -> basculerVersCreation());
    }

    private void basculerVersUtilisation() {
        this.getChildren().remove(creation);
        if (!this.getChildren().contains(usage)) {
            this.getChildren().add(usage);
        }
    }

    private void basculerVersCreation() {
        this.getChildren().remove(usage);
        if (!this.getChildren().contains(creation)) {
            this.getChildren().add(creation);
        }
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
