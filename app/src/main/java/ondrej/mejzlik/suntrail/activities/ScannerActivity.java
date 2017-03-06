package ondrej.mejzlik.suntrail.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import ondrej.mejzlik.suntrail.R;

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
    private void setTitle() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView title = (TextView) findViewById(R.id.scanner_text_view_title);
        String scanMethodIdentifier = getResources().getString(R.string.preference_scan_method);
        String scanMethod = preferences.getString(scanMethodIdentifier, null);
        String qr = getResources().getString(R.string.preference_qr);
        String nfc = getResources().getString(R.string.preference_nfc);
        String none = getResources().getString(R.string.preference_none);
        if (scanMethod.equals(qr)) {
            title.setText(getString(R.string.scanner_screen_title_qr));
        } else if (scanMethod.equals(nfc)) {
            title.setText(getString(R.string.scanner_screen_title_nfc));
        } else if (scanMethod.equals(none)) {
            title.setText("No scanner available");
        }
    }
}
