package ondrej.mejzlik.suntrail.fragments;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.utilities.HtmlConverter;

import static ondrej.mejzlik.suntrail.activities.MainMenuActivity.SCROLL_POSITION_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_HALF_PHOTO_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_ID_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_NAME_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_SYMBOL_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_TEXT_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_TEXT_TECH_KEY;

/**
 * this fragment displays text identical to what is on the Sun path information boards.
 * The fragment accepts one argument with the planet ID and then decides which data to load into
 * views based on which planet we want to display.
 */
public class PlanetTextFragment extends Fragment {
    private int scrollPosition = 0;

    public PlanetTextFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_planet_text, container, false);
        this.setContents(view);

        // If we're being restored from a previous state, restore last known scroll position.
        if (savedInstanceState != null) {
            this.scrollPosition = savedInstanceState.getInt(SCROLL_POSITION_KEY);
        }
        return view;
    }

    /**
     * Restore scroll position here, because in onCreateView the scroll view height is still 0.
     * This method is called every time the fragment is starting and after onCreateView.
     */
    @Override
    public void onResume() {
        super.onResume();
        final ScrollView scrollView = (ScrollView) getActivity().findViewById(R.id.planet_text_scroll_view);
        // For some reason scrollTo does not work in main thread.
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.scrollTo(0, scrollPosition);
            }
        });
        // Slide down the photo. We play the animation here after all other contents are set.
        ImageView planetPhoto = (ImageView) (getActivity().findViewById(R.id.planet_text_image_view_photo));
        int height = planetPhoto.getDrawable().getIntrinsicHeight();
        final ObjectAnimator spacerSlideDown = ObjectAnimator.ofFloat(planetPhoto, "translationY", -(height), 0);
        spacerSlideDown.setDuration(200);
        spacerSlideDown.setInterpolator(new DecelerateInterpolator());
        spacerSlideDown.start();
    }

    /**
     * Save scroll position here, onSaveInstanceState is only called when the activity may be
     * killed by the system. onPause is called every time the fragment is replaced with another.
     */
    @Override
    public void onPause() {
        super.onPause();
        ScrollView scrollView = (ScrollView) getActivity().findViewById(R.id.planet_text_scroll_view);
        // scrollView can be null if the system tries to save position when the user presses home
        // from a fragment opened from this fragment.
        if (scrollView != null) {
            this.scrollPosition = scrollView.getScrollY();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Saves the instance state when home button is pressed or when the system decides that it
        // may kill the activity with this fragment. Back up last known scroll position.
        outState.putInt(SCROLL_POSITION_KEY, this.scrollPosition);
    }

    /**
     * Set planet image and name and text based on planet ID in arguments.
     *
     * @param view The main view which is being modified in onCreateView
     */
    private void setContents(View view) {
        HtmlConverter htmlConverter = new HtmlConverter();
        // Get planet resources for this planet
        Bundle resources = getArguments();
        TextView mainTitle = (TextView) (view.findViewById(R.id.planet_text_title));
        ImageView planetPhoto = (ImageView) (view.findViewById(R.id.planet_text_image_view_photo));
        ImageView planetSymbol = (ImageView) (view.findViewById(R.id.planet_text_image_view_symbol));

        // Resources must contain planet id
        if (resources != null && resources.containsKey(PLANET_ID_KEY)) {
            // Set photo and texts
            // Set new title, photo and symbol
            mainTitle.setText(getResources().getString(resources.getInt(PLANET_NAME_KEY)));
            planetPhoto.setImageResource(resources.getInt(PLANET_HALF_PHOTO_KEY));
            planetSymbol.setImageResource(resources.getInt(PLANET_SYMBOL_KEY));
            // Load strings into text views
            int textTopId = resources.getInt(PLANET_TEXT_KEY);
            int textTechId = resources.getInt(PLANET_TEXT_TECH_KEY);
            TextView textViewTop = (TextView) (view.findViewById(R.id.planet_text_text_view_top));
            textViewTop.setText(htmlConverter.getHtmlForTextView(getString(textTopId)));
            TextView textViewBottom = (TextView) (view.findViewById(R.id.planet_text_text_view_tech));
            textViewBottom.setText(htmlConverter.getHtmlForTextView(getString(textTechId)));
        }
    }
}
