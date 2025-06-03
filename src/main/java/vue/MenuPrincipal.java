package vue;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MenuPrincipal extends MenuBar {

    private final MenuItem itemUtilisation = new MenuItem("_Utilisation");
    private final MenuItem itemCreation = new MenuItem("_Création");

    public MenuPrincipal() {
        Menu menu = new Menu("_Mode");
        menu.getItems().addAll(itemUtilisation, itemCreation);
        this.getMenus().add(menu);
        //ajoute mnemonic par automatiquement sinon :
        // menu.setMnemonicParsing(true);
        // itemUtilisation.setMnemonicParsing(true);
        // itemCreation.setMnemonicParsing(true);
    }

    public MenuItem getItemUtilisation() {
        return itemUtilisation;
    }

    public MenuItem getItemCreation() {
        return itemCreation;
    }
}
