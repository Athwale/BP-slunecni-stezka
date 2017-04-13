package ondrej.mejzlik.suntrail.game;

import java.util.Random;

import static ondrej.mejzlik.suntrail.configuration.Configuration.ITEM_PRICE_FALL_RISE_PROBABILITY;
import static ondrej.mejzlik.suntrail.configuration.Configuration.ITEM_PRICE_MIGHT_FALL;
import static ondrej.mejzlik.suntrail.configuration.Configuration.ITEM_PRICE_MIGHT_RISE;

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
     * This method returns the selling price for a given item. By default we sell items for
     * 10 - 20% higher price than what we bought it for.
     *
     * @param basePrice Item price from database, this is the price we bought it for.
     * @return Selling price.
     */
    public int calculateSellingPrice(int basePrice, int min, int max) {
        // Calculate how much we add to the item base price. By default 10 - 20%.
        int profit = this.randomIntGenerator(min, max);
        // Calculate new price use profit as percentage
        return basePrice + ((basePrice / 100) * profit);
    }

    /**
     * This method returns a random price in the range given by the parameters.
     *
     * @param min Minimal price.
     * @param max Maximal price.
     * @return Random price from min to max.
     */
    public int calculateBasePrice(int min, int max) {
        return this.randomIntGenerator(min, max);
    }

    /**
     * This method returns wtehther the item price will fall or raise. If the price is higher than
     * a preset price the probability that the price will fall may be higher depending on a preset
     * value. Same goes for price lower than some price.
     * If the price is between the preset fall and raise values the probability of either rise
     * or fall is 1/2.
     *
     * @param basePrice Item base price
     * @return Whether the price will raise or fall, 1 rise, 0 fall
     */
    public int calculatePriceMovement(int basePrice) {
        int result = this.randomIntGenerator(1, 10);
        int accept = ITEM_PRICE_FALL_RISE_PROBABILITY / 10;

        if (basePrice > ITEM_PRICE_MIGHT_FALL && result <= accept) {
            // price will fall
            return 0;
        } else if (basePrice < ITEM_PRICE_MIGHT_RISE && result <= accept) {
            // price will raise
            return 1;
        } else if (basePrice < ITEM_PRICE_MIGHT_FALL && basePrice > ITEM_PRICE_MIGHT_RISE) {
            if (result <= 5) {
                return 1;
            }
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
    public int randomIntGenerator(int min, int max) {
        return this.randomNumberGenerator.nextInt((max + 1) - min) + min;
    }

}
