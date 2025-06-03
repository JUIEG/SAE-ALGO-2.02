package vue.creation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class VBoxCreationTest {

    private VBoxCreation gestionnaire;

    @BeforeEach
    void setUp() {
        gestionnaire = new VBoxCreation();
        gestionnaire.creerScenario("TestScenario");
    }

    @Test
    void testAjouterEtAfficherVente() {
        gestionnaire.ajouterVente("TestScenario", "toto", "titi");
        List<String> ventes = gestionnaire.getVentes("TestScenario");

        assertEquals(1, ventes.size());
        assertEquals("toto->titi", ventes.get(0));
    }

    @Test
    void testCreerScenario() {
        gestionnaire.creerScenario("SecondScenario");
        assertTrue(gestionnaire.getListeScenarios().contains("SecondScenario"));
    }

    @Test
    void testGetVentesScenarioInexistant() {
        List<String> ventes = gestionnaire.getVentes("Inexistant");
        assertNotNull(ventes);
        assertTrue(ventes.isEmpty());
    }
}