package ondrej.mejzlik.suntrail.config;

/**
 * This class holds public static constants used to identify planets from QR codes and NFC tags.
 * You can change values here to modify or repair app behavior.
 */
public class Configuration {

    // Strings that the QR code must contain in order to verify which planet the user scanned
    public static final String SUN_TRAIL_NAME = "http://slunecnistezka.cz/app/";
    public static final String NAME_INTRO = "intro";
    public static final String NAME_SUN = "slunce";
    public static final String NAME_MERCURY = "merkur";
    public static final String NAME_VENUS = "venuse";
    public static final String NAME_EARTH = "zeme";
    public static final String NAME_MOON = "mesic";
    public static final String NAME_MARS = "mars";
    public static final String NAME_CERES = "ceres";
    public static final String NAME_JUPITER = "jupiter";
    public static final String NAME_HALLEY = "halley";
    public static final String NAME_SATURN = "saturn";
    public static final String NAME_URANUS = "uran";
    public static final String NAME_NEPTUNE = "neptun";
    // Easteregg authors name shows up
    public static final String NAME_ATHWALE = "Athwale";

    // Strings that NFC NUID must contain to verify which planet the user scanned
    public static final String NFC_INTRO = "4a3e26";
    public static final String NFC_SUN = "4573726";
    public static final String NFC_MERCURY = "349e3c26";
    public static final String NFC_VENUS = "846b3526";
    public static final String NFC_EARTH = "14513526";
    public static final String NFC_MOON = "9473926";
    public static final String NFC_MARS = "d4413b26";
    public static final String NFC_CERES = "948e3526";
    public static final String NFC_JUPITER = "34983526";
    public static final String NFC_HALLEY = "b46c3326";
    public static final String NFC_SATURN = "e4e43626";
    public static final String NFC_URANUS = "546c3926";
    public static final String NFC_NEPTUNE = "a4973526";

}
