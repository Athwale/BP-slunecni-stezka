package ondrej.mejzlik.suntrail.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import ondrej.mejzlik.suntrail.R;

import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_NAME_KEY;
import static ondrej.mejzlik.suntrail.config.Configuration.SCROLL_POSITION_KEY;


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

        // If we're being restored from a previous state,
        // Move scroll view to last known position
        if (savedInstanceState != null) {
            int scrollPosition = savedInstanceState.getInt(SCROLL_POSITION_KEY);
            ScrollView scrollView = (ScrollView) view.findViewById(R.id.all_boards_scroll_view);
            scrollView.scrollTo(0, scrollPosition);
        }

        Bundle arguments = getArguments();
        // Set the title of the screen to the planet name which is the same as was on the
        // button we used to open this menu.
        if (arguments != null && arguments.containsKey(PLANET_NAME_KEY)) {
            String newTitle = arguments.getString(PLANET_NAME_KEY);
            TextView title = (TextView) (view.findViewById(R.id.planet_menu_title));
            title.setText(newTitle);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save scroll view position
        ScrollView scrollView = (ScrollView) getActivity().findViewById(R.id.all_boards_scroll_view);
        outState.putInt(SCROLL_POSITION_KEY, scrollView.getScrollY());
    }

}
