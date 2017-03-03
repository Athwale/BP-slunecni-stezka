package ondrej.mejzlik.suntrail.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import ondrej.mejzlik.suntrail.R;

import static ondrej.mejzlik.suntrail.Activities.SettingsActivity.NFC;
import static ondrej.mejzlik.suntrail.Activities.SettingsActivity.NONE;
import static ondrej.mejzlik.suntrail.Activities.SettingsActivity.SCAN_METHOD;
import static ondrej.mejzlik.suntrail.Activities.SettingsActivity.QR;

/**
 * This activity provides NFC or QR code scanner depending on used settings.
 */
public class ScannerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        this.setTitle();
    }

    // TODO USE INTENT TO START THIS SCANNER
    // TODO BACK ALWAYS TAKES YOU TO MAIN MENU
    private void setTitle() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView title = (TextView) findViewById(R.id.scanner_text_view_title);
        String scanMethod = preferences.getString(SCAN_METHOD, null);
        if (scanMethod.equals(QR)) {
            title.setText(getString(R.string.scanner_screen_title_qr));
        } else if (scanMethod.equals(NFC)) {
            title.setText(getString(R.string.scanner_screen_title_nfc));
        } else if (scanMethod.equals(NONE)) {

        }
    }
}
