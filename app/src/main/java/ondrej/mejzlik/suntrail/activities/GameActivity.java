package ondrej.mejzlik.suntrail.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.fragments.DirectionChoiceFragment;
import ondrej.mejzlik.suntrail.fragments.ShipInfoFragment;
import ondrej.mejzlik.suntrail.fragments.StartGameFragment;
import ondrej.mejzlik.suntrail.game.GameDatabaseHelper;

import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_MERCURY;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_NEPTUNE;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_SUN;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_URANUS;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_ID_KEY;

public class GameActivity extends Activity {
    public static final String SPACESHIP_NAME_KEY = "spaceshipNameKey";
    private static final String DIRECTION_FRAGMENT_TAG = "directionFragment";
    private Bundle SpaceshipArguments = null;
    // This is used to prevent multiple toasts from showing.
    private Toast directionToast = null;
    // This is used in the game to determine which planet is next.
    // true =  from Sun to Neptune, false = from Neptune to Sun.
    private boolean tripDirection = true;
    private int scannedPlanet;
    private GameDatabaseHelper database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        FragmentManager fragmentManager = getFragmentManager();

        // Get which planet was scanned
        // Game activity is always started from scanner activity and always has a planet argument.
        this.scannedPlanet = getIntent().getExtras().getInt(PLANET_ID_KEY);

        // TODO do this in background thread
        // Create database
        this.database = new GameDatabaseHelper(tripDirection, scannedPlanet, getApplicationContext());


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

            // TODO only display this if there is no game database.
            // Both the fragments are added onto the screen,, but if the direction choice fragment
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
                // TODO only display this if no direction is set in database.
                DirectionChoiceFragment directionChoiceFragment = new DirectionChoiceFragment();
                FragmentTransaction pickDirection = fragmentManager.beginTransaction();
                pickDirection.replace(R.id.game_activity_fragment_container, directionChoiceFragment, DIRECTION_FRAGMENT_TAG);
                pickDirection.addToBackStack(null);
                pickDirection.commit();
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

}
