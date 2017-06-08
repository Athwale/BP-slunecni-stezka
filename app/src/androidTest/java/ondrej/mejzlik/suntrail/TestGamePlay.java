package ondrej.mejzlik.suntrail;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ondrej.mejzlik.suntrail.game.GameDatabaseHelper;
import ondrej.mejzlik.suntrail.game.ItemModel;
import ondrej.mejzlik.suntrail.game.PlayerModel;
import ondrej.mejzlik.suntrail.game.ShipModel;

import static ondrej.mejzlik.suntrail.configuration.Configuration.STARTING_CREDITS;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.DATABASE_NAME;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_SUN;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Instrumentation test, which will execute on an Android device.
 * Simulates a dumb player who just buys everything available and then sells everything he has on
 * the next planet and always buys a new ship if available.
 * And a smart player who only sells items which has price set to decrease and buys as many items
 * as possible increasing profit.
 */
@RunWith(AndroidJUnit4.class)
public class TestGamePlay {
    private final int DUMB_TESTS_COUNT = 10;
    private final int SMART_TESTS_COUNT = 1000;

    @Test
    public void lazyGamePlay() throws Exception {
        int startPlanet = PLANET_ID_SUN;

        // Context of the app under test.
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DATABASE_NAME);

        Log.d("GAME START CREDITS", String.valueOf(STARTING_CREDITS));

