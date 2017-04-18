package ondrej.mejzlik.suntrail.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.game.ItemModel;
import ondrej.mejzlik.suntrail.utilities.HtmlConverter;

import static ondrej.mejzlik.suntrail.activities.MainMenuActivity.SCROLL_POSITION_KEY;

/**
 * This fragment displays information about a given game item. It shows the picture, description
 * and destination. The resource id of these information is passed in arguments.
 */
public class ItemInfoFragment extends Fragment {
    public static final String ITEM_KEY = "itemKey";
    private int scrollPosition = 0;

    public ItemInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_info, container, false);
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
        final ScrollView scrollView = (ScrollView) getActivity().findViewById(R.id.item_info_scroll_view_main);
        // For some reason scrollTo does not work in main thread.
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.scrollTo(0, scrollPosition);
                ;
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
        ScrollView scrollView = (ScrollView) getActivity().findViewById(R.id.item_info_scroll_view_main);
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

    // Suppress warning that we should not use + to build strings
    @SuppressLint("SetTextI18n")
    private void setContents(View view) {
        HtmlConverter converter = new HtmlConverter();
        Bundle arguments = this.getArguments();
        if (arguments != null) {
            // Get the item model filled with data
            ItemModel item = arguments.getParcelable(ITEM_KEY);

            ImageView itemImage = (ImageView) view.findViewById(R.id.item_info_image_view_item);
            TextView itemName = (TextView) view.findViewById(R.id.item_info_text_view_item_name);
            TextView itemDescription = (TextView) view.findViewById(R.id.item_info_text_view_top);
            ImageView imagePriceMovement = (ImageView) view.findViewById(R.id.item_info_image_view_price_movement);
            TextView itemSize = (TextView) view.findViewById(R.id.item_info_text_view_cargo_size);
            TextView itemPriceMovementText = (TextView) view.findViewById(R.id.item_info_text_view_price_movement);

            if (item != null) {
                itemImage.setImageResource(item.getItemImageResId());
                itemName.setText(item.getItemNameResId());
                itemDescription.setText(converter.getHtmlForTextView(getString(item.getItemDescriptionResId())));
                if (item.getPriceMovement()) {
                    imagePriceMovement.setImageResource(R.drawable.pict_arrow_green);
                    itemPriceMovementText.setText(R.string.item_info_price_rise);
                } else {
                    imagePriceMovement.setImageResource(R.drawable.pict_arrow_red);
                    itemPriceMovementText.setText(R.string.item_info_price_decrease);
                }
                itemSize.setText(" " + String.valueOf(item.getSize()));
            }
        }
    }
}
