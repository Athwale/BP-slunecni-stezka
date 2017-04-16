package ondrej.mejzlik.suntrail.game;

/**
 * This class holds all information about a given spaceship and it is used to pass this information
 * between various methods and classes.
 */
public class ShipModel {
    private int id;
    private int shipNameResId;
    private int price;
    private int containerSize;

    /**
     * Create a new ship information holder.
     *
     * @param id            ship database row id
     * @param shipNameResId ship name string resource from database
     * @param price         ship price from database
     * @param containerSize ship cargo bay container size from database
     */
    public ShipModel(int id, int shipNameResId, int price, int containerSize) {
        this.id = id;
        this.shipNameResId = shipNameResId;
        this.price = price;
        this.containerSize = containerSize;
    }

    /**
     * Returns ship database row ID.
     *
     * @return Returns ship database row ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns ship name string resource id.
     *
     * @return Returns ship name string resource id.
     */
    public int getShipNameResId() {
        return shipNameResId;
    }

    /**
     * Returns ship price.
     *
     * @return Returns ship price.
     */
    public int getPrice() {
        return price;
    }

    /**
     * Sets ship price.
     *
     * @param price New ship price.
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Returns ship cargo bay container size.
     *
     * @return Returns ship cargo bay container size.
     */
    public int getContainerSize() {
        return containerSize;
    }
}
