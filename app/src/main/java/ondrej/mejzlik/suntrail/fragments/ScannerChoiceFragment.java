package ondrej.mejzlik.suntrail.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ondrej.mejzlik.suntrail.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScannerChoiceFragment extends Fragment {

    public ScannerChoiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scanner_choice, container, false);
    }

}
