package ondrej.mejzlik.suntrail.fragments;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ondrej.mejzlik.suntrail.R;

import static ondrej.mejzlik.suntrail.config.Configuration.HAS_NFC_QR;
import static ondrej.mejzlik.suntrail.config.Configuration.HAS_NOTHING;
import static ondrej.mejzlik.suntrail.config.Configuration.HAS_ONLY_NFC;
import static ondrej.mejzlik.suntrail.config.Configuration.HAS_ONLY_QR;

/**
 * This fragment shows buttons to select which scanner the user wants to use.
 */
public class ScannerChoiceFragment extends Fragment {

    public ScannerChoiceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner_choice, container, false);

        LinearLayout nfcButton = (LinearLayout) view.findViewById(R.id.scanner_choice_linear_layout_nfc);
        LinearLayout qrButton = (LinearLayout) view.findViewById(R.id.scanner_choice_linear_layout_qr);
        TextView noScannerTextView = (TextView) view.findViewById(R.id.scanner_choice_text_view_no_scanner);
        // Hide buttons to scanner which this device does not have
        // In an extremely unlikely case when the device has nothing, disable both buttons
        // and show a varning text.
        switch (this.checkFeatures()) {
            case HAS_NOTHING: {
                nfcButton.setVisibility(View.GONE);
                qrButton.setVisibility(View.GONE);
                noScannerTextView.setVisibility(View.VISIBLE);
                break;
            }
            case HAS_ONLY_NFC: {
                nfcButton.setVisibility(View.VISIBLE);
                qrButton.setVisibility(View.GONE);
                noScannerTextView.setVisibility(View.GONE);
                break;
            }
            case HAS_ONLY_QR: {
                nfcButton.setVisibility(View.GONE);
                qrButton.setVisibility(View.VISIBLE);
                noScannerTextView.setVisibility(View.GONE);
                break;
            }
            case HAS_NFC_QR: {
                nfcButton.setVisibility(View.VISIBLE);
                qrButton.setVisibility(View.VISIBLE);
                noScannerTextView.setVisibility(View.GONE);
                break;
            }
        }
        return view;
    }

    /**
     * Check if the system has both camera and nfc. If not, disable selection screen.
     */
    private int checkFeatures() {
        PackageManager packageManager = getActivity().getPackageManager();
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

}
