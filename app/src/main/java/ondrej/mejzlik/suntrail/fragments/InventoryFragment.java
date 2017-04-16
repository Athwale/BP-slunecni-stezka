package ondrej.mejzlik.suntrail.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.game.GameDataHolder;
import ondrej.mejzlik.suntrail.game.GameDatabaseHelper;
import ondrej.mejzlik.suntrail.game.ItemModel;
import ondrej.mejzlik.suntrail.game.PlayerModel;
import ondrej.mejzlik.suntrail.game.ShipModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class InventoryFragment extends Fragment {
    private ArrayList<ItemModel> boughtItems = null;
    private ShipModel ship = null;
    private PlayerModel playerData = null;

    public InventoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        // Get data from database
        AsyncGetData databaseQuery = new AsyncGetData(getActivity().getApplicationContext());
        databaseQuery.execute();

        return view;
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

        }
    }

}
