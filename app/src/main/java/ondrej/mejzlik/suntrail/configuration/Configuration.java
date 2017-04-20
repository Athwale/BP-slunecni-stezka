package ondrej.mejzlik.suntrail.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ondrej.mejzlik.suntrail.R;

import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_CERES;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_EARTH;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_HALLEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_JUPITER;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_MARS;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_MERCURY;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_MOON;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_SATURN;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_SUN;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_URANUS;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_VENUS;

/**
 * This class holds public static constants used to identify planets from QR codes and NFC tags.
 * You can change values here to modify or repair app behavior.
 * <p>
 * GAME ALGORITHM EXPLANATION
 * 1. The player can start the game at any planet. If the game is started on the first or last
 * two planets the direction is automatically set towards the other end of the Sun trail. Otherwise
 * the user has to select one direction.
 * <p>
 * 2. The player begins the game with a certain amount of credits (can be set in this file).
 * The player is given a small ship Icarus S.
 * Other ships are available on the Moon and Jupiter. The player will receive the new ship for free.
 * <p>
 * 3. There are 11 shops in the game. The first information board does not have a shop, scanning the
 * board opens the information fragment. First shop is on the Sun. Neptune does not have a shop
 * because it is the last planet there is no point in buying anything there.
 * <p>
 * 4. There are 33 items in the game. Each shop has 3. The number can be expanded but each shop
 * must have equal amount of items.
 * <p>
 * 5. Items are assigned random prices from a range which can be configured in this file.
 * The price of each item can either rise or fall before it is sold in another shop. By how much %
 * the price will change can be set in this file. Whether the price will rise or fall is initially
 * set when adding the items into the database. If the price is higher than a set value it is more
 * probable that the price will fall, if the price is lower than a set value it might rise more
 * probably. Otherwise the probability is equal.
 * The next movement in price of a given item which is in the player's inventory is set randomly at
 * each stop.
 * <p>
 * 6. In case the user makes many bad choices and the algorithm determines the user will not be
 * able to buy anything in a shop he arrived to, a price of one item is set low enough for the user
 * to be able to buy it and the price is set to rise. This takes into account what the user can
 * sell in that shop. This safety feature can be disabled in a harder mode.
 * <p>
 * 7. Different ships have different cargo bay size. This can be set in this file. The size of items
 * in shops is set randomly but not to be larger than a half of currently available maximum cargo
 * bay size.
 * <p>
 * 8. On the last planet which is either the Neptune on the Sun everything is automatically sold.
 * Then based on how much credits the player has he is awarded.
 * <p>
 * 9 GAMEPLAY MECHANICS
 * The user might play completely randomly not thinking about anything. In this case because of the
 * bad choices safety method the user is able to finish the game.
 * Otherwise the player is supposed to make choices whether to buy an item which will be sold for
 * more credits but the price might drop a bit, or buy an item where the price will rise. Since
 * items can be sold at any planet a decision what the user will keep in the cargo bay has to be
 * made. If he keeps it the price may rise a bit more but it will occupy some space. The player
 * has to keep an eye on the predicted price movements.
 *
 * Game method call sequence:
 * OPENING GAME MODE ON A PLANET (It is impossible to open game mode on already visited planets)
 * updateCurrentPlanet(New scanned planet id)
 * ENTERING SHOP
 *
 */
public final class Configuration {

    //----------------------------
    // Shop items:
    // The number of shop items must be X * PLANETS_WITH_SHOPS (each shop must have equal amount of
    // wares, otherwise a test will fail.
    // TODO write a test for this

    // Strings that the QR code must contain in order to verify which planet the user scanned
    public static final String SUN_TRAIL_NAME = "http://slunecnistezka.cz/app/";
    public static final String NAME_INTRO = "start";
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

    // Initial game values
    public static final int FIRST_SHIP = R.string.ship_name_icarus;
    public static final int STARTING_CREDITS = 500;

    // First shop is on the Sun because the first intro board opens Sun path info fragment and does
    // not offer the game. Last shop is on Uranus. There is no point in buying anything on Neptune
    // since there is nowhere to sell it.
    // On Neptune the player is able to sell everything he has including the ship and is rewarded.
    public static final List<Integer> PLANETS_WITH_SHOPS = Collections.unmodifiableList(
            new ArrayList<Integer>() {{
                add(PLANET_ID_SUN);
                add(PLANET_ID_MERCURY);
                add(PLANET_ID_VENUS);
                add(PLANET_ID_EARTH);
                add(PLANET_ID_MOON);
                add(PLANET_ID_MARS);
                add(PLANET_ID_CERES);
                add(PLANET_ID_JUPITER);
                add(PLANET_ID_HALLEY);
                add(PLANET_ID_SATURN);
                add(PLANET_ID_URANUS);
            }});

    public static final int ICARUS_CARGO_SIZE = 15;
    public static final int LOKYS_CARGO_SIZE = 25;
    public static final int DAEDALUS_CARGO_SIZE = 35;
    // These are used to randomly assign the price of an item
    public static final int MIN_ITEM_PRICE = 50;
    public static final int MAX_ITEM_PRICE = 500;
    // In percent how much the price will raise or fall.
    public static final int MIN_ITEM_PRICE_MOVEMENT = 10;
    public static final int MAX_ITEM_PRICE_MOVEMENT = 20;
    // From which price should the probability of price falling be higher and lower
    public static final int ITEM_PRICE_MIGHT_FALL = 450;
    public static final int ITEM_PRICE_MIGHT_RISE = 150;
    // probability in % of price fall or rise if price is above ITEM_PRICE_MIGHT_FALL or below
    // ITEM_PRICE_MIGHT_RISE
    public static final int ITEM_PRICE_FALL_RISE_PROBABILITY = 70;


    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private Configuration() {
    }

}
