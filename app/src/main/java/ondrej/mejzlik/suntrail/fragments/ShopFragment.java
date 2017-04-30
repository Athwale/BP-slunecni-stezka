package ondrej.mejzlik.suntrail.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.activities.GameActivity;
import ondrej.mejzlik.suntrail.activities.MainMenuActivity;
import ondrej.mejzlik.suntrail.game.GameDataHolder;
import ondrej.mejzlik.suntrail.game.GameDatabaseHelper;
import ondrej.mejzlik.suntrail.game.ItemListAdapter;
import ondrej.mejzlik.suntrail.game.ItemModel;
import ondrej.mejzlik.suntrail.game.PlayerModel;
import ondrej.mejzlik.suntrail.game.ShipModel;

import static ondrej.mejzlik.suntrail.activities.MainMenuActivity.SCROLL_POSITION_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_NAME_KEY;

/**
 * This fragment displays the shop. It sends a query to the database in another thread and
 * once the data is retrieved displays them on the screen. While the data is being retrieved all views
 * are hidden and the display shows a loading message.
 */
public class ShopFragment extends Fragment {
    private View mainView = null;
    private Parcelable listViewState = null;

    public ShopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.mainView = inflater.inflate(R.layout.fragment_shop, container, false);
        TextView loadingMessage = (TextView) this.mainView.findViewById(R.id.shop_fragment_text_view_loading_message);
        // If we're being restored from a previous state, restore last known scroll position.
        if (savedInstanceState != null) {
            this.listViewState = savedInstanceState.getParcelable(SCROLL_POSITION_KEY);
        }
        // Add planet name to the title
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(PLANET_NAME_KEY)) {
            TextView title = (TextView) this.mainView.findViewById(R.id.shop_fragment_title);
            title.setText(title.getText() + ": " + getResources().getString(arguments.getInt(PLANET_NAME_KEY)));
        }

        // Hide all views until the query is finished. Database is created because shop can only
        // be entered once the database is prepared.
        loadingMessage.setText(R.string.inventory_loading);
        AsyncGetData databaseQuery = new AsyncGetData(getActivity().getApplicationContext());
        databaseQuery.execute();
        this.displayLoadingMessage(this.mainView, true);

        return mainView;
    }

    /**
     * This method hides all views inside the main linear layout in the inventory and shows
     * a loadingMessage that data is being retrieved.
     *
     * @param view  main view from onCreateView
     * @param state if true, loadingMessage is shown if else contents are shown.
     */
    private void displayLoadingMessage(View view, boolean state) {
        LinearLayout mainLayout = (LinearLayout) view.findViewById(R.id.shop_fragment_linear_layout_main);
        TextView loadingMessage = (TextView) view.findViewById(R.id.shop_fragment_text_view_loading_message);
        for (int i = 0; i < mainLayout.getChildCount(); i++) {
            View element = mainLayout.getChildAt(i);
            if (state) {
                // Hide all views and show loading loadingMessage if true
                loadingMessage.setVisibility(View.VISIBLE);
                element.setVisibility(View.GONE);
            } else {
                // Show all views and hide loading loadingMessage if false
                loadingMessage.setVisibility(View.GONE);
                element.setVisibility(View.VISIBLE);
            }

        }
    }

    /**
     * Save scroll position here, onSaveInstanceState is only called when the activity may be
     * killed by the system. onPause is called every time the fragment is replaced with another.
     */
    @Override
    public void onPause() {
        ListView listView = (ListView) getActivity().findViewById(R.id.shop_fragment_list_view_items);
        // scrollView can be null if the system tries to save position when the user presses home
        // from a fragment opened from this fragment.
        if (listView != null) {
            this.listViewState = listView.onSaveInstanceState();
        }
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Saves the instance when home button is pressed
        ListView listView = (ListView) getActivity().findViewById(R.id.shop_fragment_list_view_items);
        if (listView != null) {
            outState.putParcelable(SCROLL_POSITION_KEY, this.listViewState);
        }
    }

    /**
     * This small class has access to the fragment variables and can set the item list once
     * it is retrieved from database.
     */
    private class AsyncGetData extends AsyncTask<Void, Void, GameDataHolder> {
        private final Context context;

        AsyncGetData(Context context) {
            super();
            this.context = context;
        }

        protected GameDataHolder doInBackground(Void... params) {
            // The database helper is a singleton we always get the same instance it will not
            // cause any concurrent troubles.
            GameDatabaseHelper databaseHelper = GameDatabaseHelper.getInstance(this.context);
            PlayerModel player = databaseHelper.getPlayerData();
            ArrayList<ItemModel> itemsInShop = databaseHelper.getItemsForShop(player.getCurrentPlanet());
            ShipModel ship = databaseHelper.getShipData();

            return new GameDataHolder(itemsInShop, ship, player);
        }

        @Override
        protected void onPostExecute(GameDataHolder result) {
            // This method is called in main thread automatically after finishing the work.
            // Load all the data into the fragment
            final ImageButton shipImage = (ImageButton) mainView.findViewById(R.id.shop_fragment_image_view_ship);
            TextView cargoBay = (TextView) mainView.findViewById(R.id.shop_fragment_cargo_bay_contents);
            TextView credits = (TextView) mainView.findViewById(R.id.shop_fragment_credits_amount);
            TextView shipName = (TextView) mainView.findViewById(R.id.shop_fragment_text_view_ship_name);
            final ListView wares = (ListView) mainView.findViewById(R.id.shop_fragment_list_view_items);

            shipImage.setImageResource(result.getShip().getShipImageResId());
            // Set tag on the image button for the ship info fragment. The tag contains the ship
            // name string resource id.
            shipImage.setTag(result.getShip().getShipNameResId());
            cargoBay.setText(String.valueOf(result.getShip().getRemainingCargoSpace()) + "/" + String.valueOf(result.getShip().getCargoBaySize()));
            // The amount of credits is an int but not a resource id.
            credits.setText(String.valueOf(result.getPlayer().getCredits()));
            shipName.setText(result.getShip().getShipNameResId());
            wares.setAdapter(new ItemListAdapter(getActivity().getApplicationContext(), result.getItems()));
            wares.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    // Start an alpha animation for clicked item
                    Animation rowAnimation = new TranslateAnimation(0, (view.getWidth() / 8), 0, 0);
                    rowAnimation.setDuration(200);
                    view.startAnimation(rowAnimation);
                    rowAnimation.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            // Make the Game or Main menu activity open item info fragment for the
                            // selected item when the animation has finished.
                            // Get the clicked item
                            Adapter adapter = wares.getAdapter();
                            ItemModel item = (ItemModel) adapter.getItem(i);
                            if (getActivity() instanceof GameActivity) {
                                ((GameActivity) getActivity()).openItemInfoFragment(item);
                            } else if (getActivity() instanceof MainMenuActivity) {
                                ((MainMenuActivity) getActivity()).openItemInfoFragment(item);
                            }
                        }

                        @Override
                        public void onAnimationStart(Animation animation) {
                            // Do nothing
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            // Do nothing
                        }
                    });
                }
            });
            // Once the list is filled, move to the last known position
            if (listViewState != null) {
                wares.onRestoreInstanceState(listViewState);
            }
            // Show the data
            displayLoadingMessage(mainView, false);
        }
    }

}
