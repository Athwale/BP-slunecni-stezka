package ondrej.mejzlik.suntrail.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ondrej.mejzlik.suntrail.R;

/**
 * This is the main activity which is launched after after launching the app.
 * Displays main menu.
 */
public class MainMenuActivity extends Activity {
    // Used in fragments to save scroll position
    public static final String SCROLL_POSITION_KEY = "scrollPosition";
    // Used in main menu in infoButtonsHandler to start either general info or game info fragment
    // in InfoScreen activity
    public static final String INFO_BUTTON_INTENT_KEY = "StartFragment";
    public static final int INFO_BUTTON_GENERAL = 1;
    public static final int INFO_BUTTON_GAME = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Remove scan button if the device does not have both a camera and a nfc reader
        this.disableScanButton();
    }

    /**
     * Disables game related buttons in main menu if the device does not have both a camera
     * and a NFC reader.
     */
    private void disableScanButton() {
        PackageManager packageManager = this.getPackageManager();
        boolean camera = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        boolean nfc = packageManager.hasSystemFeature(PackageManager.FEATURE_NFC);
        Button scanButton = (Button) findViewById(R.id.main_menu_button_scan);
        Button inventoryButton = (Button) findViewById(R.id.main_menu_button_inventory);
        Button settingsButton = (Button) findViewById(R.id.main_menu_button_settings);
        Button howToPlayButton = (Button) findViewById(R.id.main_menu_button_how_to_play);
        TextView warningNoScanner = (TextView) findViewById(R.id.main_menu_text_view_no_scanner);
        if (!camera && !nfc) {
            scanButton.setVisibility(View.GONE);
            inventoryButton.setVisibility(View.GONE);
            settingsButton.setVisibility(View.GONE);
            howToPlayButton.setVisibility(View.GONE);
            warningNoScanner.setVisibility(View.VISIBLE);
        } else {
            warningNoScanner.setVisibility(View.GONE);
            scanButton.setVisibility(View.VISIBLE);
            inventoryButton.setVisibility(View.VISIBLE);
            settingsButton.setVisibility(View.VISIBLE);
            howToPlayButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Handles clicks from settings button in main menu.
     * Launches a new activity with settings screen.
     *
     * @param view The button that has been clicked
     */
    public void settingsButtonHandler(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Handles clicks from scanner button in main menu.
     * Launches a new activity with scanner screen.
     *
     * @param view The button that has been clicked
     */
    public void scannerButtonHandler(View view) {
        Intent intent = new Intent(this, ScannerActivity.class);
        startActivity(intent);
    }

    /**
     * Handles clicks from all boards button in main menu.
     * Launches a new activity with scanner screen.
     *
     * @param view The button that has been clicked
     */
    public void allBoardsButtonHandler(View view) {
        Intent intent = new Intent(this, AllBoardsActivity.class);
        startActivity(intent);
    }

    /**
     * Handles clicks from info button and game info button in main menu.
     * Launches a new activity with info screen.
     *
     * @param button The button that has been clicked
     */
    public void infoButtonsHandler(View button) {
        Intent intent = new Intent(this, InfoScreenActivity.class);
        switch (button.getId()) {
            case R.id.main_menu_button_info: {
                intent.putExtra(INFO_BUTTON_INTENT_KEY, INFO_BUTTON_GENERAL);
                break;
            }
            case R.id.main_menu_button_how_to_play: {
                intent.putExtra(INFO_BUTTON_INTENT_KEY, INFO_BUTTON_GAME);
                break;
            }
        }
        startActivity(intent);
    }

}
