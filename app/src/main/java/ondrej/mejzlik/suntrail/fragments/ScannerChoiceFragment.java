package ondrej.mejzlik.suntrail.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import ondrej.mejzlik.suntrail.R;

/**
 * This fragment shows buttons to select which scanner the user wants to use.
 */
public class ScannerChoiceFragment extends Fragment {
    private static final String USE_FLASH_KEY = "useFlash";

    public ScannerChoiceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner_choice, container, false);
        // Restore if we want to use flash
        if (savedInstanceState != null) {
            boolean useFlash = savedInstanceState.getBoolean(USE_FLASH_KEY);
            CheckBox checkBoxFlash = (CheckBox) view.findViewById(R.id.scanner_choice_checkBox_flash);
            checkBoxFlash.setChecked(useFlash);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save flash preference
        CheckBox checkBoxFlash = (CheckBox) getActivity().findViewById(R.id.scanner_choice_checkBox_flash);
        if (checkBoxFlash != null) {
            outState.putBoolean(USE_FLASH_KEY, checkBoxFlash.isChecked());
        }
    }
}
