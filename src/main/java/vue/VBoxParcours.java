package vue;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

/**
 * VBox qui affiche la liste du parcours ainsi que la distance totale associée.
 */
public class VBoxParcours extends VBox {

    // Liste affichant les villes du parcours
    private final ListView<String> listeParcours = new ListView<>();

    // Label affichant la distance totale du parcours
    private final Label distanceTotal = new Label("Distance Totale : 0 km");

    /**
     * Constructeur qui initialise la mise en page et les composants.
     */
    public VBoxParcours() {
        this.setSpacing(10);  // Espacement vertical entre enfants
        this.setPadding(new Insets(10));  // Marges internes

        // Message affiché si aucun parcours n'a été généré
        listeParcours.setPlaceholder(new Label("Aucun parcours généré"));

        // Ajout des composants à la VBox
        this.getChildren().addAll(listeParcours, distanceTotal);
    }

    /**
     * Met à jour la liste des villes du parcours affiché.
     * @param parcours liste de noms de villes
     */
    public void setParcours(java.util.List<String> parcours) {
        listeParcours.getItems().setAll(parcours);
    }
    /** Affiche plusieurs parcours (k possibilités) */
    public void setParcoursMultiple(List<List<String>> tousLesParcours) {
        listeParcours.getItems().clear();
        int i = 1;
        for (List<String> p : tousLesParcours) {
            listeParcours.getItems().add("Parcours #" + (i++) + " : " + p.toString());
        }
    }

    /**
     * Met à jour l'affichage de la distance totale du parcours.
     * @param km distance totale en kilomètres
     */
    public void setDistance(int km) {
        distanceTotal.setText("Distance Totale : " + km + " km");
    }
}
