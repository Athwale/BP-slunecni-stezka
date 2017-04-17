package ondrej.mejzlik.suntrail.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.activities.GameActivity;
import ondrej.mejzlik.suntrail.game.GameDataHolder;
import ondrej.mejzlik.suntrail.game.GameDatabaseHelper;
import ondrej.mejzlik.suntrail.game.InventoryListAdapter;
import ondrej.mejzlik.suntrail.game.ItemModel;
import ondrej.mejzlik.suntrail.game.PlayerModel;
import ondrej.mejzlik.suntrail.game.ShipModel;

import static ondrej.mejzlik.suntrail.game.GameDatabaseContract.DATABASE_NAME;

/**
 * This fragment displays the inventory. It sends a query to the database in another thread and
 * once the data is retrieved displays is on the screen. While the data is being retrieved all views
 * are hidden and the display shows a message.
 */
public class InventoryFragment extends Fragment {
    private View mainView = null;

    public InventoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.mainView = inflater.inflate(R.layout.fragment_inventory, container, false);
        TextView message = (TextView) this.mainView.findViewById(R.id.inventory_text_view_loading_message);
        // Get data from database only if the database exists. Otherwise show a message, that
        // game has not been started yet.
        File dbFile = this.getActivity().getDatabasePath(DATABASE_NAME);
        if (dbFile.exists()) {
            // Hide all views until the query is finished
            message.setText(R.string.inventory_loading);
            AsyncGetData databaseQuery = new AsyncGetData(getActivity().getApplicationContext());
            databaseQuery.execute();
        } else {
            // Display that game has not been started yet.
            message.setText(R.string.inventory_empty);
        }
        this.displayMessage(this.mainView, true);

        return mainView;
    }

    /**
     * This method hides all views inside the main linear layout in the inventory and shows
     * a message that data is being retrieved.
     *
     * @param view main view from onCreateView
     * @param state if true, message is shown if else contents are shown.
     */
    private void displayMessage(View view, boolean state) {
        LinearLayout mainLayout = (LinearLayout) view.findViewById(R.id.inventory_linear_layout_main);
        TextView message = (TextView) view.findViewById(R.id.inventory_text_view_loading_message);
        for (int i = 0; i < mainLayout.getChildCount(); i++) {
            View element = mainLayout.getChildAt(i);
            if (state) {
                // Hide all views and show loading message if true
                message.setVisibility(View.VISIBLE);
                element.setVisibility(View.GONE);
            } else {
                // Show all views and hide loading message if false
                message.setVisibility(View.GONE);
                element.setVisibility(View.VISIBLE);
            }

        }
    }

    /**
     * This small class has access to the fragment variables and can set the item list once
     * it is retrieved from database.
     */
    private class AsyncGetData extends AsyncTask<Void, Void, GameDataHolder> {
        private Context context;

        AsyncGetData(Context context) {
            super();
            this.context = context;
        }

        protected GameDataHolder doInBackground(Void... params) {
            // The database helper is a singleton we always get the same instance it will not
            // cause any concurrent troubles.
            GameDatabaseHelper databaseHelper = GameDatabaseHelper.getInstance(this.context);
            ArrayList<ItemModel> items = databaseHelper.getBoughtItems();
            PlayerModel player = databaseHelper.getPlayerData();
            ShipModel ship = databaseHelper.getShipData();

            return new GameDataHolder(items, ship, player);
        }

        @Override
        protected void onPostExecute(GameDataHolder result) {
            // This method is called in main thread automatically after finishing the work.
            // Load all the data into the fragment
            ImageButton shipImage = (ImageButton) mainView.findViewById(R.id.inventory_image_view_ship);
            TextView cargoBay = (TextView) mainView.findViewById(R.id.inventory_cargo_bay_contents);
            TextView credits = (TextView) mainView.findViewById(R.id.inventory_credits_amount);
            TextView shipName = (TextView) mainView.findViewById(R.id.inventory_text_view_ship_name);
            final ListView wares = (ListView) mainView.findViewById(R.id.inventory_list_view_items);

            shipImage.setImageResource(result.getShip().getShipImageResId());
            // Set tag on the image button for the ship info fragment. The tag contains the ship
            // name string resource id.
            shipImage.setTag(result.getShip().getShipNameResId());
            cargoBay.setText(String.valueOf(result.getShip().getRemainingCargoSpace()) + "/" + String.valueOf(result.getShip().getCargoBaySize()));
            // The amount of credits is an int but not a resource id.
            credits.setText(String.valueOf(result.getPlayer().getCredits()));
            shipName.setText(result.getShip().getShipNameResId());
            wares.setAdapter(new InventoryListAdapter(getActivity().getApplicationContext(), result.getItems()));
            wares.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // Start an alpha animation for clicked item
                    Animation fadeOutAnimation = new AlphaAnimation(0.3f, 1.0f);
                    fadeOutAnimation.setDuration(400);
                    view.startAnimation(fadeOutAnimation);

                    // Get the clicked item
                    Adapter adapter = wares.getAdapter();
                    ItemModel item = (ItemModel) adapter.getItem(i);

                    // Make the Game activity open item info fragment for the selected item.
                    if (getActivity() instanceof GameActivity) {
                        ((GameActivity) getActivity()).openItemInfoFragment(item);
                    }
                }
            });
            // Show the data
            displayMessage(mainView, false);
        }
    }

}
