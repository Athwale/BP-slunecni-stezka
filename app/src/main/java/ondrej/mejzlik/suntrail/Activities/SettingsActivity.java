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
    public static final String SCAN_METHOD = "scanMethod";
    public static final String QR = "qr";
    public static final String NFC = "nfc";
    public static final String NONE = "none";
    private final String PREFERENCES_USED = "usedAlready";
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // Get shared preferences for the whole app
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Set default preferences if we run the first time
        if (!preferences.contains(PREFERENCES_USED)) {
            this.initSettings();
        } else {
            this.loadPreferences();
        }
    }

    /**
     * Initializes settings based on available device hardware.
     * Runs only once.
     */
    private void initSettings() {
        RadioButton radioButtonNfc = (RadioButton) findViewById(R.id.settings_radio_button_nfc);
        RadioButton radioButtonQr = (RadioButton) findViewById(R.id.settings_radio_button_qr);

        // Disable setting default values next time
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREFERENCES_USED, true);

        // Put the default values in
        // Enable qr if available and set it as default scan method
        if (this.enableControls(radioButtonQr, PackageManager.FEATURE_CAMERA)) {
            radioButtonQr.setChecked(true);
            editor.putString(SCAN_METHOD, QR);
        }
        // If nfc is available overwrite qr
        if (this.enableControls(radioButtonNfc, PackageManager.FEATURE_NFC)) {
            radioButtonNfc.setChecked(true);
            editor.putString(SCAN_METHOD, NFC);
        } else {
            editor.putString(SCAN_METHOD, NONE);
        }
        editor.apply();
    }


    /**
     * Loads preferences from app shared preferences and sets the radiobuttons according to them.
     */
    private void loadPreferences() {
        String scanMethod = preferences.getString(SCAN_METHOD, null);
        RadioButton radioButtonNfc = (RadioButton) findViewById(R.id.settings_radio_button_nfc);
        RadioButton radioButtonQr = (RadioButton) findViewById(R.id.settings_radio_button_qr);
        if (scanMethod.equals(QR)) {
            radioButtonQr.setChecked(true);
        } else if (scanMethod.equals(NFC)) {
            radioButtonNfc.setChecked(true);
        } else if (scanMethod.equals(NONE)) {
            radioButtonNfc.setChecked(false);
            radioButtonQr.setChecked(false);
        }
    }

    /**
     * Checkes whether a feature is present in the system and enables a radio button for it on
     * settings screen.
     *
     * @param button  The radio button to enable
     * @param feature The feature to check
     * @return true if feature is present and button was enabled
     */
    private boolean enableControls(RadioButton button, String feature) {
        PackageManager packageManager = getPackageManager();
        if (packageManager.hasSystemFeature(feature)) {
            button.setEnabled(true);
            return true;
        } else {
            button.setEnabled(false);
            return false;
        }
    }

    /**
     * Handles clicks from radio buttons in settings screen.
     * Writes values into shared preferences according to selected button.
     * @param radioButton The radio button that has been clicked
     */
    public void radioButtonClicked(View radioButton) {
        SharedPreferences.Editor editor = preferences.edit();
        // Is the button in the view checked
        boolean checked = ((RadioButton) radioButton).isChecked();
        // Check which radio button was clicked
        switch (radioButton.getId()) {
            case R.id.settings_radio_button_nfc:
                if (checked)
                    editor.putString(SCAN_METHOD, "nfc");
                break;
            case R.id.settings_radio_button_qr:
                if (checked)
                    editor.putString(SCAN_METHOD, "qr");
                break;
        }
        editor.apply();
    }

    /**
     * Handles clicks from clear game data checkbox in settings.
     * If the checkbox is clicked and checked, enables clear game data button.
     * @param checkBoxClear The button that has been clicked
     */
    public void clearCheckBoxHandler(View checkBoxClear) {
        Button buttonClear = (Button) findViewById(R.id.settings_button_clear_data);
        if (((CheckBox)checkBoxClear).isChecked()) {
            buttonClear.setEnabled(true);
        } else {
            buttonClear.setEnabled(false);
        }
    }

    /**
     * Handles clicks from clear game data button in settings.
     * If clear game data checkbox is checked, clears game data.
     * After completion disables itself and uncheckes clear game data checkbox.
     * @param view
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
