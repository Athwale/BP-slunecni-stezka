package ondrej.mejzlik.suntrail.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.LinkedList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import ondrej.mejzlik.suntrail.R;

import static ondrej.mejzlik.suntrail.config.Configuration.USE_FLASH_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class QrScannerFragment extends Fragment implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scannerView;

    public QrScannerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        scannerView = new ZXingScannerView(getActivity());
        // Turn on flash if the user wants it
        Bundle arguments = getArguments();
        // Turn on flash if user checked the use flash checkbox
        if (arguments != null && arguments.containsKey(USE_FLASH_KEY)) {
            scannerView.setFlash(arguments.getBoolean(USE_FLASH_KEY));
        }
        // Set supported format to QR code only.
        ArrayList formats = new ArrayList<BarcodeFormat>();
        formats.add(BarcodeFormat.QR_CODE);
        scannerView.setFormats(formats);
        return scannerView;
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
