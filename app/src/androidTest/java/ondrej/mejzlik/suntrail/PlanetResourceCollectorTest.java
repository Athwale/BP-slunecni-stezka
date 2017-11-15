package ondrej.mejzlik.suntrail;

import android.app.Activity;
import android.os.Bundle;
import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

import ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector;

import static junit.framework.Assert.assertEquals;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_CERES;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_AUDIO_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_HALF_PHOTO_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_ID_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_MAP_POSITION_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_NAME_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_PHOTO_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_QUARTER_PHOTO_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_SYMBOL_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_TEXT_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_TEXT_TECH_KEY;

/**
 * Instrumentation test, which will execute on an Android device.
 * Testing resource collection
 */
@RunWith(AndroidJUnit4.class)
public class PlanetResourceCollectorTest {

    public void TestResourceCollection() throws Exception {
        PlanetResourceCollector collector = new PlanetResourceCollector();
        Activity activity = getActivity();

        Bundle resources = collector.getPlanetResources(PLANET_ID_CERES, activity);
        if (resources != null && resources.containsKey(PLANET_ID_KEY)) {
            assertEquals(resources.getInt(PLANET_NAME_KEY), R.id.all_boards_button_ceres);
            assertEquals(resources.getInt(PLANET_PHOTO_KEY), R.drawable.pict_ceres);
            assertEquals(resources.getInt(PLANET_HALF_PHOTO_KEY), R.drawable.pict_ceres_half);
            assertEquals(resources.getInt(PLANET_QUARTER_PHOTO_KEY), R.drawable.pict_ceres_quarter);
            assertEquals(resources.getInt(PLANET_SYMBOL_KEY), R.drawable.pict_symbol_ceres);
            assertEquals(resources.getInt(PLANET_TEXT_KEY), R.string.planet_text_ceres_top);
            assertEquals(resources.getInt(PLANET_TEXT_TECH_KEY), R.string.planet_text_ceres_tech);
            assertEquals(resources.getInt(PLANET_AUDIO_KEY), R.raw.planet_audio_ceres);
            assertEquals(resources.getInt(PLANET_MAP_POSITION_KEY), R.drawable.pict_map_planets_ceres);
        }
    }
}
