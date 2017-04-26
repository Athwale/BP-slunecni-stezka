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
    private final boolean isSaleable;
    private int price;
    private boolean priceMovement;
    private boolean isInShop;
    private boolean isBought;
    private boolean canBeBought;

    /**
     * Create a new game item information holder.
     *
     * @param id                   item database row id
     * @param price                item price from database
     * @param itemNameResId        item name string resource id from database
     * @param itemImageResId       item image resource id from database
     * @param itemDescriptionResId item description string resource id from database
     * @param priceMovement        true if the price will rise, false otherwise
     * @param availableAtPlanet    planet id (from PlanetIdentifier) where this item is available
     * @param isSaleable           true if the item can be displayed in shop or inventory
     * @param isBought             is the item in player's inventory?
     * @param size                 item size in cargo bay (S, M, L)
     */
    ItemModel(int id, int price, int itemNameResId, int itemImageResId, int itemImageIconResId, int itemDescriptionResId, int availableAtPlanet, boolean isSaleable,
              boolean isBought, boolean priceMovement, int size) {
        this.id = id;
        this.price = price;
        this.itemNameResId = itemNameResId;
        this.itemImageResId = itemImageResId;
        this.itemImageIconResId = itemImageIconResId;
        this.itemDescriptionResId = itemDescriptionResId;
        this.isSaleable = isSaleable;
        this.availableAtPlanet = availableAtPlanet;
        this.isBought = isBought;
        this.priceMovement = priceMovement;
        this.size = size;
    }

    private ItemModel(Parcel in) {
        this.id = in.readInt();
        this.itemNameResId = in.readInt();
        this.itemImageResId = in.readInt();
        this.itemImageIconResId = in.readInt();
        this.itemDescriptionResId = in.readInt();
        this.availableAtPlanet = in.readInt();
        this.size = in.readInt();
        this.price = in.readInt();
        this.priceMovement = in.readByte() != 0;
        this.isInShop = in.readByte() != 0;
        this.isBought = in.readByte() != 0;
        this.canBeBought = in.readByte() != 0;
        this.isSaleable = in.readByte() != 0;
    }

    /**
     * Returns true if this item can be shown in a list.
     *
     * @return Returns true if this item can be shown in a list.
     */
    public boolean isSaleable() {
        return isSaleable;
    }

    /**
     * Returns true if this item can be sold.
     *
     * @return Returns true if this item can be sold.
     */
    public boolean canBeBought() {
        return canBeBought;
    }

    /**
     * Sets if this item can be sold. True - can be sold.
     *
     * @param canBeBought True - can be sold, false otherwise.
     */
    void setCanBeBought(boolean canBeBought) {
        this.canBeBought = canBeBought;
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
    void setInShop(boolean inShop) {
        isInShop = inShop;
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
     * Sets a new price of the item.
     *
     * @param price New price of the item.
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
     * Returns true if the player has bought this item and it is in his inventory.
     *
     * @return Returns true if the player has bought this item and it is in his inventory.
     */
    public boolean isBought() {
        return isBought;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.itemNameResId);
        dest.writeInt(this.itemImageResId);
        dest.writeInt(this.itemImageIconResId);
        dest.writeInt(this.itemDescriptionResId);
        dest.writeInt(this.availableAtPlanet);
        dest.writeInt(this.size);
        dest.writeInt(this.price);
        dest.writeByte(this.priceMovement ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isInShop ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isBought ? (byte) 1 : (byte) 0);
        dest.writeByte(this.canBeBought ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSaleable ? (byte) 1 : (byte) 0);
    }
}
