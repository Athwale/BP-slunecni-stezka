package ondrej.mejzlik.suntrail.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.fragments.BoardsListFragment;
import ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment;
import ondrej.mejzlik.suntrail.fragments.PlanetTextFragment;
import ondrej.mejzlik.suntrail.fragments.ZoomableImageFragment;
import ondrej.mejzlik.suntrail.utilities.PlanetIdentifier;
import ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector;

import static ondrej.mejzlik.suntrail.fragments.ZoomableImageFragment.IMAGE_KEY;

/**
 * This activity displays a list of all available planets on the Sun path.
 * Users can select one and then be presented with a choice whether to display the text
 * or play an audio file.
 */
public class AllBoardsActivity extends Activity {
    // Key to identify planet resources in a bundle
    public static final String PLANET_RESOURCES_KEY = "planetResourcesKey";
    // Key to identify bundle with sun trail map in a saved instance bundle
    public static final String MAP_KEY = "mapKey";
    // mapArguments hold the sun trail map for zoomable image fragment
    private Bundle mapArguments = null;
    private Bundle planetResources = null;
    private PlanetIdentifier identifier;
    private PlanetResourceCollector resourceCollector = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_boards);

        // Initialize variables
        this.mapArguments = new Bundle();
        this.planetResources = new Bundle();
        this.identifier = new PlanetIdentifier();
        this.resourceCollector = new PlanetResourceCollector();

        if (findViewById(R.id.all_boards_fragment_container) != null) {
            // If we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                // Restore bundle with planet id and image
                this.planetResources = savedInstanceState.getBundle(PLANET_RESOURCES_KEY);
                this.mapArguments = savedInstanceState.getBundle(MAP_KEY);
                return;
            }

            // Put the map into mapArguments. We will not change the image any more, we can do it once.
            this.mapArguments.putInt(IMAGE_KEY, R.drawable.pict_map_planets);
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
        // Save the chosen planet resources for use in planet text and audio fragments
        savedInstanceState.putBundle(PLANET_RESOURCES_KEY, this.planetResources);
        // Save the bundle with sun trail map
        savedInstanceState.putBundle(MAP_KEY, this.mapArguments);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        this.planetResources = savedInstanceState.getBundle(PLANET_RESOURCES_KEY);
        this.mapArguments = savedInstanceState.getBundle(MAP_KEY);
    }

    /**
     * Handles clicks from buttons in all boards list.
     * Launches a new fragment corresponding to selected button.
     *
     * @param planetButton The button that has been clicked
     */
    public void planetButtonsHandler(View planetButton) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Identify planet and get it's ID and get all planet resources
        int chosenPlanet = this.identifier.getPlanetId((Button) planetButton);
        this.planetResources = this.resourceCollector.getPlanetResources(chosenPlanet, this);

        PlanetMenuFragment planetMenuFragment = new PlanetMenuFragment();
        planetMenuFragment.setArguments(this.planetResources);
        transaction.replace(R.id.all_boards_fragment_container, planetMenuFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Handles clicks from view text button in planet menu.
     * Launches a new fragment corresponding to selected button.
     *
     * @param planetButton The button that has been clicked
     */
    public void viewTextButtonHandler(View planetButton) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Pass chosen planet to fragment
        PlanetTextFragment planetTextFragment = new PlanetTextFragment();
        planetTextFragment.setArguments(this.planetResources);
        transaction.replace(R.id.all_boards_fragment_container, planetTextFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Handles clicks from play audio button in planet menu.
     * Launches a new fragment corresponding to selected button.
     *
     * @param planetButton The button that has been clicked
     */
    public void playAudioButtonHandler(View planetButton) {

        // Pass chosen planet to fragment

    }

    /**
     * Handles clicks from display map button.
     * Opens a new zoomable image fragment with map.
     *
     * @param view The button which has been clicked
     */
    public void mapButtonHandler(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Create fragment and set fragment mapArguments
        ZoomableImageFragment imageFragment = new ZoomableImageFragment();
        // Set the argument to contain boards map
        imageFragment.setArguments(this.mapArguments);

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.all_boards_fragment_container, imageFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
