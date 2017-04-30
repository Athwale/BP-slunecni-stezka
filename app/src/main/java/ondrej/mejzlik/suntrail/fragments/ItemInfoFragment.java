package ondrej.mejzlik.suntrail.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.game.GameDatabaseHelper;
import ondrej.mejzlik.suntrail.game.ItemModel;
import ondrej.mejzlik.suntrail.game.ShipModel;
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

        // Set onKeyListener to be able to handle back click from the fragment it self.
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });

        Bundle arguments = this.getArguments();
        if (arguments != null) {
            // Get the item model filled with data
            final ItemModel item = arguments.getParcelable(ITEM_KEY);

            ImageView itemImage = (ImageView) view.findViewById(R.id.item_info_image_view_item);
            TextView itemName = (TextView) view.findViewById(R.id.item_info_text_view_item_name);
            TextView itemDescription = (TextView) view.findViewById(R.id.item_info_text_view_top);
            ImageView imagePriceMovement = (ImageView) view.findViewById(R.id.item_info_image_view_price_movement);
            TextView itemSize = (TextView) view.findViewById(R.id.item_info_text_view_cargo_size);
            TextView itemPriceMovementText = (TextView) view.findViewById(R.id.item_info_text_view_price_movement);
            TextView itemPriceTitle = (TextView) view.findViewById(R.id.item_info_text_view_price_title);
            TextView itemSizeTitle = (TextView) view.findViewById(R.id.item_info_text_view_cargo_size_title);
            TextView itemPrice = (TextView) view.findViewById(R.id.item_info_text_view_item_price);
            TextView itemPriceCr = (TextView) view.findViewById(R.id.item_info_text_view_item_price_cr);
            TextView message = (TextView) view.findViewById(R.id.item_info_text_view_cant_be_bought);
            LinearLayout itemBuy = (LinearLayout) view.findViewById(R.id.item_info_linear_layout_buy);
            LinearLayout itemSell = (LinearLayout) view.findViewById(R.id.item_info_linear_layout_sell);
            final Button buyButton = (Button) view.findViewById(R.id.item_info_button_buy);
            Button sellButton = (Button) view.findViewById(R.id.item_info_button_sell);

            buyButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item != null && item.isShip()) {
                        buyShip();
                    } else {
                        itemBuyOrSell(v);
                    }
                }
            });

            sellButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemBuyOrSell(v);
                }
            });

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
                itemPrice.setText(" " + String.valueOf(item.getPrice()));
                if (item.isInShop()) {
                    // Set price and enable sell/buy.
                    itemPriceTitle.setText(R.string.list_price);
                    itemSizeTitle.setText(R.string.list_size);
                    itemPriceTitle.setVisibility(View.VISIBLE);
                    itemPriceCr.setVisibility(View.VISIBLE);
                    if (item.canBeBought()) {
                        // Display price and show buy button.
                        itemPrice.setText(" " + String.valueOf(item.getPrice()));
                        itemBuy.setVisibility(View.VISIBLE);
                        itemSell.setVisibility(View.GONE);
                        message.setVisibility(View.GONE);
                    } else if (item.isBought() && item.isSaleable()) {
                        // Display price and sell button.
                        itemPrice.setText(" " + String.valueOf(item.getPrice()));
                        itemPriceTitle.setText(R.string.list_selling_price);
                        itemBuy.setVisibility(View.GONE);
                        itemSell.setVisibility(View.VISIBLE);
                        message.setVisibility(View.GONE);
                    } else if (!item.isSaleable()) {
                        // We bough the item in this shop and must not be able to sell it back
                        itemPrice.setText(" " + String.valueOf(item.getPrice()));
                        itemPriceTitle.setText(R.string.list_selling_price);
                        itemBuy.setVisibility(View.GONE);
                        itemSell.setVisibility(View.GONE);
                        message.setText(R.string.shop_can_not_sell);
                        message.setVisibility(View.VISIBLE);
                    } else {
                        // Item can not be bought, not enough credits or cargo space
                        itemPrice.setText(" " + String.valueOf(item.getPrice()));
                        itemBuy.setVisibility(View.GONE);
                        itemSell.setVisibility(View.GONE);
                        message.setText(R.string.shop_can_not_be_bought);
                        message.setVisibility(View.VISIBLE);
                    }
                } else if (item.isShip()) {
                    // The item is a special item which is a spaceship.
                    if (item.canBeBought()) {
                        itemBuy.setVisibility(View.VISIBLE);
                        message.setVisibility(View.GONE);
                    } else {
                        itemBuy.setVisibility(View.GONE);
                        message.setText(R.string.shop_can_not_be_bought);
                        message.setVisibility(View.VISIBLE);
                    }
                    imagePriceMovement.setVisibility(View.GONE);
                    itemSizeTitle.setText(R.string.ship_info_cargo);
                    itemPriceMovementText.setVisibility(View.GONE);
                    message.setVisibility(View.GONE);
                    itemSell.setVisibility(View.GONE);
                } else {
                    // We are in inventory, hide all shop related stuff
                    itemSell.setVisibility(View.GONE);
                    itemBuy.setVisibility(View.GONE);
                    message.setVisibility(View.GONE);
                    itemPriceTitle.setText(R.string.list_selling_price);
                }
            }
        }
    }

    /**
     * This method handles buy and sell buttons. It updates the database and closes the fragment as
     * soon as the change is done while displaying a message. This change is finished quickly and
     * does not have to be done in a separate thread.
     *
     * @param view The button that was clicked.
     */
    private void itemBuyOrSell(View view) {
        Button button = (Button) view;
        GameDatabaseHelper databaseHelper = GameDatabaseHelper.getInstance(getActivity());
        // Get the item row id in database
        Bundle arguments = this.getArguments();
        ItemModel item = arguments.getParcelable(ITEM_KEY);
        if (item != null) {
            switch (button.getId()) {
                case R.id.item_info_button_buy: {
                    Toast.makeText(getActivity(), getResources().getString(R.string.shop_buying_item), Toast.LENGTH_SHORT).show();
                    databaseHelper.buySellItem(item, true);
                    break;
                }
                case R.id.item_info_button_sell: {
                    Toast.makeText(getActivity(), getResources().getString(R.string.shop_selling_item), Toast.LENGTH_SHORT).show();
                    databaseHelper.buySellItem(item, false);
                    break;
                }
            }
        }
        // Close this fragment as soon as the item is bought.
        getFragmentManager().popBackStackImmediate();
    }

    /**
     * This method updates the database and changes the player's ship. Then closes the fragment as
     * soon as the change is done while displaying a message that a purchase is being made. This
     * change is finished quickly and does not have to be done in a separate thread.
     */
    private void buyShip() {
        GameDatabaseHelper databaseHelper = GameDatabaseHelper.getInstance(getActivity());
        // Get the item row id in database
        Bundle arguments = this.getArguments();
        ItemModel item = arguments.getParcelable(ITEM_KEY);
        if (item != null) {
            Toast.makeText(getActivity(), getResources().getString(R.string.shop_buying_item), Toast.LENGTH_SHORT).show();
            ShipModel ship = new ShipModel(500, item.getItemImageResId(), item.getItemNameResId(), item.getPrice(), item.getSize(), item.getSize());
            databaseHelper.buyShip(ship);
        }
        // Close this fragment as soon as the item is bought.
        getFragmentManager().popBackStackImmediate();
    }
}
