package ondrej.mejzlik.suntrail.utilities;

import android.widget.Button;

import ondrej.mejzlik.suntrail.R;

import static ondrej.mejzlik.suntrail.configuration.Configuration.NAME_ATHWALE;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NAME_CERES;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NAME_EARTH;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NAME_HALLEY;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NAME_INTRO;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NAME_JUPITER;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NAME_MARS;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NAME_MERCURY;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NAME_MOON;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NAME_NEPTUNE;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NAME_SATURN;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NAME_SUN;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NAME_URANUS;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NAME_VENUS;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NFC_CERES;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NFC_EARTH;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NFC_HALLEY;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NFC_INTRO;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NFC_JUPITER;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NFC_MARS;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NFC_MERCURY;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NFC_MOON;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NFC_NEPTUNE;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NFC_SATURN;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NFC_SUN;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NFC_URANUS;
import static ondrej.mejzlik.suntrail.configuration.Configuration.NFC_VENUS;
import static ondrej.mejzlik.suntrail.configuration.Configuration.SUN_TRAIL_NAME;

/**
 * This class provides methods to get a unified planet identity integer from buttons in all boards
 * menu, QR codes and NFC codes.
 */
public class PlanetIdentifier {
    public static final int PLANET_ID_JUPITER = 8;
    public static final int PLANET_ID_HALLEY = 9;
    public static final int PLANET_ID_SATURN = 10;
    public static final int PLANET_ID_NEPTUNE = 12;
    public static final int PLANET_ID_INVALID = 13;
    // Cheat authors name shows up
    public static final int PLANET_ID_ATHWALE = 14;
    public static final int PLANET_ID_SUN = 1;
    public static final int PLANET_ID_MERCURY = 2;
    public static final int PLANET_ID_URANUS = 11;
    static final int PLANET_ID_EARTH = 4;
    static final int PLANET_ID_MOON = 5;
    static final int PLANET_ID_MARS = 6;
    static final int PLANET_ID_CERES = 7;
    // Planet identifiers used in PlanetResourceCollector and PlanetIdentifier to identify which
    // planet data should we load.
    private final int PLANET_ID_INTRO = 0;
    private final int PLANET_ID_VENUS = 3;

    /**
     * Identifies planet based on which button was pressed in all boards fragment.
     *
     * @param button The button which has been pressed
     * @return PLANET_ID constant from Configuration
     */
    public int getPlanetIdFromButton(Button button) {
        switch (button.getId()) {
            case R.id.all_boards_button_ceres: {
                return PLANET_ID_CERES;
            }
            case R.id.all_boards_button_earth: {
                return PLANET_ID_EARTH;
            }
            case R.id.all_boards_button_halley: {
                return PLANET_ID_HALLEY;
            }
            case R.id.all_boards_button_jupiter: {
                return PLANET_ID_JUPITER;
            }
            case R.id.all_boards_button_mars: {
                return PLANET_ID_MARS;
            }
            case R.id.all_boards_button_mercury: {
                return PLANET_ID_MERCURY;
            }
            case R.id.all_boards_button_moon: {
                return PLANET_ID_MOON;
            }
            case R.id.all_boards_button_neptune: {
                return PLANET_ID_NEPTUNE;
            }
            case R.id.all_boards_button_saturn: {
                return PLANET_ID_SATURN;
            }
            case R.id.all_boards_button_venus: {
                return PLANET_ID_VENUS;
            }
            case R.id.all_boards_button_uranus: {
                return PLANET_ID_URANUS;
            }
            default: {
                // Only other option is Sun.
                return PLANET_ID_SUN;
            }
        }
    }

