package vue;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.cell.PropertyValueFactory;
/**
 * Composant JavaFX affichant un chemin sous forme de table.
 * Chaque ligne représente une étape avec l'index, la ville visitée et la distance cumulée.
 */
public class AffichageChemin extends VBox {

    // Table principale affichant les étapes du chemin
    private final TableView<Ligne> table = new TableView<>();

    /**
     * Constructeur du composant d'affichage.
     * Initialise la table avec les colonnes : position, ville, et distance.
     */
    public AffichageChemin() {
        // Colonne affichant la position (index) dans le parcours
        TableColumn<Ligne, Integer> colIndex = new TableColumn<>("Position");
        colIndex.setCellValueFactory(new PropertyValueFactory<>("index"));

        // Colonne affichant le nom de la ville visitée
        TableColumn<Ligne, String> colVille = new TableColumn<>("Ville");
        colVille.setCellValueFactory(new PropertyValueFactory<>("ville"));

        // Colonne affichant la distance cumulée jusqu'à cette étape
        TableColumn<Ligne, Integer> colDistance = new TableColumn<>("Distance");
        colDistance.setCellValueFactory(new PropertyValueFactory<>("distance"));

        // Ajout des colonnes à la table
        table.getColumns().addAll(colIndex, colVille, colDistance);
        table.setPlaceholder(new Label("Aucun résultat")); // Message quand la table est vide

        // Ajout de la table à la VBox (conteneur vertical)
        this.getChildren().add(table);
    }

    /**
     * Classe interne représentant une ligne du tableau.
     * Contient la position, la ville visitée et la distance cumulée.
     */
    public static class Ligne {
        private final int index;       // Position dans le parcours
        private final String ville;    // Nom de la ville
        private final int distance;    // Distance cumulée

        /**
         * Constructeur d'une ligne d'affichage.
         * @param index Position dans le parcours
         * @param ville Nom de la ville
         * @param distance Distance cumulée à cette étape
         */
        public Ligne(int index, String ville, int distance) {
            this.index = index;
            this.ville = ville;
            this.distance = distance;
        }

        // Getters pour les propriétés utilisées dans le tableau
        public int getIndex() { return index; }
        public String getVille() { return ville; }
        public int getDistance() { return distance; }
    }

    /**
     * Met à jour les lignes du tableau avec un nouveau chemin.
     * @param lignes Liste des étapes à afficher (position, ville, distance cumulée)
     */
    public void setChemin(java.util.List<Ligne> lignes) {
        table.setItems(FXCollections.observableArrayList(lignes));
    }
}

