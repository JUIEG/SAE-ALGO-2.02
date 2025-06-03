package vue.creation;

import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import modele.Vente;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class VBoxCreationTest {

    private VBoxCreation vboxCreation;
    private static boolean javafxInitialized = false;

    @BeforeAll
    public static void initToolkit() throws Exception {
        if (!javafxInitialized) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(() -> {
                javafxInitialized = true;
                latch.countDown();
            });
            if (!latch.await(5, TimeUnit.SECONDS)) {
                throw new RuntimeException("Timeout initialisation JavaFX");
            }
        }
    }

    @BeforeEach
    public void setUp() {
        vboxCreation = new VBoxCreation();
    }

    @Test
    public void testModeCreationParDefaut() {
        assertTrue(vboxCreation.isModeCreation(), "Le mode par défaut doit être création");
    }

    @Test
    public void testListeScenarioVideAuDepart() {
        List<String> scenarios = vboxCreation.listerFichiersScenario("scenarios");
        assertNotNull(scenarios, "La liste de scénarios ne doit pas être null");
    }

    @Test
    public void testAjouterVentes() {
        Vente v1 = new Vente("Paris", "Lyon");
        Vente v2 = new Vente("Nice", "Marseille");
        vboxCreation.ajouterVentes(List.of(v1, v2));
        assertEquals(2, vboxCreation.getListeVentes().size(), "Deux ventes doivent être ajoutées");
    }

    @Test
    public void testReinitialiserFormulaire() {
        Vente v1 = new Vente("Paris", "Lyon");
        vboxCreation.ajouterVentes(List.of(v1));
        vboxCreation.reinitialiserFormulaire();
        assertEquals(0, vboxCreation.getListeVentes().size(), "Le formulaire doit être vidé");
        assertTrue(vboxCreation.getSelectionVerticale().isEmpty(), "La sélection doit être vide");
    }

    @Test
    public void testGetNumeroScenarioActuelValide() {
        Platform.runLater(() -> {
            vboxCreation.rafraichirListeScenario();
            ComboBox<String> combo = vboxCreation.getScenarioCombo();
            combo.getItems().add("scenario_3");
            combo.getSelectionModel().select("scenario_3");

            int num = vboxCreation.getNumeroScenarioActuel();
            assertEquals(3, num, "Le numéro de scénario extrait doit être 3");
        });
    }

    @Test
    public void testGetNumeroScenarioActuelInvalide() {
        Platform.runLater(() -> {
            ComboBox<String> combo = vboxCreation.getScenarioCombo();
            combo.getItems().add("invalid_name");
            combo.getSelectionModel().select("invalid_name");

            assertThrows(IllegalArgumentException.class, () -> {
                vboxCreation.getNumeroScenarioActuel();
            }, "Doit lancer une exception pour un nom invalide");
        });
    }
}
