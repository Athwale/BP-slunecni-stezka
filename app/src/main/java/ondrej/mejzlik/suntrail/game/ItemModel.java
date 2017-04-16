package ondrej.mejzlik.suntrail.game;

/**
 * This class holds all information about a given game item and it is used to pass this information
 * between various methods and classes.
 */
public class ItemModel {
    private int id;
    private int price;
    private int sellPrice;
    private int itemNameResId;
    private int itemImageResId;
    private int itemImageIconResId;
    private int itemDescriptionResId;
    private int availableAtPlanet;
    private boolean isBought;
    private boolean priceMovement;
    private int size;

    /**
     * Create a new game item information holder.
     *
     * @param id                   item database row id
     * @param price                item price from database
     * @param sellPrice            item selling price from database
     * @param itemNameResId        item name string resource id from database
     * @param itemImageResId       item image resource id from database
     * @param itemDescriptionResId item description string resource id from database
     * @param priceMovement        true if the price will rise, false otherwise
     * @param availableAtPlanet    planet id (from PlanetIdentifier) where this item is available
     * @param isBought             is the item in player's inventory?
     * @param size                 item size in cargo bay (S, M, L)
     */
    public ItemModel(int id, int price, int sellPrice, int itemNameResId, int itemImageResId, int itemImageIconResId, int itemDescriptionResId, int availableAtPlanet, boolean isBought, boolean priceMovement, int size) {
        this.id = id;
        this.price = price;
        this.sellPrice = sellPrice;
        this.itemNameResId = itemNameResId;
        this.itemImageResId = itemImageResId;
        this.itemImageIconResId = itemImageIconResId;
        this.itemDescriptionResId = itemDescriptionResId;
        this.availableAtPlanet = availableAtPlanet;
        this.isBought = isBought;
        this.priceMovement = priceMovement;
        this.size = size;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
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
     * Returns item image icon resource id.
     *
     * @return Returns item image icon resource id.
     */
    public int getItemImageIconResId() {
        return itemImageIconResId;
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
    public int getSize() {
        return size;
    }

    public boolean getPriceMovement() {
        return priceMovement;
    }

    public void setPriceMovement(boolean priceMovement) {
        this.priceMovement = priceMovement;
    }
}
