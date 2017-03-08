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
    public static final String IMAGE_KEY = "image";
    // Name for the bundle of arguments as an identifier in saved state
    public static final String IMAGE_ARGUMENT = "imageArgument";

    // Used in fragments to save scroll position
    public static final String SCROLL_POSITION_KEY = "scrollPosition";

    public static final String PLANET_ID_KEY = "planet";
}
