package modele;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

public class DistanceMapTest {

    @BeforeEach
    void setUpBeforeClass() throws Exception {
        System.out.println("SetUp");
    }

    @AfterEach
    void tearDownAfterClass() throws Exception {
        System.out.println("TearDown");
    }

    @Test
    public void testAddAndGetDistance() {
        DistanceMap dm = new DistanceMap();
        dm.addDistance("Paris", "Lyon", 460);

        assertEquals(460, dm.getDistance("Paris", "Lyon"));
        assertEquals(Integer.MAX_VALUE, dm.getDistance("Lyon", "Paris")); // non défini
    }

    @Test
    public void testGetNeighbours() {
        DistanceMap dm = new DistanceMap();
        dm.addDistance("Paris", "Lyon", 460);
        dm.addDistance("Paris", "Marseille", 775);

        Map<String, Integer> voisins = dm.getNeighbours("Paris");
        assertEquals(2, voisins.size());
        assertTrue(voisins.containsKey("Lyon"));
        assertTrue(voisins.containsKey("Marseille"));
    }
}
