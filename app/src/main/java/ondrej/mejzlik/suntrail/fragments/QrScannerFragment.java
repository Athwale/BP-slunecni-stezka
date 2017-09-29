package ondrej.mejzlik.suntrail.fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import ondrej.mejzlik.suntrail.activities.ScannerActivity;
import ondrej.mejzlik.suntrail.utilities.PlanetIdentifier;

import static ondrej.mejzlik.suntrail.activities.GameActivity.PREFERENCES_KEY;
import static ondrej.mejzlik.suntrail.activities.ScannerActivity.PERMISSION_CAMERA;
import static ondrej.mejzlik.suntrail.activities.ScannerActivity.USE_FLASH_KEY;

/**
 * This fragment contains the qr scanner which uses zxing library.
 * This fragment asks for camera permissions.
 */
public class QrScannerFragment extends Fragment implements ZXingScannerView.ResultHandler {
    public static final String USE_FLASH_PREFERENCE_KEY = "useFlashPreferenceKey";
    private ZXingScannerView scannerView;
    private PlanetIdentifier identifier;

    public QrScannerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.identifier = new PlanetIdentifier();
        this.getPermissions();

        scannerView = new ZXingScannerView(getActivity());
        // Turn on flash if the user wants it
        Bundle arguments = getArguments();
        // Turn on flash if user checked the use flash checkbox in scanner choice or settings
        // Load flash preference from preferences
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFERENCES_KEY, 0);
        boolean useFlash = preferences.getBoolean(USE_FLASH_PREFERENCE_KEY, false);
        if (arguments != null && arguments.containsKey(USE_FLASH_KEY)) {
            scannerView.setFlash(arguments.getBoolean(USE_FLASH_KEY));
        } else if (useFlash) {
            scannerView.setFlash(true);
        }
        // Set supported format to QR code only.
        ArrayList<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.QR_CODE);
        scannerView.setFormats(formats);
        return scannerView;
    }

    /**
     * Checks if this app has camera permission, if not, asks the user to allow using camera.
     * After this the onRequestPermissionsResult in scanner activity is called.
     */
    private void getPermissions() {
        // Fragment is attached, onAttach is called before onCreateView
        int cameraPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            // Ask for camera permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                    PERMISSION_CAMERA);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (scannerView != null) {
            scannerView.setResultHandler(this);
            scannerView.startCamera();
        }
    }

    @Override
    public void onPause() {
        if (scannerView != null) {
            scannerView.stopCameraPreview();
            scannerView.removeAllViews();
            scannerView.stopCamera();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // This is needed on slower devices to properly close the camera. Sometimes the scanner
        // wiew will be destroyed before this on very fast devices so the null checking is needed.
        if (scannerView != null) {
            scannerView.stopCameraPreview();
            scannerView.removeAllViews();
            scannerView.stopCamera();
        }
        super.onDestroy();
    }

    @Override
    public void handleResult(Result rawResult) {
        // Scanner is run from scanner activity only.
        if (scannerView != null) {
            scannerView.stopCameraPreview();
            scannerView.removeAllViews();
            scannerView.stopCamera();
        }

        // Parse and pass planet id to scanner activity
        int planetId = this.identifier.getPlanetIdFromQr(rawResult.getText());
        ((ScannerActivity) this.getActivity()).processScannerResult(planetId, true);
    }
}
