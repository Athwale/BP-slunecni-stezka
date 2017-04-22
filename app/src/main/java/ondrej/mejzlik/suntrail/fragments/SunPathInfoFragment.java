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

import static ondrej.mejzlik.suntrail.activities.MainMenuActivity.SCROLL_POSITION_KEY;

/**
 * Fragment class for general Sun path information.
 * Shows general information about the Sun Path.
 */
public class SunPathInfoFragment extends Fragment {
    private int scrollPosition = 0;

    public SunPathInfoFragment() {
        // Required empty public constructor
    }

    // onCreateView runs every time this fragment is opened or returned to.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sun_path_info, container, false);
        this.fillText(view);

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
        final ScrollView scrollView = (ScrollView) getActivity().findViewById(R.id.sun_path_info_scroll_view);
        // For some reason scrollTo does not work in main thread.
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.scrollTo(0, scrollPosition);
            }
        });
    }

    /**
     * Save scroll position here, onSaveInstanceState is only called when the activity may be
     * killed by the system. onPause is called every time the fragment is replaced with another.
     */
    @Override
    public void onPause() {
        super.onPause();
        ScrollView scrollView = (ScrollView) getActivity().findViewById(R.id.sun_path_info_scroll_view);
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
     * Fills text views of this fragment with required strings.
     * This can not be done from xml since xml does not support using html markup in strings.
     *
     * @param view The main view of this fragment
     */
    private void fillText(View view) {
        HtmlConverter htmlConverter = new HtmlConverter();
        // Load strings into textViews
        TextView textViewOne = (TextView) (view.findViewById(R.id.sun_path_info_text_view_one));
        textViewOne.setText(htmlConverter.getHtmlForTextView(getString(R.string.sun_path_info_one)));
        textViewOne.setMovementMethod(LinkMovementMethod.getInstance());

        TextView textViewTwo = (TextView) (view.findViewById(R.id.sun_path_info_text_view_two));
        textViewTwo.setText(htmlConverter.getHtmlForTextView(getString(R.string.sun_path_info_two)));
        textViewTwo.setMovementMethod(LinkMovementMethod.getInstance());

        TextView textViewFour = (TextView) (view.findViewById(R.id.sun_path_info_text_view_four));
        textViewFour.setText(htmlConverter.getHtmlForTextView(getString(R.string.sun_path_info_four_schema)));
        textViewFour.setMovementMethod(LinkMovementMethod.getInstance());

        TextView textViewFive = (TextView) (view.findViewById(R.id.sun_path_info_text_view_five));
        textViewFive.setText(htmlConverter.getHtmlForTextView(getString(R.string.sun_path_info_five)));
        textViewFive.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
