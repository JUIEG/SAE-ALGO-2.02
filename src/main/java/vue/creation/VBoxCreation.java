package vue.creation;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import modele.Vente;

import java.io.File;
import java.util.List;

public class VBoxCreation extends VBox {

    private final ComboBox<String> modeCombo;           // Création ou Modification
    private final ComboBox<String> scenarioCombo;       // Liste des scénarios si modification
    private final Button btnValiderScenario;            // Créer/modifier le fichier
    private final TableView<Vente> tableauVentes;       // Ventes sélectionnées
    private final ListView<String> listePseudoVille;    // Pseudo (Ville)
    private final Button boutonAjouterVente;
    private final Button boutonSupprimerVente;

    public VBoxCreation() {
        this.setSpacing(10);
        this.setPadding(new Insets(10));
        this.setPrefWidth(800);

        // Barre supérieure : Choix mode + scénario + validation
        modeCombo = new ComboBox<>();
        modeCombo.getItems().addAll("Créer un scénario", "Modifier un scénario existant");
        modeCombo.setPromptText("Choisissez un mode");

        scenarioCombo = new ComboBox<>();
        scenarioCombo.setPromptText("Scénario à modifier");
        scenarioCombo.setVisible(false);

        // Affiche la combo des scénarios uniquement si "modifier" est sélectionné
        modeCombo.setOnAction(e -> {
            scenarioCombo.setVisible("Modifier un scénario existant".equals(modeCombo.getValue()));
            if (scenarioCombo.isVisible()) {
                scenarioCombo.getItems().setAll(listerFichiersScenario("scenarios"));
            }
        });

        btnValiderScenario = new Button("Créer / Modifier");

        HBox barreSuperieure = new HBox(10, modeCombo, scenarioCombo, btnValiderScenario);

        // Tableau des ventes à gauche
        tableauVentes = new TableView<>();
        TableColumn<Vente, String> rangCol = new TableColumn<>("Rang");
        rangCol.setCellValueFactory(c -> new ReadOnlyStringWrapper(String.valueOf(tableauVentes.getItems().indexOf(c.getValue()) + 1)));

        TableColumn<Vente, String> colDepart = new TableColumn<>("Ville Départ");
        colDepart.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getVilleVendeur()));

        TableColumn<Vente, String> colArrivee = new TableColumn<>("Ville Arrivée");
        colArrivee.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getVilleAcheteur()));

        tableauVentes.getColumns().addAll(rangCol, colDepart, colArrivee);
        tableauVentes.setPrefWidth(300);

        // Liste des pseudo (ville) à droite
        listePseudoVille = new ListView<>();
        listePseudoVille.setPrefWidth(300);

        // Chargement des membres
        try {
            List<String> membres = modele.Membre.formaterPseudosAvecVille();
            listePseudoVille.getItems().addAll(membres);
        } catch (Exception e) {
            listePseudoVille.getItems().add("⚠️ Erreur de chargement des membres");
        }

        // Boutons entre les deux
        boutonAjouterVente = new Button("←");
        boutonSupprimerVente = new Button("➜");9

        boutonAjouterVente.setOnAction(e -> {
            List<String> selected = listePseudoVille.getSelectionModel().getSelectedItems();
            if (selected.size() == 2) {
                String depart = extraireVilleDepuisTexte(selected.get(0));
                String arrivee = extraireVilleDepuisTexte(selected.get(1));
                tableauVentes.getItems().add(new Vente(depart, arrivee));
            }
        });

        boutonSupprimerVente.setOnAction(e -> {
            Vente selected = tableauVentes.getSelectionModel().getSelectedItem();
            if (selected != null) {
                tableauVentes.getItems().remove(selected);
            }
        });

        VBox boutonsCentre = new VBox(10, boutonAjouterVente, boutonSupprimerVente);
        boutonsCentre.setPadding(new Insets(40, 0, 0, 0));

        HBox contenuPrincipal = new HBox(10, tableauVentes, boutonsCentre, listePseudoVille);

        this.getChildren().addAll(barreSuperieure, contenuPrincipal);
    }

    /**
     * Récupère tous les fichiers "scenario_*.txt" dans un dossier récursivement
     */
    private List<String> listerFichiersScenario(String racine) {
        File dossier = new File(racine);
        return parcoursFichiers(dossier, "");
    }

    private List<String> parcoursFichiers(File racine, String cheminRelatif) {
        List<String> resultats = new java.util.ArrayList<>();
        File[] fichiers = racine.listFiles();
        if (fichiers != null) {
            for (File f : fichiers) {
                if (f.isDirectory()) {
                    resultats.addAll(parcoursFichiers(f, cheminRelatif + f.getName() + "/"));
                } else if (f.getName().startsWith("scenario_") && f.getName().endsWith(".txt")) {
                    resultats.add(cheminRelatif + f.getName().replace(".txt", ""));
                }
            }
        }
        return resultats;
    }

    private String extraireVilleDepuisTexte(String texte) {
        if (texte == null || !texte.contains("(")) return "";
        int start = texte.indexOf('(');
        int end = texte.indexOf(')');
        return texte.substring(start + 1, end);
    }

    public Button getBtnValiderScenario() {
        return btnValiderScenario;
    }

    public List<Vente> getVentesActuelles() {
        return tableauVentes.getItems();
    }

    public String getNomScenario() {
        if ("Créer un scénario".equals(modeCombo.getValue())) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Nom du scénario");
            dialog.setHeaderText("Entrez le nom du scénario (ex: scenario_9)");
            dialog.setContentText("Nom :");
            return dialog.showAndWait().orElse(null);
        } else {
            return scenarioCombo.getValue();
        }
    }
}
