package modele;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class VenteTest {

    @BeforeEach
    void setUpBeforeClass() throws Exception {
        System.out.println("SetUp");
    }

    @AfterEach
    void tearDownAfterClass() throws Exception {
        System.out.println("TearDown");
    }

    @Test
    public void testVenteCreation() {
        Vente v = new Vente("Salamèche", "Carapuce");

        assertEquals("Salamèche", v.getVilleVendeur());
        assertEquals("Carapuce", v.getVilleAcheteur());
    }

    @Test
    public void testToString() {
        Vente v = new Vente("Rondoudou", "Dracaufeu");
        assertEquals("Rondoudou -> Dracaufeu", v.toString());
    }
}
