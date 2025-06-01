package vue;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principale de l'application JavaFX d'optimisation de parcours.
 * Elle initialise et affiche la fenêtre principale.
 */
public class APPLI extends Application {

    @Override
    public void start(Stage stage) {
        VBoxRoot root = new VBoxRoot();

        root.getMenu().getBoutonValider().setOnAction(root.getControleur());

        Scene scene = new Scene(root, 800, 600);

        scene.getStylesheets().add("file:src/css/style.css");

        stage.setScene(scene);
        stage.setTitle("Optimisation de Parcours");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

}
