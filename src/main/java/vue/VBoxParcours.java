package vue;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class VBoxParcours extends VBox {

    private final ListView<String> listeParcours = new ListView<>();
    private final Label distanceTotal = new Label("Distance Totale : 0 km");

    public VBoxParcours() {
        this.setSpacing(10);
        this.setPadding(new Insets(10));

        listeParcours.setPlaceholder(new Label("Aucun parcours généré"));

        this.getChildren().addAll(listeParcours, distanceTotal);
    }

    // Méthodes pour mettre à jour l'affichage
    public void setParcours(java.util.List<String> parcours) {
        listeParcours.getItems().setAll(parcours);
    }

    public void setDistance(int km) {
        distanceTotal.setText("Distance Totale : " + km + " km");
    }
}
