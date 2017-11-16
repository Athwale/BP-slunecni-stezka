package ondrej.mejzlik.suntrail;

import org.junit.Test;

import ondrej.mejzlik.suntrail.game.ShipModel;

import static org.junit.Assert.assertEquals;

/**
 * Test for ensuring that the planet identifier returns correct values.
 */
public class ShipModelTest {

    @Test
    public void shipModelTest() throws Exception {
        ShipModel model = new ShipModel(1, 10, 20, 100, 200, 20);

        assertEquals(20, model.getRemainingCargoSpace());
        assertEquals(200, model.getCargoBaySize());
        assertEquals(100, model.getPrice());
        assertEquals(20, model.getShipNameResId());
        assertEquals(20, model.getRemainingCargoSpace());
    }
}