package vue;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import vue.usage.VBoxRoot;

import java.io.File;

/**
 * Classe principale de l'application JavaFX d'optimisation de parcours.
 * Elle initialise et affiche la fenêtre principale.
 */
public class APPLI extends Application {

    @Override
    public void start(Stage stage) {
        VBoxRoot root = new VBoxRoot();

        Scene scene = new Scene(root, 800, 600);

        scene.getStylesheets().add(new File("css/style.css").toURI().toString());

        stage.setScene(scene);
        stage.setTitle("Optimisation de Parcours");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

