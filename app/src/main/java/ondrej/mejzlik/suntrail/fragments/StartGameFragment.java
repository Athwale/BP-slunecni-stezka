package ondrej.mejzlik.suntrail.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ondrej.mejzlik.suntrail.R;

/**
 * This fragment appears when the user clicks the game button for the first time.
 * The player is informed that he was given a ship and a small amount of credits to start with.
 * When the confirm button is pressed a new database and game data are created.
 */
public class StartGameFragment extends Fragment {


    public StartGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_game, container, false);
    }

}