    /**
     * Identifies planet by comparing the name from QR code contents string.
     *
     * @param string The string read from QR code
     * @return PLANET_ID constant from Configuration
     */
    public int getPlanetIdFromQr(String string) {
        // This happens when you scan the code in scanner choice fragment
        if (string.contains(NAME_ATHWALE)) {
            return PLANET_ID_ATHWALE;
        }

        // Check if the QR code contains http://slunecnistezka.cz/app/
        if (!string.contains(SUN_TRAIL_NAME)) {
            return PLANET_ID_INVALID;
        }

        if (string.contains(NAME_INTRO)) {
            return PLANET_ID_INTRO;
        }
        if (string.contains(NAME_CERES)) {
            return PLANET_ID_CERES;
        }
        if (string.contains(NAME_EARTH)) {
            return PLANET_ID_EARTH;
        }
        if (string.contains(NAME_HALLEY)) {
            return PLANET_ID_HALLEY;
        }
        if (string.contains(NAME_JUPITER)) {
            return PLANET_ID_JUPITER;
        }
        if (string.contains(NAME_MARS)) {
            return PLANET_ID_MARS;
        }
        if (string.contains(NAME_MERCURY)) {
            return PLANET_ID_MERCURY;
        }
        if (string.contains(NAME_MOON)) {
            return PLANET_ID_MOON;
        }
        if (string.contains(NAME_NEPTUNE)) {
            return PLANET_ID_NEPTUNE;
        }
        if (string.contains(NAME_SATURN)) {
            return PLANET_ID_SATURN;
        }
        if (string.contains(NAME_SUN)) {
            return PLANET_ID_SUN;
        }
        if (string.contains(NAME_URANUS)) {
            return PLANET_ID_URANUS;
        }
        if (string.contains(NAME_VENUS)) {
            return PLANET_ID_VENUS;
        }

        // If none of the ifs above worked, we scanned a wrong code.
        return PLANET_ID_INVALID;
    }

    /**
     * Identifies planet by comparing the code from NFC tag string.
     *
     * @param string The string read NFC tag
     * @return PLANET_ID constant from Configuration
     */
    public int getPlanetIdFromNfc(String string) {
        if (string.equals(NFC_INTRO)) {
            return PLANET_ID_INTRO;
        }
        if (string.equals(NFC_CERES)) {
            return PLANET_ID_CERES;
        }
        if (string.equals(NFC_EARTH)) {
            return PLANET_ID_EARTH;
        }
        if (string.equals(NFC_HALLEY)) {
            return PLANET_ID_HALLEY;
        }
        if (string.equals(NFC_JUPITER)) {
            return PLANET_ID_JUPITER;
        }
        if (string.equals(NFC_MARS)) {
            return PLANET_ID_MARS;
        }
        if (string.equals(NFC_MERCURY)) {
            return PLANET_ID_MERCURY;
        }
        if (string.equals(NFC_MOON)) {
            return PLANET_ID_MOON;
        }
        if (string.equals(NFC_NEPTUNE)) {
            return PLANET_ID_NEPTUNE;
        }
        if (string.equals(NFC_SATURN)) {
            return PLANET_ID_SATURN;
        }
        if (string.equals(NFC_SUN)) {
            return PLANET_ID_SUN;
        }
        if (string.equals(NFC_URANUS)) {
            return PLANET_ID_URANUS;
        }
        if (string.equals(NFC_VENUS)) {
            return PLANET_ID_VENUS;
        }

        // If none of the ifs above worked, we scanned a wrong code.
        return PLANET_ID_INVALID;
    }

    /**
     * Reverse method to get planet name from id.
     *
     * @param planetId Constant planet ID from PlanetIdentifier
     * @return Planet name from Configuration
     */
    public String getPlanetName(int planetId) {
        switch (planetId) {
            case PLANET_ID_CERES: {
                return NAME_CERES;
            }
            case PLANET_ID_EARTH: {
                return NAME_EARTH;
            }
            case PLANET_ID_HALLEY: {
                return NAME_HALLEY;
            }
            case PLANET_ID_JUPITER: {
                return NAME_JUPITER;
            }
            case PLANET_ID_MARS: {
                return NAME_MARS;
            }
            case PLANET_ID_MERCURY: {
                return NAME_MERCURY;
            }
            case PLANET_ID_MOON: {
                return NAME_MOON;
            }
            case PLANET_ID_NEPTUNE: {
                return NAME_NEPTUNE;
            }
            case PLANET_ID_SATURN: {
                return NAME_SATURN;
            }
            case PLANET_ID_VENUS: {
                return NAME_VENUS;
            }
            case PLANET_ID_URANUS: {
                return NAME_URANUS;
            }
            case PLANET_ID_SUN: {
                return NAME_SUN;
            }
            case PLANET_ID_INTRO: {
                return NAME_INTRO;
            }
        }
        return "INVALID_ID";
    }
}
