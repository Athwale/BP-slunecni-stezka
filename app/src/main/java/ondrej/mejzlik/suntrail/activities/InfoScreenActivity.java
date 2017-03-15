package ondrej.mejzlik.suntrail.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.fragments.GameInfoFragment;
import ondrej.mejzlik.suntrail.fragments.SunPathInfoFragment;
import ondrej.mejzlik.suntrail.fragments.ZoomableImageFragment;

import static ondrej.mejzlik.suntrail.config.Configuration.IMAGE_ARGUMENT;
import static ondrej.mejzlik.suntrail.config.Configuration.IMAGE_KEY;
import static ondrej.mejzlik.suntrail.config.Configuration.INFO_BUTTON_GAME;
import static ondrej.mejzlik.suntrail.config.Configuration.INFO_BUTTON_GENERAL;
import static ondrej.mejzlik.suntrail.config.Configuration.INFO_BUTTON_INTENT_KEY;

/**
 * This activity displays general information about Sun Path and How to play information using
 * fragments.
 */
public class InfoScreenActivity extends Activity {
    private Bundle arguments = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_screen);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.info_screen_fragment_container) != null) {

            // If we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                // Restore bundle with image
                this.arguments = savedInstanceState.getBundle(IMAGE_ARGUMENT);
                return;
            }

            this.arguments = new Bundle();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            // Load fragment according to which button we used
            Intent intent = this.getIntent();
            int type = intent.getIntExtra(INFO_BUTTON_INTENT_KEY, INFO_BUTTON_GENERAL);
            if (type == INFO_BUTTON_GENERAL) {
                SunPathInfoFragment infoFragment = new SunPathInfoFragment();
                // Add the fragment to the fragment_container in FrameLayout
                transaction.add(R.id.info_screen_fragment_container, infoFragment);
            } else if (type == INFO_BUTTON_GAME) {
                GameInfoFragment gameInfoFragment = new GameInfoFragment();
                transaction.add(R.id.info_screen_fragment_container, gameInfoFragment);
            }
            // Commit changes and display fragment
            transaction.commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the bundle with image
        savedInstanceState.putBundle(IMAGE_ARGUMENT, arguments);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        this.arguments = savedInstanceState.getBundle(IMAGE_ARGUMENT);
    }

    /**
     * Handles clicks from display map button and schema button.
     * Opens a new zoomable image fragment with the image.
     *
     * @param view The button which has been clicked
     */
    public void imageButtonsHandler(View view) {
        switch (view.getId()) {
            case R.id.sun_path_info_button_map: {
                // Prepare map for zoomable image argument
                this.arguments.putInt(IMAGE_KEY, R.drawable.pict_overall_map_full_size);
                break;
            }
            case R.id.sun_path_info_button_schema: {
                this.arguments.putInt(IMAGE_KEY, R.drawable.pict_schema_white);
                break;
            }
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Create fragment and set fragment arguments
        ZoomableImageFragment imageFragment = new ZoomableImageFragment();
        imageFragment.setArguments(arguments);

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.info_screen_fragment_container, imageFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Handles clicks from scanner button in game info screen.
     * Launches a new activity with scanner screen.
     *
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

    /**
     * Handles clicks from scanner button in game info screen.
     * Launches a new activity with scanner screen.
     *
     * @param view The button that has been clicked
     */
    public void allBoardsButtonHandlerInfoScreen(View view) {
        Intent intent = new Intent(this, AllBoardsActivity.class);
        startActivity(intent);
        this.finish();
    }
}
