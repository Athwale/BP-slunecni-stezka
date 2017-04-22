package ondrej.mejzlik.suntrail.game;

/**
 * This class holds all information about the player and it is used to pass this information
 * between various methods and classes.
 */
public class PlayerModel {
    private final int id;
    private final boolean direction;
    private int shipResId;
    private int credits;
    private int currentPlanet;

    /**
     * Creates a new player and sets all attributes.
     *
     * @param shipResId     Resource id of the ship name.
     * @param credits       Credits
     * @param currentPlanet Current planet
     * @param direction     Trip direction
     */
    public PlayerModel(int id, int shipResId, int credits, int currentPlanet, boolean direction) {
        this.id = id;
        this.shipResId = shipResId;
        this.credits = credits;
        this.currentPlanet = currentPlanet;
        this.direction = direction;
    }

    /**
     * Returns the ship name resource id.
     *
     * @return Returns the ship name resource id.
     */
    public int getShipResId() {
        return shipResId;
    }

    /**
     * Sets the the ship name resource id.
     *
     * @param shipResId Ship name resource id.
     */
    public void setShipResId(int shipResId) {
        this.shipResId = shipResId;
    }

    /**
     * Returns how much credits the player has.
     *
     * @return Returns how much credits the player has.
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Sets a new credit value.
     *
     * @param credits New credit value.
     */
    public void setCredits(int credits) {
        this.credits = credits;
    }

    /**
     * Returns current planet.
     *
     * @return Current planet.
     */
    public int getCurrentPlanet() {
        return currentPlanet;
    }

    /**
     * Sets current planet.
     *
     * @param currentPlanet New current planet.
     */
    public void setCurrentPlanet(int currentPlanet) {
        this.currentPlanet = currentPlanet;
    }

    /**
     * Gets the direction of the trip.
     *
     * @return Direction of the trip.
     */
    public boolean getDirection() {
        return direction;
    }

    /**
     * Returns player row id.
     *
     * @return Returns player row id.
     */
    public int getId() {
        return id;
    }
}
