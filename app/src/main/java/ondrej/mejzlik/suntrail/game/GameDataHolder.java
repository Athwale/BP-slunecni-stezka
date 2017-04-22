package ondrej.mejzlik.suntrail.game;

import java.util.ArrayList;

/**
 * This class can hold and array of game items, a player instance and a ship instance.
 * It is used to transport the game data.
 */
public class GameDataHolder {
    private final ArrayList<ItemModel> items;
    private final ShipModel ship;
    private final PlayerModel player;

    /**
     * Creates a new data holder.
     *
     * @param items  game items
     * @param ship   game ship
     * @param player game player
     */
    public GameDataHolder(ArrayList<ItemModel> items, ShipModel ship, PlayerModel player) {
        this.items = items;
        this.ship = ship;
        this.player = player;
    }

    /**
     * Returns the items.
     *
     * @return Returns the items.
     */
    public ArrayList<ItemModel> getItems() {
        return items;
    }

    /**
     * Returns the ship.
     *
     * @return Returns the ship.
     */
    public ShipModel getShip() {
        return ship;
    }

    /**
     * Returns the player.
     *
     * @return Returns the player.
     */
    public PlayerModel getPlayer() {
        return player;
    }
}
