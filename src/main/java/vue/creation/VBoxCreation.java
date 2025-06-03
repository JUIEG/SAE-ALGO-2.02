package vue.creation;

import controleur.ControleurCreation;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import modele.Membre;
import modele.Vente;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VBoxCreation extends VBox {

    private final ComboBox<String> modeCombo;
    private final ComboBox<String> scenarioCombo;
    private final Button btnValiderScenario;
    private final TableView<Vente> tableauVentes;
    private final ListView<String> listePseudoVille;
    private final Button boutonAjouterVente;
    private final Button boutonSupprimerVente;

    public VBoxCreation() {
        ControleurCreation controleur = new ControleurCreation(this);
        this.setSpacing(10);
        this.setPadding(new Insets(10));
        this.setPrefWidth(800);

        // Barre supérieure
        modeCombo = new ComboBox<>();
        modeCombo.getItems().addAll("Créer un scénario", "Modifier un scénario existant");
        modeCombo.setPromptText("Choisissez un mode");
        modeCombo.getSelectionModel().selectFirst();

        scenarioCombo = new ComboBox<>();
        scenarioCombo.setPromptText("Scénario à modifier");
        scenarioCombo.setVisible(false);

        modeCombo.setOnAction(e -> {
            boolean isModif = "Modifier un scénario existant".equals(modeCombo.getValue());
            scenarioCombo.setVisible(isModif);
            if (isModif) {
                scenarioCombo.getItems().setAll(listerFichiersScenario("scenarios"));
            }
        });

        btnValiderScenario = new Button("Créer / Modifier");
        btnValiderScenario.setId("creerScenario");
        btnValiderScenario.setOnAction(controleur);

        HBox barreSuperieure = new HBox(10, modeCombo, scenarioCombo, btnValiderScenario);

        // Tableau des ventes
        tableauVentes = new TableView<>();
        TableColumn<Vente, String> rangCol = new TableColumn<>("Rang");
        rangCol.setCellValueFactory(c -> new ReadOnlyStringWrapper(String.valueOf(tableauVentes.getItems().indexOf(c.getValue()) + 1)));

        TableColumn<Vente, String> colDepart = new TableColumn<>("Ville Départ");
        colDepart.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getVilleVendeur()));

        TableColumn<Vente, String> colArrivee = new TableColumn<>("Ville Arrivée");
        colArrivee.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getVilleAcheteur()));

        tableauVentes.getColumns().addAll(rangCol, colDepart, colArrivee);
        tableauVentes.setPrefWidth(300);

        // Liste des membres
        listePseudoVille = new ListView<>();
        listePseudoVille.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listePseudoVille.setPrefWidth(300);
        try {
            listePseudoVille.getItems().addAll(Membre.formaterPseudosAvecVille());
        } catch (Exception e) {
            listePseudoVille.getItems().add("Erreur de chargement des membres");
        }

        // Boutons entre les deux
        boutonAjouterVente = new Button("←");
        boutonAjouterVente.setId("ajouterVente");
        boutonAjouterVente.setOnAction(controleur);

        boutonSupprimerVente = new Button("→");
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

    private List<String> listerFichiersScenario(String racine) {
        File dossier = new File(racine);
        return parcoursFichiers(dossier, "");
    }

    private List<String> parcoursFichiers(File racine, String cheminRelatif) {
        List<String> resultats = new ArrayList<>();
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

    // Méthodes d'accès pour le contrôleur
    public Button getBoutonAjouterVente() {
        return boutonAjouterVente;
    }

    public Button getBtnValiderScenario() {
        return btnValiderScenario;
    }

    public List<String> getSelectionVerticale() {
        return listePseudoVille.getSelectionModel().getSelectedItems();
    }

    public void ajouterVentes(List<Vente> nouvellesVentes) {
        tableauVentes.getItems().addAll(nouvellesVentes);
    }

    public List<Vente> getListeVentes() {
        return tableauVentes.getItems();
    }

    public boolean isModeCreation() {
        return "Créer un scénario".equals(modeCombo.getValue());
    }

    public String getNomScenario() {
        return isModeCreation() ? null : scenarioCombo.getValue();
    }

    public void reinitialiserFormulaire() {
        tableauVentes.getItems().clear();
        listePseudoVille.getSelectionModel().clearSelection();
    }

    public void rafraichirListeScenario() {
        scenarioCombo.getItems().setAll(listerFichiersScenario("scenarios"));
    }
}