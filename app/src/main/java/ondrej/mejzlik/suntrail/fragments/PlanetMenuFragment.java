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
import android.widget.Toast;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.utilities.ParametrizedToastOnClickListener;

import static ondrej.mejzlik.suntrail.activities.MainMenuActivity.SCROLL_POSITION_KEY;
import static ondrej.mejzlik.suntrail.activities.ScannerActivity.SHOW_GAME_BUTTON;
import static ondrej.mejzlik.suntrail.activities.ScannerActivity.SHOW_GAME_BUTTON_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_ID_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_NAME_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_PHOTO_AUTHOR_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_PHOTO_KEY;


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
        // Set planet image, title and author based on planet resources in arguments.
        this.setContents(view);
        return view;
    }

    /**
     * Set planet image and name based on planet ID in arguments.
     *
     * @param view The main view which is being modified in onCreateView
     */
    private void setContents(View view) {
        // Get planet resources
        Bundle resources = getArguments();
        TextView mainTitle = (TextView) (view.findViewById(R.id.planet_menu_title));
        ImageView planetPhoto = (ImageView) (view.findViewById(R.id.planet_menu_image_view_photo));
        // This variable will be assigned a value of who shot the planet photo and then is used
        // To set author toast.
        String author;

        // Resources must contain planet id
        if (resources != null && resources.containsKey(PLANET_ID_KEY)) {
            // Set photo and texts
            // Set new title, photo and author
            mainTitle.setText(resources.getString(PLANET_NAME_KEY));
            planetPhoto.setImageResource(resources.getInt(PLANET_PHOTO_KEY));
            author = resources.getString(PLANET_PHOTO_AUTHOR_KEY);

            // Make game mode button visible if we started this fragment from scanner.
            Button gameButton = (Button) (view.findViewById(R.id.planet_menu_button_play_game));
            if (resources.containsKey(SHOW_GAME_BUTTON_KEY)) {
                if (resources.getString(SHOW_GAME_BUTTON_KEY).equals(SHOW_GAME_BUTTON)) {
                    gameButton.setVisibility(View.VISIBLE);
                }
            } else {
                gameButton.setVisibility(View.GONE);
            }
            // Set on click listener once for photo to show author
            ParametrizedToastOnClickListener listener = new ParametrizedToastOnClickListener();
            listener.setToast(author, getActivity(), Toast.LENGTH_SHORT);
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
