package ondrej.mejzlik.suntrail.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import ondrej.mejzlik.suntrail.R;

import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.DATABASE_NAME;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView databaseExistsText = (TextView) findViewById(R.id.settings_text_view_data_info);
        Button buttonClear = (Button) findViewById(R.id.settings_button_clear_data);
        CheckBox checkBoxClear = (CheckBox) findViewById(R.id.settings_checkbox_clear_data);

        if (this.checkDatabaseExistence()) {
            databaseExistsText.setText(R.string.settings_data_info_exists);
            buttonClear.setEnabled(true);
            checkBoxClear.setEnabled(true);
        } else {
            databaseExistsText.setText(R.string.settings_data_info_not_exists);
            buttonClear.setEnabled(false);
            checkBoxClear.setEnabled(false);
        }
    }

    /**
     * Handles clicks from clear game data checkbox in settings.
     * If the checkbox is clicked and checked, enables clear game data button.
     *
     * @param checkBoxClear The button that has been clicked
     */
    public void clearCheckBoxHandler(View checkBoxClear) {
        Button buttonClear = (Button) findViewById(R.id.settings_button_clear_data);
        if (((CheckBox) checkBoxClear).isChecked()) {
            buttonClear.setEnabled(true);
        } else {
            buttonClear.setEnabled(false);
        }
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
        // All database connections must be closed before calling this, but since they
        // are closed when exiting game activity, it is safe to call.
        if (checkBoxClear.isEnabled()) {
            // There is a database to delete
            if (checkBoxClear.isChecked()) {
                this.deleteDatabase(DATABASE_NAME);
                if (!this.checkDatabaseExistence()) {
                    // The database was really deleted
                    Toast.makeText(this, this.getResources().getString(R.string.toast_game_data_clear), Toast.LENGTH_SHORT).show();
                    checkBoxClear.setChecked(false);
                    view.setEnabled(false);
                    checkBoxClear.setEnabled(false);
                    databaseExistsText.setText(R.string.settings_data_info_not_exists);
                } else {
                    Toast.makeText(this, this.getResources().getString(R.string.toast_database_delete_fail), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * This method returns true if there is a game database file for this app in the system system.
     *
     * @return This method returns true if there is a game database file for this app.
     */
    private boolean checkDatabaseExistence() {
        File dbFile = this.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }
}
