package ondrej.mejzlik.suntrail.utilities;

import android.app.Activity;
import android.os.Bundle;

import ondrej.mejzlik.suntrail.R;

import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_CERES;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_EARTH;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_HALLEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_JUPITER;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_MARS;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_MERCURY;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_MOON;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_NEPTUNE;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_SATURN;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_SUN;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_URANUS;

/**
 * This class gathers all resources for planet menu fragment and planet text fragment based on
 * planet ID and returns them as a bundle which can be passed between fragments.
 */
public class PlanetResourceCollector {
    // Name for the key in arguments that identifies planet photo.
    // Used in planet menu fragment and planet text fragment
    public static final String PLANET_PHOTO_KEY = "planetPhotoKey";
    public static final String PLANET_HALF_PHOTO_KEY = "planetHalfPhotoKey";
    public static final String PLANET_SYMBOL_KEY = "planetSymbolKey";
    public static final String PLANET_NAME_KEY = "planetNameKey";
    public static final String PLANET_TEXT_KEY = "planetTextKey";
    public static final String PLANET_TEXT_TECH_KEY = "planetTextTechKey";
    public static final String PLANET_PHOTO_AUTHOR_KEY = "planetPhotoAuthorKey";
    // Identifier key for planet id when saved in bundle of arguments for fragment
    public static final String PLANET_ID_KEY = "planetId";

    private static final String CERES_AUTHOR = "NASA/JPL-Caltech";
    private static final String EARTH_AUTHOR = "NASA/Goddard";
    private static final String HALLEY_AUTHOR = "Giotto Project, ESA";
    private static final String JUPITER_AUTHOR = "NASA/JPL/Voyager 1";
    private static final String MARS_AUTHOR = "NASA/Hubble";
    private static final String MERCURY_AUTHOR = "NASA/Messenger";
    private static final String MOON_AUTHOR = "Fred Locklear";
    private static final String NEPTUNE_AUTHOR = "NASA/JPL/Voyager 2";
    private static final String SATURN_AUTHOR = "NASA/ESA/JPL/Cassini";
    private static final String SUN_AUTHOR = "NASA/Goddard/SDO";
    private static final String URANUS_AUTHOR = "NASA/JPL/Voyager 2/Joe Ruhinski";
    private static final String VENUS_AUTHOR = "NASA/JPL/Magellan";


