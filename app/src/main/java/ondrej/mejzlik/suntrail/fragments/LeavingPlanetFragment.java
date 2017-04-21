package ondrej.mejzlik.suntrail.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ondrej.mejzlik.suntrail.R;

/**
 * This fragment displays a warning message that the user is leaving a planet in game mode.
 * And it will not be possible to return to the shop on that planet once they leave.
 */
public class LeavingPlanetFragment extends Fragment {

    public LeavingPlanetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaving_planet, container, false);
    }

}
