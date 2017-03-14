package ondrej.mejzlik.suntrail.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ondrej.mejzlik.suntrail.R;

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
        View view = inflater.inflate(R.layout.fragment_planet_text, container, false);
        this.setContents(view);
        return view;
    }

    /**
     * Set planet image and name based on planet ID in arguments.
     * Resizes image to fit screen width based on how large display the device has.
     *
     * @param view The main view which is being modified in onCreateView
     */
    private void setContents(View view) {
        Bundle arguments = getArguments();
        TextView mainTitle = (TextView) (view.findViewById(R.id.planet_text_title));
        String newTitle;
        ImageView planetPhoto = (ImageView) (view.findViewById(R.id.planet_text_image_view_photo));

        if (arguments != null && arguments.containsKey(PLANET_ID_KEY)) {
            switch (arguments.getInt(PLANET_ID_KEY)) {
                case PLANET_ID_CERES: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_ceres);
                    planetPhoto.setImageResource(R.drawable.pict_ceres);
                    break;
                }
                case PLANET_ID_EARTH: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_earth);
                    planetPhoto.setImageResource(R.drawable.pict_earth);
                    break;
                }
                case PLANET_ID_HALLEY: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_halley);
                    planetPhoto.setImageResource(R.drawable.pict_halley);
                    break;
                }
                case PLANET_ID_JUPITER: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_jupiter);
                    planetPhoto.setImageResource(R.drawable.pict_jupiter);
                    break;
                }
                case PLANET_ID_MARS: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_mars);
                    planetPhoto.setImageResource(R.drawable.pict_mars);
                    break;
                }
                case PLANET_ID_MERCURY: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_mercury);
                    planetPhoto.setImageResource(R.drawable.pict_mercury);
                    break;
                }
                case PLANET_ID_MOON: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_moon);
                    planetPhoto.setImageResource(R.drawable.pict_moon);
                    break;
                }
                case PLANET_ID_NEPTUNE: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_neptune);
                    planetPhoto.setImageResource(R.drawable.pict_neptune);
                    break;
                }
                case PLANET_ID_SATURN: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_saturn);
                    planetPhoto.setImageResource(R.drawable.pict_saturn);
                    break;
                }
                case PLANET_ID_SUN: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_sun);
                    planetPhoto.setImageResource(R.drawable.pict_sun_quarter);
                    break;
                }
                case PLANET_ID_URANUS: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_uranus);
                    planetPhoto.setImageResource(R.drawable.pict_uranus);
                    break;
                }
                // Only other possible option is venus
                default: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_venus);
                    planetPhoto.setImageResource(R.drawable.pict_venus);
                    break;
                }
            }
            // Set new title
            mainTitle.setText(newTitle);
        }
    }

}
