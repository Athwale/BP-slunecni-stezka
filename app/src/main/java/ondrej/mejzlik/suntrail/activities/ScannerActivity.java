package ondrej.mejzlik.suntrail.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.fragments.QrScannerFragment;
import ondrej.mejzlik.suntrail.fragments.ScannerChoiceFragment;

import static ondrej.mejzlik.suntrail.config.Configuration.HAS_NFC_QR;
import static ondrej.mejzlik.suntrail.config.Configuration.HAS_NOTHING;
import static ondrej.mejzlik.suntrail.config.Configuration.HAS_ONLY_NFC;
import static ondrej.mejzlik.suntrail.config.Configuration.HAS_ONLY_QR;
import static ondrej.mejzlik.suntrail.config.Configuration.PERMISSION_CAMERA;
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

            // If the device only has a camera and no NFC reader, run qr scanner right away.
            // If the device only has NFC run NFC scanner.
            // If the device has both, open a selection screen.
            // If nothing show that scanner functions are disabled.
            this.selectScanner();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CAMERA) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getResources().getString(R.string.toast_permission_qr), Toast.LENGTH_SHORT).show();
            } else {
                // permission denied, remove this fragment
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStackImmediate();
            }
        }
    }

    /**
     * Opens a scanner right away if the device has only a camera or only a nfc reader
     * Otherwise opens a selection screen.
     */
    private void selectScanner() {
        // Fragment we add here is the first one. No add to backstack is needed.
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        switch (this.checkFeatures()) {
            case HAS_NOTHING: {
                // Show warning and close scanner activity
                Toast.makeText(this, getResources().getString(R.string.toast_no_scanner), Toast.LENGTH_SHORT).show();
                this.finish();
                break;
            }
            case HAS_ONLY_NFC: {
                // TODO Run NFC scanner

                break;
            }
            case HAS_ONLY_QR: {
                // Open qr scanner fragment
                transaction.add(R.id.scanner_fragment_container, new QrScannerFragment());
                break;
            }
            case HAS_NFC_QR: {
                // Open scanner selection fragment
                transaction.add(R.id.scanner_fragment_container, new ScannerChoiceFragment());
                break;
            }
        }
        transaction.commit();
    }

    /**
     * Checks what scanning capabilities the device has.
     */
    private int checkFeatures() {
        PackageManager packageManager = this.getPackageManager();
        boolean camera = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        boolean nfc = packageManager.hasSystemFeature(PackageManager.FEATURE_NFC);

        if (nfc && camera) {
            return HAS_NFC_QR;
        } else if (nfc) {
            return HAS_ONLY_NFC;
        } else if (camera) {
            return HAS_ONLY_QR;
        } else {
            return HAS_NOTHING;
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
        transaction.replace(R.id.scanner_fragment_container, qrScannerFragment);
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
