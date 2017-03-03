package ondrej.mejzlik.suntrail.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ondrej.mejzlik.suntrail.Fragments.GameInfoFragment;
import ondrej.mejzlik.suntrail.Fragments.SunPathInfoFragment;
import ondrej.mejzlik.suntrail.R;

/**
 * This activity displays general information about Sun Path and How to play information using
 * fragments.
 */
public class InfoScreenActivity extends Activity {
    // Create a new Fragments to be placed in the activity layout
    private GameInfoFragment gameInfoFragment = new GameInfoFragment();
    private SunPathInfoFragment infoFragment = new SunPathInfoFragment();

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

            // Add the fragment to the 'fragment_container' FrameLayout
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.info_screen_fragment_container, infoFragment);
            transaction.commit();
        }
    }

    /**
     * Handles clicks from image button displaying the map
     * @param view The button which has been clicked
     */
    public void mapButtonHandler(View view) {
        Intent intent = new Intent(this, ZoomableWebViewActivity.class);
        startActivity(intent);
    }

    /**
     * Handles clicks from how to play button on information screen.
     * Replaces info screen fragment with game info fragment.
     * @param view The button which has been clicked
     */
    public void howToPlayButtonHandler(View view) {
        // Add the fragment to the 'fragment_container' FrameLayout
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
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
    }
}
