package vue;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class APPLI extends Application {

    @Override
    public void start(Stage stage) {
        VBoxRoot root = new VBoxRoot();

        root.getMenu().getBoutonValider().setOnAction(root.getControleur());

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Optimisation de Parcours");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
