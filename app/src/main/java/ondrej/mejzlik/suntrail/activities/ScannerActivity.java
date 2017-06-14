package ondrej.mejzlik.suntrail.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.fragments.AudioPlayerFragment;
import ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment;
import ondrej.mejzlik.suntrail.fragments.PlanetTextFragment;
import ondrej.mejzlik.suntrail.fragments.QrScannerFragment;
import ondrej.mejzlik.suntrail.fragments.ScannerChoiceFragment;
import ondrej.mejzlik.suntrail.fragments.SunPathInfoFragment;
import ondrej.mejzlik.suntrail.fragments.ZoomableImageFragment;
import ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector;

import static ondrej.mejzlik.suntrail.activities.AllBoardsActivity.PLANET_RESOURCES_KEY;
import static ondrej.mejzlik.suntrail.activities.GameActivity.END_GAME_PREFERENCE_KEY;
import static ondrej.mejzlik.suntrail.activities.GameActivity.PREFERENCES_KEY;
import static ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment.ROTATION_END;
import static ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment.ROTATION_KEY_FROM;
import static ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment.ROTATION_KEY_TO;
import static ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment.ROTATION_START;
import static ondrej.mejzlik.suntrail.fragments.ZoomableImageFragment.IMAGE_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_ATHWALE;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_INVALID;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_ID_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_MAP_POSITION_KEY;

/**
 * This activity allows the user to pick which scanner to use. Then starts corresponding fragment
 * with the selected scanner.
 */
public class ScannerActivity extends Activity {
    // Used in scanner choice fragment and qr fragment to pass argument whether to use flash
    public static final String USE_FLASH_KEY = "useFlash";
    // Used in scanner activity and qr fragment to identify camera permission request
    public static final int PERMISSION_CAMERA = 1;
    // Used in scanner activity and planet menu fragment to indicate that we scanned a planet
    // and want the game mode button to appear.
    public static final String SHOW_GAME_BUTTON_KEY = "gameModeKey";
    public static final String SHOW_GAME_BUTTON = "gameMode";
    // Used to indicate which scanner or scanner options should the app use or offer in
    // scanner choice fragment and scanner activity
    private static final int HAS_NOTHING = 0;
    private static final int HAS_NFC_QR = 1;
    private static final int HAS_ONLY_QR = 2;
    private static final int HAS_ONLY_NFC = 3;
    private static final int NFC_REQUEST = 1;
    private PlanetResourceCollector resourceCollector;
    private Bundle planetResources = null;
    private float savedRotationFrom = ROTATION_END;
    private float savedRotationTo = ROTATION_START;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        // Initialize variables
        this.resourceCollector = new PlanetResourceCollector();
        this.planetResources = new Bundle();
        if (findViewById(R.id.scanner_fragment_container) != null) {
            // If we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                // Restore planet resources
                this.planetResources = savedInstanceState.getBundle(PLANET_RESOURCES_KEY);
                this.savedRotationFrom = savedInstanceState.getFloat(ROTATION_KEY_FROM);
                this.savedRotationTo = savedInstanceState.getFloat(ROTATION_KEY_TO);
                return;
            }

            // If the device only has a camera and no NFC reader, run qr scanner right away.
            // If the device only has NFC run NFC scanner.
            // If the device has both, open a selection screen.
            // If nothing show that scanner functions are disabled.
            // TODO remove this and uncomment selectScanner this is for testing purposes in emulator
            //this.processScannerResult(2);
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
     * Opens a scanner right away if the device has only a camera or only a nfc reader
     * Otherwise opens a selection screen.
     */
    private void selectScanner() {
        // Fragment we add here is the first one. No add to back stack is needed.
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
                Intent intent = new Intent(this, NfcScannerActivity.class);
                // Start the NFC scanner to get its result. This result is received in
                // onActivityResult().
                startActivityForResult(intent, NFC_REQUEST);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Process NFC scanner result
        // Check which request we are responding to
        if (requestCode == NFC_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle result = data.getExtras();
                this.processScannerResult(result.getInt(PLANET_ID_KEY), false);
            }
        }
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
     * @param planetId Scanned decoded planet ID
     * @param playSound True if this method should play scanning success sound. Used when we are
     *                  not using the NFC scanner.
     */
    public void processScannerResult(int planetId, boolean playSound) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Remove QR scanner fragment which is on top of the stack.
        // This repeats last add operation. If this is used on a device with only one scanner
        // It will add the scanner back onto screen because there is no scanner choice fragment.
        // On such devices if the QR code is incorrect, we have to finish scanner activity.
        fragmentManager.popBackStackImmediate();

        if (playSound) {
            // Play sound only when we are using the QR scanner, NFC plays it's own sound.
            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(this.getApplicationContext(), notification);
                r.play();
            } catch (Exception e) {
                Toast.makeText(this, getResources().getString(R.string.toast_scanning_success), Toast.LENGTH_SHORT).show();
                // If the device can not provide built in ringtone, there is not much else we can do.
            }
        }

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
            // Code valid we have a planet. Get all planet resources
            // If planet ID is 0, it is the first board with general info
            if (planetId == 0) {
                // Open general info fragment
                SunPathInfoFragment infoFragment = new SunPathInfoFragment();
                transaction.replace(R.id.scanner_fragment_container, infoFragment);
            } else {
                // It is a planet, open planet menu
                this.planetResources = this.resourceCollector.getPlanetResources(planetId, this);
                // Make the menu fragment show the game mode button if game is still in progress.
                // If game has been ended, it is inside the shared preferences, disable game button.
                SharedPreferences preferences = getSharedPreferences(PREFERENCES_KEY, 0);
                boolean isGameFinished = preferences.getBoolean(END_GAME_PREFERENCE_KEY, false);
                if (!isGameFinished) {
                    this.planetResources.putString(SHOW_GAME_BUTTON_KEY, SHOW_GAME_BUTTON);
                }
                // Send rotation data to continue where we finished
                this.planetResources.putFloat(ROTATION_KEY_FROM, this.savedRotationFrom);
                this.planetResources.putFloat(ROTATION_KEY_TO, this.savedRotationTo);

                // Open planet menu fragment.
                PlanetMenuFragment planetMenuFragment = new PlanetMenuFragment();
                planetMenuFragment.setArguments(this.planetResources);
                // Set tag to the fragment in order to recognize it in onBackPressed.
                transaction.replace(R.id.scanner_fragment_container, planetMenuFragment);
            }
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
        transaction.replace(R.id.scanner_fragment_container, imageFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
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
        transaction.replace(R.id.scanner_fragment_container, planetTextFragment);
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
        playerFragment.setArguments(this.planetResources);
        transaction.replace(R.id.scanner_fragment_container, playerFragment);
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

    /**
     * Handles clicks from nfc scanner button in scanner choice fragment.
     * Launches a new fragment with nfc scanner.
     *
     * @param view The button that has been clicked
     */
    public void nfcScannerButtonHandler(View view) {
        Intent intent = new Intent(this, NfcScannerActivity.class);
        // Start the NFC scanner to get its result
        startActivityForResult(intent, NFC_REQUEST);
    }

    /**
     * Handles clicks from game button in planet menu fragment.
     * Launches a new activity with game menu. If the variable isShopLocked is true
     * shows a message that the user already visited this planet.
     *
     * @param view The button that has been clicked
     */
    public void playGameButtonHandler(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        // Pass scanned planet id to the game activity
        Bundle parameters = new Bundle();
        parameters.putBundle(PLANET_RESOURCES_KEY, this.planetResources);
        intent.putExtras(parameters);
        startActivity(intent);
    }

}
