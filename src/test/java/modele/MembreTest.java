package modele;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class MembreTest {

    @BeforeEach
    void setUpBeforeClass() throws Exception {
        System.out.println("SetUp");
    }

    @AfterEach
    void tearDownAfterClass() throws Exception {
        System.out.println("TearDown");
    }

    @Test
    public void testGetPseudoEtVille() {
        Membre m = new Membre("Pikachu", "Paris");

        assertEquals("Pikachu", m.getPseudo());
        assertEquals("Paris", m.getVille());
    }

    @Test
    public void testToString() {
        Membre m = new Membre("Bulbizarre", "Lyon");
        assertEquals("Bulbizarre (Lyon)", m.toString());
    }
}
