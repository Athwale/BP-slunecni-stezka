package ondrej.mejzlik.suntrail.game;

/**
 * This class holds all information about a given spaceship and it is used to pass this information
 * between various methods and classes.
 */
public class ShipModel {
    private final int id;
    private final int imageResId;
    private final int shipNameResId;
    private final int cargoBaySize;
    private int price;
    private int remainingCargoSpace;

    /**
     * Create a new ship information holder.
     *
     * @param id                  ship database row id
     * @param imageResId          ship image resource id
     * @param shipNameResId       ship name string resource from database
     * @param price               ship price from database
     * @param cargoBaySize        ship cargo bay container size from database
     * @param remainingCargoSpace ship cargo bay container size from database
     */
    public ShipModel(int id, int imageResId, int shipNameResId, int price, int cargoBaySize, int remainingCargoSpace) {
        this.id = id;
        this.imageResId = imageResId;
        this.shipNameResId = shipNameResId;
        this.price = price;
        this.cargoBaySize = cargoBaySize;
        this.remainingCargoSpace = remainingCargoSpace;
    }

    /**
     * Returns remaining cargo bay space.
     *
     * @return Returns ship database row ID.
     */
    public int getRemainingCargoSpace() {
        return remainingCargoSpace;
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
     * Returns ship image resource id.
     *
     * @return Returns ship name string resource id.
     */
    public int getShipImageResId() {
        return imageResId;
    }

    /**
     * Returns ship cargo bay container size.
     *
     * @return Returns ship cargo bay container size.
     */
    public int getCargoBaySize() {
        return cargoBaySize;
    }

    /**
     * Returns ship price.
     *
     * @return Returns ship price.
     */
    public int getPrice() {
        return price;
    }
}
