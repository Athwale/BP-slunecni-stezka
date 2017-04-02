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

import static ondrej.mejzlik.suntrail.activities.GameActivity.SPACESHIP_NAME_KEY;

/**
 * This fragment displays space ship information. It decides which info to display based on the
 * space ship name string resource id passed in arguments. We only have 3 space ships in the game
 * and it is improbable that there will be more because the number of planets where to buy them
 * is limited.
 */
public class ShipInfoFragment extends Fragment {

    public ShipInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ship_info, container, false);
        this.setContents(view);
        return view;
    }

    /**
     * This method fills the texts and picture into the fragment based on which ship name string
     * resource id we passed into the fragment as argument.
     *
     * @param view view from onCreateView
     */
    private void setContents(View view) {
        HtmlConverter htmlConverter = new HtmlConverter();
        Bundle arguments = getArguments();
        ImageView shipPicture = (ImageView) (view.findViewById(R.id.ship_info_image_view_ship));
        TextView shipName = (TextView) (view.findViewById(R.id.ship_info_text_view_ship_name));
        TextView infoText = (TextView) (view.findViewById(R.id.ship_info_text_view_top));
        TextView cargoSize = (TextView) (view.findViewById(R.id.ship_info_text_view_cargo_size));
        int shipNameResourceId = arguments.getInt(SPACESHIP_NAME_KEY);
        int imageResource;
        int textResource;
        int cargoSizeResource;

        switch (shipNameResourceId) {
            case R.string.ship_name_icarus: {
                imageResource = R.drawable.pict_icarus;
                textResource = R.string.ship_info_icarus;
                cargoSizeResource = R.string.ship_cargo_size_small;
                break;
            }
            case R.string.ship_name_lokys: {
                imageResource = R.drawable.pict_lokys;
                textResource = R.string.ship_info_lokys;
                cargoSizeResource = R.string.ship_cargo_size_medium;
                break;
            }
            // Only other option is Daedalus.
            default: {
                imageResource = R.drawable.pict_daedalus;
                textResource = R.string.ship_info_daedalus;
                cargoSizeResource = R.string.ship_cargo_size_large;
            }
        }
        shipPicture.setImageResource(imageResource);
        shipName.setText(shipNameResourceId);
        // The main text is a html
        infoText.setText(htmlConverter.getHtmlForTextView(getString(textResource)));
        cargoSize.setText(cargoSizeResource);
    }
}
