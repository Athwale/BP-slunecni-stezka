package ondrej.mejzlik.suntrail.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.fragments.ScannerChoiceFragment;

/**
 * This activity allows the user to pick which scanner to use. Then starts corresponding feagment
 * with the selected scanner.
 */
public class ScannerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        if (findViewById(R.id.scanner_fragment_container) != null) {
            // If we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                // Restore bundle with image
                return;
            }

            // Add scanner choice as first fragment to the fragment_container
            ScannerChoiceFragment scannerChoiceFragment = new ScannerChoiceFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.scanner_fragment_container, scannerChoiceFragment);
            transaction.commit();
        }
    }
}
