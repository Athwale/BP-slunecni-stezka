package ondrej.mejzlik.suntrail.fragments;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import ondrej.mejzlik.suntrail.R;

import static ondrej.mejzlik.suntrail.activities.GameActivity.PREFERENCES_KEY;
import static ondrej.mejzlik.suntrail.fragments.QrScannerFragment.USE_FLASH_PREFERENCE_KEY;

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
        final CheckBox checkBoxFlash = (CheckBox) view.findViewById(R.id.scanner_choice_checkBox_flash);
        // Load flash preference from preferences and set checkbox state
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFERENCES_KEY, 0);
        boolean useFlash = preferences.getBoolean(USE_FLASH_PREFERENCE_KEY, false);
        checkBoxFlash.setChecked(useFlash);

        checkBoxFlash.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getActivity().getSharedPreferences(PREFERENCES_KEY, 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(USE_FLASH_PREFERENCE_KEY, checkBoxFlash.isChecked()).apply();
            }
        });

        return view;
    }
}
