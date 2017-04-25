package ondrej.mejzlik.suntrail.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.fragments.DirectionChoiceFragment;
import ondrej.mejzlik.suntrail.fragments.GameMenuFragment;
import ondrej.mejzlik.suntrail.fragments.InventoryFragment;
import ondrej.mejzlik.suntrail.fragments.ItemInfoFragment;
import ondrej.mejzlik.suntrail.fragments.ShipInfoFragment;
import ondrej.mejzlik.suntrail.fragments.ShopFragment;
import ondrej.mejzlik.suntrail.fragments.StartGameFragment;
import ondrej.mejzlik.suntrail.game.GameDatabaseHelper;
import ondrej.mejzlik.suntrail.game.GameUtilities;
import ondrej.mejzlik.suntrail.game.ItemModel;
import ondrej.mejzlik.suntrail.game.PlayerModel;

import static ondrej.mejzlik.suntrail.activities.AllBoardsActivity.PLANET_RESOURCES_KEY;
import static ondrej.mejzlik.suntrail.fragments.ItemInfoFragment.ITEM_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_MERCURY;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_NEPTUNE;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_SUN;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_URANUS;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_ID_KEY;

public class GameActivity extends Activity {
    public static final String SPACESHIP_NAME_KEY = "spaceshipNameKey";
    private static final String DIRECTION_FRAGMENT_TAG = "directionFragment";
    // Used to back up lock variables
    private static final String DATABASE_CREATED_KEY = "isDatabaseCreatedKey";
    private static final String CURRENT_PLANET_UPDATED_KEY = "isPlanetUpdatedKey";
    private AsyncDatabaseInitializer databaseInitializer = null;
    // The resources contain all about the planet
    private Bundle planetResources = null;
    // This is used to prevent multiple toasts from showing.
    private Toast directionToast = null;
    private int scannedPlanet;
    // These variables are set to true once we have a functional, initialized and updated database.
    private boolean isDatabaseCreated = false;
    private boolean isCurrentDatabaseUpdated = false;

