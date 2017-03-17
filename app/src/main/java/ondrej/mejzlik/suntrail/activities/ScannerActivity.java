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
import ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment;
import ondrej.mejzlik.suntrail.fragments.PlanetTextFragment;
import ondrej.mejzlik.suntrail.fragments.QrScannerFragment;
import ondrej.mejzlik.suntrail.fragments.ScannerChoiceFragment;
import ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector;

import static ondrej.mejzlik.suntrail.activities.AllBoardsActivity.PLANET_RESOURCES_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_ATHWALE;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_INVALID;

/**
 * This activity allows the user to pick which scanner to use. Then starts corresponding feagment
 * with the selected scanner.
 */
public class ScannerActivity extends Activity {
    // Used in scanner choice fragment and qr fragment to pass argument whether to use flash
    public static final String USE_FLASH_KEY = "useFlash";
    // Used to indicate which scanner or scanner options should the app use or offer in
    // scanner choice fragment and scanner activity
    public static final int HAS_NOTHING = 0;
    public static final int HAS_NFC_QR = 1;
    public static final int HAS_ONLY_QR = 2;
    public static final int HAS_ONLY_NFC = 3;
    // Used in scanner activity and qr fragment to identify camera permission request
    public static final int PERMISSION_CAMERA = 1;
    // Used in scanner activity and planet menu fragment to indicate that we scanned a planet
    // and want the game mode button to appear.
    public static final String SHOW_GAME_BUTTON_KEY = "gameModeKey";
    public static final String SHOW_GAME_BUTTON = "gameMode";

    private PlanetResourceCollector resourceCollector;
    private Bundle planetResources = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        this.resourceCollector = new PlanetResourceCollector();
        this.planetResources = new Bundle();
        if (findViewById(R.id.scanner_fragment_container) != null) {
            // If we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                // Restore planet resources
                this.planetResources = savedInstanceState.getBundle(PLANET_RESOURCES_KEY);
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the chosen planet resources for use in planet text and audio fragments
        savedInstanceState.putBundle(PLANET_RESOURCES_KEY, this.planetResources);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        this.planetResources = savedInstanceState.getBundle(PLANET_RESOURCES_KEY);
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
                // TODO Run NFC scanner activity

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
     * Takes planet ID from qr scanner and runs the corresponding planet menu fragment.
     * QR scanner fragment is destroyed.
     *
     * @param planetId Result from QR scanner
     */
    public void processQrScannerResult(int planetId) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Remove QR scanner fragment which is on top of the stack.
        // This repeats last add operation. If this is used on a device with only one scanner
        // It will add the scanner back onto screen because there is no scanner choice fragment.
        // On such devices if the QR code is incorrect, we have to finish scanner activity.
        fragmentManager.popBackStackImmediate();

        // Scanned code is not a Sun trail QR planet identifier
        if (planetId == PLANET_ID_ATHWALE || planetId == PLANET_ID_INVALID) {
            if (planetId == PLANET_ID_ATHWALE) {
                Toast.makeText(this, getResources().getString(R.string.toast_athwale), Toast.LENGTH_SHORT).show();
            } else {
                // If it is not Athwale it is definitely invalid
                Toast.makeText(this, getResources().getString(R.string.toast_scanning_fail), Toast.LENGTH_SHORT).show();
            }
            // Close scanner activity and return to menu if we have a device with only one scanner
            int features = this.checkFeatures();
            if (features == HAS_ONLY_NFC || features == HAS_ONLY_QR) {
                this.finish();
            }
        } else {
            // Code valid we have a planet
            Toast.makeText(this, getResources().getString(R.string.toast_scanning_success), Toast.LENGTH_SHORT).show();
            // Get all planet resources
            this.planetResources = this.resourceCollector.getPlanetResources(planetId, this);
            // Make the menu fragment show the game mode button
            this.planetResources.putString(SHOW_GAME_BUTTON_KEY, SHOW_GAME_BUTTON);

            // Open planet menu fragment.
            PlanetMenuFragment planetMenuFragment = new PlanetMenuFragment();
            planetMenuFragment.setArguments(this.planetResources);
            transaction.replace(R.id.scanner_fragment_container, planetMenuFragment);
            transaction.addToBackStack(null);
            transaction.commit();
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
     * Handles clicks from view text button in planet menu.
     * Launches a new fragment corresponding to selected button.
     *
     * @param planetButton The button that has been clicked
     */
    public void viewTextButtonHandler(View planetButton) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Pass identified planet to fragment
        PlanetTextFragment planetTextFragment = new PlanetTextFragment();
        planetTextFragment.setArguments(this.planetResources);
        transaction.replace(R.id.scanner_fragment_container, planetTextFragment);
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
        // TODO Pro nfc scanner startovat novou aktivitu abychom ji mohli enable a disable a tím
        // TODO zmemožňovat čtení nfc z jiného místa než scanneru
    }
}
