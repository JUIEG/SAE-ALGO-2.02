module vue {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens vue to javafx.fxml;
    exports vue;
    exports modele;
    exports console;
}