    // todo safety function price decrease

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Game utilities contain a method to check database existence.
        GameUtilities gameUtilities = new GameUtilities();

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.game_activity_fragment_container) != null) {

            // If we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                this.planetResources = savedInstanceState.getBundle(PLANET_RESOURCES_KEY);
                this.isDatabaseCreated = savedInstanceState.getBoolean(DATABASE_CREATED_KEY);
                this.isCurrentDatabaseUpdated = savedInstanceState.getBoolean(CURRENT_PLANET_UPDATED_KEY);
                return;
            }

            // If savedInstance == null initialize all variables that were not retrieved
            FragmentManager fragmentManager = getFragmentManager();
            // Get which planet was scanned
            // The planet id is inside the planet resources
            this.planetResources = getIntent().getExtras().getBundle(PLANET_RESOURCES_KEY);
            if (this.planetResources != null && this.planetResources.containsKey(PLANET_ID_KEY)) {
                this.scannedPlanet = this.planetResources.getInt(PLANET_ID_KEY);
            }

            // If there is no database, we start a new game and initialize the database to default
            // contents.
            if (!gameUtilities.isDatabaseCreated(this)) {
                // The database is created when we call getWritable or getReadable database the first
                // time, which happens here and may take longer time to finish. Therefore we run
                // it in a background thread. Once the initialization is done game buttons are
                // enabled by checking isDatabaseCreated. Database initialization only happens in this
                // activity. Other activities use the created database through the singleton helper.
                // Only the creation of the database is expensive once it is done here other calls
                // for writable or readable database are quick.
                this.databaseInitializer = new AsyncDatabaseInitializer(this.scannedPlanet, this);

                // Both the fragments are added onto the screen, but if the direction choice fragment
                // is used, it covers the game start fragment until the player picks a direction.
                StartGameFragment startGameFragment = new StartGameFragment();
                FragmentTransaction startGame = fragmentManager.beginTransaction();
                startGame.add(R.id.game_activity_fragment_container, startGameFragment);
                startGame.commit();

                // Check if we are going to have to manually pick direction.
                // If the user scans first or second planet, the direction is set to Sun -> Neptune
                // If the user scans one of the last two planets, direction is set to Neptune -> Sun
                // Playing the game on two planets is useless.
                // Otherwise we let the user select the direction.
                if (this.scannedPlanet == PLANET_ID_SUN || this.scannedPlanet == PLANET_ID_MERCURY) {
                    // We go from Sun to Neptune = true
                    // Initialize database with the now known direction
                    databaseInitializer.execute(true);
                } else if (this.scannedPlanet == PLANET_ID_NEPTUNE || this.scannedPlanet == PLANET_ID_URANUS) {
                    // We go from Neptune to Sun = false
                    databaseInitializer.execute(false);
                } else {
                    // Let the player choose
                    // No need to add to back stack, this is the first fragment and we do not want
                    // to return to it in the future.
                    DirectionChoiceFragment directionChoiceFragment = new DirectionChoiceFragment();
                    FragmentTransaction pickDirection = fragmentManager.beginTransaction();
                    pickDirection.replace(R.id.game_activity_fragment_container, directionChoiceFragment, DIRECTION_FRAGMENT_TAG);
                    pickDirection.addToBackStack(null);
                    pickDirection.commit();
                }
            } else {
                // Update current planet in database. No need to do this above as current planet is
                // set to scanned planet when a new database is initialized.
                AsyncUpdater planetUpdater = new AsyncUpdater(this.scannedPlanet, this);
                planetUpdater.execute();
                // We already have a database from before.
                this.isDatabaseCreated = true;
                this.openGameMenuFragment();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBundle(PLANET_RESOURCES_KEY, this.planetResources);
        savedInstanceState.putBoolean(DATABASE_CREATED_KEY, this.isDatabaseCreated);
        savedInstanceState.putBoolean(CURRENT_PLANET_UPDATED_KEY, this.isCurrentDatabaseUpdated);
        // Needed for saving state in fragments
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        // Do not allow the user to go back from direction choice fragment until a decision is made.
        DirectionChoiceFragment fragment = (DirectionChoiceFragment) getFragmentManager().findFragmentByTag(DIRECTION_FRAGMENT_TAG);
        if (fragment != null && fragment.isVisible()) {

            // Tell the user to pick direction, but only show one toast not overlapping toasts
            try {
                this.directionToast.getView().isShown();
                this.directionToast.setText(this.getResources().getString(R.string.toast_pick_direction));
            } catch (Exception e) {
                // No toast visible, make toast
                this.directionToast = Toast.makeText(this, this.getResources().getString(R.string.toast_pick_direction), Toast.LENGTH_LONG);
            }
            this.directionToast.show();

            return;
        }
        // In all other cases except the direction fragment, call back normally.
        super.onBackPressed();
    }

    /**
     * This method handles clicks to direction buttons from direction choice fragment.
     *
     * @param view The button that has been pressed.
     */
    public void directionChoiceButtonHandler(View view) {
        int buttonId = view.getId();
        boolean direction;
        switch (buttonId) {
            // Going from Neptune -> Sun
            case R.id.direction_choice_button_sun: {
                direction = false;
                break;
            }
            // Going from Sun -> Neptune. Only other option is direction_choice_button_neptune.
            // Back button is disabled until a direction is chosen.
            default: {
                direction = true;
                break;
            }
        }
        // Initialize database with the now known direction. Initialization did not happen in
        // onCreate. The initializer might be null if the activity was killed and the
        // instance from onCreate did not survive.
        if (this.databaseInitializer == null) {
            this.databaseInitializer = new AsyncDatabaseInitializer(this.scannedPlanet, this);
        }
        this.databaseInitializer.execute(direction);
        // Remove the direction choice fragment now
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStackImmediate();
    }

    /**
     * This method handles clicks to confirm button from start new game fragment.
     * Opens the game menu fragment.
     *
     * @param view The button that has been pressed.
     */
    public void confirmGameStartButtonHandler(View view) {
        this.openGameMenuFragment();
    }

    /**
     * This method replaces the current fragment with the GameMenuFragment and passes planet
     * resources as an argument which contain the planet id, name and quarter photo.
     */
    private void openGameMenuFragment() {
        // Do not add the fragment onto back stack as this is considered the first fragment
        // added to the activity. Even if the start new game and direction choice fragments
        // were shown, we will never return to them and they are not on the stack. So this is still
        // the first fragment.
        FragmentManager fragmentManager = getFragmentManager();
        GameMenuFragment gameMenuFragment = new GameMenuFragment();

        gameMenuFragment.setArguments(this.planetResources);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.game_activity_fragment_container, gameMenuFragment);
        transaction.commit();
    }

    /**
     * This method opens an ItemInfoFragment and passes the item data into it.
     * It is used from the inventory or shop.
     *
     * @param item The game item for which we want to see the info.
     */
    public void openItemInfoFragment(ItemModel item) {
        FragmentManager fragmentManager = getFragmentManager();
        ItemInfoFragment itemInfoFragment = new ItemInfoFragment();

        Bundle arguments = new Bundle();
        arguments.putParcelable(ITEM_KEY, item);
        itemInfoFragment.setArguments(arguments);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.game_activity_fragment_container, itemInfoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Handles clicks from ship info button in start new game fragment and inventory.
     * Launches a new fragment with information about the ship.
     * Which ship info is displayed is based on the ship name string resource id.
     *
     * @param view The button that has been clicked
     */
    public void showShipInfoButtonHandler(View view) {
        // The button contains a tag which holds the name of the ship which is then passed to the
        // ship info fragment. The ship info fragment identifies which spaceship info to display by
        // the spaceship name string resource id.
        ImageButton shipButton = (ImageButton) view;

        Bundle spaceshipArguments = new Bundle();
        spaceshipArguments.putInt(SPACESHIP_NAME_KEY, (int) shipButton.getTag());

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Pass space ship name string resource id to fragment
        ShipInfoFragment shipInfoFragment = new ShipInfoFragment();
        shipInfoFragment.setArguments(spaceshipArguments);
        transaction.replace(R.id.game_activity_fragment_container, shipInfoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Handles clicks from Inventory button in game menu fragment.
     *
     * @param view The button that has been clicked
     */
    public void inventoryButtonHandlerGameMenu(View view) {
        if (this.checkIsDatabaseCreated()) {
            FragmentManager fragmentManager = getFragmentManager();
            InventoryFragment inventoryFragment = new InventoryFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.game_activity_fragment_container, inventoryFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    /**
     * Handles clicks from Shop button in game menu fragment.
     *
     * @param view The button that has been clicked
     */
    public void shopButtonHandler(View view) {
        if (this.checkIsDatabaseCreated()) {
            FragmentManager fragmentManager = getFragmentManager();
            ShopFragment shopFragment = new ShopFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.game_activity_fragment_container, shopFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    /**
     * Handles clicks from Leave planet button in game menu fragment.
     *
     * @param view The button that has been clicked
     */
    public void leavePlanetButtonHandler(View view) {
        if (this.checkIsDatabaseCreated()) {
            Intent intent = new Intent(this, MainMenuActivity.class);
            // Closes all activities up to Main Menu.
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    /**
     * This method checks if the database was successfully created and if current planet was updated
     * and returns true. If not, returns false and displays a toast message to wait.
     * Buttons from game menu call this method to ensure that the player will not open any fragment
     * that uses the database before the database is prepared. This will rarely happen as it is
     * usually fast.
     *
     * @return True if database was created and updated else false and displays a toast.
     */
    private boolean checkIsDatabaseCreated() {
        if (this.isDatabaseCreated && this.isCurrentDatabaseUpdated) {
            return true;
        }
        Toast.makeText(this, this.getResources().getString(R.string.toast_flight_route), Toast.LENGTH_LONG).show();
        return false;
    }

    /**
     * This small class has access to the activity's variables and can set the isDatabaseCreated
     * to true once the database initialization is completed in the background thread.
     */
    private class AsyncDatabaseInitializer extends AsyncTask<Boolean, Void, Void> {
        private final int startPlanet;
        private final Context context;

        AsyncDatabaseInitializer(int startPlanet, Context context) {
            super();
            this.startPlanet = startPlanet;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Boolean... params) {
            // The database helper is a singleton we always get the same instance it will not
            // cause any concurrent troubles.
            GameDatabaseHelper databaseHelper = GameDatabaseHelper.getInstance(this.context);
            databaseHelper.initializeDatabaseContents(params[0], this.startPlanet, this.context);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // This method is called in main thread automatically after finishing the work.
            // Once both isDatabaseCreated and isCurrentDatabaseUpdated are true, the player can
            // enter inventory and shop.
            isDatabaseCreated = true;
            isCurrentDatabaseUpdated = true;
        }
    }

    /**
     * This asynchronous class runs a new thread to update the current planet and prices and price
     * movement of bought items in the database. When the user opens the game activity the new
     * current planet is written into the database. It is the planet that was last scanned and is
     * passed into the activity in an intent.
     */
    private class AsyncUpdater extends AsyncTask<Void, Void, Void> {
        private final int currentPlanet;
        private final Context context;

        AsyncUpdater(int currentPlanet, Context context) {
            super();
            this.currentPlanet = currentPlanet;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            // The database helper is a singleton we always get the same instance it will not
            // cause any concurrent troubles.
            GameDatabaseHelper databaseHelper = GameDatabaseHelper.getInstance(this.context);
            // Only update the database if we open the game activity on a not yet visited planet.
            PlayerModel player = databaseHelper.getPlayerData();
            if (this.currentPlanet == player.getCurrentPlanet()) {
                return null;
            }
            // Else update the database
            databaseHelper.updateCurrentPlanet(this.currentPlanet);
            databaseHelper.updateItems();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // isDatabaseCreated is set to true in onCreate.
            isCurrentDatabaseUpdated = true;
        }
    }

}
