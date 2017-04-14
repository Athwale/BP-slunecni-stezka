package ondrej.mejzlik.suntrail.utilities;

import android.app.Activity;
import android.os.Bundle;

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
    public static final String PLANET_QUARTER_PHOTO_KEY = "planetQuarterPhotoKey";
    public static final String PLANET_SYMBOL_KEY = "planetSymbolKey";
    public static final String PLANET_NAME_KEY = "planetNameKey";
    public static final String PLANET_TEXT_KEY = "planetTextKey";
    public static final String PLANET_TEXT_TECH_KEY = "planetTextTechKey";
    public static final String PLANET_PHOTO_AUTHOR_KEY = "planetPhotoAuthorKey";
    // Identifier key for planet id when saved in bundle of arguments for fragment
    public static final String PLANET_ID_KEY = "planetId";
    public static final String PLANET_AUDIO_KEY = "planetAudioKey";

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
     * Gathers planet resources and returns them as a Bundle.
     * Resources are:
     * - Resource ID - translated planet name for the screen title
     * - String - main photo author
     * - Resource ID - planet photo for planet menu fragment
     * - Resource ID - planet half photo for planet text fragment
     * - Resource ID - planet quarter photo for game fragments
     * - Resource ID - planet symbol for planet text fragment
     * - Resource ID - main planet text for planet text fragment
     * - Resource ID - main planet parameters for planet text fragment
     * This returns resource ids because they get automatically translated when
     * the language of the app changes.
     *
     * @param planetId Planet id from configuration
     * @param activity The activity from which we call this method
     * @return Bundle of resources used throughout the app
     */
    public Bundle getPlanetResources(int planetId, Activity activity) {
        // Container for resources
        Bundle resources = new Bundle();
        // Internal variable used to get the right resource. This name is used in resource names.
        String planetName;
        // This variable holds the name of the author who took the planet photo
        String author;
        switch (planetId) {
            case PLANET_ID_CERES: {
                planetName = "ceres";
                author = CERES_AUTHOR;
                break;
            }
            case PLANET_ID_EARTH: {
                planetName = "earth";
                author = EARTH_AUTHOR;
                break;
            }
            case PLANET_ID_HALLEY: {
                planetName = "halley";
                author = HALLEY_AUTHOR;
                break;
            }
            case PLANET_ID_JUPITER: {
                planetName = "jupiter";
                author = JUPITER_AUTHOR;
                break;
            }
            case PLANET_ID_MARS: {
                planetName = "mars";
                author = MARS_AUTHOR;
                break;
            }
            case PLANET_ID_MERCURY: {
                planetName = "mercury";
                author = MERCURY_AUTHOR;
                break;
            }
            case PLANET_ID_MOON: {
                planetName = "moon";
                author = MOON_AUTHOR;
                break;
            }
            case PLANET_ID_NEPTUNE: {
                planetName = "neptune";
                author = NEPTUNE_AUTHOR;
                break;
            }
            case PLANET_ID_SATURN: {
                planetName = "saturn";
                author = SATURN_AUTHOR;
                break;
            }
            case PLANET_ID_SUN: {
                planetName = "sun";
                author = SUN_AUTHOR;
                break;
            }
            case PLANET_ID_URANUS: {
                planetName = "uranus";
                author = URANUS_AUTHOR;
                break;
            }
            // Only other possible option is venus
            default: {
                planetName = "venus";
                author = VENUS_AUTHOR;
                break;
            }
        }

        // Get fragment title resource according to which planet we are displaying
        // Titles are the same as button names in all boards list
        // The resources for planet halves must have a name "all_boards_button_name_*name*"
        String titleResourceName = "all_boards_button_name_" + planetName;
        int titleResourceId = activity.getResources().getIdentifier(titleResourceName, "string", activity.getPackageName());

        // Get main planet photo resource according to which planet we are displaying
        // The resources for planet halves must have a name "pict_*planet*"
        String mainPhotoResourceName = "pict_" + planetName;
        int mainPhotoResourceId = activity.getResources().getIdentifier(mainPhotoResourceName, "drawable", activity.getPackageName());

        // Get half planet photo resource
        // The resources for planet halves must have a name "pict_*planet*_half"
        String halfPhotoResourceName = "pict_" + planetName + "_half";
        int halfPhotoResourceId = activity.getResources().getIdentifier(halfPhotoResourceName, "drawable", activity.getPackageName());

        // Get quarter planet photo resource
        // The resources for planet halves must have a name "pict_*planet*_half"
        String quarterPhotoResourceName = "pict_" + planetName + "_quarter";
        int quarterPhotoResourceId = activity.getResources().getIdentifier(quarterPhotoResourceName, "drawable", activity.getPackageName());

        // Get symbol image resource
        // The resources for planet symbols must have a name "pict_symbol_*name*"
        String symbolResourceName = "pict_symbol_" + planetName;
        int symbolResourceId = activity.getResources().getIdentifier(symbolResourceName, "drawable", activity.getPackageName());

        // Get planet audio resource
        // The resources for planet audio files must have a name "planet_audio_*name*"
        // If resource is not found it throws ResourceNotFoundException
        String audioResourceName = "planet_audio_" + planetName;
        int audioResourceId = activity.getResources().getIdentifier(audioResourceName, "raw", activity.getPackageName());

        // If the resource is not found, no image is displayed. Anyway resources do not change
        // the photo should always be found.

        // Get text resources
        // The resources for planet text must have a name "planet_text_*name*_top" and
        // "planet_text_*name*_tech"
        // If resource is not found it throws ResourceNotFoundException
        // But since resources do not change after install, this should not happen.
        String textTopName = "planet_text_" + planetName + "_top";
        String textTechName = "planet_text_" + planetName + "_tech";
        int textTopId = activity.getResources().getIdentifier(textTopName, "string", activity.getPackageName());
        int textTechId = activity.getResources().getIdentifier(textTechName, "string", activity.getPackageName());

        // Add all resources into bundle
        resources.putInt(PLANET_NAME_KEY, titleResourceId);
        resources.putString(PLANET_PHOTO_AUTHOR_KEY, author);
        resources.putInt(PLANET_ID_KEY, planetId);
        resources.putInt(PLANET_PHOTO_KEY, mainPhotoResourceId);
        resources.putInt(PLANET_HALF_PHOTO_KEY, halfPhotoResourceId);
        resources.putInt(PLANET_QUARTER_PHOTO_KEY, quarterPhotoResourceId);
        resources.putInt(PLANET_SYMBOL_KEY, symbolResourceId);
        resources.putInt(PLANET_TEXT_KEY, textTopId);
        resources.putInt(PLANET_TEXT_TECH_KEY, textTechId);
        resources.putInt(PLANET_AUDIO_KEY, audioResourceId);

        return resources;
    }

}
