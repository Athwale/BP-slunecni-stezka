package ondrej.mejzlik.suntrail.fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static ondrej.mejzlik.suntrail.config.Configuration.PERMISSION_CAMERA;
import static ondrej.mejzlik.suntrail.config.Configuration.USE_FLASH_KEY;

/**
 * This fragment contains the qr scanner which uses zxing library.
 * This fragment asks for camera permissions.
 */
public class QrScannerFragment extends Fragment implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scannerView;

    public QrScannerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.getPermissions();

        scannerView = new ZXingScannerView(getActivity());
        // Turn on flash if the user wants it
        Bundle arguments = getArguments();
        // Turn on flash if user checked the use flash checkbox
        if (arguments != null && arguments.containsKey(USE_FLASH_KEY)) {
            scannerView.setFlash(arguments.getBoolean(USE_FLASH_KEY));
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
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Toast.makeText(getActivity(), "Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();
        // Wait 2 seconds to resume the preview.
        // On older devices stopping and resuming camera preview can result in freezing the app.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scannerView.resumeCameraPreview(QrScannerFragment.this);
            }
        }, 2000);
    }
}
