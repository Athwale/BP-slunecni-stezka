package ondrej.mejzlik.suntrail.game;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import ondrej.mejzlik.suntrail.R;

import static ondrej.mejzlik.suntrail.configuration.Configuration.FIRST_SHIP;
import static ondrej.mejzlik.suntrail.configuration.Configuration.ICARUS_DEFAULT_PRICE;
import static ondrej.mejzlik.suntrail.configuration.Configuration.PLANETS_WITH_SHOPS;
import static ondrej.mejzlik.suntrail.configuration.Configuration.STARTING_CREDITS;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.DATABASE_NAME;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.DATABASE_VERSION;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_ANY;

/**
 * This class provides methods to handle the database. Create it, add tables, remove whole database,
 * add rows and get rows and get various information from the database.
 */

public class GameDatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_PLAYER_TABLE = "CREATE TABLE " + GameDatabaseContract.PlayerTable.TABLE_NAME_PLAYER + " (" +
            GameDatabaseContract.PlayerTable._ID + " INTEGER," +
            GameDatabaseContract.PlayerTable.COLUMN_NAME_PLAYER_SHIP_NAME_RES_ID + " INTEGER PRIMARY KEY," +
            GameDatabaseContract.PlayerTable.COLUMN_NAME_PLAYER_CREDITS + " INTEGER," +
            GameDatabaseContract.PlayerTable.COLUMN_NAME_PLAYER_CURRENT_PLANET + " INTEGER," +
            GameDatabaseContract.PlayerTable.COLUMN_NAME_PLAYER_DIRECTION + " INTEGER)";

    private static final String CREATE_SPACESHIP_TABLE = "CREATE TABLE " + GameDatabaseContract.SpaceshipTable.TABLE_NAME_SPACESHIP + " (" +
            GameDatabaseContract.SpaceshipTable._ID + " INTEGER," +
            GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_NAME_RES_ID + " INTEGER PRIMARY KEY," +
            GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_CONTAINER_SIZE + " TEXT," +
            GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_PRICE + " INTEGER)";

    private static final String CREATE_ITEMS_TABLE = "CREATE TABLE " + GameDatabaseContract.ItemsTable.TABLE_NAME_ITEMS + " (" +
            GameDatabaseContract.ItemsTable._ID + " INTEGER PRIMARY KEY," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_PRICE + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_SIZE + " TEXT," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_NAME_RES_ID + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_IMAGE_RES_ID + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_TEXT_RES_ID + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_SELL_AT + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_IS_BOUGHT + " INTEGER," +
            GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_AVAILABLE_AT + " INTEGER)";

    private static final String DELETE_PLAYER_TABLE = "DROP TABLE IF EXISTS " + GameDatabaseContract.PlayerTable.TABLE_NAME_PLAYER;
    private static final String DELETE_SPACESHIP_TABLE = "DROP TABLE IF EXISTS " + GameDatabaseContract.SpaceshipTable.TABLE_NAME_SPACESHIP;
    private static final String DELETE_ITEMS_TABLE = "DROP TABLE IF EXISTS " + GameDatabaseContract.ItemsTable.TABLE_NAME_ITEMS;
    private GameEngine engine = null;
    // The direction is used when setting item saleability
    private boolean tripDirection;
    // The start planet is used when setting item cargo size. We always have to begin with S size.
    private int startPlanet;

    public GameDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.engine = new GameEngine();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
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
     */
    public void initializeDatabaseContents(boolean direction, int currentPlanet, Activity activity) {
        this.tripDirection = direction;
        this.startPlanet = currentPlanet;

        SQLiteDatabase db = this.getWritableDatabase();
        this.addPlayer(db, direction, currentPlanet);
        this.addShips(db);
        this.addItems(db, activity);
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    /**
     * This method is used when creating a new database to add one player to the table. As a part
     * of game data initialization.
     * Default values for a new player are taken from Configuration:
     * Starting ship - Icarus S
     * Starting credits - 42
     *
     * @param db Open SQLite database from initializeDatabaseContents
     * @param direction Trip direction Sun to Neptune or vice versa
     * @param currentPlanet Current planet at which the user first opened the game mode
     */
    private void addPlayer(SQLiteDatabase db, boolean direction, int currentPlanet) {
        // Create a new map of values, where column names are the keys
        ContentValues player = new ContentValues();

        player.put(GameDatabaseContract.PlayerTable.COLUMN_NAME_PLAYER_SHIP_NAME_RES_ID, FIRST_SHIP);
        player.put(GameDatabaseContract.PlayerTable.COLUMN_NAME_PLAYER_CREDITS, STARTING_CREDITS);
        player.put(GameDatabaseContract.PlayerTable.COLUMN_NAME_PLAYER_CURRENT_PLANET, currentPlanet);
        player.put(GameDatabaseContract.PlayerTable.COLUMN_NAME_PLAYER_DIRECTION, this.booleanToInt(direction));
        db.insert(GameDatabaseContract.PlayerTable.TABLE_NAME_PLAYER, null, player);

        player.clear();
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

        // Add icarus
        ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_NAME_RES_ID, R.string.ship_name_icarus);
        ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_CONTAINER_SIZE, R.string.ship_cargo_size_small);
        // Change icarus default price by a few percent.
        ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_PRICE, engine.calculateSellingPrice(ICARUS_DEFAULT_PRICE));
        // Insert the new row, (returning the primary key value of the new row which we do not use)
        db.insert(GameDatabaseContract.SpaceshipTable.TABLE_NAME_SPACESHIP, null, ship);

        // Add lokys
        ship.clear();
        ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_NAME_RES_ID, R.string.ship_name_lokys);
        ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_CONTAINER_SIZE, R.string.ship_cargo_size_medium);
        // Lokys and Daedalus price will change in the game.
        ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_PRICE, 0);
        // Insert the new row, (returning the primary key value of the new row which we do not use)
        db.insert(GameDatabaseContract.SpaceshipTable.TABLE_NAME_SPACESHIP, null, ship);

        // Add daedalus
        ship.clear();
        ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_NAME_RES_ID, R.string.ship_name_daedalus);
        ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_CONTAINER_SIZE, R.string.ship_cargo_size_large);
        // Lokys and Daedalus price will change in the game.
        ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_PRICE, 0);
        // Insert the new row, (returning the primary key value of the new row which we do not use)
        db.insert(GameDatabaseContract.SpaceshipTable.TABLE_NAME_SPACESHIP, null, ship);

        ship.clear();
    }

    /**
     * This method adds all shop items into the database as a part of game data initialization.
     * Items do not have prices set, because the price changes during the game.
     * Each item is randomly assigned a planet where it will appear in the shop.
     * Each shop MUST have equal amount of items. Number of wares % Number of planets with shops = 0
     * Otherwise a test will fail.
     *
     * Planets which have a new ship available are determined in advance:
     * Icarus S - Sun, Lokys M - Moon, Daedalus L - Jupiter
     * The cargo size of the items is S until second ship can be bought. Then at least one is
     * randomly S the rest is M. Once the L size ship can be bought, at least one item is S, at
     * least one is M and the rest is L.
     * @param db Open SQLite database.
     * @param activity Activity that called this method. It is needed to have access to resources.
     */
    private void addItems(SQLiteDatabase db, Activity activity) {
        // Get all shop item names the name is a base for getting other resources
        ArrayList<String> itemNames = this.getAllItemStringNames();
        // Make a writable list of all planets where we have shops
        ArrayList<Integer> shopsWithoutWares = new ArrayList<>();
        // This must be an integer number regardless variable type, each planet must have equal amount of wares
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
            int titleResourceId = activity.getResources().getIdentifier(itemName, "string", activity.getPackageName());
            // Get resource id for the item image
            String itemImage = itemName.replace("game_item_name_", "pict_");
            int imageResourceId = activity.getResources().getIdentifier(itemImage, "drawable", activity.getPackageName());
            // Get resource id for the item description
            String itemDescription = itemName.replace("pict_", "game_item_info_");
            int descriptionResourceId = activity.getResources().getIdentifier(itemDescription, "string", activity.getPackageName());

            shopItem.put(GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_NAME_RES_ID, titleResourceId);
            shopItem.put(GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_IMAGE_RES_ID, imageResourceId);
            shopItem.put(GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_TEXT_RES_ID, descriptionResourceId);
            // Assign a planet where this item will be available. We randomized the shopsWithoutWares
            // list so we can simply pict a planet at the index where we are in this loop.
            shopItem.put(GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_AVAILABLE_AT, shopsWithoutWares.get(i));
            shopItem.put(GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_PRICE, -1);
            shopItem.put(GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_IS_BOUGHT, 0);
            // Items can be sold anywhere
            shopItem.put(GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_SELL_AT, PLANET_ID_ANY);
            shopItem.put(GameDatabaseContract.ItemsTable.COLUMN_NAME_ITEM_SIZE, "U");

            db.insert(GameDatabaseContract.ItemsTable.TABLE_NAME_ITEMS, null, shopItem);
            shopItem.clear();
        }
        // Decide where items can be sold
        //this.setSellAt(db);
    }

    /**
     * This method sets where shop items can be sold for each planet. It is set randomly either the
     * next planet or the planet after the next. With the exception is that at least one item has to
     * be saleable right at the next planet. And when setting saleability at the penultimate,
     * everything is set to be saleable at the last planet. Where the items can be sold depends
     * on the trip direction.
     *
     * @param db Open SQLite database
     */
    private void setSellAt(SQLiteDatabase db) {

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
}
