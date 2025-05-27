package vue;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class APPLI {

    public static void main(String[] args) {
        Application.launch(args);}

    private void ouvrirAffichage(Stage stage) {
        Stage stage1 = new Stage();
        Scene scene = new Scene();

        stage.setScene(scene);
        stage.setTitle("APPLI");
        stage.show();
    }
}