package ondrej.mejzlik.suntrail.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
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
import ondrej.mejzlik.suntrail.game.GameDatabaseHelper;
import ondrej.mejzlik.suntrail.game.GameUtilities;
import ondrej.mejzlik.suntrail.game.PlayerModel;
import ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector;

import static ondrej.mejzlik.suntrail.activities.AllBoardsActivity.MAP_KEY;
import static ondrej.mejzlik.suntrail.activities.AllBoardsActivity.PLANET_RESOURCES_KEY;
import static ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment.ROTATION_END;
import static ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment.ROTATION_KEY_FROM;
import static ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment.ROTATION_KEY_TO;
import static ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment.ROTATION_START;
import static ondrej.mejzlik.suntrail.fragments.ZoomableImageFragment.IMAGE_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_ATHWALE;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_INVALID;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_ID_KEY;

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
    private Bundle mapArguments = null;
    private float savedRotationFrom = ROTATION_END;
    private float savedRotationTo = ROTATION_START;
    // Used to disable the game button if the user already visited this planet
    private boolean isShopLocked = true;
    private boolean isDatabaseBeingAccessed = true;
    private Toast shopLockedToast = null;
    private Toast databaseBeingAccessedToast = null;
    private GameUtilities gameUtilities = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        // Initialize variables
        this.gameUtilities = new GameUtilities();
        this.mapArguments = new Bundle();
        this.resourceCollector = new PlanetResourceCollector();
        this.planetResources = new Bundle();
        if (findViewById(R.id.scanner_fragment_container) != null) {
            // If we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                // Restore planet resources
                this.planetResources = savedInstanceState.getBundle(PLANET_RESOURCES_KEY);
                this.mapArguments = savedInstanceState.getBundle(MAP_KEY);
                this.savedRotationFrom = savedInstanceState.getFloat(ROTATION_KEY_FROM);
                this.savedRotationTo = savedInstanceState.getFloat(ROTATION_KEY_TO);
                return;
            }

            // Put the map into mapArguments. We will not change the image any more, we can do it once.
            this.mapArguments.putInt(IMAGE_KEY, R.drawable.pict_map_planets);
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
        // Save the bundle with sun trail map
        savedInstanceState.putBundle(MAP_KEY, this.mapArguments);
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
        this.mapArguments = savedInstanceState.getBundle(MAP_KEY);
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
                this.processScannerResult(result.getInt(PLANET_ID_KEY));
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
     */
    public void processScannerResult(int planetId) {
        // Check if this planet has already been visited, if yes disable game mode. If there is no
        // database, we have not started the game yet, enable game mode.
        if (this.gameUtilities.isDatabaseCreated(this)) {
            AsyncDatabaseAccess checkPlanet = new AsyncDatabaseAccess(planetId, this);
            checkPlanet.execute();
        } else {
            this.isShopLocked = false;
            this.isDatabaseBeingAccessed = false;
        }

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
            // Code valid we have a planet. Get all planet resources
            // If planet ID is 0, it is the first board with general info
            if (planetId == 0) {
                // Open general info fragment
                SunPathInfoFragment infoFragment = new SunPathInfoFragment();
                transaction.replace(R.id.scanner_fragment_container, infoFragment);
            } else {
                // It is a planet, open planet menu
                this.planetResources = this.resourceCollector.getPlanetResources(planetId, this);
                // Make the menu fragment show the game mode button
                this.planetResources.putString(SHOW_GAME_BUTTON_KEY, SHOW_GAME_BUTTON);
                // Send rotation data to continue where we finished
                this.planetResources.putFloat(ROTATION_KEY_FROM, this.savedRotationFrom);
                this.planetResources.putFloat(ROTATION_KEY_TO, this.savedRotationTo);

                // Open planet menu fragment.
                PlanetMenuFragment planetMenuFragment = new PlanetMenuFragment();
                planetMenuFragment.setArguments(this.planetResources);
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
        // Set the argument to contain boards map
        imageFragment.setArguments(this.mapArguments);

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
        // The query to check whether we should enable game mode has not yet finished.
        if (this.isDatabaseBeingAccessed) {
            // Show a message camouflaging that we are querying the database.
            try {
                this.databaseBeingAccessedToast.getView().isShown();
                this.databaseBeingAccessedToast.setText(this.getResources().getString(R.string.toast_querying_database));
            } catch (Exception e) {
                // No toast visible, make toast
                this.databaseBeingAccessedToast = Toast.makeText(this, this.getResources().getString(R.string.toast_querying_database), Toast.LENGTH_LONG);
            }
            this.databaseBeingAccessedToast.show();
        } else if (this.isShopLocked) {
            // This planet has been visited already
            try {
                this.shopLockedToast.getView().isShown();
                this.shopLockedToast.setText(this.getResources().getString(R.string.toast_planet_already_visited));
            } catch (Exception e) {
                // No toast visible, make toast
                this.shopLockedToast = Toast.makeText(this, this.getResources().getString(R.string.toast_planet_already_visited), Toast.LENGTH_LONG);
            }
            this.shopLockedToast.show();
        } else {
            Intent intent = new Intent(this, GameActivity.class);
            // Pass scanned planet id to the game activity
            Bundle parameters = new Bundle();
            parameters.putBundle(PLANET_RESOURCES_KEY, this.planetResources);
            intent.putExtras(parameters);
            startActivity(intent);
        }
    }

    /**
     * This small class has access to the activity's variables and can set the isShopLocked and
     * isDatabaseBeingAccessed to true or false. Here we check if the player has already visited
     * the planet we currently scanned. If yes the game button will display a toast and
     * the game mode will be inaccessible.
     */
    private class AsyncDatabaseAccess extends AsyncTask<Void, Void, Boolean> {
        private int scannedPlanet;
        private Context context;

        AsyncDatabaseAccess(int scannedPlanet, Context context) {
            super();
            this.scannedPlanet = scannedPlanet;
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // The database helper is a singleton we always get the same instance it will not
            // cause any concurrent troubles.
            GameDatabaseHelper databaseHelper = GameDatabaseHelper.getInstance(this.context);
            PlayerModel playerData = databaseHelper.getPlayerData();
            int lastPlanet = playerData.getCurrentPlanet();
            boolean tripDirection = playerData.getDirection();
            // We returned to a shop we have already been to. Last planet is updated
            // when the user enters the Game Activity.
            if (tripDirection) {
                // true = Sun -> Neptune
                // Planet numbers increase as the user advances
                return this.scannedPlanet <= lastPlanet;
            } else {
                // false = Neptune -> Sun
                // Planet numbers decrease as the user advances
                return this.scannedPlanet >= lastPlanet;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // This method is called in main thread automatically after finishing the work.
            isShopLocked = result;
            isDatabaseBeingAccessed = false;
        }
    }
}
