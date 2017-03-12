package ondrej.mejzlik.suntrail.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.utilities.HtmlConverter;

import static ondrej.mejzlik.suntrail.config.Configuration.SCROLL_POSITION_KEY;

/**
 * Fragment class for general Sun path information.
 * Shows general information about the Sun Path.
 */
public class SunPathInfoFragment extends Fragment {

    public SunPathInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sun_path_info, container, false);
        this.fillText(view);

        // If we're being restored from a previous state,
        // Move to last known position
        if (savedInstanceState != null) {
            int scrollPosition = savedInstanceState.getInt(SCROLL_POSITION_KEY);
            ScrollView scrollView = (ScrollView) view.findViewById(R.id.sun_path_info_scroll_view);
            scrollView.scrollTo(0, scrollPosition);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ScrollView scrollView = (ScrollView) getActivity().findViewById(R.id.sun_path_info_scroll_view);
        // scrollView can be null if the system tries to save position when the user presses home
        // from a fragment opened from this fragment.
        if (scrollView != null) {
            outState.putInt(SCROLL_POSITION_KEY, scrollView.getScrollY());
        }
    }

    /**
     * Fills text views of this fragment with required strings.
     * This can not be done from xml since xml does not support using html markup in strings.
     *
     * @param view The main view of this fragment
     */
    private void fillText(View view) {
        HtmlConverter htmlConverter = new HtmlConverter();
        // Load strings into textviews
        TextView textViewTop = (TextView) (view.findViewById(R.id.sun_path_info_text_view_top));
        textViewTop.setText(htmlConverter.getHtmlForTextView(getString(R.string.sun_path_info_top)));
        textViewTop.setMovementMethod(LinkMovementMethod.getInstance());

        TextView textViewMiddle = (TextView) (view.findViewById(R.id.sun_path_info_text_view_middle));
        textViewMiddle.setText(htmlConverter.getHtmlForTextView(getString(R.string.sun_path_info_middle)));
        textViewMiddle.setMovementMethod(LinkMovementMethod.getInstance());

        TextView textViewBottom = (TextView) (view.findViewById(R.id.sun_path_info_text_view_bottom));
        textViewBottom.setText(htmlConverter.getHtmlForTextView(getString(R.string.sun_path_info_bottom)));
        textViewBottom.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
