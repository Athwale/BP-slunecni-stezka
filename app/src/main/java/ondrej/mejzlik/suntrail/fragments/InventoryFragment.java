package ondrej.mejzlik.suntrail.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.game.GameDataHolder;
import ondrej.mejzlik.suntrail.game.GameDatabaseHelper;
import ondrej.mejzlik.suntrail.game.InventoryListAdapter;
import ondrej.mejzlik.suntrail.game.ItemModel;
import ondrej.mejzlik.suntrail.game.PlayerModel;
import ondrej.mejzlik.suntrail.game.ShipModel;

/**
 * A simple {@link Fragment} subclass.
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

        // Get data from database
        AsyncGetData databaseQuery = new AsyncGetData(getActivity().getApplicationContext());
        databaseQuery.execute();

        return mainView;
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
            ListView wares = (ListView) mainView.findViewById(R.id.inventory_list_view_items);

            shipImage.setImageResource(result.getShip().getShipImageResId());
            cargoBay.setText(String.valueOf(result.getShip().getRemainingCargoSpace()) + "/" + String.valueOf(result.getShip().getCargoBaySize()));
            // The amount of credits is an int but not a resource id.
            credits.setText(String.valueOf(result.getPlayer().getCredits()));
            shipName.setText(result.getShip().getShipNameResId());
            wares.setAdapter(new InventoryListAdapter(getActivity().getApplicationContext(), result.getItems()));
        }
    }

}
