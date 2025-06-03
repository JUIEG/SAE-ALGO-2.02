package vue.usage;

import controleur.ControleurAppli;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.File;

public class MenuAlgoScenario extends HBox {

    private final ComboBox<String> scenarioCombo = new ComboBox<>();
    private final ComboBox<String> algoCombo = new ComboBox<>();
    private final ComboBox<String> methodeGreedyCombo = new ComboBox<>();
    private final TextField kField = new TextField();


    private ControleurAppli controleur; // pour déclencher l'exécution

    public MenuAlgoScenario() {
        this.setSpacing(10);
        this.setPadding(new Insets(10));

        // Remplissage des scénarios
        File folder = new File("scenarios");
        File[] files = folder.listFiles((dir, name) -> name.startsWith("scenario_") && name.endsWith(".txt"));
        if (files != null) {
            for (File file : files) {
                scenarioCombo.getItems().add(file.getName().replace(".txt", ""));
            }
        }
        scenarioCombo.setPromptText("Scénarios");

        // Ajout des algorithmes
        algoCombo.getItems().addAll("Algo de base", "Algo heuristique", "K possibilités");
        algoCombo.setPromptText("Algorithme");
        algoCombo.getSelectionModel().selectFirst();

        // Méthodes greedy
        methodeGreedyCombo.getItems().addAll(
                "1 - Ville la plus proche",
                "2 - Ville la plus éloignée",
                "3 - Ordre alphabétique",
                "4 - Débloque le plus de dépendances",
                "5 - Ville la moins visitée"
        );
        methodeGreedyCombo.setPromptText("Méthode greedy");
        methodeGreedyCombo.setVisible(false);

        kField.setPromptText("k possibilités");
        kField.setVisible(false);

        // Gestion visibilité + vérification
        algoCombo.setOnAction(e -> {
            String selected = algoCombo.getValue();
            methodeGreedyCombo.setVisible("Algo heuristique".equals(selected));
            kField.setVisible("K possibilités".equals(selected));
            verifierEtExecuter(); // après changement
        });

        // Ajout au layout
        this.getChildren().addAll(scenarioCombo, algoCombo, methodeGreedyCombo, kField);

        // Ecouteurs pour tous les champs
        ChangeListener<Object> listener = (obs, oldVal, newVal) -> verifierEtExecuter();
        scenarioCombo.valueProperty().addListener(listener);
        algoCombo.valueProperty().addListener(listener);
        methodeGreedyCombo.valueProperty().addListener(listener);
        kField.textProperty().addListener(listener);
    }

    public void setControleur(ControleurAppli controleur) {
        this.controleur = controleur;
    }

    private void verifierEtExecuter() {
        String algo = getAlgo();
        if (getScenario() == null || getScenario().isEmpty()) return;
        if (algo == null || algo.isEmpty()) return;

        if (algo.equals("Algo heuristique")) {
            if (getMethodeGreedy() == null || getMethodeGreedy().isEmpty()) return;
        }

        if (algo.equals("K possibilités")) {
            if (getK() == null || getK().isEmpty()) return;
            try {
                Integer.parseInt(getK());
            } catch (NumberFormatException e) {
                return;
            }
        }

        if (controleur != null) {
            controleur.execute();
        }
    }

    public String getScenario() { return scenarioCombo.getValue(); }
    public String getAlgo() { return algoCombo.getValue(); }
    public String getMethodeGreedy() { return methodeGreedyCombo.getValue(); }
    public String getK() { return kField.getText(); }
}
