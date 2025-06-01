package vue;

import controleur.ControleurAppli;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.File;

/**
 * MenuAlgoScenario est une interface utilisateur JavaFX permettant
 * de sélectionner un scénario, un algorithme, une méthode greedy
 * ou un nombre k de possibilités. Lorsqu'une sélection valide est faite,
 * le contrôleur déclenche automatiquement l'exécution.
 */
public class MenuAlgoScenario extends HBox {

    // Composants de l'interface
    private final ComboBox<String> scenarioCombo = new ComboBox<>();
    private final ComboBox<String> algoCombo = new ComboBox<>();
    private final ComboBox<String> methodeGreedyCombo = new ComboBox<>();
    private final TextField kField = new TextField();

    private ControleurAppli controleur; // Contrôleur principal pour exécuter la logique métier

    /**
     * Constructeur qui initialise le menu de sélection avec ses composants,
     * leurs valeurs possibles et leurs comportements.
     */
    public MenuAlgoScenario() {
        this.setSpacing(10);
        this.setPadding(new Insets(10));

        // Chargement dynamique des scénarios depuis le dossier /scenarios
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
        algoCombo.getSelectionModel().selectFirst(); // Par défaut

        // Méthodes heuristiques disponibles
        methodeGreedyCombo.getItems().addAll(
                "1 - Ville la plus proche",
                "2 - Ville la plus éloignée",
                "3 - Ordre alphabétique",
                "4 - Débloque le plus de dépendances",
                "5 - Ville la moins visitée"
        );
        methodeGreedyCombo.setPromptText("Méthode greedy");
        methodeGreedyCombo.setVisible(false); // Cachée sauf pour algo heuristique

        // Champ pour le nombre de possibilités (k)
        kField.setPromptText("k possibilités");
        kField.setVisible(false); // Caché sauf pour K possibilités

        // Gestion de la visibilité selon l'algorithme sélectionné
        algoCombo.setOnAction(e -> {
            String selected = algoCombo.getValue();
            methodeGreedyCombo.setVisible("Algo heuristique".equals(selected));
            kField.setVisible("K possibilités".equals(selected));
            verifierEtExecuter(); // Re-vérifie à chaque changement
        });

        // Ajout des éléments graphiques au layout
        this.getChildren().addAll(scenarioCombo, algoCombo, methodeGreedyCombo, kField);

        // Listener commun à tous les champs pour détecter les changements
        ChangeListener<Object> listener = (obs, oldVal, newVal) -> verifierEtExecuter();
        scenarioCombo.valueProperty().addListener(listener);
        algoCombo.valueProperty().addListener(listener);
        methodeGreedyCombo.valueProperty().addListener(listener);
        kField.textProperty().addListener(listener);
    }

    /**
     * Associe un contrôleur à ce menu pour exécuter l'application
     * @param controleur le contrôleur de l'application
     */
    public void setControleur(ControleurAppli controleur) {
        this.controleur = controleur;
    }

    /**
     * Vérifie si tous les champs nécessaires sont valides selon
     * l'algorithme sélectionné, puis déclenche le contrôleur.
     */
    private void verifierEtExecuter() {
        String algo = getAlgo();

        // Conditions générales
        if (getScenario() == null || getScenario().isEmpty()) return;
        if (algo == null || algo.isEmpty()) return;

        // Si l'algo heuristique est sélectionné, vérifier la méthode greedy
        if (algo.equals("Algo heuristique")) {
            if (getMethodeGreedy() == null || getMethodeGreedy().isEmpty()) return;
        }

        // Si l'algo K possibilités est sélectionné, vérifier que k est un entier
        if (algo.equals("K possibilités")) {
            if (getK() == null || getK().isEmpty()) return;
            try {
                Integer.parseInt(getK());
            } catch (NumberFormatException e) {
                return; // k non valide
            }
        }

        // Si tout est bon, on déclenche l'exécution via le contrôleur
        if (controleur != null) {
            controleur.execute();
        }
    }

    /**
     * @return le nom du scénario sélectionné (sans .txt)
     */
    public String getScenario() {
        return scenarioCombo.getValue();
    }

    /**
     * @return le nom de l'algorithme sélectionné
     */
    public String getAlgo() {
        return algoCombo.getValue();
    }

    /**
     * @return la méthode greedy sélectionnée (si applicable)
     */
    public String getMethodeGreedy() {
        return methodeGreedyCombo.getValue();
    }

    /**
     * @return le nombre k de possibilités entré par l'utilisateur (si applicable)
     */
    public String getK() {
        return kField.getText();
    }
}
