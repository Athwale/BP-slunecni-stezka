package ondrej.mejzlik.suntrail.game;

import android.app.Activity;

import java.io.File;
import java.util.Random;

import static ondrej.mejzlik.suntrail.configuration.Configuration.MAX_ITEM_PRICE_MOVEMENT;
import static ondrej.mejzlik.suntrail.configuration.Configuration.MIN_ITEM_PRICE_MOVEMENT;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.DATABASE_NAME;

/**
 * This class contains methods necessary for the game to work.
 */
public class GameUtilities {
    private Random randomNumberGenerator = null;

    public GameUtilities() {
        // Initialize random number generator once
        this.randomNumberGenerator = new Random(System.nanoTime());
    }

    /**
     * This method returns a random price in the range given by the parameters.
     *
     * @param min Minimal price.
     * @param max Maximal price.
     * @return Random price from min to max.
     */
    int calculateBasePrice(int min, int max) {
        return this.randomIntGenerator(min, max);
    }

    /**
     * This method returns the selling price for a given item. By default we sell items for
     * 10 - 20% higher price than what we bought it for.
     *
     * @param basePrice Item price from database, this is the price we bought it for.
     * @return Selling price.
     */
    int calculateSellingPrice(int basePrice, boolean movement) {
        // Calculate how much we add or take from to the item base price.
        int profit = this.randomIntGenerator(MIN_ITEM_PRICE_MOVEMENT, MAX_ITEM_PRICE_MOVEMENT);
        double priceStep = (basePrice / 100.0) * profit;
        // Calculate new price use profit as percentage
        if (movement) {
            // Price will rise
            return (int) Math.round(basePrice + priceStep);
        } else {
            // Price will fall
            return (int) Math.round(basePrice - priceStep);
        }
    }

    /**
     * This method returns weather the item price will fall or raise. The probability of either rise
     * or fall is 1/2.
     *
     * @return Whether the price will raise or fall, 1 rise, 0 fall
     */
    int calculatePriceMovement() {
        boolean result = this.randomNumberGenerator.nextBoolean();
        if (result) {
            return 1;
        }
        return 0;
    }

    /**
     * This method returns a random int in the range from min to max.
     *
     * @param min Minimal possible number.
     * @param max Maximal possible number.
     * @return Random number between min and max.
     */
    int randomIntGenerator(int min, int max) {
        return this.randomNumberGenerator.nextInt((max + 1) - min) + min;
    }

    /**
     * Returns true if there is a game database in the system. Else false.
     *
     * @return Returns true if there is a game database in the system. Else false.
     */
    public boolean isDatabaseCreated(Activity activity) {
        File dbFile = activity.getApplicationContext().getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }

}
