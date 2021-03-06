package ondrej.mejzlik.suntrail.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ondrej.mejzlik.suntrail.R;

import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_SATURN;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_ID_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_NAME_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_QUARTER_PHOTO_KEY;

/**
 * This fragment shows the game menu - Inventory, Shop and Leave buttons.
 * It displays a planet quarter photo in the top right corner and planet name. These are obtained
 * from planet resources bundle which is passed into the fragment in arguments.
 */
public class GameMenuFragment extends Fragment {

    public GameMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game_menu, container, false);
        // Set the name of the planet into the title and set the quarter image.
        this.setContents(view);
        return view;
    }

    /**
     * Set the name of the planet into the title and set the quarter planet image into the right
     * upper corner. All this information is passed inside arguments into the fragment.
     *
     * @param view The main view which is being modified in onCreateView
     */
    private void setContents(View view) {
        // Get planet resources for this planet
        Bundle resources = getArguments();

        TextView title = (TextView) (view.findViewById(R.id.game_menu_title));
        ImageView planetQuarterPhoto = (ImageView) (view.findViewById(R.id.game_menu_image_view_photo));
        LinearLayout endGameLayout = (LinearLayout) (view.findViewById(R.id.game_menu_linear_layout_exit));

        // Resources must contain planet id
        if (resources != null && resources.containsKey(PLANET_ID_KEY)) {
            planetQuarterPhoto.setImageResource(resources.getInt(PLANET_QUARTER_PHOTO_KEY));
            String newTitle = title.getText() + " " + getResources().getString(resources.getInt(PLANET_NAME_KEY));
            title.setText(newTitle);

            // The trip can end early at the Saturn board
            if (resources.getInt(PLANET_ID_KEY) == PLANET_ID_SATURN) {
                // Enable end game button
                endGameLayout.setVisibility(View.VISIBLE);
            } else {
                // Disable end game button
                endGameLayout.setVisibility(View.GONE);
            }
        }
    }
}
