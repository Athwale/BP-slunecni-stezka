package ondrej.mejzlik.suntrail.fragments;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.game.GameDatabaseHelper;
import ondrej.mejzlik.suntrail.game.PlayerModel;

import static ondrej.mejzlik.suntrail.activities.GameActivity.END_GAME_PREFERENCE_KEY;
import static ondrej.mejzlik.suntrail.activities.GameActivity.PREFERENCES_KEY;

/**
 * This fragment is shown at the end of the game. It runs the endGame method and then loads data
 * from the database, and shows the user how much credits did he earn and what reward he was given.
 */
public class EndGameFragment extends Fragment {
    private TextView finalCredits = null;
    private TextView rewardDescription = null;
    private LinearLayout score = null;
    private ImageView clearReward = null;
    private ImageView blurryReward = null;
    private ObjectAnimator scoreSlideIn = null;
    private ObjectAnimator clearRewardFadeIn = null;
    private ObjectAnimator blurryRewardFadeOut = null;
    private ObjectAnimator rewardDescriptionFadeIn = null;
    private MediaPlayer player = null;

    public EndGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_end_game, container, false);
        this.finalCredits = (TextView) view.findViewById(R.id.end_game_final_credits);
        this.rewardDescription = (TextView) view.findViewById(R.id.end_game_text_bottom);
        this.score = (LinearLayout) view.findViewById(R.id.end_game_linear_layout_score);
        this.clearReward = (ImageView) view.findViewById(R.id.end_game_image_view_reward_clear);
        this.blurryReward = (ImageView) view.findViewById(R.id.end_game_image_view_reward_blurry);
        // Prepare animations
        this.prepareAnimations();
        // Load results and then play animations.
        AsyncEndGame endGame = new AsyncEndGame(getActivity());
        endGame.execute();
        return view;
    }

    @Override
    public void onPause() {
        this.stopSound();
        super.onPause();
    }

    /**
     * Sets up the fade in animations.
     */
    private void prepareAnimations() {
        this.score.setVisibility(View.INVISIBLE);
        this.clearReward.setVisibility(View.GONE);
        this.rewardDescription.setVisibility(View.GONE);

        // Get screen width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        this.scoreSlideIn = ObjectAnimator.ofFloat(this.score, "translationX", -(width * 2), 0);
        this.scoreSlideIn.setInterpolator(new DecelerateInterpolator());
        this.scoreSlideIn.setDuration(1000);

        this.clearRewardFadeIn = ObjectAnimator.ofFloat(this.clearReward, "alpha", 0f, 1f);
        this.clearRewardFadeIn.setDuration(1000);

        this.blurryRewardFadeOut = ObjectAnimator.ofFloat(this.blurryReward, "alpha", 1f, 0f);
        this.blurryRewardFadeOut.setDuration(1000);

        this.rewardDescriptionFadeIn = ObjectAnimator.ofFloat(this.rewardDescription, "alpha", 0f, 1f);
        this.rewardDescriptionFadeIn.setDuration(1000);
    }

    private void playSound() {
        this.stopSound();
        this.player = MediaPlayer.create(getActivity(), R.raw.sound_you_won);
        this.player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopSound();
            }
        });
        this.player.start();
    }

    /**
     * Stops playback and destroys the player.
     */
    private void stopSound() {
        if (this.player != null) {
            if (this.player.isPlaying()) {
                this.player.stop();
            }
            this.player.release();
            this.player = null;
        }
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
            // End the game and save it to preferences that disables game button and opens end
            // game fragment from main menu inventory button. This also prevents recalculation of
            // game results. This has to be done here after the game results were calculated.
            SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences(PREFERENCES_KEY, 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(END_GAME_PREFERENCE_KEY, true).apply();
            // Set texts
            finalCredits.setText(String.valueOf(player.getCredits()));
            rewardDescription.setText(player.getReward());

            // Set correct medal
            switch (player.getReward()) {
                case R.string.end_game_first_reward: {
                    clearReward.setImageResource(R.drawable.pict_award_first);
                    break;
                }
                case R.string.end_game_second_reward: {
                    clearReward.setImageResource(R.drawable.pict_award_second);
                    break;
                }
                default: {
                    clearReward.setImageResource(R.drawable.pict_award_third);
                    break;
                }
            }

            playSound();
            // Start playing the fade in/out animation and then make it visible.
            scoreSlideIn.start();
            clearRewardFadeIn.start();
            blurryRewardFadeOut.start();
            rewardDescriptionFadeIn.start();
            // Make the view visible
            rewardDescription.setVisibility(View.VISIBLE);
            score.setVisibility(View.VISIBLE);
            clearReward.setVisibility(View.VISIBLE);
        }
    }
}
