package ondrej.mejzlik.suntrail.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
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
import static ondrej.mejzlik.suntrail.config.Configuration.SCROLL_POSITION_KEY;

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
        // If we're being restored from a previous state,
        // Move to last known position
        if (savedInstanceState != null) {
            int scrollPosition = savedInstanceState.getInt(SCROLL_POSITION_KEY);
            ScrollView scrollView = (ScrollView) view.findViewById(R.id.planet_text_scroll_view);
            scrollView.scrollTo(0, scrollPosition);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ScrollView scrollView = (ScrollView) getActivity().findViewById(R.id.planet_text_scroll_view);
        // scrollView can be null if the system tries to save position when the user presses home
        // from a fragment opened from this fragment.
        if (scrollView != null) {
            outState.putInt(SCROLL_POSITION_KEY, scrollView.getScrollY());
        }
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
        String planetName;
        ImageView planetPhoto = (ImageView) (view.findViewById(R.id.planet_text_image_view_photo));
        ImageView planetSymbol = (ImageView) (view.findViewById(R.id.planet_text_image_view_symbol));
        // Holder for a new main title which is set according to which planet ID we get.
        String newTitle;

        if (arguments != null && arguments.containsKey(PLANET_ID_KEY)) {
            switch (arguments.getInt(PLANET_ID_KEY)) {
                case PLANET_ID_CERES: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_ceres);
                    planetName = "ceres";
                    break;
                }
                case PLANET_ID_EARTH: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_earth);
                    planetName = "earth";
                    break;
                }
                case PLANET_ID_HALLEY: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_halley);
                    planetName = "halley";
                    break;
                }
                case PLANET_ID_JUPITER: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_jupiter);
                    planetName = "jupiter";
                    break;
                }
                case PLANET_ID_MARS: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_mars);
                    planetName = "mars";
                    break;
                }
                case PLANET_ID_MERCURY: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_mercury);
                    planetName = "mercury";
                    break;
                }
                case PLANET_ID_MOON: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_moon);
                    planetName = "moon";
                    break;
                }
                case PLANET_ID_NEPTUNE: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_neptune);
                    planetName = "neptune";
                    break;
                }
                case PLANET_ID_SATURN: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_saturn);
                    planetName = "saturn";
                    break;
                }
                case PLANET_ID_SUN: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_sun);
                    planetName = "sun";
                    break;
                }
                case PLANET_ID_URANUS: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_uranus);
                    planetName = "uranus";
                    break;
                }
                // Only other possible option is venus
                default: {
                    newTitle = getResources().getString(R.string.all_boards_button_name_venus);
                    planetName = "venus";
                    break;
                }
            }

            // Get main image resource according to which planet we are displaying
            // The resources for planet halves must have a name "pict_*planet*_half"
            String planetResourceName = "pict_" + planetName + "_half";
            int planetResourceId = this.getResources().getIdentifier(planetResourceName, "drawable", getActivity().getPackageName());
            // Get symbol image resource
            // The resources for planet symbols must have a name "pict_symbol_*name*"
            String planetSymbolResourceName = "pict_symbol_" + planetName;
            int symbolResourceId = this.getResources().getIdentifier(planetSymbolResourceName, "drawable", getActivity().getPackageName());
            // Set new images
            // If the resource is not found no image is displayed. Anyway resources do not change
            // the photo should always be found.
            planetPhoto.setImageResource(planetResourceId);
            planetSymbol.setImageResource(symbolResourceId);
            // Set new title
            mainTitle.setText(newTitle);
            // Fill text views
            this.fillText(view, planetName);
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
        String textTopName = "planet_text_" + name + "_top";
        String textBottomName = "planet_text_" + name + "_tech";
        int textTopId = this.getResources().getIdentifier(textTopName, "string", getActivity().getPackageName());
        int textTechId = this.getResources().getIdentifier(textBottomName, "string", getActivity().getPackageName());
        // Load strings into textviews
        TextView textViewTop = (TextView) (view.findViewById(R.id.planet_text_text_view_top));
        textViewTop.setText(htmlConverter.getHtmlForTextView(getString(textTopId)));
        TextView textViewBottom = (TextView) (view.findViewById(R.id.planet_text_text_view_tech));
        textViewBottom.setText(htmlConverter.getHtmlForTextView(getString(textTechId)));
    }

}
