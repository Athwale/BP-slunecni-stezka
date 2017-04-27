package ondrej.mejzlik.suntrail.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.utilities.HtmlConverter;

import static ondrej.mejzlik.suntrail.activities.MainMenuActivity.SCROLL_POSITION_KEY;

/**
 * This fragment appears when the user clicks the game button for the first time.
 * The player is informed that he was given a ship and a small amount of credits to start with.
 * When the confirm button is pressed a new database and game data are created.
 */
public class ReceiveShipFragment extends Fragment {
    private int scrollPosition = 0;

    public ReceiveShipFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_receive_ship, container, false);
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
        final ScrollView scrollView = (ScrollView) getActivity().findViewById(R.id.receive_ship_scroll_view_main);
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
        ScrollView scrollView = (ScrollView) getActivity().findViewById(R.id.receive_ship_scroll_view_main);
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
    private void setContents(View view) {
        HtmlConverter htmlConverter = new HtmlConverter();
        ImageButton shipButton = (ImageButton) (view.findViewById(R.id.receive_ship_image_button_ship));
        TextView textViewTop = (TextView) (view.findViewById(R.id.receive_ship_text_view_top));
        TextView textViewBottom = (TextView) (view.findViewById(R.id.receive_ship_text_view_bottom));
        // Set a tag to the ship image button containing the ship name string resource id for the
        // ship info fragment.
        shipButton.setTag(R.string.ship_name_icarus);
        textViewTop.setText(htmlConverter.getHtmlForTextView(getString(R.string.start_new_game_text_top)));
        textViewBottom.setText(htmlConverter.getHtmlForTextView(getString(R.string.start_new_game_text_bottom)));

    }
}
