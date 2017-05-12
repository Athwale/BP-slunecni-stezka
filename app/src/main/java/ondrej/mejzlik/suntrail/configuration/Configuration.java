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
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_URANUS;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_VENUS;

/**
 * This class holds public static constants used to influence the behavior of the game.
 * <p>
 * GAME ALGORITHM EXPLANATION
 * 1. The player can start the game at any planet. If the game is started on the first or last
 * two planets the direction of the trip is automatically set towards the other end of the Sun
 * trail. Otherwise the player has to select one direction.
 * <p>
 * 2. The player begins the game with a certain amount of credits (can be set in this file).
 * The player is given a small ship Icarus S. Other ships are available on the Moon and Jupiter.
 * The player can buy these ships. When new ship is bought, old ship is automatically sold.
 * <p>
 * 3. There are 11 shops in the game. The first information board does not have a shop, scanning the
 * board opens the information fragment. First shop is on the Sun. Neptune does not have a shop
 * because it is the last planet there is no point in buying anything there. Or vice versa.
 * <p>
 * 4. There are 33 items in the game. Each shop has 3. The number can be expanded but each shop
 * must always have equal amount of items. New items must be added in multiples of 11.
 * <p>
 * 5. Items are assigned random prices from a range which can be configured in this file.
 * The price of each item can either rise or fall before it is sold in another shop. By how much %
 * the price will change can be set in this file. Prices of all items are set to rise initially.
 * Whether the price will fall or rise in the next shop is equally probable.
 * The next movement in price of a given item which is in the player's inventory is set randomly at
 * each stop.
 * <p>
 * 6. In case the user makes many bad choices and the algorithm determines the user will not be
 * able to buy anything in a shop he arrived to, a price of one item is set low enough for the user
 * to be able to buy it. This takes into account what the user can sell in that shop.
 * (TODO: This safety feature can be disabled in a harder mode - not yet implemented.)
 * <p>
 * 7. Different ships have different cargo bay size. This can be set in this file. The size of items
 * in shops is set randomly but a single item can not be larger than currently available maximum
 * cargo bay size.
 * <p>
 * 8. On the last planet which is either the Neptune on the Sun everything is automatically sold.
 * Then based on how much credits the player has he/she is awarded.
 * <p>
 * 9 GAMEPLAY MECHANICS
 * The user might play completely randomly not thinking about anything. In this case because of the
 * bad choices safety method the user is able to finish the game.
 * Otherwise the player is supposed to make choices whether to keep some items in the bay. If the
 * price of an item turns out to rise over more planets, it might be better to keep that item and
 * make more money but it will occupy some cargo space. Also the player should buy as many items
 * on a given planet as possible. The price of each of these item will rise in the next shop which
 * in turn makes more money than buying only one item. Here the user has to keep an eye on the
 * cargo space and money.
 * <p>
 * Game method call sequence:
 * <p>
 * OPENING GAME MODE ON A PLANET:
 * First planet:
 * GameDatabaseHelper databaseHelper = GameDatabaseHelper.getInstance(this.context);
 * databaseHelper.initializeDatabaseContents(direction, this.startPlanet, this.context);
 * databaseHelper.adaptItemSizes(this.startPlanet);
 * <p>
 * Other planets:
 * databaseHelper.updateItems(this.currentPlanet);
 * databaseHelper.adaptItemSizes(this.currentPlanet);
 * databaseHelper.failSafe(this.currentPlanet, true);
 * databaseHelper.updateVisitedPlanets(this.currentPlanet);
 * <p>
 * On each planet:
 * databaseHelper.buySellItem(item, true/false);
 * if ship is available:
 * databaseHelper.buyShip(ship);
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
    // Contains planet Ids where shops are located. The Sun or Neptune is added to the list inside
    // GameDatabaseHelper depending on which trip direction is chosen.
    // If the player goes Sun -> Neptune, there is no shop on Neptune, since the game ends there and
    // vice versa.
    public static final List<Integer> PLANETS_WITH_SHOPS = Collections.unmodifiableList(
            new ArrayList<Integer>() {{
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
    // Initial game values
    public static final int FIRST_SHIP = R.string.ship_name_icarus;
    public static final int STARTING_CREDITS = 500;
    public static final int ICARUS_CARGO_SIZE = 10;
    public static final int LOKYS_CARGO_SIZE = 15;
    public static final int DAEDALUS_CARGO_SIZE = 25;
    // These are used to randomly assign the price of an item
    public static final int MIN_ITEM_PRICE = 40;
    public static final int MAX_ITEM_PRICE = 300;
    // In percent how much the price will raise or fall.
    public static final int MIN_ITEM_PRICE_MOVEMENT = 30;
    public static final int MAX_ITEM_PRICE_MOVEMENT = 60;
    // Prize limits
    public static final int FIRST_PLACE = 5500;
    public static final int SECOND_PLACE = 3500;

    // To prevent someone from accidentally instantiating this class, make the constructor private.
    private Configuration() {
    }

}
