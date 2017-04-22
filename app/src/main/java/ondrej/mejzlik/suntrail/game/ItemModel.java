package ondrej.mejzlik.suntrail.game;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class holds all information about a given game item and it is used to pass this information
 * between various methods and classes. This class implements parcelable interface which makes it
 * suitable to be passed to a fragment or activity as a parcel. Then we can reconstruct the object
 * and use the getters and setters as usual.
 */
public class ItemModel implements Parcelable {
    public static final Parcelable.Creator<ItemModel> CREATOR = new Parcelable.Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel source) {
            return new ItemModel(source);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };
    private final int id;
    private final int itemNameResId;
    private final int itemImageResId;
    private final int itemImageIconResId;
    private final int itemDescriptionResId;
    private final int availableAtPlanet;
    private final int size;
    private int price;
    private int sellPrice;
    private boolean isBought;
    private boolean priceMovement;
    private boolean isInShop;

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

    private ItemModel(Parcel in) {
        this.id = in.readInt();
        this.price = in.readInt();
        this.sellPrice = in.readInt();
        this.itemNameResId = in.readInt();
        this.itemImageResId = in.readInt();
        this.itemImageIconResId = in.readInt();
        this.itemDescriptionResId = in.readInt();
        this.availableAtPlanet = in.readInt();
        this.isBought = in.readByte() != 0;
        this.priceMovement = in.readByte() != 0;
        this.isInShop = in.readByte() != 0;
        this.size = in.readInt();
    }

    /**
     * Returns if the item is supposed to be displayed in shop.
     *
     * @return Returns if the item is supposed to be displayed in shop.
     */
    public boolean isInShop() {
        return isInShop;
    }

    /**
     * Sets if the item is supposed to be displayed in shop.
     *
     * @param inShop True if the item is supposed to be displayed in shop.
     */
    public void setInShop(boolean inShop) {
        isInShop = inShop;
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

    /**
     * Returns the price movement for this item. True - price will rise, false - price will fall.
     *
     * @return Returns the price movement for this item. True - price will rise, false - price will
     * fall.
     */
    public boolean getPriceMovement() {
        return priceMovement;
    }

    /**
     * Sets the price movement for this item. True - price will rise, false - price will fall.
     *
     * @param priceMovement Sets the price movement for this item. True - price will rise, false -
     *                      price will fall.
     */
    public void setPriceMovement(boolean priceMovement) {
        this.priceMovement = priceMovement;
    }

    @Override
    public int describeContents() {
        // Unique descriptor. Resource ids are unique, we can use that.
        return this.hashCode() + this.getItemNameResId();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.price);
        dest.writeInt(this.sellPrice);
        dest.writeInt(this.itemNameResId);
        dest.writeInt(this.itemImageResId);
        dest.writeInt(this.itemImageIconResId);
        dest.writeInt(this.itemDescriptionResId);
        dest.writeInt(this.availableAtPlanet);
        dest.writeByte(this.isBought ? (byte) 1 : (byte) 0);
        dest.writeByte(this.priceMovement ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isInShop ? (byte) 1 : (byte) 0);
        dest.writeInt(this.size);
    }
}
