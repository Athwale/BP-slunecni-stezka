package ondrej.mejzlik.suntrail.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ondrej.mejzlik.suntrail.fragments.BoardsListFragment;
import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment;

import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_KEY;

public class AllBoardsActivity extends Activity {
    private int chosenPlanet = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_boards);

        if (findViewById(R.id.all_boards_fragment_container) != null) {
            // If we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                // Restore bundle with image
                this.chosenPlanet = savedInstanceState.getInt(PLANET_ID_KEY);
                return;
            }

            // Add planet list fragment to the fragment_container
            BoardsListFragment boardsListFragment = new BoardsListFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.all_boards_fragment_container, boardsListFragment);
            transaction.commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the chosen planet id
        savedInstanceState.putInt(PLANET_ID_KEY, chosenPlanet);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        this.chosenPlanet = savedInstanceState.getInt(PLANET_ID_KEY);
    }

    /**
     * Handles clicks from buttons in all boards list.
     * Launches a new fragment corresponding to selected button.
     *
     * @param planetButton The button that has been clicked
     */
    public void planetButtonsHandler(View planetButton) {
        Bundle arguments = new Bundle();
        String planetName = ((Button) planetButton).getText().toString();
        arguments.putString(PLANET_ID_KEY, planetName);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        PlanetMenuFragment planetMenuFragment = new PlanetMenuFragment();
        planetMenuFragment.setArguments(arguments);
        transaction.replace(R.id.all_boards_fragment_container, planetMenuFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
