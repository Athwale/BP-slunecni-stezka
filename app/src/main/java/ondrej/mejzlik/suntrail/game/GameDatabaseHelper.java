package ondrej.mejzlik.suntrail.game;

import android.content.ContentValues;
import android.content.Context;
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
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.ItemsTable.TABLE_NAME_ITEMS;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.PlayerTable.TABLE_NAME_PLAYER;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.SpaceshipTable.TABLE_NAME_SPACESHIP;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_CERES;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_EARTH;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_MARS;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_MERCURY;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_MOON;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_SUN;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_VENUS;

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
            GameDatabaseContract.PlayerTable.COLUMN_NAME_PLAYER_DIRECTION + " INTEGER)";

    private static final String CREATE_SPACESHIP_TABLE = "CREATE TABLE " + TABLE_NAME_SPACESHIP + " (" +
            GameDatabaseContract.SpaceshipTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_NAME_RES_ID + " INTEGER," +
            GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_CARGO_SIZE + " INTEGER," +
            GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_PRICE + " INTEGER)";

    private static final String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_NAME_ITEMS + " (" +
            GameDatabaseContract.ItemsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_PRICE + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_SELL_PRICE + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_SIZE + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_NAME_RES_ID + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_IMAGE_RES_ID + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_IMAGE_ICON_RES_ID + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_TEXT_RES_ID + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_PRICE_RISE + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_IS_BOUGHT + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_AVAILABLE_AT + " INTEGER)";

    private static final String DELETE_PLAYER_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME_PLAYER;
    private static final String DELETE_SPACESHIP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME_SPACESHIP;
    private static final String DELETE_ITEMS_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME_ITEMS;
    private static final String TAG = "DATABASE";
    private static GameDatabaseHelper instance;
    private GameUtilities engine = null;

    /**
     * Constructor creates the database helper.
     *
     * @param context App context
     */
    private GameDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.engine = new GameUtilities();
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int j) {
        // Upgrade policy is to simply to discard the data and start over
        database.execSQL(DELETE_PLAYER_TABLE);
        database.execSQL(DELETE_SPACESHIP_TABLE);
        database.execSQL(DELETE_ITEMS_TABLE);
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
        this.addItems(db, context.getApplicationContext());
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
            ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_CARGO_SIZE, ICARUS_CARGO_SIZE);
            // Ships may have a price (currently unused)
            ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_PRICE, 0);
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
     * @param db      Open SQLite database.
     * @param context Context of the app. It is needed to have access to resources.
     */
    private void addItems(SQLiteDatabase db, Context context) {
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
            int itemBasePrice = engine.calculateBasePrice(MIN_ITEM_PRICE, MAX_ITEM_PRICE);
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
                // Set the same price as buy price. New sell prices are calculated when starting the
                // game activity. Recalculation is prevented with a lock boolean variable set to
                // true until you leave the planet.
                shopItem.put(GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_SELL_PRICE, itemBasePrice);
                // No items are bought in the beginning
                shopItem.put(GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_IS_BOUGHT, 0);
                // Determine whether the price will raise or fall if price is high it might fall if
                // low it might rise
                shopItem.put(GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_PRICE_RISE, engine.calculatePriceMovement(itemBasePrice));
                // Determine item size. The size of the item can not be larger than a half of the
                // available cargo bay size.
                int itemSize;
                if (availableAt == PLANET_ID_SUN || availableAt == PLANET_ID_MERCURY || availableAt == PLANET_ID_VENUS || availableAt == PLANET_ID_EARTH) {
                    // We have Icarus S
                    itemSize = engine.randomIntGenerator(1, ICARUS_CARGO_SIZE / 2);
                } else if (availableAt == PLANET_ID_MOON || availableAt == PLANET_ID_MARS || availableAt == PLANET_ID_CERES) {
                    // We have Lokys M
                    itemSize = engine.randomIntGenerator(1, LOKYS_CARGO_SIZE / 2);
                } else {
                    // We have Daedalus L
                    itemSize = engine.randomIntGenerator(1, DAEDALUS_CARGO_SIZE / 2);
                }
                shopItem.put(GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_SIZE, itemSize);

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
     * Returns all database tables and rows in a string.
     *
     * @return Returns all database tables and rows in a string.
     */
    public String toString() {
        SQLiteDatabase db = this.getReadableDatabase();
        // Add player table data
        String output = "Player table: \n\tID\t\t\tSHIP NAME RES ID\tCREDITS\t\tCUR PLANET\t\tDIRECTION\n";
        Cursor cursorPlayer = db.rawQuery("select * from " + TABLE_NAME_PLAYER, null);
        output += this.getDataFromCursor(cursorPlayer) + "\n\n";

        // Add spaceship table data
        output += "Spaceships table: \n\tID\t\t\tSHIP NAME RES ID\tCARGO SPACE\tSHIP PRICE\n";
        Cursor cursorShips = db.rawQuery("select * from " + TABLE_NAME_SPACESHIP, null);
        output += this.getDataFromCursor(cursorShips) + "\n\n";

        // Add item data
        output += "Items table: \n\tID\t\t\tPRICE\t\tSELL PRICE\tSIZE\t\tITEM NAME RES ID\tIMAGE RES ID\t\tICON RES ID\t\t\tTEXT RES ID\t\t\tRISE/FALL\tIS BOUGH\tAVAILABLE AT\n";
        Cursor cursorItems = db.rawQuery("select * from " + TABLE_NAME_ITEMS, null);
        output += this.getDataFromCursor(cursorItems) + "\n\n";

        cursorPlayer.close();
        cursorShips.close();
        cursorItems.close();
        db.close();

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
