package ondrej.mejzlik.suntrail;

import org.junit.Test;

import ondrej.mejzlik.suntrail.utilities.PlanetIdentifier;

import static ondrej.mejzlik.suntrail.configuration.Configuration.NAME_CERES;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NAME_MOON;
import static ondrej.mejzlik.suntrail.configuration.Configuration.SUN_TRAIL_NAME;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_CERES;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_MOON;
import static org.junit.Assert.assertEquals;

/**
 * Test for ensuring that the planet identifier returns correct values.
 */
public class PlanetIdentifierTest {

    @Test
    public void getPlanetIdFromQRTest() throws Exception {
        PlanetIdentifier identifier = new PlanetIdentifier();
        String qrToTest = SUN_TRAIL_NAME + NAME_CERES;
        int expectedResult = PLANET_ID_CERES;

        assertEquals(expectedResult, identifier.getPlanetIdFromQr(qrToTest));
    }

    public void getPlanetIdFromNFCTest() throws Exception {
        PlanetIdentifier identifier = new PlanetIdentifier();
        String nfcToTest = "9473926";
        int expectedResult = PLANET_ID_MOON;

        assertEquals(expectedResult, identifier.getPlanetIdFromQr(nfcToTest));
    }

    public void getPlanetQrNameTest() throws Exception {
        PlanetIdentifier identifier = new PlanetIdentifier();
        int idToTest = PLANET_ID_MOON;
        String expectedResult = NAME_MOON;

        assertEquals(expectedResult, identifier.getPlanetQrName(idToTest));
    }
}