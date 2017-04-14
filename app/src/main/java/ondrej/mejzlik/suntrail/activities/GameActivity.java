package ondrej.mejzlik.suntrail.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.fragments.DirectionChoiceFragment;
import ondrej.mejzlik.suntrail.fragments.GameMenuFragment;
import ondrej.mejzlik.suntrail.fragments.ShipInfoFragment;
import ondrej.mejzlik.suntrail.fragments.StartGameFragment;
import ondrej.mejzlik.suntrail.game.GameDatabaseHelper;

import static ondrej.mejzlik.suntrail.activities.AllBoardsActivity.PLANET_RESOURCES_KEY;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.DATABASE_NAME;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_MERCURY;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_NEPTUNE;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_SUN;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_URANUS;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_ID_KEY;

public class GameActivity extends Activity {
    public static final String SPACESHIP_NAME_KEY = "spaceshipNameKey";
    private static final String DIRECTION_FRAGMENT_TAG = "directionFragment";
    private Bundle SpaceshipArguments = null;
    // The resources contain all about the planet
    private Bundle planetResources = null;
    // This is used to prevent multiple toasts from showing.
    private Toast directionToast = null;
    // This is used in the game to determine which planet is next.
    // true =  from Sun to Neptune, false = from Neptune to Sun.
    private boolean tripDirection = true;
    private int scannedPlanet;
    private GameDatabaseHelper database = null;
    // This variable is set to true once we have a functional, initialized database.
    private boolean databaseCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        FragmentManager fragmentManager = getFragmentManager();

        // Get which planet was scanned
        // The planet id is inside the planet resources
        this.planetResources = getIntent().getExtras().getBundle(PLANET_RESOURCES_KEY);
        if (this.planetResources != null && this.planetResources.containsKey(PLANET_ID_KEY)) {
            this.scannedPlanet = this.planetResources.getInt(PLANET_ID_KEY);
        }

        this.database = GameDatabaseHelper.getInstance(this);

        // Initialize SpaceshipArguments for spaceship info fragment. From here we can only
        // display Icarus S info, no database exists at this point since game has not
        // been started yet.
        this.SpaceshipArguments = new Bundle();
        // The ship info fragment identifies which spaceship info to display by the spaceship name
        // string id.
        SpaceshipArguments.putInt(SPACESHIP_NAME_KEY, R.string.ship_name_icarus);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.game_activity_fragment_container) != null) {

            // If we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // If there is no database, we start a new game and initialize the database to default
            // contents.
            File dbFile = this.getDatabasePath(DATABASE_NAME);
            if (!dbFile.exists()) {
                // The database is created when we call getWritable or getReadable database the first
                // time, which happens here and may take longer time to finish. Therefore we run
                // it in a background thread. Once the initialization is done game buttons are
                // enabled by checking databaseCreated. Database initialization only happens in this
                // activity. Other activities use the created database through the singleton helper.
                // Only the creation of the database is expensive once it is done here other calls
                // for writable or readable database are quick.
                // TODO all game buttons show wait toast until database is made
                AsyncDatabaseInitializer databaseInitializer = new AsyncDatabaseInitializer(this.tripDirection, this.scannedPlanet, this);
                databaseInitializer.execute();

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
                    // We go from Sun to Neptune
                    this.tripDirection = true;
                } else if (this.scannedPlanet == PLANET_ID_NEPTUNE || this.scannedPlanet == PLANET_ID_URANUS) {
                    // We go from Neptune to Sun
                    this.tripDirection = false;
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
                // We already have a database from before.
                this.databaseCreated = true;
                this.openGameMenuFragment();
            }
        }
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
        switch (buttonId) {
            case R.id.direction_choice_button_to_sun: {
                this.tripDirection = true;
                break;
            }
            case R.id.direction_choice_button_to_neptune: {
                this.tripDirection = false;
                break;
            }
        }
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
     * This method replaces the current fragment with the GameMenuFragment and passes all
     * // TODO what?
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
     * Handles clicks from Icarus info button in start new game fragment.
     * Launches a new fragment with information about Icarus S.
     * Which ship info is displayed is based on the ship name string resource id.
     *
     * @param view The button that has been clicked
     */
    public void showIcarusInfoButtonHandler(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Pass chosen planet resources to fragment
        ShipInfoFragment shipInfoFragment = new ShipInfoFragment();
        shipInfoFragment.setArguments(this.SpaceshipArguments);
        transaction.replace(R.id.game_activity_fragment_container, shipInfoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Handles clicks from Inventory button in game menu fragment.
     *
     * @param view The button that has been clicked
     */
    public void InventoryButtonHandler(View view) {
        if (this.checkDatabaseCreated()) {

        }
    }

    /**
     * Handles clicks from Shop button in game menu fragment.
     *
     * @param view The button that has been clicked
     */
    public void ShopButtonHandler(View view) {
        if (this.checkDatabaseCreated()) {

        }
    }

    /**
     * Handles clicks from Leave planet button in game menu fragment.
     *
     * @param view The button that has been clicked
     */
    public void LeavePlanetButtonHandler(View view) {
        if (this.checkDatabaseCreated()) {

        }
    }

    /**
     * This method checks if the database was successfully created and returns true.
     * If not, returns false and displays a toast message to wait. Buttons from game menu
     * call this method to ensure that the player will not open any fragment that uses the database
     * before the database is created. This will rarely happen.
     *
     * @return True if databse was created else false and displays a toast.
     */
    private boolean checkDatabaseCreated() {
        if (!this.databaseCreated) {
            Toast.makeText(this, this.getResources().getString(R.string.toast_flight_route), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * This small class has access to the activity's variables and can set the databaseCreated
     * to true once the database initialization is completed in the background thread.
     */
    private class AsyncDatabaseInitializer extends AsyncTask<Void, Void, Void> {
        private boolean direction;
        private int startPlanet;
        private Context context;

        AsyncDatabaseInitializer(boolean direction, int startPlanet, Context context) {
            super();
            this.direction = direction;
            this.startPlanet = startPlanet;
            this.context = context;
        }

        protected Void doInBackground(Void... params) {
            // The database helper is a singleton we always get the same instance it will not
            // cause any concurent troubles.
            GameDatabaseHelper databaseHelper = GameDatabaseHelper.getInstance(context);
            databaseHelper.initializeDatabaseContents(direction, startPlanet, context);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // This method is called in main thread automatically after finishing the work.
            databaseCreated = true;
        }
    }

}
