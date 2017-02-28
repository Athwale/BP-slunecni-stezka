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
import ondrej.mejzlik.suntrail.ZoomableWebViewActivity;

public class InfoScreenActivity extends Activity {
    // Create a new Fragments to be placed in the activity layout
    private GameInfoFragment mGameInfoFragment = new GameInfoFragment();
    private SunPathInfoFragment mInfoFragment = new SunPathInfoFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_screen);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            mInfoFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fragment_container, mInfoFragment);
            transaction.commit();
        }
    }

    public void mapButtonHandler(View view) {
        Intent intent = new Intent(this, ZoomableWebViewActivity.class);
        startActivity(intent);
    }

    public void howToPlayButtonHandler(View view) {
        // Add the fragment to the 'fragment_container' FrameLayout
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, mGameInfoFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
