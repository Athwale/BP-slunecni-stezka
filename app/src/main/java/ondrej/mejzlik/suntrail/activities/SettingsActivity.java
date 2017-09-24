package ondrej.mejzlik.suntrail.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.game.GameDatabaseHelper;
import ondrej.mejzlik.suntrail.game.GameUtilities;

import static ondrej.mejzlik.suntrail.activities.GameActivity.END_GAME_PREFERENCE_KEY;
import static ondrej.mejzlik.suntrail.activities.GameActivity.PREFERENCES_KEY;
import static ondrej.mejzlik.suntrail.fragments.QrScannerFragment.USE_FLASH_PREFERENCE_KEY;
import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.DATABASE_NAME;

public class SettingsActivity extends Activity {
    private GameUtilities gameUtilities = null;
    private SharedPreferences preferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.gameUtilities = new GameUtilities();
        setContentView(R.layout.activity_settings);

        TextView databaseExistsText = (TextView) findViewById(R.id.settings_text_view_data_info);
        Button buttonClear = (Button) findViewById(R.id.settings_button_clear_data);
        CheckBox checkBoxClear = (CheckBox) findViewById(R.id.settings_checkbox_clear_data);
        CheckBox checkBoxFlash = (CheckBox) findViewById(R.id.settings_checkbox_use_flash);

        if (this.gameUtilities.isDatabaseCreated(this)) {
            databaseExistsText.setText(R.string.settings_data_info_exists);
            databaseExistsText.setTextColor(ContextCompat.getColor(this, R.color.textGreen));
            checkBoxClear.setEnabled(true);
        } else {
            databaseExistsText.setText(R.string.settings_data_info_not_exists);
            databaseExistsText.setTextColor(ContextCompat.getColor(this, R.color.textRed));
            buttonClear.setEnabled(false);
            checkBoxClear.setEnabled(false);
        }

        // Set onClickListeners because using XML makes problems in MIUI launcher
        checkBoxClear.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCheckBoxHandler(v);
            }
        });

        checkBoxFlash.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                useFlashCheckBoxHandler(v);
            }
        });

        // Restore flash checkbox state
        this.preferences = this.getSharedPreferences(PREFERENCES_KEY, 0);
        checkBoxFlash.setChecked(preferences.getBoolean(USE_FLASH_PREFERENCE_KEY, false));
    }

    @Override
    public void onResume() {
        Button buttonClear = (Button) findViewById(R.id.settings_button_clear_data);
        CheckBox checkBoxClear = (CheckBox) findViewById(R.id.settings_checkbox_clear_data);

        // The button would stay disabled if the the user left the activity and then returned.
        if (checkBoxClear.isChecked()) {
            buttonClear.setEnabled(true);
        }
        super.onResume();
    }

    /**
     * Handles clicks from clear game data checkbox in settings.
     * If the checkbox is clicked and checked, enables clear game data button.
     *
     * @param checkBoxClear The button that has been clicked
     */
    private void clearCheckBoxHandler(View checkBoxClear) {
        Button buttonClear = (Button) findViewById(R.id.settings_button_clear_data);
        if (((CheckBox) checkBoxClear).isChecked()) {
            buttonClear.setEnabled(true);
        } else {
            buttonClear.setEnabled(false);
        }
    }

    /**
     * Handles clicks from use flash checkbox in settings.
     * If the checkbox is clicked and checked, saves a value into preference so that
     * flash is turned on when using the qr scanner.
     *
     * @param checkBoxClear The button that has been clicked
     */
    private void useFlashCheckBoxHandler(View checkBoxClear) {
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putBoolean(USE_FLASH_PREFERENCE_KEY, ((CheckBox) checkBoxClear).isChecked()).apply();
    }

    /**
     * Handles clicks from clear game data button in settings.
     * If clear game data checkbox is checked, clears game data.
     * After completion disables itself and uncheck clear game data checkbox.
     *
     * @param view The button that has been clicked
     */
    public void clearButtonHandler(View view) {
        CheckBox checkBoxClear = (CheckBox) findViewById(R.id.settings_checkbox_clear_data);
        TextView databaseExistsText = (TextView) findViewById(R.id.settings_text_view_data_info);
        // Clear game data
        // All database connections must be closed before calling this.
        if (checkBoxClear.isEnabled()) {
            // There is a database to delete
            if (checkBoxClear.isChecked()) {
                // Get the helper singleton to close the database connections
                GameDatabaseHelper databaseHelper = GameDatabaseHelper.getInstance(this.getApplicationContext());
                databaseHelper.close();
                this.deleteDatabase(DATABASE_NAME);
                if (!this.gameUtilities.isDatabaseCreated(this)) {
                    // The database was really deleted
                    Toast.makeText(this, this.getResources().getString(R.string.toast_game_data_clear), Toast.LENGTH_SHORT).show();
                    checkBoxClear.setChecked(false);
                    view.setEnabled(false);
                    checkBoxClear.setEnabled(false);
                    databaseExistsText.setText(R.string.settings_data_info_not_exists);
                    databaseExistsText.setTextColor(ContextCompat.getColor(this, R.color.textRed));
                    // Clear shared preferences, because now we need to display inventory again.
                    SharedPreferences.Editor editor = this.preferences.edit();
                    // Set that game is running, used in main activity in inventory button handler and scanner
                    // activity to disable game mode.
                    editor.putBoolean(END_GAME_PREFERENCE_KEY, false).apply();
                } else {
                    Toast.makeText(this, this.getResources().getString(R.string.toast_database_delete_fail), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
