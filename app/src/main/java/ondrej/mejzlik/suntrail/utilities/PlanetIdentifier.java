package ondrej.mejzlik.suntrail.utilities;

import android.widget.Button;

import ondrej.mejzlik.suntrail.R;

import static ondrej.mejzlik.suntrail.config.Configuration.NAME_ATHWALE;
import static ondrej.mejzlik.suntrail.config.Configuration.NAME_CERES;
import static ondrej.mejzlik.suntrail.config.Configuration.NAME_EARTH;
import static ondrej.mejzlik.suntrail.config.Configuration.NAME_HALLEY;
import static ondrej.mejzlik.suntrail.config.Configuration.NAME_JUPITER;
import static ondrej.mejzlik.suntrail.config.Configuration.NAME_MARS;
import static ondrej.mejzlik.suntrail.config.Configuration.NAME_MERCURY;
import static ondrej.mejzlik.suntrail.config.Configuration.NAME_MOON;
import static ondrej.mejzlik.suntrail.config.Configuration.NAME_NEPTUNE;
import static ondrej.mejzlik.suntrail.config.Configuration.NAME_SATURN;
import static ondrej.mejzlik.suntrail.config.Configuration.NAME_SUN;
import static ondrej.mejzlik.suntrail.config.Configuration.NAME_URANUS;
import static ondrej.mejzlik.suntrail.config.Configuration.NAME_VENUS;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_ATHWALE;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_CERES;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_EARTH;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_HALLEY;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_INVALID;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_JUPITER;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_MARS;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_MERCURY;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_MOON;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_NEPTUNE;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_SATURN;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_SUN;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_URANUS;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_VENUS;
import static ondrej.mejzlik.suntrail.config.Configuration.SUN_TRAIL_NAME;

/**
 * Created by Ondrej Mejzlik on 3/13/17.
 */

public class PlanetIdentifier {

    /**
     * Identifies planet based on which button was pressed in all boards fragment.
     *
     * @param button The button which has been pressed
     * @return PLANET_ID constant from Configuration
     */
    public int getPlanetId(Button button) {
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
     * Identifies planet by parsing the name from QR code string.
     *
     * @param qrCodeContents The string read from QR code
     * @return PLANET_ID constant from Configuration
     */
    public int getPlanetId(String qrCodeContents) {
        // This happens when you scan the code in scanner choice fragment
        if (qrCodeContents.contains(NAME_ATHWALE)) {
            return PLANET_ID_ATHWALE;
        }

        // Check if the QR code contains http://slunecnistezka.cz/app/
        if (!qrCodeContents.contains(SUN_TRAIL_NAME)) {
            return PLANET_ID_INVALID;
        }

        if (qrCodeContents.contains(NAME_CERES)) {
            return PLANET_ID_CERES;
        }
        if (qrCodeContents.contains(NAME_EARTH)) {
            return PLANET_ID_EARTH;
        }
        if (qrCodeContents.contains(NAME_HALLEY)) {
            return PLANET_ID_HALLEY;
        }
        if (qrCodeContents.contains(NAME_JUPITER)) {
            return PLANET_ID_JUPITER;
        }
        if (qrCodeContents.contains(NAME_MARS)) {
            return PLANET_ID_MARS;
        }
        if (qrCodeContents.contains(NAME_MERCURY)) {
            return PLANET_ID_MERCURY;
        }
        if (qrCodeContents.contains(NAME_MOON)) {
            return PLANET_ID_MOON;
        }
        if (qrCodeContents.contains(NAME_NEPTUNE)) {
            return PLANET_ID_NEPTUNE;
        }
        if (qrCodeContents.contains(NAME_SATURN)) {
            return PLANET_ID_SATURN;
        }
        if (qrCodeContents.contains(NAME_SUN)) {
            return PLANET_ID_SUN;
        }
        if (qrCodeContents.contains(NAME_URANUS)) {
            return PLANET_ID_URANUS;
        }
        if (qrCodeContents.contains(NAME_VENUS)) {
            return PLANET_ID_VENUS;
        }

        // If none of the ifs above worked, we scanned a wrong code.
        return PLANET_ID_INVALID;
    }
}
