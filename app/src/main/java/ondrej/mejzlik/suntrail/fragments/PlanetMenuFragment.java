package ondrej.mejzlik.suntrail.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ondrej.mejzlik.suntrail.R;

import static ondrej.mejzlik.suntrail.activities.AllBoardsActivity.PLANET_ID_KEY;


/**
 * This fragment displays a menu after selecting a planet from all boards menu.
 * In this menu the user can select whether to view the text or play it as mp3.
 */
public class PlanetMenuFragment extends Fragment {

    public PlanetMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planet_menu, container, false);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(PLANET_ID_KEY)) {
            String newTitle = arguments.getString(PLANET_ID_KEY);
            TextView title = (TextView) (view.findViewById(R.id.planet_menu_title));
            title.setText(newTitle);
        }

        return view;
    }

}
