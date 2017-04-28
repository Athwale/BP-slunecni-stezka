package ondrej.mejzlik.suntrail.game;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import ondrej.mejzlik.suntrail.R;

import static android.provider.BaseColumns._ID;
import static ondrej.mejzlik.suntrail.activities.GameActivity.END_GAME_PREFERENCE_KEY;
import static ondrej.mejzlik.suntrail.activities.GameActivity.PREFERENCES_KEY;
import static ondrej.mejzlik.suntrail.configuration.Configuration.DAEDALUS_CARGO_SIZE;
import static ondrej.mejzlik.suntrail.configuration.Configuration.FIRST_SHIP;
import static ondrej.mejzlik.suntrail.configuration.Configuration.ICARUS_CARGO_SIZE;
import static ondrej.mejzlik.suntrail.configuration.Configuration.LOKYS_CARGO_SIZE;
import static ondrej.mejzlik.suntrail.configuration.Configuration.MAX_ITEM_PRICE;
import static ondrej.mejzlik.suntrail.configuration.Configuration.MIN_ITEM_PRICE;
import static ondrej.mejzlik.suntrail.configuration.Configuration.PLANETS_WITH_SHOPS;
import static ondrej.mejzlik.suntrail.configuration.Configuration.STARTING_CREDITS;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.DATABASE_NAME;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.DATABASE_VERSION;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_AVAILABLE_AT;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_HAS_BEEN_BOUGHT;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_IMAGE_ICON_RES_ID;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_IMAGE_RES_ID;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_IS_BOUGHT;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_IS_SALEABLE;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_NAME_RES_ID;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_PRICE;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_PRICE_RISE;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_SIZE;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_TEXT_RES_ID;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.ItemsTable.TABLE_NAME_ITEMS;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.PlayerTable.COLUMN_NAME_PLAYER_CREDITS;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.PlayerTable.COLUMN_NAME_PLAYER_CURRENT_PLANET;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.PlayerTable.COLUMN_NAME_PLAYER_DIRECTION;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.PlayerTable.COLUMN_NAME_PLAYER_REWARD;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.PlayerTable.COLUMN_NAME_PLAYER_SHIP_NAME_RES_ID;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.PlayerTable.TABLE_NAME_PLAYER;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_CARGO_SIZE;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_IMAGE_RES_ID;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_NAME_RES_ID;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_PRICE;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.SpaceshipTable.TABLE_NAME_SPACESHIP;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.VisitedPlanets.COLUMN_NAME_PLANET_ID;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.VisitedPlanets.TABLE_NAME_VISITED_PLANETS;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_JUPITER;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_MOON;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_NEPTUNE;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_SUN;

/**
 * This class is a singleton.
 * This class provides methods to handle the database. Create it, add tables, remove whole database,
 * add rows and get rows and get various information from the database.
 */

