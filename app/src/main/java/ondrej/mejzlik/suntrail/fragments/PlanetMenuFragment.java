package ondrej.mejzlik.suntrail.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.utilities.ParametrizedToastOnClickListener;

import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_CERES;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_EARTH;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_HALLEY;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_JUPITER;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_KEY;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_MARS;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_MERCURY;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_MOON;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_NEPTUNE;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_SATURN;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_SUN;
import static ondrej.mejzlik.suntrail.config.Configuration.PLANET_ID_URANUS;
import static ondrej.mejzlik.suntrail.config.Configuration.SCROLL_POSITION_KEY;
import static ondrej.mejzlik.suntrail.config.Configuration.SHOW_GAME_BUTTON;
import static ondrej.mejzlik.suntrail.config.Configuration.SHOW_GAME_BUTTON_KEY;


/**
 * This fragment displays a menu after selecting a planet from all boards menu.
 * In this menu the user can select whether to view the text or play it as mp3.
 */
public class PlanetMenuFragment extends Fragment {
    private static final String CERES_AUTHOR = "NASA/JPL-Caltech";
    private static final String EARTH_AUTHOR = "NASA/Goddard";
    private static final String HALLEY_AUTHOR = "Giotto Project, ESA";
    private static final String JUPITER_AUTHOR = "NASA/JPL/Voyager 1";
    private static final String MARS_AUTHOR = "NASA/Hubble";
    private static final String MERCURY_AUTHOR = "NASA/Messenger";
    private static final String MOON_AUTHOR = "Fred Locklear";
    private static final String NEPTUNE_AUTHOR = "NASA/JPL/Voyager 2";
    private static final String SATURN_AUTHOR = "NASA/ESA/JPL/Cassini";
    private static final String SUN_AUTHOR = "NASA/Goddard/SDO";
    private static final String URANUS_AUTHOR = "NASA/JPL/Voyager 2/Joe Ruhinski";
    private static final String VENUS_AUTHOR = "NASA/JPL/Magellan";

    public PlanetMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planet_menu, container, false);
        // Set planet image and name based on planet ID in arguments.
        this.setContents(view);
        return view;
    }

    /**
     * Set planet image and name based on planet ID in arguments.
     *
     * @param view The main view which is being modified in onCreateView
     */
    private void setContents(View view) {
        Bundle arguments = getArguments();
        TextView mainTitle = (TextView) (view.findViewById(R.id.planet_menu_title));
        String newTitle;
        String author;
        ImageView planetPhoto = (ImageView) (view.findViewById(R.id.planet_menu_image_view_photo));

        if (arguments != null && arguments.containsKey(PLANET_ID_KEY)) {
            switch (arguments.getInt(PLANET_ID_KEY)) {
                case PLANET_ID_CERES: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_ceres);
                    planetPhoto.setImageResource(R.drawable.pict_ceres);
                    author = CERES_AUTHOR;
                    break;
                }
                case PLANET_ID_EARTH: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_earth);
                    planetPhoto.setImageResource(R.drawable.pict_earth);
                    author = EARTH_AUTHOR;
                    break;
                }
                case PLANET_ID_HALLEY: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_halley);
                    planetPhoto.setImageResource(R.drawable.pict_halley);
                    author = HALLEY_AUTHOR;
                    break;
                }
                case PLANET_ID_JUPITER: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_jupiter);
                    planetPhoto.setImageResource(R.drawable.pict_jupiter);
                    author = JUPITER_AUTHOR;
                    break;
                }
                case PLANET_ID_MARS: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_mars);
                    planetPhoto.setImageResource(R.drawable.pict_mars);
                    author = MARS_AUTHOR;
                    break;
                }
                case PLANET_ID_MERCURY: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_mercury);
                    planetPhoto.setImageResource(R.drawable.pict_mercury);
                    author = MERCURY_AUTHOR;
                    break;
                }
                case PLANET_ID_MOON: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_moon);
                    planetPhoto.setImageResource(R.drawable.pict_moon);
                    author = MOON_AUTHOR;
                    break;
                }
                case PLANET_ID_NEPTUNE: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_neptune);
                    planetPhoto.setImageResource(R.drawable.pict_neptune);
                    author = NEPTUNE_AUTHOR;
                    break;
                }
                case PLANET_ID_SATURN: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_saturn);
                    planetPhoto.setImageResource(R.drawable.pict_saturn);
                    author = SATURN_AUTHOR;
                    break;
                }
                case PLANET_ID_SUN: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_sun);
                    planetPhoto.setImageResource(R.drawable.pict_sun);
                    author = SUN_AUTHOR;
                    break;
                }
                case PLANET_ID_URANUS: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_uranus);
                    planetPhoto.setImageResource(R.drawable.pict_uranus);
                    author = URANUS_AUTHOR;
                    break;
                }
                // Only other possible option is venus
                default: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_venus);
                    planetPhoto.setImageResource(R.drawable.pict_venus);
                    author = VENUS_AUTHOR;
                    break;
                }
            }
            // Set new title
            mainTitle.setText(newTitle);
            // Make game mode button visible if we started this fragment from scanner.
            Button gameButton = (Button) (view.findViewById(R.id.planet_menu_button_play_game));
            if (arguments.containsKey(SHOW_GAME_BUTTON_KEY)) {
                if (arguments.getString(SHOW_GAME_BUTTON_KEY).equals(SHOW_GAME_BUTTON)) {
                    gameButton.setVisibility(View.VISIBLE);
                }
            } else {
                gameButton.setVisibility(View.GONE);
            }

            // Set on click listener for photo to show author
            ParametrizedToastOnClickListener listener = new ParametrizedToastOnClickListener();
            listener.setToast(author, getActivity());
            planetPhoto.setOnClickListener(listener);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save scroll view position
        ScrollView scrollView = (ScrollView) getActivity().findViewById(R.id.all_boards_scroll_view);
        // scrollView can be null if the system tries to save position when the user presses home
        // from a fragment opened from this fragment.
        if (scrollView != null) {
            outState.putInt(SCROLL_POSITION_KEY, scrollView.getScrollY());
        }
    }

}
