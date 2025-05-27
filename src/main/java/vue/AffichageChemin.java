package vue;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.cell.PropertyValueFactory;

public class AffichageChemin extends VBox {

    private final TableView<Ligne> table = new TableView<>();

    public AffichageChemin() {
        TableColumn<Ligne, Integer> colIndex = new TableColumn<>("Position");
        colIndex.setCellValueFactory(new PropertyValueFactory<>("index"));

        TableColumn<Ligne, String> colVille = new TableColumn<>("Ville");
        colVille.setCellValueFactory(new PropertyValueFactory<>("ville"));

        TableColumn<Ligne, Integer> colDistance = new TableColumn<>("Distance");
        colDistance.setCellValueFactory(new PropertyValueFactory<>("distance"));

        table.getColumns().addAll(colIndex, colVille, colDistance);
        table.setPlaceholder(new Label("Aucun résultat"));
        this.getChildren().add(table);
    }

    // Classe interne pour représenter une ligne
    public static class Ligne {
        private final int index;
        private final String ville;
        private final int distance;

        public Ligne(int index, String ville, int distance) {
            this.index = index;
            this.ville = ville;
            this.distance = distance;
        }

        public int getIndex() { return index; }
        public String getVille() { return ville; }
        public int getDistance() { return distance; }
    }

    public void setChemin(java.util.List<Ligne> lignes) {
        table.setItems(FXCollections.observableArrayList(lignes));
    }
}
