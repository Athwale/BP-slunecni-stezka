package ondrej.mejzlik.suntrail.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ondrej.mejzlik.suntrail.R;

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
     * Handles clicks from info button in main menu.
     * Launches a new activity with info screen.
     * @param view The button that has been clicked
     */
    public void infoButtonHandler(View view) {
        Intent intent = new Intent(this, InfoScreenActivity.class);
        startActivity(intent);
    }

    /**
     * Handles clicks from settings button in main menu.
     * Launches a new activity with settings screen.
     * @param view The button that has been clicked
     */
    public void settingsButtonHandler(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Handles clicks from scanner button in main menu.
     * Launches a new activity with scanner screen.
     * @param view The button that has been clicked
     */
    public void scannerButtonHandler(View view) {
        Intent intent = new Intent(this, ScannerActivity.class);
        startActivity(intent);
    }
}
