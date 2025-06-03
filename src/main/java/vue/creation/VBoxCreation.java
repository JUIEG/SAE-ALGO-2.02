package vue.creation;

import controleur.ControleurCreation;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import modele.Membre;
import modele.Vente;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;



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

        // Labels avec mnemonics pour ComboBox
        Label labelMode = new Label("_Mode:");
        labelMode.setMnemonicParsing(true);

        Label labelScenario = new Label("_Scénario:");
        labelScenario.setMnemonicParsing(true);
        labelScenario.setVisible(false);

        // Barre supérieure
        modeCombo = new ComboBox<>();
        modeCombo.getItems().addAll("Créer un scénario", "Modifier un scénario existant");
        modeCombo.setPromptText("Choisissez un mode");
        modeCombo.getSelectionModel().selectFirst();
        labelMode.setLabelFor(modeCombo);

        scenarioCombo = new ComboBox<>();
        scenarioCombo.setPromptText("Scénario à modifier");
        scenarioCombo.setVisible(false);
        labelScenario.setLabelFor(scenarioCombo);

        modeCombo.setOnAction(e -> {
            boolean isModif = "Modifier un scénario existant".equals(modeCombo.getValue());
            scenarioCombo.setVisible(isModif);
            labelScenario.setVisible(isModif);
            if (isModif) {
                scenarioCombo.getItems().setAll(listerFichiersScenario("scenarios"));
            }
        });

        btnValiderScenario = new Button("_Créer / Modifier");
        btnValiderScenario.setMnemonicParsing(true);
        btnValiderScenario.setId("creerScenario");
        btnValiderScenario.setOnAction(controleur);

        HBox barreSuperieure = new HBox(10, labelMode, modeCombo, labelScenario, scenarioCombo, btnValiderScenario);

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
        rangCol.setReorderable(false);
        rangCol.setSortable(false);
        colDepart.setReorderable(false);
        colDepart.setSortable(false);
        colArrivee.setReorderable(false);
        colArrivee.setSortable(false);


        // Liste des membres
        listePseudoVille = new ListView<>();
        listePseudoVille.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listePseudoVille.setPrefWidth(300);
        try {
            listePseudoVille.getItems().addAll(Membre.formaterPseudosAvecVille());
        } catch (Exception e) {
            listePseudoVille.getItems().add("Erreur de chargement des membres");
        }

        // Boutons entre les deux
        boutonAjouterVente = new Button("_←");
        boutonAjouterVente.setMnemonicParsing(true);
        boutonAjouterVente.setId("ajouterVente");
        boutonAjouterVente.setOnAction(controleur);

        boutonSupprimerVente = new Button("_→");
        boutonSupprimerVente.setMnemonicParsing(true);
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

    public List<String> listerFichiersScenario(String racine) {
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

    public int getNumeroScenarioActuel() {
        String nomScenario = scenarioCombo.getSelectionModel().getSelectedItem(); // par ex. "scenario_2"
        if (nomScenario == null) {
            throw new IllegalArgumentException("Aucun scénario sélectionné");
        }

        // Extraire le numéro à partir du nom
        try {
            // Supposons que le format soit "scenario_X" où X est un entier
            String[] parts = nomScenario.split("_");
            return Integer.parseInt(parts[1]);
        } catch (Exception e) {
            throw new IllegalArgumentException("Nom de fichier invalide : " + nomScenario, e);
        }
    }

    public boolean isScenarioSelectionne() {
        return scenarioCombo.getSelectionModel().getSelectedItem() != null;
    }

    public String getNomScenarioSelectionne() {
        return scenarioCombo.getSelectionModel().getSelectedItem();
    }
    public ComboBox<String> getScenarioCombo() {
        return scenarioCombo;
    }

}