    /**
     * Gathers planet resouces and returns them as a Bundle.
     * Resources are:
     * - String - translated planet name for the screen title
     * - String - main photo author
     * - Resource ID - planet photo for planet menu fragment
     * - Resource ID - planet half photo for planet text fragment
     * - Resource ID - planet symbol for planet text fragment
     * - Resource ID - main planet text for planet text fragment
     * - Resource ID - main planet parameters for planet text fragment
     *
     * @param planetId Planet id from configuration
     * @param activity The activity from which we call this method
     * @return Bundle of resources used throughout the app
     */
    public Bundle getPlanetResources(int planetId, Activity activity) {
        // Container for resources
        Bundle resources = new Bundle();
        // New title for the fragment based on planet ID and which language we use
        String newTitle;
        // Internal variable used to get the right resource. This name is used in resource names.
        String planetName;
        // This variable holds the name of the author who took the planet photo
        String author;
        switch (planetId) {
            case PLANET_ID_CERES: {
                newTitle = activity.getResources().getString(R.string.all_boards_button_name_ceres);
                planetName = "ceres";
                author = CERES_AUTHOR;
                break;
            }
            case PLANET_ID_EARTH: {
                newTitle = activity.getResources().getString(R.string.all_boards_button_name_earth);
                planetName = "earth";
                author = EARTH_AUTHOR;
                break;
            }
            case PLANET_ID_HALLEY: {
                newTitle = activity.getResources().getString(R.string.all_boards_button_name_halley);
                planetName = "halley";
                author = HALLEY_AUTHOR;
                break;
            }
            case PLANET_ID_JUPITER: {
                newTitle = activity.getResources().getString(R.string.all_boards_button_name_jupiter);
                planetName = "jupiter";
                author = JUPITER_AUTHOR;
                break;
            }
            case PLANET_ID_MARS: {
                newTitle = activity.getResources().getString(R.string.all_boards_button_name_mars);
                planetName = "mars";
                author = MARS_AUTHOR;
                break;
            }
            case PLANET_ID_MERCURY: {
                newTitle = activity.getResources().getString(R.string.all_boards_button_name_mercury);
                planetName = "mercury";
                author = MERCURY_AUTHOR;
                break;
            }
            case PLANET_ID_MOON: {
                newTitle = activity.getResources().getString(R.string.all_boards_button_name_moon);
                planetName = "moon";
                author = MOON_AUTHOR;
                break;
            }
            case PLANET_ID_NEPTUNE: {
                newTitle = activity.getResources().getString(R.string.all_boards_button_name_neptune);
                planetName = "neptune";
                author = NEPTUNE_AUTHOR;
                break;
            }
            case PLANET_ID_SATURN: {
                newTitle = activity.getResources().getString(R.string.all_boards_button_name_saturn);
                planetName = "saturn";
                author = SATURN_AUTHOR;
                break;
            }
            case PLANET_ID_SUN: {
                newTitle = activity.getResources().getString(R.string.all_boards_button_name_sun);
                planetName = "sun";
                author = SUN_AUTHOR;
                break;
            }
            case PLANET_ID_URANUS: {
                newTitle = activity.getResources().getString(R.string.all_boards_button_name_uranus);
                planetName = "uranus";
                author = URANUS_AUTHOR;
                break;
            }
            // Only other possible option is venus
            default: {
                newTitle = activity.getResources().getString(R.string.all_boards_button_name_venus);
                planetName = "venus";
                author = VENUS_AUTHOR;
                break;
            }
        }

        // Get main planet photo resource according to which planet we are displaying
        // The resources for planet halves must have a name "pict_*planet*"
        String mainPhotoResourceName = "pict_" + planetName;
        int mainPhotoResourceId = activity.getResources().getIdentifier(mainPhotoResourceName, "drawable", activity.getPackageName());

        // Get half planet photo resource
        // The resources for planet halves must have a name "pict_*planet*_half"
        String halfPhotoResourceName = "pict_" + planetName + "_half";
        int halfPhotoResourceId = activity.getResources().getIdentifier(halfPhotoResourceName, "drawable", activity.getPackageName());

        // Get symbol image resource
        // The resources for planet symbols must have a name "pict_symbol_*name*"
        String symbolResourceName = "pict_symbol_" + planetName;
        int symbolResourceId = activity.getResources().getIdentifier(symbolResourceName, "drawable", activity.getPackageName());

        // If the resource is not found, no image is displayed. Anyway resources do not change
        // the photo should always be found.

        // Get text resources
        // The resources for planet text must have a name "planet_text_*name*_top" and
        // "planet_text_*name*_tech"
        // If resource is not found it throws ResouceNotFoundException
        // But since resources do not change after install, this should not happen.
        String textTopName = "planet_text_" + planetName + "_top";
        String textTechName = "planet_text_" + planetName + "_tech";
        int textTopId = activity.getResources().getIdentifier(textTopName, "string", activity.getPackageName());
        int textTechId = activity.getResources().getIdentifier(textTechName, "string", activity.getPackageName());

        // Add all resources into bundle
        resources.putString(PLANET_NAME_KEY, newTitle);
        resources.putString(PLANET_PHOTO_AUTHOR_KEY, author);
        resources.putInt(PLANET_ID_KEY, planetId);
        resources.putInt(PLANET_PHOTO_KEY, mainPhotoResourceId);
        resources.putInt(PLANET_HALF_PHOTO_KEY, halfPhotoResourceId);
        resources.putInt(PLANET_SYMBOL_KEY, symbolResourceId);
        resources.putInt(PLANET_TEXT_KEY, textTopId);
        resources.putInt(PLANET_TEXT_TECH_KEY, textTechId);

        return resources;
    }

}