        for (int i = 0; i < DUMB_TESTS_COUNT; i++) {

            // Initialize database
            GameDatabaseHelper databaseHelper = GameDatabaseHelper.getInstance(context);
            databaseHelper.initializeDatabaseContents(true, startPlanet, context);
            databaseHelper.adaptItemSizes(startPlanet);

            // Simulate buying and selling items and ships.
            // We have 10 shops, 11th planet is the end.
            for (int currentPlanet = 1; currentPlanet <= 10; currentPlanet++) {
                // Perform database updates before buying just like in game activity.
                databaseHelper.updateItems(currentPlanet);
                databaseHelper.adaptItemSizes(currentPlanet);
                boolean alreadyVisited = databaseHelper.checkVisitedPlanets(currentPlanet);
                // If the planet has been visited, we do no need to run the failsafe again and update
                // the visited planets table
                if (!alreadyVisited) {
                    databaseHelper.failSafe(currentPlanet);
                    databaseHelper.updateVisitedPlanets(currentPlanet);
                } else {
                    // Add current planet to visited planets table
                    databaseHelper.updateVisitedPlanets(currentPlanet);
                }
                // Update current planet value in the player table.
                databaseHelper.updateCurrentPlanet(currentPlanet);

                // Enter shop.
                ArrayList<ItemModel> itemsForShop = databaseHelper.getItemsForShop(currentPlanet);
                // Sell everything the player has in inventory.
                for (ItemModel item : itemsForShop) {
                    if (item.isBought() && item.isSaleable() && item.isInShop()) {
                        databaseHelper.buySellItem(item, false);
                    }
                }
                // Update the list
                itemsForShop = databaseHelper.getItemsForShop(currentPlanet);

                // Buy ship if available
                for (ItemModel item : itemsForShop) {
                    if (item.canBeBought() && item.isShip()) {
                        ShipModel ship = new ShipModel(500, item.getItemImageResId(), item.getItemNameResId(), item.getPrice(), item.getSize(), item.getSize());
                        databaseHelper.buyShip(ship);
                    }
                }

                // Update the list
                itemsForShop = databaseHelper.getItemsForShop(currentPlanet);

                // Buy everything possible
                for (ItemModel item : itemsForShop) {
                    if (item.canBeBought() && item.isInShop() && !item.isBought()) {
                        databaseHelper.buySellItem(item, true);
                    }
                }
            }
            databaseHelper.endGame(context);
            // Get score
            PlayerModel playerData = databaseHelper.getPlayerData();
            assertTrue(playerData.getCredits() > STARTING_CREDITS);
            Log.d("GAME FINAL CREDITS", String.valueOf(playerData.getCredits()));
            Log.d("GAME FINAL SHIP", context.getResources().getString(playerData.getShipResId()));
            Log.d("GAME", "-- -- -- -- --");

            // Destroy database
            databaseHelper.close();
            context.deleteDatabase(DATABASE_NAME);
            File dbFile = context.getApplicationContext().getDatabasePath(DATABASE_NAME);
            // Check database destroyed
            assertFalse("Database destroyed", dbFile.exists());
        }
    }

    @Test
    public void smartGamePlay() {
        int startPlanet = PLANET_ID_SUN;

        // Context of the app under test.
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(DATABASE_NAME);

        Log.d("GAME START CREDITS", String.valueOf(STARTING_CREDITS));

        for (int i = 0; i < SMART_TESTS_COUNT; i++) {

            // Initialize database
            GameDatabaseHelper databaseHelper = GameDatabaseHelper.getInstance(context);
            databaseHelper.initializeDatabaseContents(true, startPlanet, context);
            databaseHelper.adaptItemSizes(startPlanet);

            // Simulate buying and selling items and ships.
            // We have 10 shops, 11th planet is the end.
            for (int currentPlanet = 1; currentPlanet <= 10; currentPlanet++) {
                // Perform database updates before buying just like in game activity.
                databaseHelper.updateItems(currentPlanet);
                databaseHelper.adaptItemSizes(currentPlanet);
                boolean alreadyVisited = databaseHelper.checkVisitedPlanets(currentPlanet);
                // If the planet has been visited, we do no need to run the failsafe again and update
                // the visited planets table
                if (!alreadyVisited) {
                    databaseHelper.failSafe(currentPlanet);
                    databaseHelper.updateVisitedPlanets(currentPlanet);
                } else {
                    // Add current planet to visited planets table
                    databaseHelper.updateVisitedPlanets(currentPlanet);
                }
                // Update current planet value in the player table.
                databaseHelper.updateCurrentPlanet(currentPlanet);

                // Enter shop.
                ArrayList<ItemModel> itemsForShop = databaseHelper.getItemsForShop(currentPlanet);
                // Sell only items where the price movement is set to decrease.
                for (ItemModel item : itemsForShop) {
                    if (item.isBought() && item.isSaleable() && item.isInShop() && !item.getPriceMovement()) {
                        databaseHelper.buySellItem(item, false);
                    }
                }
                // Update the list
                itemsForShop = databaseHelper.getItemsForShop(currentPlanet);

                // Buy ship if available and if possible
                for (ItemModel item : itemsForShop) {
                    if (item.canBeBought() && item.isShip()) {
                        ShipModel ship = new ShipModel(500, item.getItemImageResId(), item.getItemNameResId(), item.getPrice(), item.getSize(), item.getSize());
                        databaseHelper.buyShip(ship);
                    }
                }

                // Update the list
                itemsForShop = databaseHelper.getItemsForShop(currentPlanet);
                // Sort the list from smallest to largest.
                Collections.sort(itemsForShop, new ItemSizeComparator());

                // Buy items that are smaller in order to buy more of them in each shop.
                for (ItemModel item : itemsForShop) {
                    if (item.canBeBought() && item.isInShop() && !item.isBought()) {
                        databaseHelper.buySellItem(item, true);
                    }
                }
            }
            databaseHelper.endGame(context);
            // Get score
            PlayerModel playerData = databaseHelper.getPlayerData();
            assertTrue(playerData.getCredits() > STARTING_CREDITS);
            Log.d("GAME FINAL CREDITS", String.valueOf(playerData.getCredits()));
            Log.d("GAME FINAL SHIP", context.getResources().getString(playerData.getShipResId()));
            Log.d("GAME", "-- -- -- -- --");

            // Destroy database
            databaseHelper.close();
            context.deleteDatabase(DATABASE_NAME);
            File dbFile = context.getApplicationContext().getDatabasePath(DATABASE_NAME);
            // Check database destroyed
            assertFalse("Database destroyed", dbFile.exists());
        }
    }

    private class ItemSizeComparator implements Comparator<ItemModel> {

        @Override
        public int compare(ItemModel item1, ItemModel item2) {
            int size1 = item1.getSize();
            int size2 = item2.getSize();

            if (size1 == size2) {
                return 0;
            } else if (size1 > size2) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
