package ondrej.mejzlik.suntrail.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

import ondrej.mejzlik.suntrail.R;

public class SettingsActivity extends Activity {
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // Get shared preferences for the whole app
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Preferences has been set to default when the main activity was first started
        // We can load them.
        this.loadPreferences();
    }

    /**
     * Loads preferences from app shared preferences and sets the radiobuttons according to them.
     */
    private void loadPreferences() {
        String scanMethod = preferences.getString(getResources().getString(R.string.preference_scan_method), null);
        RadioButton radioButtonNfc = (RadioButton) findViewById(R.id.settings_radio_button_nfc);
        RadioButton radioButtonQr = (RadioButton) findViewById(R.id.settings_radio_button_qr);
        // Enable buttons
        PackageManager packageManager = getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            radioButtonQr.setEnabled(true);
        }
        // If nfc is available overwrite qr
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)) {
            radioButtonNfc.setEnabled(true);
        }
        // Set button as checked according to scan method from preferences
        if (scanMethod.equals(getResources().getString(R.string.preference_qr))) {
            radioButtonQr.setChecked(true);
        } else if (scanMethod.equals(getResources().getString(R.string.preference_nfc))) {
            radioButtonNfc.setChecked(true);
        } else if (scanMethod.equals(getResources().getString(R.string.preference_none))) {
            radioButtonNfc.setChecked(false);
            radioButtonQr.setChecked(false);
        }
    }

    /**
     * Handles clicks from radio buttons in settings screen.
     * Writes values into shared preferences according to selected button.
     *
     * @param radioButton The radio button that has been clicked
     */
    public void radioButtonClicked(View radioButton) {
        SharedPreferences.Editor editor = preferences.edit();
        String scanMethod = getResources().getString(R.string.preference_scan_method);
        // Is the button in the view checked
        boolean checked = ((RadioButton) radioButton).isChecked();
        // Check which radio button was clicked
        switch (radioButton.getId()) {
            case R.id.settings_radio_button_nfc:
                if (checked) {
                    String nfc = getResources().getString(R.string.preference_nfc);
                    editor.putString(scanMethod, nfc);
                    break;
                }
            case R.id.settings_radio_button_qr:
                if (checked) {
                    String qr = getResources().getString(R.string.preference_qr);
                    editor.putString(scanMethod, qr);
                    break;
                }
        }
        editor.apply();
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
     * After completion disables itself and uncheckes clear game data checkbox.
     *
     * @param view The button that has been clicked
     */
    public void clearButtonHandler(View view) {
        CheckBox checkBoxClear = (CheckBox) findViewById(R.id.settings_checkbox_clear_data);
        if (checkBoxClear.isChecked()) {
            // Clear game data

            checkBoxClear.setChecked(false);
            view.setEnabled(false);
        }
    }
}
