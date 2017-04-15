package ondrej.mejzlik.suntrail.game;

import android.provider.BaseColumns;

/**
 * This class is a holder for constants which define the database, tables and columns.
 */

public final class GameDatabaseContract {
    public static final String DATABASE_NAME = "GameDatabase.db";
    // If you change the database schema, you must increment the database version.
    static final int DATABASE_VERSION = 1;


    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private GameDatabaseContract() {
    }

    /**
     * This class defines the names for Player table. It holds basic player information.
     * This table has only one row, because we have only one player.
     * The ID column is present by default.
     * By implementing the BaseColumns interface, the inner class can inherit a primary key
     * field called _ID that some Android classes such as cursor adaptors will expect it to have.
     */
    public static class PlayerTable implements BaseColumns {
        public static final String TABLE_NAME_PLAYER = "player";
        // All Integer values
        // Primary key is _ID
        public static final String COLUMN_NAME_PLAYER_SHIP_NAME_RES_ID = "shipName";
        public static final String COLUMN_NAME_PLAYER_CREDITS = "credits";
        public static final String COLUMN_NAME_PLAYER_CURRENT_PLANET = "currentPlanet";
        public static final String COLUMN_NAME_PLAYER_DIRECTION = "direction";
    }

    /**
     * This class defines the names for Spaceship table. This table has 3 rows.
     */
    public static class SpaceshipTable implements BaseColumns {
        public static final String TABLE_NAME_SPACESHIP = "spaceship";
        // All Integer values
        // Primary key is _ID
        public static final String COLUMN_NAME_SHIP_NAME_RES_ID = "shipName";
        // Description is not a part of the row. ShipInfoFragment decides what to show by the ship
        // name. Because when we first use ship description before starting a game, we do not have
        // the database.
        public static final String COLUMN_NAME_SHIP_CARGO_SIZE = "shipContainerSize";
        public static final String COLUMN_NAME_SHIP_PRICE = "shipPrice";
    }

    /**
     * This class defines the names for Game items table. This table has 33 rows.
     */
    public static class ItemsTable implements BaseColumns {
        public static final String TABLE_NAME_ITEMS = "gameItems";
        // All Integer values
        // Primary key is _ID
        public static final String COLUMN_NAME_ITEM_PRICE = "itemPrice";
        public static final String COLUMN_NAME_ITEM_SIZE = "itemSize";
        public static final String COLUMN_NAME_ITEM_NAME_RES_ID = "itemName";
        public static final String COLUMN_NAME_ITEM_IMAGE_RES_ID = "itemImage";
        public static final String COLUMN_NAME_ITEM_IMAGE_ICON_RES_ID = "itemImageIcon";
        public static final String COLUMN_NAME_ITEM_TEXT_RES_ID = "itemDescription";
        public static final String COLUMN_NAME_ITEM_IS_BOUGHT = "isBought";
        public static final String COLUMN_NAME_ITEM_AVAILABLE_AT = "availableAt";
        public static final String COLUMN_NAME_ITEM_PRICE_RISE = "priceRise";
        public static final String COLUMN_NAME_ITEM_SELL_PRICE = "sellPrice";
    }
}
