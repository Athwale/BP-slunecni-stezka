package ondrej.mejzlik.suntrail.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.utilities.HtmlConverter;

/**
 * This fragment appears when the user clicks the game button for the first time.
 * The player is informed that he was given a ship and a small amount of credits to start with.
 * When the confirm button is pressed a new database and game data are created.
 */
public class StartGameFragment extends Fragment {


    public StartGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start_game, container, false);
        this.fillText(view);
        return view;
    }

    /**
     * Fills text views of this fragment with required strings.
     * This can not be done from xml since xml does not support using html markup in strings.
     *
     * @param view The main view of this fragment
     */
    private void fillText(View view) {
        HtmlConverter htmlConverter = new HtmlConverter();
        TextView textViewTop = (TextView) (view.findViewById(R.id.start_game_text_view_top));
        textViewTop.setText(htmlConverter.getHtmlForTextView(getString(R.string.start_new_game_text_top)));

        TextView textViewBottom = (TextView) (view.findViewById(R.id.start_game_text_view_bottom));
        textViewBottom.setText(htmlConverter.getHtmlForTextView(getString(R.string.start_new_game_text_bottom)));
    }
}