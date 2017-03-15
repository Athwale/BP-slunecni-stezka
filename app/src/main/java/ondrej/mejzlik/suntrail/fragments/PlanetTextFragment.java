package ondrej.mejzlik.suntrail.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.utilities.HtmlConverter;

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
     * Set planet image and name and text based on planet ID in arguments.
     *
     * @param view The main view which is being modified in onCreateView
     */
    private void setContents(View view) {
        Bundle arguments = getArguments();
        TextView mainTitle = (TextView) (view.findViewById(R.id.planet_text_title));
        // This will be used to build resource name
        String imageResourceName;
        ImageView planetPhoto = (ImageView) (view.findViewById(R.id.planet_text_image_view_photo));
        // Holder for a new main title which is set according to which planet ID we get.
        String newTitle;

        if (arguments != null && arguments.containsKey(PLANET_ID_KEY)) {
            switch (arguments.getInt(PLANET_ID_KEY)) {
                case PLANET_ID_CERES: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_ceres);
                    imageResourceName = "ceres";
                    break;
                }
                case PLANET_ID_EARTH: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_earth);
                    imageResourceName = "earth";
                    break;
                }
                case PLANET_ID_HALLEY: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_halley);
                    imageResourceName = "halley";
                    break;
                }
                case PLANET_ID_JUPITER: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_jupiter);
                    imageResourceName = "jupiter";
                    break;
                }
                case PLANET_ID_MARS: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_mars);
                    imageResourceName = "mars";
                    break;
                }
                case PLANET_ID_MERCURY: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_mercury);
                    imageResourceName = "mercury";
                    break;
                }
                case PLANET_ID_MOON: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_moon);
                    imageResourceName = "moon";
                    break;
                }
                case PLANET_ID_NEPTUNE: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_neptune);
                    imageResourceName = "neptune";
                    break;
                }
                case PLANET_ID_SATURN: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_saturn);
                    imageResourceName = "saturn";
                    break;
                }
                case PLANET_ID_SUN: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_sun);
                    imageResourceName = "sun";
                    break;
                }
                case PLANET_ID_URANUS: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_uranus);
                    imageResourceName = "uranus";
                    break;
                }
                // Only other possible option is venus
                default: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_venus);
                    imageResourceName = "venus";
                    break;
                }
            }

            // Set image resource according to which planet we are displaying
            // The resources for planet quarters must have a name "pict_*planet*_quarter"
            String resourceName = "pict_" + imageResourceName + "_quarter";
            int resourceId = this.getResources().getIdentifier(resourceName, "drawable", getActivity().getPackageName());
            // Set new image
            // If the resource is not found no image is displayed. Anyway resources do not change
            // the photo should always be found.
            planetPhoto.setImageResource(resourceId);
            // Set new title
            mainTitle.setText(newTitle);
            // Fill text views
            this.fillText(view, imageResourceName);
        }
    }

    /**
     * Fills text views of this fragment with required strings.
     * This can not be done from xml since xml does not support using html markup in strings.
     *
     * @param view The main view of this fragment
     * @param name Name of the planet which is determined by setContents method.
     */
    private void fillText(View view, String name) {
        HtmlConverter htmlConverter = new HtmlConverter();
        // Get text resource according to which planet we are displaying
        // The resources for planet text must have a name "planet_text_*name*"
        // If resource is not found it throws resouceNotFoundException
        // But since resources do not change after install, this should not happen.
        String resourceName = "planet_text_" + name;
        int resourceId = this.getResources().getIdentifier(resourceName, "string", getActivity().getPackageName());
        // Load strings into textviews
        TextView textViewOne = (TextView) (view.findViewById(R.id.planet_text_text_view_top));
        textViewOne.setText(htmlConverter.getHtmlForTextView(getString(resourceId)));
    }

}
