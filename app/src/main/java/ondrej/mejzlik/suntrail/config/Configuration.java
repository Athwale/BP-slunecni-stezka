package ondrej.mejzlik.suntrail.config;

/**
 * This class holds public static constants used throughout the whole app.
 * You can change values here to modify or repair app behavior.
 */
public class Configuration {
    // Used in main menu in infoButtonsHandler to start either general info or game info fragment
    // in InfoScreen activity
    public static final String INFO_BUTTON_INTENT_KEY = "StartFragment";
    public static final int INFO_BUTTON_GENERAL = 1;
    public static final int INFO_BUTTON_GAME = 2;

    // Name for the key in arguments that identifies the saved image.
    // Used to start ZoomableImageFragment with an image identified by R.id.
    public static final String IMAGE_KEY = "imageKey";
    // Name for the bundle of arguments as an identifier in saved state
    public static final String IMAGE_ARGUMENT = "imageArgument";

    // Used in fragments to save scroll position
    public static final String SCROLL_POSITION_KEY = "scrollPosition";

    // Used to identify which data should PlanetText fragment load
    public static final int PLANET_ID_SUN = 1;
    public static final int PLANET_ID_MERCURY = 2;
    public static final int PLANET_ID_VENUS = 3;
    public static final int PLANET_ID_EARTH = 4;
    public static final int PLANET_ID_MOON = 5;
    public static final int PLANET_ID_MARS = 6;
    public static final int PLANET_ID_CERES = 7;
    public static final int PLANET_ID_JUPITER = 8;
    public static final int PLANET_ID_HALLEY = 9;
    public static final int PLANET_ID_SATURN = 10;
    public static final int PLANET_ID_URANUS = 11;
    public static final int PLANET_ID_NEPTUNE = 12;
    // Identifier key for planet id when saved in bundle of arguments for fragment
    public static final String PLANET_ID_KEY = "planetId";
    // Used to pass name of the planet to planet menu fragment
    public static final String PLANET_NAME_KEY = "planetName";
    // Used in scanner choice fragment and qr fragment to pass argument whether to use flash
    public static final String USE_FLASH_KEY = "useFlash";
    // Used to indicate which scanner or scanner options should the app use or offer in scanner choice
    public static final int HAS_NOTHING = 0;
    public static final int HAS_NFC_QR = 1;
    public static final int HAS_ONLY_QR = 2;
    public static final int HAS_ONLY_NFC = 3;
    // Used in scanner activity and qr fragment to identify camera permission request
    public static final int PERMISSION_CAMERA = 1;
    // Used as identifier for qr scanner fragment
    public static final String QR_SCANNER_TAG = "qrFragment";

}
