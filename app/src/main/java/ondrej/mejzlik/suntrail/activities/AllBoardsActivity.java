package ondrej.mejzlik.suntrail.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.fragments.BoardsListFragment;
import ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment;

import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_CERES;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_EARTH;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_HALLEY;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_JUPITER;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_KEY;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_MARS;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_MERCURY;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_MOON;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_NEPTUNE;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_SATURN;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_SUN;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_URANUS;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_VENUS;

/**
 * This activity displays a list of all available planets on the Sun path.
 * Users can select one and then be presented with a choice whether to display the text
 * or play an audio file.
 */
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
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        int planetId = this.getPlanetIdFromButton(planetButton);
        arguments.putInt(PLANET_ID_KEY, planetId);

        PlanetMenuFragment planetMenuFragment = new PlanetMenuFragment();
        planetMenuFragment.setArguments(arguments);
        transaction.replace(R.id.all_boards_fragment_container, planetMenuFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Converts button id from all boards menu to a planet identifier int from Configuration class.
     *
     * @param button Button which we convert to planet id
     */
    private int getPlanetIdFromButton(View button) {
        switch (button.getId()) {
            case R.id.all_boards_button_ceres: {
                return PLANET_ID_CERES;
            }
            case R.id.all_boards_button_earth: {
                return PLANET_ID_EARTH;
            }
            case R.id.all_boards_button_halley: {
                return PLANET_ID_HALLEY;
            }
            case R.id.all_boards_button_jupiter: {
                return PLANET_ID_JUPITER;
            }
            case R.id.all_boards_button_mars: {
                return PLANET_ID_MARS;
            }
            case R.id.all_boards_button_mercury: {
                return PLANET_ID_MERCURY;
            }
            case R.id.all_boards_button_moon: {
                return PLANET_ID_MOON;
            }
            case R.id.all_boards_button_neptune: {
                return PLANET_ID_NEPTUNE;
            }
            case R.id.all_boards_button_saturn: {
                return PLANET_ID_SATURN;
            }
            case R.id.all_boards_button_venus: {
                return PLANET_ID_VENUS;
            }
            case R.id.all_boards_button_uranus: {
                return PLANET_ID_URANUS;
            }
            default: {
                return PLANET_ID_SUN;
            }
        }
    }
}
