package ondrej.mejzlik.suntrail.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.Utilities.HtmlConverter;

/**
 * Fragment class for how to play info screen.
 */
public class GameInfoFragment extends Fragment {
    private HtmlConverter mHtmlConverter = new HtmlConverter();

    public GameInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_info, container, false);
        this.fillText(view);
        return view;
    }

    private void fillText(View view) {
        TextView textViewTop = (TextView) (view.findViewById(R.id.game_info_text_view_top));
        textViewTop.setText(mHtmlConverter.getHtmlForTextView(getString(R.string.game_info_text_top)));

        TextView textViewMiddle = (TextView) (view.findViewById(R.id.game_info_text_view_bottom));
        textViewMiddle.setText(mHtmlConverter.getHtmlForTextView(getString(R.string.game_info_text_bottom)));
    }

}
