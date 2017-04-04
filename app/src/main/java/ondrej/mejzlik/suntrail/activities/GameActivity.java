package ondrej.mejzlik.suntrail.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.fragments.ShipInfoFragment;
import ondrej.mejzlik.suntrail.fragments.StartGameFragment;

public class GameActivity extends Activity {
    public static final String SPACESHIP_NAME_KEY = "spaceshipNameKey";
    private Bundle arguments = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Initialize spaceship arguments for spaceship info fragment. From here we can only
        // display Icarus S info, no database exists at this point since game has not
        // been started yet.
        this.arguments = new Bundle();
        // The ship info fragment identifies which spaceship info to display by the spaceship name
        // string id.
        arguments.putInt(SPACESHIP_NAME_KEY, R.string.ship_name_lokys);
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

            StartGameFragment startGameFragment = new StartGameFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.game_activity_fragment_container, startGameFragment);
            // Commit changes and display fragment
            transaction.commit();
        }
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
        shipInfoFragment.setArguments(this.arguments);
        transaction.replace(R.id.game_activity_fragment_container, shipInfoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
