package ondrej.mejzlik.suntrail.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ondrej.mejzlik.suntrail.R;

/**
 * This activity displays a menu of all available boards using fragments.
 * Each menu item opens a selection fragment to either display the text or play it as mp3.
 */
public class BoardsListFragment extends Fragment {

    public BoardsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_boards_list, container, false);
    }

}
