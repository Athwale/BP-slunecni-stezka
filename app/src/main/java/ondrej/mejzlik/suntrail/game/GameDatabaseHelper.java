package ondrej.mejzlik.suntrail.game;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import ondrej.mejzlik.suntrail.R;

import static ondrej.mejzlik.suntrail.configuration.Configuration.FIRST_SHIP;
import static ondrej.mejzlik.suntrail.configuration.Configuration.ICARUS_DEFAULT_PRICE;
import static ondrej.mejzlik.suntrail.configuration.Configuration.SHOPS_COUNT;
import static ondrej.mejzlik.suntrail.configuration.Configuration.STARTING_CREDITS;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.DATABASE_NAME;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.DATABASE_VERSION;

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
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
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
    public void initializeDatabaseContents(boolean direction, int currentPlanet) {
        // Get all string resource shop item names for later use to add to the database.
        ArrayList<String> shopItemNamesNoPrefix = this.getAllItemStringNames();

        SQLiteDatabase db = this.getWritableDatabase();
        this.addPlayer(db, direction, currentPlanet);
        this.addShips(db);
        this.addItems(db, shopItemNamesNoPrefix);
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    /**
     * This method returns a list of all saleable items for the game.
     * It returns the string name="..." for later use when we add records to the database for
     * each item.
     *
     * @return Names of shop item string resources without the common prefix.
     */
    private ArrayList<String> getAllItemStringNames() {
        ArrayList<String> names = new ArrayList<>();

        for (Field field : R.string.class.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) && !Modifier.isPrivate(field.getModifiers()) && field.getType().equals(int.class)) {
                try {
                    if (field.getName().startsWith("game_item_name_")) {
                        // Cut the prefix away
                        names.add(field.getName().substring(15, field.getName().length()));
                    }
                } catch (IllegalArgumentException ex) {
                    // Ignore exception
                }
            }
        }
        return names;
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
     * Items do not have prices set, because the price changes according to how much money the
     * player has during the game. Each item is randomly assigned a planet where it will appear in
     * in the shop. Each planet has equal amount of items with the exception that the last planet
     * might have less. Each item is assigned a planet where it can be sold. It is assigned randomly
     * but at least one item has to be saleable on the next planet.
     * Planets which have a new ship available are determined in advance:
     * Icarus S - Sun, Lokys M - Moon, Daedalus L - Jupiter
     * The cargo size of the items is S until second ship can be bought. Then at least one is
     * randomly S the rest is M. Once the L size ship can be bought, at least one item is S, at
     * least one is M and the rest is L.
     * // TODO change algorithm description
     * @param db Open SQLite database.
     */
    private void addItems(SQLiteDatabase db, ArrayList<String> shopItemsNoPrefix) {
        // Get how many items we have
        int itemCount = shopItemsNoPrefix.size();
        // Determine how many items each planet can have
        int itemsInShop = itemCount / SHOPS_COUNT;

    }
}
