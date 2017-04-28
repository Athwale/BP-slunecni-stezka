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
    private int reward;

    /**
     * Creates a new player and sets all attributes.
     *
     * @param shipResId     Resource id of the ship name.
     * @param credits       Credits
     * @param currentPlanet Current planet
     * @param direction     Trip direction
     */
    PlayerModel(int id, int shipResId, int credits, int currentPlanet, boolean direction) {
        this.id = id;
        this.shipResId = shipResId;
        this.credits = credits;
        this.currentPlanet = currentPlanet;
        this.direction = direction;
    }

    /**
     * Returns database id.
     *
     * @return Returns database id.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns ship name string resource id.
     *
     * @return Returns ship name string resource id.
     */
    public int getShipResId() {
        return shipResId;
    }

    /**
     * Returns reward id.
     *
     * @return Returns reward id;
     */
    public int getReward() {
        return reward;
    }

    /**
     * Set reward id;
     *
     * @param reward New reward id.
     */
    public void setReward(int reward) {
        this.reward = reward;
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
     * Returns current planet.
     *
     * @return Current planet.
     */
    public int getCurrentPlanet() {
        return currentPlanet;
    }

    /**
     * Gets the direction of the trip.
     *
     * @return Direction of the trip.
     */
    public boolean getDirection() {
        return direction;
    }

}
