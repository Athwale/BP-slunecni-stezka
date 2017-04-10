package ondrej.mejzlik.suntrail.game;

import java.util.Random;

import static ondrej.mejzlik.suntrail.configuration.Configuration.MAX_SELL_PROFIT_PERCENT;
import static ondrej.mejzlik.suntrail.configuration.Configuration.MIN_SELL_PROFIT_PERCENT;

/**
 * This class contains methods necessary for the game to work.
 */
public class GameEngine {
    private Random randomNumberGenerator = null;

    public GameEngine() {
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
    public int calculateSellingPrice(int basePrice) {
        // Calculate how much we add to the item base price. By default 10 - 20%.
        int profit = this.randomIntGenerator(MIN_SELL_PROFIT_PERCENT, MAX_SELL_PROFIT_PERCENT);
        // Calculate new price use profit as percentage
        return basePrice + ((basePrice / 100) * profit);
    }

    /**
     * This method returns a random int in the range from min to max.
     *
     * @param min Minimal possible number.
     * @param max Maximal possible number.
     * @return Random number between min and max.
     */
    private int randomIntGenerator(int min, int max) {
        return this.randomNumberGenerator.nextInt((max + 1) - min) + min;
    }

}