public class GameDatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_PLAYER_TABLE = "CREATE TABLE " + TABLE_NAME_PLAYER + " (" +
            GameDatabaseContract.PlayerTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            GameDatabaseContract.PlayerTable.COLUMN_NAME_PLAYER_SHIP_NAME_RES_ID + " INTEGER," +
            GameDatabaseContract.PlayerTable.COLUMN_NAME_PLAYER_CREDITS + " INTEGER," +
            GameDatabaseContract.PlayerTable.COLUMN_NAME_PLAYER_CURRENT_PLANET + " INTEGER," +
            GameDatabaseContract.PlayerTable.COLUMN_NAME_PLAYER_DIRECTION + " INTEGER," +
            GameDatabaseContract.PlayerTable.COLUMN_NAME_PLAYER_REWARD + " INTEGER)";

    private static final String CREATE_SPACESHIP_TABLE = "CREATE TABLE " + TABLE_NAME_SPACESHIP + " (" +
            GameDatabaseContract.SpaceshipTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_NAME_RES_ID + " INTEGER," +
            GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_IMAGE_RES_ID + " INTEGER," +
            GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_CARGO_SIZE + " INTEGER," +
            GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_PRICE + " INTEGER)";

    private static final String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_NAME_ITEMS + " (" +
            GameDatabaseContract.ItemsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_PRICE + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_SIZE + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_NAME_RES_ID + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_IMAGE_RES_ID + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_IMAGE_ICON_RES_ID + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_TEXT_RES_ID + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_PRICE_RISE + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_IS_BOUGHT + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_IS_SALEABLE + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_HAS_BEEN_BOUGHT + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_AVAILABLE_AT + " INTEGER)";

    private static final String CREATE_VISITED_PLANETS_TABLE = "CREATE TABLE " + TABLE_NAME_VISITED_PLANETS + " (" +
            GameDatabaseContract.VisitedPlanets._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME_PLANET_ID + " INTEGER)";

    private static final String DELETE_PLAYER_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME_PLAYER;
    private static final String DELETE_SPACESHIP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME_SPACESHIP;
    private static final String DELETE_ITEMS_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME_ITEMS;
    private static final String DELETE_VISITED_PLANETS_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME_VISITED_PLANETS;
    private static final String TAG = "DATABASE";
    private static GameDatabaseHelper instance;
    private GameUtilities gameUtilities = null;

    /**
     * Constructor creates the database helper.
     *
     * @param context App context
     */
    private GameDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.gameUtilities = new GameUtilities();
    }

    /**
     * Factory method to obtain a singleton instance of this database helper.
     *
     * @param context Application context.
     * @return Singleton instance of this object.
     */
    public static synchronized GameDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that we
        // don't accidentally leak an Activity's context.
        if (instance == null) {
            instance = new GameDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        // This method is by default called if the database does not exist. It is called when
        // getWritableDatabase or readable method is called.
        // Create new empty tables for game data.
        database.execSQL(CREATE_PLAYER_TABLE);
        database.execSQL(CREATE_SPACESHIP_TABLE);
        database.execSQL(CREATE_ITEMS_TABLE);
        database.execSQL(CREATE_VISITED_PLANETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int j) {
        // Upgrade policy is to simply to discard the data and start over
        database.execSQL(DELETE_PLAYER_TABLE);
        database.execSQL(DELETE_SPACESHIP_TABLE);
        database.execSQL(DELETE_ITEMS_TABLE);
        database.execSQL(DELETE_VISITED_PLANETS_TABLE);
        // Create new tables
        onCreate(database);
    }

    /**
     * This method fills the database with initial data. Creates a player, ships and items.
     *
     * @param direction     Whether we are going from the Sun to Neptune or vice versa
     * @param currentPlanet Which planet we scanned
     * @param context       App context
     */
    public void initializeDatabaseContents(boolean direction, int currentPlanet, Context context) {
        // Open database.
        SQLiteDatabase db = this.getWritableDatabase();
        // Add rows
        this.addPlayer(db, direction, currentPlanet);
        this.addShips(db);
        this.addItems(db, context.getApplicationContext(), direction);
        this.updateVisitedPlanets(currentPlanet);
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    /**
     * This method is used when creating a new database to add one player to the table. As a part
     * of game data initialization.
     * Default values for a new player are taken from Configuration:
     * Starting ship  = FIRST_SHIP
     * Starting credits = STARTING_CREDITS
     *
     * @param db            Open SQLite database from initializeDatabaseContents
     * @param direction     Trip direction Sun to Neptune or vice versa
     * @param currentPlanet Current planet at which the user first opened the game mode
     */
    private void addPlayer(SQLiteDatabase db, boolean direction, int currentPlanet) {
        // This helps with performance and ensures consistency of the database.
        db.beginTransaction();
        try {
            // Create a new map of values, where column names are the keys
            ContentValues player = new ContentValues();

            player.put(GameDatabaseContract.PlayerTable.COLUMN_NAME_PLAYER_SHIP_NAME_RES_ID, FIRST_SHIP);
            player.put(GameDatabaseContract.PlayerTable.COLUMN_NAME_PLAYER_CREDITS, STARTING_CREDITS);
            player.put(GameDatabaseContract.PlayerTable.COLUMN_NAME_PLAYER_CURRENT_PLANET, currentPlanet);
            player.put(GameDatabaseContract.PlayerTable.COLUMN_NAME_PLAYER_DIRECTION, this.booleanToInt(direction));
            player.put(GameDatabaseContract.PlayerTable.COLUMN_NAME_PLAYER_REWARD, 1);

            db.insertOrThrow(TABLE_NAME_PLAYER, null, player);
            db.setTransactionSuccessful();
        } catch (SQLException ex) {
            Log.d(TAG, "Error while trying to add player to database");
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Because SQLite database can not save boolean values, we need to convert true to 1 and
     * false to 0.
     *
     * @param input Input boolean value.
     * @return 1 if input = true, else 0;
     */
    private int booleanToInt(boolean input) {
        if (input) {
            return 1;
        }
        return 0;
    }

    /**
     * Converts int to boolean. Everything larger than 0 i true.
     *
     * @param input Input int value.
     * @return True if input > 0.
     */
    private boolean intToBoolean(int input) {
        return input > 0;
    }

    /**
     * This method adds all ships into the database as a part of game data initialization.
     *
     * @param db Open SQLite database.
     */
    private void addShips(SQLiteDatabase db) {
        ContentValues ship = new ContentValues();

        db.beginTransaction();
        try {
            // Add icarus
            ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_NAME_RES_ID, R.string.ship_name_icarus);
            ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_IMAGE_RES_ID, R.drawable.pict_icarus_info);
            ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_CARGO_SIZE, ICARUS_CARGO_SIZE);
            // Icarus always costs 100.
            ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_PRICE, 100);
            // Insert the new row (return the primary key value of the new row which we do not use)
            db.insertOrThrow(TABLE_NAME_SPACESHIP, null, ship);
            db.setTransactionSuccessful();
        } catch (SQLException ex) {
            Log.d(TAG, "Error while trying to add icarus to database");
        } finally {
            db.endTransaction();
            ship.clear();
        }

        db.beginTransaction();
        try {
            // Add lokys
            ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_NAME_RES_ID, R.string.ship_name_lokys);
            ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_IMAGE_RES_ID, R.drawable.pict_lokys_info);
            ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_CARGO_SIZE, LOKYS_CARGO_SIZE);
            ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_PRICE, 0);
            // Insert the new row (return the primary key value of the new row which we do not use)
            db.insertOrThrow(TABLE_NAME_SPACESHIP, null, ship);
            db.setTransactionSuccessful();
        } catch (SQLException ex) {
            Log.d(TAG, "Error while trying to add lokys to database");
        } finally {
            db.endTransaction();
            ship.clear();
        }

        db.beginTransaction();
        try {
            // Add daedalus
            ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_NAME_RES_ID, R.string.ship_name_daedalus);
            ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_IMAGE_RES_ID, R.drawable.pict_daedalus_info);
            ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_CARGO_SIZE, DAEDALUS_CARGO_SIZE);
            ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_PRICE, 0);
            // Insert the new row (return the primary key value of the new row which we do not use)
            db.insertOrThrow(TABLE_NAME_SPACESHIP, null, ship);
            db.setTransactionSuccessful();
        } catch (SQLException ex) {
            Log.d(TAG, "Error while trying to add daedalus to database");
        } finally {
            db.endTransaction();
            ship.clear();
        }
    }

    /**
     * This method adds all shop items into the database as a part of game data initialization.
     *
     * @param db        Open SQLite database.
     * @param context   Context of the app. It is needed to have access to resources.
     * @param direction Trip direction
     */
    private void addItems(SQLiteDatabase db, Context context, boolean direction) {
        // Get all shop item names the name is a base for getting other resources
        ArrayList<String> itemNames = this.getAllItemStringNames();
        // Make a writable list of all planets where we have shops
        ArrayList<Integer> shopsWithoutWares = new ArrayList<>();
        // This must be an integer number regardless variable type, each planet must have equal
        // amount of wares
        int howManyItemsToShop = itemNames.size() / PLANETS_WITH_SHOPS.size();
        // Prepare the array list for random planet choosing. We add each planet to the list
        // the number of times the number of items we want to have on each planet.
        // This list must have the same size as the itemNames
        for (int i = 0; i < howManyItemsToShop; i++) {
            shopsWithoutWares.addAll(PLANETS_WITH_SHOPS);
            // If we end on Neptune put items onto the Sun, and vice versa.
            if (direction) {
                // Sun -> Neptune
                shopsWithoutWares.add(PLANET_ID_SUN);
            } else {
                // Neptune -> Sun
                shopsWithoutWares.add(PLANET_ID_NEPTUNE);
            }
        }
        // Randomize the shopsWithoutWares list
        Collections.shuffle(shopsWithoutWares, new Random(System.nanoTime()));

        // Get resources for all items and put them into the database
        for (int i = 0; i < itemNames.size(); i++) {
            // Make a database row container
            ContentValues shopItem = new ContentValues();
            // Get the item string resource name
            String itemName = itemNames.get(i);
            // Get resource id for the item name
            int titleResourceId = context.getResources().getIdentifier(itemName, "string", context.getPackageName());
            // Get resource id for the item image
            String itemImage = itemName.replace("game_item_name_", "pict_");
            int imageResourceId = context.getResources().getIdentifier(itemImage, "drawable", context.getPackageName());
            // Get resource id for the item description
            String itemDescription = itemName.replace("game_item_name_", "game_item_info_");
            int descriptionResourceId = context.getResources().getIdentifier(itemDescription, "string", context.getPackageName());
            // Get resource id for item icon
            String itemIcon = itemName.replace("game_item_name_", "pict_") + "_small";
            int imageIconResourceId = context.getResources().getIdentifier(itemIcon, "drawable", context.getPackageName());
            int itemBasePrice = gameUtilities.calculateBasePrice(MIN_ITEM_PRICE, MAX_ITEM_PRICE);
            int availableAt = shopsWithoutWares.get(i);

            // Add item to database
            db.beginTransaction();
            try {
                shopItem.put(GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_NAME_RES_ID, titleResourceId);
                shopItem.put(GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_IMAGE_RES_ID, imageResourceId);
                shopItem.put(GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_IMAGE_ICON_RES_ID, imageIconResourceId);
                shopItem.put(GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_TEXT_RES_ID, descriptionResourceId);
                // Assign a planet where this item will be available. We randomized the
                // shopsWithoutWares list so we can simply pick a planet at the index where we are
                // in this loop.
                shopItem.put(GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_AVAILABLE_AT, availableAt);
                // Set random item price from a defined range
                shopItem.put(GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_PRICE, itemBasePrice);
                // No items are bought in the beginning
                shopItem.put(GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_IS_BOUGHT, 0);
                // Set all items as displayable
                shopItem.put(GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_IS_SALEABLE, 1);
                // All items have price movement set to rise initially.
                shopItem.put(GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_PRICE_RISE, 1);
                // The has been bought is used to indicate that an a player once in the past bought
                // this item. It is later used to exclude all once bought items from shops if the
                // player returns to an already visited planet.
                shopItem.put(GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_HAS_BEEN_BOUGHT, 0);
                // Size of items is dynamically changed while entering each shop to fit current ship
                shopItem.put(GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_SIZE, 0);
                db.insertOrThrow(TABLE_NAME_ITEMS, null, shopItem);
                db.setTransactionSuccessful();
            } catch (SQLException ex) {
                Log.d(TAG, "Error while trying to add item " + itemImage + " to database");
            } finally {
                db.endTransaction();
                shopItem.clear();
            }
        }
    }

    /**
     * This method returns a list of all saleable items for the game.
     * The list contains the string name="..." of game item name strings for example:
     * <string name="game_item_name_computer_parts">Computer parts</string> it will contain:
     * "game_item_name_computer_parts"
     * It is used later when we add rows to the database for each item.
     *
     * @return Names of shop item string resources.
     */
    private ArrayList<String> getAllItemStringNames() {
        ArrayList<String> names = new ArrayList<>();

        for (Field field : R.string.class.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) && !Modifier.isPrivate(field.getModifiers()) && field.getType().equals(int.class)) {
                try {
                    if (field.getName().startsWith("game_item_name_")) {
                        names.add(field.getName());
                    }
                } catch (IllegalArgumentException ex) {
                    // Ignore exception
                }
            }
        }
        return names;
    }

    /**
     * This method returns all items which the player bought and thus have isBought set to 1 in
     * the database.
     *
     * @return All bought items.
     */
    public ArrayList<ItemModel> getBoughtItems() {
        // "getReadableDatabase()" and "getWritableDatabase()" return the same object
        // (except under low disk space scenarios)
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ItemModel> boughtItems = new ArrayList<>();

        final String QUERY = "SELECT * FROM " + TABLE_NAME_ITEMS + " WHERE " + COLUMN_NAME_ITEM_IS_BOUGHT + " = 1";
        Cursor cursor = db.rawQuery(QUERY, null);

        // Get the data from the cursor, make new game item object and fill it, then add it to the
        // list.
        try {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(_ID));
                    int itemPrice = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ITEM_PRICE));
                    int itemSize = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ITEM_SIZE));
                    int itemName = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ITEM_NAME_RES_ID));
                    int itemImage = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ITEM_IMAGE_RES_ID));
                    int itemImageIcon = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ITEM_IMAGE_ICON_RES_ID));
                    int itemDescription = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ITEM_TEXT_RES_ID));
                    int isBought = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ITEM_IS_BOUGHT));
                    int availableAt = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ITEM_AVAILABLE_AT));
                    int isSaleable = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ITEM_IS_SALEABLE));
                    int priceMovement = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ITEM_PRICE_RISE));
                    ItemModel item = new ItemModel(id, itemPrice, itemName, itemImage,
                            itemImageIcon, itemDescription, availableAt, this.intToBoolean(isSaleable), this.intToBoolean(isBought), this.intToBoolean(priceMovement), itemSize, false);
                    boughtItems.add(item);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Getting items from database failed");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return boughtItems;
    }

    /**
     * Returns a PlayerModel object filled with current player data from the database.
     *
     * @return Returns a PlayerModel object filled with current player data from the database.
     */
    public PlayerModel getPlayerData() {
        SQLiteDatabase db = this.getReadableDatabase();

        final String QUERY = "SELECT * FROM " + TABLE_NAME_PLAYER;
        Cursor cursor = db.rawQuery(QUERY, null);
        PlayerModel playerModel = null;

        try {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(_ID));
                    int shipName = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_PLAYER_SHIP_NAME_RES_ID));
                    int credits = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_PLAYER_CREDITS));
                    int currentPlanet = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_PLAYER_CURRENT_PLANET));
                    int direction = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_PLAYER_DIRECTION));
                    playerModel = new PlayerModel(id, shipName, credits, currentPlanet, this.intToBoolean(direction));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Getting player from database failed");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return playerModel;
    }

    /**
     * Returns ShipModel object containing the ship data corresponding to which ship the player
     * currently has.
     *
     * @return Returns ShipModel object containing the ship data corresponding to which ship the
     * player currently has.
     */
    public ShipModel getShipData() {
        SQLiteDatabase db = this.getReadableDatabase();

        final String QUERY_SHIP = "SELECT * FROM " + TABLE_NAME_PLAYER + " JOIN " + TABLE_NAME_SPACESHIP + " ON " + TABLE_NAME_PLAYER + "." + COLUMN_NAME_PLAYER_SHIP_NAME_RES_ID + " = " + TABLE_NAME_SPACESHIP + "." + COLUMN_NAME_SHIP_NAME_RES_ID;
        Cursor cursorShip = db.rawQuery(QUERY_SHIP, null);
        ShipModel shipModel = null;

        // Calculate the combined size of bought items in cargo bay.
        final String QUERY_ITEMS = "SELECT sum(" + COLUMN_NAME_ITEM_SIZE + ") AS totalSize FROM " + TABLE_NAME_ITEMS + " WHERE " + COLUMN_NAME_ITEM_IS_BOUGHT + " = 1";
        Cursor cursorItems = db.rawQuery(QUERY_ITEMS, null);
        int itemSizeSum = 0;

        try {
            if (cursorItems.moveToFirst()) {
                itemSizeSum = cursorItems.getInt(cursorItems.getColumnIndex("totalSize"));
            }
        } catch (Exception e) {
            Log.d(TAG, "Getting total items size from database failed");
        } finally {
            if (cursorItems != null && !cursorItems.isClosed()) {
                cursorItems.close();
            }
        }

        try {
            if (cursorShip.moveToFirst()) {
                do {
                    int id = cursorShip.getInt(cursorShip.getColumnIndex(_ID));
                    int image = cursorShip.getInt(cursorShip.getColumnIndex(COLUMN_NAME_SHIP_IMAGE_RES_ID));
                    int shipName = cursorShip.getInt(cursorShip.getColumnIndex(COLUMN_NAME_SHIP_NAME_RES_ID));
                    int cargoSize = cursorShip.getInt(cursorShip.getColumnIndex(COLUMN_NAME_SHIP_CARGO_SIZE));
                    int shipPrice = cursorShip.getInt(cursorShip.getColumnIndex(COLUMN_NAME_SHIP_PRICE));
                    shipModel = new ShipModel(id, image, shipName, shipPrice, cargoSize, (cargoSize - itemSizeSum));
                } while (cursorShip.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Getting ship from database failed");
        } finally {
            if (cursorShip != null && !cursorShip.isClosed()) {
                cursorShip.close();
            }
        }
        return shipModel;
    }

    /**
     * Returns items for shop fragment. Each item has isInShop set to true. If the item can not
     * be bought because cargo bay is too small or the player has not enough credits canBeBought is
     * se to false. The returned list contains both already bough items which can be sold and
     * items available in the shop on the current planet.
     *
     * @return List of items in the shop on the current planet + bought items.
     */
    public ArrayList<ItemModel> getItemsForShop(int currentPlanet) {
        ArrayList<ItemModel> shopItems = new ArrayList<>();

        // Get all bought items from database and set that they are now displayed in shop.
        shopItems.addAll(this.getBoughtItems());
        for (ItemModel item : shopItems) {
            item.setInShop(true);
            // Here we could prevent bought items from showing up green in the shop once bought.
        }

        // Get current planet
        PlayerModel player = this.getPlayerData();

        // Get remaining cargo space
        int remainingCargoSpace = this.getShipData().getRemainingCargoSpace();

        // Get items available on current planet and set they are in shop and whether they can be
        // bought. Current planet is updated when we call this method.
        final String QUERY = "SELECT * FROM " + TABLE_NAME_ITEMS + " WHERE " + COLUMN_NAME_ITEM_AVAILABLE_AT + " = " + String.valueOf(currentPlanet);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(_ID));
                    int itemPrice = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ITEM_PRICE));
                    int itemSize = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ITEM_SIZE));
                    int itemName = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ITEM_NAME_RES_ID));
                    int itemImage = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ITEM_IMAGE_RES_ID));
                    int itemImageIcon = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ITEM_IMAGE_ICON_RES_ID));
                    int itemDescription = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ITEM_TEXT_RES_ID));
                    int isBought = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ITEM_IS_BOUGHT));
                    int availableAt = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ITEM_AVAILABLE_AT));
                    int isSaleable = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ITEM_IS_SALEABLE));
                    int priceMovement = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ITEM_PRICE_RISE));
                    int hasBeenBought = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ITEM_HAS_BEEN_BOUGHT));
                    ItemModel item = new ItemModel(id, itemPrice, itemName, itemImage,
                            itemImageIcon, itemDescription, availableAt, this.intToBoolean(isSaleable), this.intToBoolean(isBought), this.intToBoolean(priceMovement), itemSize, false);
                    // Set items are in shop and set if can be bought.
                    if (remainingCargoSpace < itemSize || player.getCredits() < itemPrice) {
                        item.setCanBeBought(false);
                    } else {
                        item.setCanBeBought(true);
                    }
                    item.setInShop(true);
                    if (!this.intToBoolean(hasBeenBought)) {
                        // ShopItems already contain the items the user have in inventory.
                        // IsBought can not be used because it is set to false in every new shop.
                        // This prevents bought items from appearing in the shop twice and only adds
                        // those items to the list that are supposed to be in the shop. Items must
                        // not be saleable in the same shop where they were bought at. Bought items
                        // may have isSaleable set to false. isSaleable is used in ItemInfoFragment
                        // to prevent the user from selling the item in the same shop the user is in.
                        shopItems.add(item);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Getting shop items from database failed");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        // Add ship to the list if we are on the right planet.
        ItemModel ship = this.makeShip(player, currentPlanet);
        if (ship != null) {
            // Set if the ship can be bought.
            if (player.getCredits() < ship.getPrice()) {
                ship.setCanBeBought(false);
            } else {
                ship.setCanBeBought(true);
            }
            shopItems.add(ship);
        }
        return shopItems;
    }

    /**
     * This method makes a special game item which represents a spaceship and returns it only if
     * the player is on a planet where a ship should be available. Otherwise returns null.
     *
     * @param player        PlayerModel instance.
     * @param currentPlanet Current planet
     * @return ItemModel representing a ship or null.
     */
    private ItemModel makeShip(PlayerModel player, int currentPlanet) {
        // The id is irrelevant the ship item is not in database.
        // Calculate how much money the player has and determine ship price as half of that.
        int credits = player.getCredits();
        ArrayList<ItemModel> boughtItems = this.getBoughtItems();
        for (ItemModel item : boughtItems) {
            credits += item.getPrice();
        }
        int shipPrice = credits / 2;
        ShipModel shipData = this.getShipData();
        int currentShip = shipData.getShipNameResId();

        ItemModel lokys = new ItemModel(500, shipPrice, R.string.ship_name_lokys, R.drawable.pict_lokys,
                R.drawable.pict_lokys_small, R.string.ship_info_lokys, currentPlanet, false, false, false, LOKYS_CARGO_SIZE, true);
        lokys.setCanBeBought(true);
        ItemModel daedalus = new ItemModel(500, shipPrice, R.string.ship_name_daedalus, R.drawable.pict_daedalus,
                R.drawable.pict_daedalus_small, R.string.ship_info_daedalus, currentPlanet, false, false, false, DAEDALUS_CARGO_SIZE, true);
        daedalus.setCanBeBought(true);

        if (player.getDirection()) {
            // Sun -> Neptune
            if (currentPlanet == PLANET_ID_MOON) {
                // Only return lokys if we have a worse ship.
                if (currentShip == R.string.ship_name_lokys || currentShip == R.string.ship_info_daedalus) {
                    return null;
                } else {
                    return lokys;
                }
            } else if (currentPlanet == PLANET_ID_JUPITER) {
                if (currentShip == R.string.ship_name_daedalus) {
                    return null;
                } else {
                    return daedalus;
                }
            }
        } else {
            // Neptune -> Sun
            if (currentPlanet == PLANET_ID_JUPITER) {
                if (currentShip == R.string.ship_name_lokys || currentShip == R.string.ship_info_daedalus) {
                    return null;
                } else {
                    return lokys;
                }
            } else if (currentPlanet == PLANET_ID_MOON) {
                if (currentShip == R.string.ship_name_daedalus) {
                    return null;
                } else {
                    return daedalus;
                }
            }
        }
        return null;
    }

    /**
     * Changes which ship the player has in database and decreases credits.
     *
     * @param shipToChange The ship to buy.
     */
    public void buyShip(ShipModel shipToChange) {
        int shipNameResId = shipToChange.getShipNameResId();
        int price = shipToChange.getPrice();
        int currentCredits = this.getPlayerData().getCredits();

        // Change ship in database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues ship = new ContentValues();
        // Set new price
        ship.put(COLUMN_NAME_PLAYER_SHIP_NAME_RES_ID, shipNameResId);
        ship.put(COLUMN_NAME_PLAYER_CREDITS, currentCredits - price);
        // The table has only one row with automatic id 1.
        db.update(TABLE_NAME_PLAYER, ship, "_id=1", null);

        // Update ship price, this is later used to sell the ship at game end.
        ship.clear();
        // Add a little bit to the ship price.
        ship.put(COLUMN_NAME_SHIP_PRICE, this.gameUtilities.calculateSellingPrice(price, true));
        db.update(TABLE_NAME_SPACESHIP, ship, COLUMN_NAME_SHIP_NAME_RES_ID + "=" + String.valueOf(shipNameResId), null);
    }

    /**
     * Updates the current planet value in the player table.
     *
     * @param currentlyScannedPlanetId The planet id the user scanned.
     */
    private void updateCurrentPlanet(int currentlyScannedPlanetId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues player = new ContentValues();
        player.put(COLUMN_NAME_PLAYER_CURRENT_PLANET, currentlyScannedPlanetId);
        // The table has only one row with automatic id 1.
        db.update(TABLE_NAME_PLAYER, player, "_id=1", null);
    }

    /**
     * Updates the price of bought items for which they can be sold. How the price is updated
     * depends on whether it should rise or fall. The percentage of new price difference is set
     * in Configuration. Also all bought items are set to be displayable because we are now in a
     * different shop where we need to be able to sell bought items. Combination of isBought and
     * isSaleable makes items saleable.
     *
     * @param currentPlanet The planet id the user scanned.
     */
    public void updateItems(int currentPlanet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updatedItem = new ContentValues();

        final String QUERY = "SELECT * FROM " + TABLE_NAME_ITEMS + " WHERE " + COLUMN_NAME_ITEM_IS_BOUGHT + " = 1";
        Cursor cursor = db.rawQuery(QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(_ID));
                    int itemPrice = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ITEM_PRICE));
                    int priceMovement = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ITEM_PRICE_RISE));
                    int availableAt = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ITEM_AVAILABLE_AT));
                    // Check if we are on a planet that we already visited if yes, do not update the
                    // price of bought items.
                    if (!this.checkVisitedPlanets(currentPlanet)) {
                        // Update the item price and price movement and return it into the database
                        // only on items not available in the shop we are currently in. Even though items
                        // bought in a shop will not appear in the shop again if the user returns
                        // we also do not want to update the prices of items on the same planet
                        // where we bought them.
                        int newPrice = this.gameUtilities.calculateSellingPrice(itemPrice, this.intToBoolean(priceMovement));
                        updatedItem.put(COLUMN_NAME_ITEM_PRICE, newPrice);
                        updatedItem.put(COLUMN_NAME_ITEM_PRICE_RISE, gameUtilities.calculatePriceMovement());
                        // Set all bought items to be saleable because we are now in a different shop.
                        updatedItem.put(COLUMN_NAME_ITEM_IS_SALEABLE, 1);
                    } else {
                        // Only prevent selling the item in the shop it was bought in.
                        if (availableAt == currentPlanet) {
                            updatedItem.put(COLUMN_NAME_ITEM_IS_SALEABLE, 0);
                        } else {
                            updatedItem.put(COLUMN_NAME_ITEM_IS_SALEABLE, 1);
                        }
                    }
                    db.update(TABLE_NAME_ITEMS, updatedItem, "_id=" + String.valueOf(id), null);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Updating prices in database failed");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    /**
     * This method changes the size of items inside shop on the current planet so that a single
     * item may not be bigger than available cargo space of the current ship.
     *
     * @param currentPlanet Current planet.
     */
    public void adaptItemSizes(int currentPlanet) {
        int cargoSize = this.getShipData().getCargoBaySize();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updatedItem = new ContentValues();

        // Only select items that are available on current planet and has not been given size yet.
        // Only update the size of items that can be bought in the shop.
        final String QUERY = "SELECT * FROM " + TABLE_NAME_ITEMS + " WHERE " + COLUMN_NAME_ITEM_AVAILABLE_AT + " = " + String.valueOf(currentPlanet) + " AND " + COLUMN_NAME_ITEM_SIZE
                + " = 0" + " AND " + COLUMN_NAME_ITEM_IS_BOUGHT + " = 0" + " AND " + COLUMN_NAME_ITEM_HAS_BEEN_BOUGHT + " = 0";
        Cursor cursor = db.rawQuery(QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(_ID));
                    // Update the size of items only when we visit the planet the first time.
                    int newSize = gameUtilities.randomIntGenerator(1, cargoSize - 1);
                    updatedItem.put(COLUMN_NAME_ITEM_SIZE, newSize);
                    db.update(TABLE_NAME_ITEMS, updatedItem, "_id=" + String.valueOf(id), null);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Updating item size in database failed");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    /**
     * This method add the currently visited planet to the list of visited planets.
     * Visited planets are used to check where we must not update prices of bought items.
     */
    public void updateVisitedPlanets(int currentPlanet) {
        // The database connection is cached so it's not expensive to call getWritableDatabase()
        // multiple times.
        SQLiteDatabase db = getWritableDatabase();
        // Update current planet in player table.
        this.updateCurrentPlanet(currentPlanet);

        try {
            db.beginTransaction();
            if (!this.checkVisitedPlanets(currentPlanet)) {
                // The current planet is not in the database, insert it
                ContentValues planet = new ContentValues();
                planet.put(COLUMN_NAME_PLANET_ID, currentPlanet);
                db.insertOrThrow(TABLE_NAME_VISITED_PLANETS, null, planet);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(TAG, "Inserting visited planet list from database failed");
        } finally {
            db.endTransaction();
        }
    }

    /**
     * This method verifies if the planet id is already in the list of visited planets.
     * if yes, returns true else false;
     *
     * @param planetId Planet id to check.
     * @return True if the planet is already in database, false otherwise.
     */
    private boolean checkVisitedPlanets(int planetId) {
        SQLiteDatabase db = getReadableDatabase();
        // Check if this planet already is in the database, if yes return true.
        final String QUERY = "SELECT * FROM " + TABLE_NAME_VISITED_PLANETS + " WHERE " + COLUMN_NAME_PLANET_ID + " = " + String.valueOf(planetId);
        Cursor cursor = db.rawQuery(QUERY, null);
        boolean isPlanetInDatabase = false;

        try {
            if (cursor.moveToFirst()) {
                do {
                    int planetIdFromDb = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_PLANET_ID));
                    if (planetIdFromDb == planetId) {
                        isPlanetInDatabase = true;
                        // We found the planet in database.
                        break;
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Reading visited planets list from database failed");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return isPlanetInDatabase;
    }

    /**
     * This method either performs a buy or a sell operation with an item depending on the operation
     * variable. True = buy, False = sell;
     *
     * @param itemFromShop The item that was bough or sold
     * @param operation    True to buy, false to sell.
     */

    public void buySellItem(ItemModel itemFromShop, boolean operation) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Mark item as bought
        ContentValues item = new ContentValues();
        // True - buy = 1, False - sell = 0.
        item.put(COLUMN_NAME_ITEM_IS_BOUGHT, this.booleanToInt(operation));
        // If we buy an item, remove it from the planet. If we sell an item, do not add it into the
        // shop. This variable is also used to disable selling in a shop where the item was bought.
        item.put(COLUMN_NAME_ITEM_IS_SALEABLE, 0);
        item.put(COLUMN_NAME_ITEM_HAS_BEEN_BOUGHT, 1);

        // The table has only one row with automatic id 1.
        db.update(TABLE_NAME_ITEMS, item, "_id=" + String.valueOf(itemFromShop.getId()), null);

        // Get current player's credits
        int currentCredits = this.getPlayerData().getCredits();
        int newCreditValue;
        if (operation) {
            // True - we buy, decrease player's credits
            newCreditValue = currentCredits - itemFromShop.getPrice();
        } else {
            newCreditValue = currentCredits + itemFromShop.getPrice();
        }

        // Update player's credits.
        ContentValues player = new ContentValues();
        player.put(COLUMN_NAME_PLAYER_CREDITS, newCreditValue);
        db.update(TABLE_NAME_PLAYER, player, "_id=1", null);
    }

    /**
     * This method sells everything including the player's ship.
     *
     * @param context Application context.
     */
    public void endGame(Context context) {
        // Only calculate the final credits if the game is ending the first time.
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREFERENCES_KEY, 0);
        boolean gameEnded = preferences.getBoolean(END_GAME_PREFERENCE_KEY, false);
        if (!gameEnded) {
            SQLiteDatabase db = this.getWritableDatabase();
            // Get total credits
            ArrayList<ItemModel> boughtItems = this.getBoughtItems();
            int credits = this.getPlayerData().getCredits();
            for (ItemModel item : boughtItems) {
                credits += item.getPrice();
            }
            credits += this.getShipData().getPrice();

            // Set game finished 1. Used in fragments to display that game has finished and update
            // credits.
            ContentValues player = new ContentValues();
            player.put(COLUMN_NAME_PLAYER_CREDITS, credits);
            // todo assign reward
            player.put(COLUMN_NAME_PLAYER_REWARD, 100);
            db.update(TABLE_NAME_PLAYER, player, "_id=1", null);
        }
    }

    /**
     * This method checks if the player can buy something in the shop on the current planet. It
     * takes into account both the credits and how many credits the player can obtain if he sells
     * everything he has in the current shop. If despite that nothing can be bought, a price of
     * one item is decreased to the poin the player can buy it.
     *
     * @param enabled True if this method should be run.
     */
    public void failSafe(int currentPlanet, boolean enabled) {
        if (enabled) {
            if (!this.checkVisitedPlanets(currentPlanet)) {
                // Get current credits
                int credits = this.getPlayerData().getCredits();
                // Get available items
                ArrayList<ItemModel> itemsForShop = this.getItemsForShop(currentPlanet);
                // Add credits that the player would have if he sold what he has
                for (ItemModel item : itemsForShop) {
                    if (item.isSaleable() && item.isBought()) {
                        credits += item.getPrice();
                    }
                }
                // Check if some item can be bought
                for (ItemModel item : itemsForShop) {
                    if (!item.isBought() && (item.getPrice() < credits)) {
                        return;
                    }
                }
                // Nothing can be bought lower the price of one item
                if (!itemsForShop.isEmpty()) {
                    ItemModel itemToChange = itemsForShop.get(0);
                    itemToChange.setPrice(credits / 2);
                    this.updateItemPrice(itemToChange);
                }
            }
        }
    }

    /**
     * This method updates an item price.
     *
     * @param itemToChange Item to be updated in the database.
     */
    private void updateItemPrice(ItemModel itemToChange) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues item = new ContentValues();
        // Set new price
        item.put(COLUMN_NAME_ITEM_PRICE, itemToChange.getPrice());
        // The table has only one row with automatic id 1.
        db.update(TABLE_NAME_ITEMS, item, "_id=" + String.valueOf(itemToChange.getId()), null);
    }

    /**
     * Returns all database tables and rows in a string.
     *
     * @return Returns all database tables and rows in a string.
     */
    public String toString() {
        SQLiteDatabase db = this.getReadableDatabase();
        // Add player table data
        String output = "PlayerModel table: \n\tID\t\t\tSHIP NAME RES ID\tCREDITS\t\tCUR PLANET\t\tDIRECTION\t\tREWARD\n";
        Cursor cursorPlayer = db.rawQuery("select * from " + TABLE_NAME_PLAYER, null);
        output += this.getDataFromCursor(cursorPlayer) + "\n\n";

        // Add spaceship table data
        output += "Spaceships table: \n\tID\t\t\tSHIP NAME RES ID\tIMAGE RES ID\t\tCARGO SPACE\tSHIP PRICE\n";
        Cursor cursorShips = db.rawQuery("select * from " + TABLE_NAME_SPACESHIP, null);
        output += this.getDataFromCursor(cursorShips) + "\n\n";

        // Add item data
        output += "Items table: \n\tID\t\t\tPRICE\t\tSIZE\t\tITEM NAME RES ID\tIMAGE RES ID\t\tICON RES ID\t\t\tTEXT RES ID\t\t\tRISE/FALL\tIS BOUGH\tDISPLAYABLE\tHAS BEEN BOUGT\tAVAILABLE AT\n";
        Cursor cursorItems = db.rawQuery("select * from " + TABLE_NAME_ITEMS, null);
        output += this.getDataFromCursor(cursorItems) + "\n\n";

        // Add visited planet data
        output += "Visited planets table: \n\tID\t\t\tPLANET ID\n";
        Cursor cursorPlanets = db.rawQuery("select * from " + TABLE_NAME_VISITED_PLANETS, null);
        output += this.getDataFromCursor(cursorPlanets) + "\n\n";


        if (cursorPlayer != null && !cursorPlayer.isClosed()) {
            cursorPlayer.close();
        }
        if (cursorShips != null && !cursorShips.isClosed()) {
            cursorShips.close();
        }
        if (cursorItems != null && !cursorItems.isClosed()) {
            cursorItems.close();
        }
        if (cursorPlanets != null && !cursorPlanets.isClosed()) {
            cursorPlanets.close();
        }

        Log.d(TAG, output);
        return output;
    }

    /**
     * This method returns all rows from a cursor in a string form. It is used by the
     * toString method.
     *
     * @param cursor Input cursor from a database
     * @return Everything in the cursor as a String
     */
    private String getDataFromCursor(Cursor cursor) {
        String output = "";
        // Move the cursor to the first row
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
        }
        do {
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                output = output + "\t|\t" + String.valueOf(cursor.getInt(i)) + "\t";
            }
            output += "\n";
        } while (cursor.moveToNext());
        return output;
    }
}
