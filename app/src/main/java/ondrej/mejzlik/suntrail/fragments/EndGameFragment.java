package ondrej.mejzlik.suntrail.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.game.GameDatabaseHelper;
import ondrej.mejzlik.suntrail.game.PlayerModel;

/**
 * This fragment is shown at the end of the game. It runs the endGame method and then loads data
 * from the database, and shows the user how much credits did he earn and what reward he was given.
 */
public class EndGameFragment extends Fragment {
    private int finalCredits = 0;
    private int rewardResource = 0;

    // todo save cre

    public EndGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_end_game, container, false);
    }

    /**
     * This asynchronous class runs a new thread to run the endGame method in database. This method
     * sells all items the player has and sells the ship. Then updates the database with the final
     * amount of credits and gives the player a reward.
     */
    private class AsyncEndGame extends AsyncTask<Void, Void, PlayerModel> {
        private final Context context;

        AsyncEndGame(Context context) {
            super();
            this.context = context;
        }

        @Override
        protected PlayerModel doInBackground(Void... params) {
            // The database helper is a singleton we always get the same instance it will not
            // cause any concurrent troubles.
            GameDatabaseHelper databaseHelper = GameDatabaseHelper.getInstance(this.context);
            databaseHelper.endGame(getActivity());
            return databaseHelper.getPlayerData();
        }

        @Override
        protected void onPostExecute(PlayerModel player) {
            finalCredits = player.getCredits();
            rewardResource = player.getReward();
        }
    }
}
