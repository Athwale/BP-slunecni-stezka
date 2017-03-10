package ondrej.mejzlik.suntrail.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.fragments.QrScannerFragment;
import ondrej.mejzlik.suntrail.fragments.ScannerChoiceFragment;

import static ondrej.mejzlik.suntrail.config.Configuration.PERMISSION_CAMERA;
import static ondrej.mejzlik.suntrail.config.Configuration.QR_SCANNER_TAG;
import static ondrej.mejzlik.suntrail.config.Configuration.USE_FLASH_KEY;

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CAMERA) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length == 0) {
                // permission denied, remove this fragment
                FragmentManager fragmentManager = getFragmentManager();
                // TODO remove scanner fragment
                //fragmentManager.getFragment()
            }
        }
    }

    /**
     * Handles clicks from qr scanner button in scanner choice fragment.
     * Launches a new fragment with qr scanner.
     *
     * @param view The button that has been clicked
     */
    public void qrScannerButtonHandler(View view) {
        Bundle arguments = new Bundle();
        QrScannerFragment qrScannerFragment = new QrScannerFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // Check if the qr code should use flash and put it into arguments
        boolean useFlash = ((CheckBox) findViewById(R.id.scanner_choice_checkBox_flash)).isChecked();

        arguments.putBoolean(USE_FLASH_KEY, useFlash);
        qrScannerFragment.setArguments(arguments);
        transaction.replace(R.id.scanner_fragment_container, qrScannerFragment, QR_SCANNER_TAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Handles clicks from nfc scanner button in scanner choice fragment.
     * Launches a new fragment with nfc scanner.
     *
     * @param view The button that has been clicked
     */
    public void nfcScannerButtonHandler(View view) {

    }
}
