package ondrej.mejzlik.suntrail.fragments;


import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ondrej.mejzlik.suntrail.R;

/**
 * This fragment shows the main menu. In case the device does not have neither a camera nor a nfc
 * scanner the game related buttons are hidden and a message is shown. The user still can at least
 * browse the planet menu.
 */
public class MainMenuFragment extends Fragment {

    public MainMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        // Remove scan button if the device does not have both a camera and a nfc reader
        this.disableScanButton(view);
        return view;
    }

    /**
     * Disables game related buttons in main menu if the device does not have both a camera
     * and a NFC reader.
     */
    private void disableScanButton(View view) {
        PackageManager packageManager = getActivity().getPackageManager();
        boolean camera = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        boolean nfc = packageManager.hasSystemFeature(PackageManager.FEATURE_NFC);
        Button scanButton = (Button) view.findViewById(R.id.main_menu_button_scan);
        Button inventoryButton = (Button) view.findViewById(R.id.main_menu_button_inventory);
        Button allBoardsButton = (Button) view.findViewById(R.id.main_menu_button_all_boards);
        Button settingsButton = (Button) view.findViewById(R.id.main_menu_button_settings);
        LinearLayout howToPlayLayout = (LinearLayout) view.findViewById(R.id.main_menu_linear_layout_how_to_play);
        TextView warningNoScanner = (TextView) view.findViewById(R.id.main_menu_text_view_no_scanner);
        if (!camera && !nfc) {
            scanButton.setVisibility(View.GONE);
            inventoryButton.setVisibility(View.GONE);
            settingsButton.setVisibility(View.GONE);
            howToPlayLayout.setVisibility(View.GONE);
            warningNoScanner.setVisibility(View.VISIBLE);
            // In normal state, the all boards button has some top margin, it would look weird
            // when all the other buttons are gone, so we set it to 0.
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) allBoardsButton.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            allBoardsButton.setLayoutParams(params);
        } else {
            warningNoScanner.setVisibility(View.GONE);
            scanButton.setVisibility(View.VISIBLE);
            inventoryButton.setVisibility(View.VISIBLE);
            settingsButton.setVisibility(View.VISIBLE);
            howToPlayLayout.setVisibility(View.VISIBLE);
        }
    }

}
