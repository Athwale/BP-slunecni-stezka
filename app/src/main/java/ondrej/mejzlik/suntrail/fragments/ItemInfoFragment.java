package ondrej.mejzlik.suntrail.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.game.ItemModel;
import ondrej.mejzlik.suntrail.utilities.HtmlConverter;

/**
 * This fragment displays information about a given game item. It shows the picture, description
 * and destination. The resource id of these information is passed in arguments.
 */
public class ItemInfoFragment extends Fragment {
    public static final String ITEM_KEY = "itemKey";

    public ItemInfoFragment() {
        // Required empty public constructor
    }


    // TODO REMEMBER POSITION
    // TODO REMEMBER LIST POSITION IN INVENTORY
    // TODO CHANGE FRAGMENT AFTER ANIMATION
    // TODO MAKE LARGE VERSION OF INFO LAYOUT


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_info, container, false);
        this.setContents(view);
        return view;
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
