package vue;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;


public class MenuAlgoScenario extends HBox {

    private final ComboBox<String> scenarioCombo = new ComboBox<>();
    private final ComboBox<String> algoCombo = new ComboBox<>();
    private final ComboBox<String> methodeGreedyCombo = new ComboBox<>();
    private final TextField kField = new TextField();
    private final Button valider = new Button("Valider");  // ✅ ajout ici

    public MenuAlgoScenario() {
        this.setSpacing(10);
        this.setPadding(new Insets(10));

        scenarioCombo.getItems().addAll("scenario_0", "scenario_1", "scenario_2");
        scenarioCombo.setPromptText("Scénarios");

        algoCombo.getItems().addAll("Algo de base", "Algo heuristique", "K possibilités");
        algoCombo.setPromptText("Algorithme");

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

        algoCombo.setOnAction(e -> {
            String selected = algoCombo.getValue();
            methodeGreedyCombo.setVisible("Algo heuristique".equals(selected));
            kField.setVisible("K possibilités".equals(selected));
        });

        valider.setId("Valider");

        this.getChildren().addAll(scenarioCombo, algoCombo, methodeGreedyCombo, kField, valider);
    }

    public String getScenario() { return scenarioCombo.getValue(); }
    public String getAlgo() { return algoCombo.getValue(); }
    public String getGreedyIndex() { return methodeGreedyCombo.getValue(); }
    public String getK() { return kField.getText(); }

    public Button getBoutonValider() { return valider; }  // ✅ ajout de cette méthode
}
