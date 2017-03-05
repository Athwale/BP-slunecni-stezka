package ondrej.mejzlik.suntrail.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ondrej.mejzlik.suntrail.Fragments.GameInfoFragment;
import ondrej.mejzlik.suntrail.Fragments.SunPathInfoFragment;
import ondrej.mejzlik.suntrail.Fragments.ZoomableImageFragment;
import ondrej.mejzlik.suntrail.R;

/**
 * This activity displays general information about Sun Path and How to play information using
 * fragments.
 */
public class InfoScreenActivity extends Activity {
    // Create a new Fragments to be placed in the activity layout
    private GameInfoFragment gameInfoFragment = new GameInfoFragment();
    private SunPathInfoFragment infoFragment = new SunPathInfoFragment();
    private ZoomableImageFragment imageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_screen);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.info_screen_fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Prepare zoomable image fragment with map
            Bundle arguments = new Bundle();
            int imageId = R.drawable.overall_map_full_size;
            arguments.putInt("image", imageId);
            // set fragment arguments
            imageFragment = new ZoomableImageFragment();
            imageFragment.setArguments(arguments);

            // Add the fragment to the fragment_container in FrameLayout
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.info_screen_fragment_container, infoFragment);
            transaction.commit();
        }
    }

    /**
     * Handles clicks from display map button.
     * Opens a new fragment with the map.
     * @param view The button which has been clicked
     */
    public void mapButtonHandler(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.info_screen_fragment_container, imageFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Handles clicks from how to play button on information screen.
     * Replaces info screen fragment with game info fragment.
     * @param view The button which has been clicked
     */
    public void howToPlayButtonHandler(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.info_screen_fragment_container, gameInfoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Handles clicks from scanner button in game info screen.
     * Launches a new activity with scanner screen.
     * @param view The button that has been clicked
     */
    public void scannerButtonHandlerInfoScreen(View view) {
        Intent intent = new Intent(this, ScannerActivity.class);
        startActivity(intent);
        // Finish this activity, removes activity from back stack but allows resuming it
        // until user clicks scanner button. User will return to main menu from scanner.
        // This is ok to do.
        this.finish();
    }
}
