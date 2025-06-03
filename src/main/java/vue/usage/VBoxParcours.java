package vue.usage;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.List;

public class VBoxParcours extends VBox {

    private final ListView<String> listeParcours = new ListView<>();
    private final Label distanceTotal = new Label("Distance Totale : 0 km");

    public VBoxParcours() {
        this.setSpacing(10);
        this.setPadding(new Insets(10));

        listeParcours.setPlaceholder(new Label("Aucun parcours généré"));
        this.getChildren().addAll(listeParcours, distanceTotal);
    }

    /** Affiche un seul parcours sous forme [ville1, ville2, ...] */
    public void setParcours(List<String> parcours) {
        listeParcours.getItems().setAll(List.of(parcours.toString()));
    }

    /** Affiche plusieurs parcours (k possibilités) */
    public void setParcoursMultiple(List<List<String>> tousLesParcours) {
        listeParcours.getItems().clear();
        int i = 1;
        for (List<String> p : tousLesParcours) {
            listeParcours.getItems().add("Parcours #" + (i++) + " : " + p.toString());
        }
    }

    public void setDistance(int km) {
        distanceTotal.setText("Distance Totale : " + km + " km");
    }
}
