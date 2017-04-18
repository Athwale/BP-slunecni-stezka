package ondrej.mejzlik.suntrail.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import ondrej.mejzlik.suntrail.R;

import static ondrej.mejzlik.suntrail.activities.MainMenuActivity.SCROLL_POSITION_KEY;

/**
 * This activity displays a menu of all available boards using fragments.
 * Each menu item opens a selection fragment to either display the text or play it as mp3.
 */
public class BoardsListFragment extends Fragment {
    private int scrollPosition = 0;

    public BoardsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boards_list, container, false);

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
        final ScrollView scrollView = (ScrollView) getActivity().findViewById(R.id.all_boards_scroll_view);
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
        ScrollView scrollView = (ScrollView) getActivity().findViewById(R.id.all_boards_scroll_view);
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

}
