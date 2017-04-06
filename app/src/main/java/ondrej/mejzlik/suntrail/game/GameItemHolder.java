package ondrej.mejzlik.suntrail.game;

/**
 * This class holds all information about a given game item and it is used to pass this information
 * between various methods and classes.
 */
public class GameItemHolder {
    private int id;
    private int price;
    private int itemNameResId;
    private int itemImageResId;
    private int itemDescriptionResId;
    private int sellableAtPlanetId;
    private int availableAtPlanet;
    private boolean isBought;
    private String size;

    /**
     * Create a new game item information holder.
     *
     * @param id                   item database row id
     * @param price                item price from database
     * @param itemNameResId        item name string resource id from database
     * @param itemImageResId       irem image resource id from database
     * @param itemDescriptionResId item description string resource id from database
     * @param sellableAtPlanetId   planet id (from PlanetIdentifier) where this item can be sold
     * @param availableAtPlanet    planet id (from PlanetIdentifier) where this item is available
     * @param isBought             is the item in player's inventory?
     * @param size                 item size in cargo bay (S, M, L)
     */
    public GameItemHolder(int id, int price, int itemNameResId, int itemImageResId, int itemDescriptionResId, int sellableAtPlanetId, int availableAtPlanet, boolean isBought, String size) {
        this.id = id;
        this.price = price;
        this.itemNameResId = itemNameResId;
        this.itemImageResId = itemImageResId;
        this.itemDescriptionResId = itemDescriptionResId;
        this.sellableAtPlanetId = sellableAtPlanetId;
        this.availableAtPlanet = availableAtPlanet;
        this.isBought = isBought;
        this.size = size;
    }

    /**
     * Returns item database row id.
     *
     * @return Returns item database row id.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns item price.
     *
     * @return Returns item price.
     */
    public int getPrice() {
        return price;
    }

    /**
     * Sets item price.
     *
     * @param price New item price.
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Returns item name string resource id.
     *
     * @return Returns item name string resource id.
     */
    public int getItemNameResId() {
        return itemNameResId;
    }

    /**
     * Returns item image resource id.
     *
     * @return Returns item image resource id.
     */
    public int getItemImageResId() {
        return itemImageResId;
    }

    /**
     * Returns item description string resource id.
     *
     * @return Returns item description string resource id.
     */
    public int getItemDescriptionResId() {
        return itemDescriptionResId;
    }

    /**
     * Returns planet id (from PlanetIdentifier) where this item can be sold.
     *
     * @return Returns planet id (from PlanetIdentifier) where this item can be sold.
     */
    public int getSellableAtPlanetId() {
        return sellableAtPlanetId;
    }

    /**
     * Returns planet id (from PlanetIdentifier) where this item can be bought.
     *
     * @return Returns planet id (from PlanetIdentifier) where this item can be bought.
     */
    public int getAvailableAtPlanet() {
        return availableAtPlanet;
    }

    /**
     * Returns true if the player has bought this item and it is in his inventory.
     *
     * @return Returns true if the player has bought this item and it is in his inventory.
     */
    public boolean isBought() {
        return isBought;
    }

    /**
     * Pass true to indicate that this item is in the player's inventory.
     *
     * @param bought Pass true to indicate that this item is in the player's inventory.
     */
    public void setBought(boolean bought) {
        isBought = bought;
    }

    /**
     * Returns item size as set in database (S, M, L)
     *
     * @return Returns item size as set in database (S, M, L)
     */
    public String getSize() {
        return size;
    }
}
