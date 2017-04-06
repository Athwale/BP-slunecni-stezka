package ondrej.mejzlik.suntrail.game;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Random;

import ondrej.mejzlik.suntrail.R;

import static ondrej.mejzlik.suntrail.configuration.Configuration.ICARUS_DEFAULT_PRICE;
import static ondrej.mejzlik.suntrail.configuration.Configuration.MAX_SELL_PROFIT_PERCENT;
import static ondrej.mejzlik.suntrail.configuration.Configuration.MIN_SELL_PROFIT_PERCENT;
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

    private Random randomNumberGenerator = null;


    public GameDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // Initialize random number generator once
        this.randomNumberGenerator = new Random();
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        // Create new empty tables for game data.
        database.execSQL(CREATE_PLAYER_TABLE);
        database.execSQL(CREATE_SPACESHIP_TABLE);
        database.execSQL(CREATE_ITEMS_TABLE);

        // TODO remove call this from somewhere else
        this.initializeDatabaseContents();
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
    private void initializeDatabaseContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        this.addPlayer(db);
        this.addShips(db);
        this.addItems(db);
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    /**
     * This method is used when creating a new database to add one player to the table.
     */
    private void addPlayer(SQLiteDatabase db) {

    }

    private void addShips(SQLiteDatabase db) {
        // Create a new map of values, where column names are the keys
        ContentValues ship = new ContentValues();

        // Add icarus
        ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_NAME_RES_ID, R.string.ship_name_icarus);
        ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_CONTAINER_SIZE, R.string.ship_cargo_size_small);
        // Change icarus default price by a few percent.
        ship.put(GameDatabaseContract.SpaceshipTable.COLUMN_NAME_SHIP_PRICE, this.calculateSellingPrice(ICARUS_DEFAULT_PRICE));
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

    private void addItems(SQLiteDatabase db) {

    }

    /**
     * This method returns the selling price for a given item. By default we sell items for
     * 10 - 20% higher price than what we bought it for.
     *
     * @param basePrice Item price from database, this is the price we bought it for.
     * @return Selling price.
     */
    private int calculateSellingPrice(int basePrice) {
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
