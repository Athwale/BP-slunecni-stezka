package ondrej.mejzlik.suntrail.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import ondrej.mejzlik.suntrail.R;

import static ondrej.mejzlik.suntrail.config.Configuration.INFO_BUTTON_GAME;
import static ondrej.mejzlik.suntrail.config.Configuration.INFO_BUTTON_GENERAL;
import static ondrej.mejzlik.suntrail.config.Configuration.INFO_BUTTON_INTENT_KEY;

/**
 * This is the main activity which is launched after after launching the app.
 * Displays main menu.
 */
public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
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
