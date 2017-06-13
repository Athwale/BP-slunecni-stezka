package ondrej.mejzlik.suntrail.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.fragments.AudioPlayerFragment;
import ondrej.mejzlik.suntrail.fragments.BoardsListFragment;
import ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment;
import ondrej.mejzlik.suntrail.fragments.PlanetTextFragment;
import ondrej.mejzlik.suntrail.fragments.ZoomableImageFragment;
import ondrej.mejzlik.suntrail.utilities.PlanetIdentifier;
import ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector;

import static ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment.ROTATION_END;
import static ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment.ROTATION_KEY_FROM;
import static ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment.ROTATION_KEY_TO;
import static ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment.ROTATION_START;
import static ondrej.mejzlik.suntrail.fragments.ZoomableImageFragment.IMAGE_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_MAP_POSITION_KEY;

/**
 * This activity displays a list of all available planets on the Sun path.
 * Users can select one and then be presented with a choice whether to display the text
 * or play an audio file.
 */
public class AllBoardsActivity extends Activity {
    // Key to identify planet resources in a bundle
    public static final String PLANET_RESOURCES_KEY = "planetResourcesKey";
    private Bundle planetResources = null;
    private float savedRotationFrom = ROTATION_END;
    private float savedRotationTo = ROTATION_START;
    private PlanetIdentifier identifier = null;
    private PlanetResourceCollector resourceCollector = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_boards);

        // There variables are not saved in savedInstance and must be initialized here
        this.identifier = new PlanetIdentifier();
        this.resourceCollector = new PlanetResourceCollector();

        if (findViewById(R.id.all_boards_fragment_container) != null) {
            // If we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                // Restore bundle with planet id and image
                this.planetResources = savedInstanceState.getBundle(PLANET_RESOURCES_KEY);
                this.savedRotationFrom = savedInstanceState.getFloat(ROTATION_KEY_FROM);
                this.savedRotationTo = savedInstanceState.getFloat(ROTATION_KEY_TO);
                return;
            }

            // Initialize variables is savedInstance == null and they were not retrieved
            this.planetResources = new Bundle();

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
        // Save planet rotation
        savedInstanceState.putFloat(ROTATION_KEY_FROM, this.savedRotationFrom);
        savedInstanceState.putFloat(ROTATION_KEY_TO, this.savedRotationTo);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        this.planetResources = savedInstanceState.getBundle(PLANET_RESOURCES_KEY);
        this.savedRotationFrom = savedInstanceState.getFloat(ROTATION_KEY_FROM);
        this.savedRotationTo = savedInstanceState.getFloat(ROTATION_KEY_TO);
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
        int chosenPlanet = this.identifier.getPlanetIdFromButton((Button) planetButton);
        this.planetResources = this.resourceCollector.getPlanetResources(chosenPlanet, this);

        PlanetMenuFragment planetMenuFragment = new PlanetMenuFragment();
        // Send rotation data to continue where we finished
        this.planetResources.putFloat(ROTATION_KEY_FROM, this.savedRotationFrom);
        this.planetResources.putFloat(ROTATION_KEY_TO, this.savedRotationTo);
        planetMenuFragment.setArguments(this.planetResources);
        transaction.replace(R.id.all_boards_fragment_container, planetMenuFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * This method replaces the planet menu fragment with a planet text fragment.
     * This should only be called from planet menu fragment.
     */
    public void showTextFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

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
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Pass chosen planet resources to fragment
        AudioPlayerFragment playerFragment = new AudioPlayerFragment();
        // Send rotation data to continue where we finished
        this.planetResources.putFloat(ROTATION_KEY_FROM, this.savedRotationFrom);
        this.planetResources.putFloat(ROTATION_KEY_TO, this.savedRotationTo);
        playerFragment.setArguments(this.planetResources);
        transaction.replace(R.id.all_boards_fragment_container, playerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
        // Put the right map with marked position into the zoomable image fragment arguments
        Bundle mapArguments = new Bundle();
        mapArguments.putInt(IMAGE_KEY, this.planetResources.getInt(PLANET_MAP_POSITION_KEY));
        // Set the argument to contain boards map
        imageFragment.setArguments(mapArguments);

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.all_boards_fragment_container, imageFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Saves planet rotation from fragments into member variables which are saved later
     * into savedInstanceBundle.
     *
     * @param from Where the animation ended.
     * @param to   Where the animation ended.
     */
    public void saveRotationValue(float from, float to) {
        this.savedRotationFrom = from;
        this.savedRotationTo = to;
    }
}
