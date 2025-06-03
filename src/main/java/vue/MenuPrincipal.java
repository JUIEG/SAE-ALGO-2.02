package vue;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MenuPrincipal extends MenuBar {

    private final MenuItem itemUtilisation = new MenuItem("Utilisation");
    private final MenuItem itemCreation = new MenuItem("Création");

    public MenuPrincipal() {
        Menu menu = new Menu("Mode");
        menu.getItems().addAll(itemUtilisation, itemCreation);
        this.getMenus().add(menu);
    }

    public MenuItem getItemUtilisation() {
        return itemUtilisation;
    }

    public MenuItem getItemCreation() {
        return itemCreation;
    }
}
