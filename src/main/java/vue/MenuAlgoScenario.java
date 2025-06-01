package vue;

import controleur.ControleurAppli;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.File;

/**
 * MenuAlgoScenario est une interface utilisateur affichée sous forme de barre horizontale (HBox)
 * permettant de sélectionner un scénario, un algorithme, une méthode greedy (si applicable),
 * un paramètre k (si applicable), et de lancer l'exécution via un bouton "Valider".
 */
public class MenuAlgoScenario extends HBox {

    // Composants de l'interface
    private final ComboBox<String> scenarioCombo = new ComboBox<>();
    private final ComboBox<String> algoCombo = new ComboBox<>();
    private final ComboBox<String> methodeGreedyCombo = new ComboBox<>();
    private final TextField kField = new TextField();
    private final Button boutonValider = new Button("Valider");

    // Référence vers le contrôleur principal
    private ControleurAppli controleur;

    /**
     * Constructeur du menu : initialise les composants,
     * configure leur comportement, et les ajoute au layout.
     */
    public MenuAlgoScenario() {
        this.setSpacing(10);
        this.setPadding(new Insets(10));

        // Chargement des scénarios depuis le dossier "scenarios"
        File folder = new File("scenarios");
        File[] files = folder.listFiles((dir, name) -> name.startsWith("scenario_") && name.endsWith(".txt"));
        if (files != null) {
            for (File file : files) {
                scenarioCombo.getItems().add(file.getName().replace(".txt", ""));
            }
        }
        scenarioCombo.setPromptText("Scénarios");

        // Liste des algorithmes disponibles
        algoCombo.getItems().addAll("Algo de base", "Algo heuristique", "K possibilités");
        algoCombo.setPromptText("Algorithme");
        algoCombo.getSelectionModel().selectFirst();

        // Liste des méthodes pour l'algo heuristique
        methodeGreedyCombo.getItems().addAll(
                "1 - Ville la plus proche",
                "2 - Ville la plus éloignée",
                "3 - Ordre alphabétique",
                "4 - Débloque le plus de dépendances",
                "5 - Ville la moins visitée"
        );
        methodeGreedyCombo.setPromptText("Méthode greedy");
        methodeGreedyCombo.setVisible(false); // Masqué par défaut

        // Champ pour saisir k (si "K possibilités")
        kField.setPromptText("k possibilités");
        kField.setVisible(false);

        // Lorsque l'utilisateur change d'algorithme, on affiche ou masque les champs spécifiques
        algoCombo.setOnAction(e -> {
            String selected = algoCombo.getValue();
            methodeGreedyCombo.setVisible("Algo heuristique".equals(selected));
            kField.setVisible("K possibilités".equals(selected));
            verifierEtExecuter(); // Re-vérifie et exécute si tout est prêt
        });

        // Ajout de tous les composants au layout horizontal
        this.getChildren().addAll(scenarioCombo, algoCombo, methodeGreedyCombo, kField, boutonValider);

        // Ajout de listeners pour déclencher l'exécution dès qu'un champ change
        ChangeListener<Object> listener = (obs, oldVal, newVal) -> verifierEtExecuter();
        scenarioCombo.valueProperty().addListener(listener);
        algoCombo.valueProperty().addListener(listener);
        methodeGreedyCombo.valueProperty().addListener(listener);
        kField.textProperty().addListener(listener);
    }

    /**
     * Affecte le contrôleur à utiliser pour déclencher l'exécution.
     * @param controleur le contrôleur principal de l'application
     */
    public void setControleur(ControleurAppli controleur) {
        this.controleur = controleur;
    }

    /**
     * Vérifie que toutes les conditions sont remplies pour exécuter l'algorithme
     * (champs remplis, valeurs valides...) puis appelle le contrôleur si tout est OK.
     */
    private void verifierEtExecuter() {
        String algo = getAlgo();

        // Vérifie que le scénario et l'algo sont sélectionnés
        if (getScenario() == null || getScenario().isEmpty()) return;
        if (algo == null || algo.isEmpty()) return;

        // Si l'algo est heuristique, une méthode doit être sélectionnée
        if (algo.equals("Algo heuristique")) {
            if (getMethodeGreedy() == null || getMethodeGreedy().isEmpty()) return;
        }

        // Si l'algo est "K possibilités", il faut un k entier valide
        if (algo.equals("K possibilités")) {
            if (getK() == null || getK().isEmpty()) return;
            try {
                Integer.parseInt(getK());
            } catch (NumberFormatException e) {
                return;
            }
        }

        // Si tout est valide, on lance l'exécution via le contrôleur
        if (controleur != null) {
            controleur.execute();
        }
    }

    /** @return le scénario sélectionné (ex: "scenario_1") */
    public String getScenario() {
        return scenarioCombo.getValue();
    }

    /** @return l'algorithme sélectionné */
    public String getAlgo() {
        return algoCombo.getValue();
    }

    /** @return la méthode greedy choisie (si "Algo heuristique") */
    public String getMethodeGreedy() {
        return methodeGreedyCombo.getValue();
    }

    /** @return la valeur k (si "K possibilités") */
    public String getK() {
        return kField.getText();
    }

    /** @return le bouton permettant de valider manuellement les choix */
    public Button getBoutonValider() {
        return boutonValider;
    }
}

