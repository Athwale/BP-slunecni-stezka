package ondrej.mejzlik.suntrail.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ondrej.mejzlik.suntrail.R;

/**
 * this fragment displays text identical to what is on the Sun path information boards.
 * The fragment accepts one argument with the planet ID and then decides which data to load into
 * views based on which planet we want to display.
 */
public class PlanetTextFragment extends Fragment {


    public PlanetTextFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_planet_text, container, false);
    }

}
