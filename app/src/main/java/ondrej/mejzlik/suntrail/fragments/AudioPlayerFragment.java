package ondrej.mejzlik.suntrail.fragments;


import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.activities.AllBoardsActivity;
import ondrej.mejzlik.suntrail.activities.ScannerActivity;

import static ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment.ROTATION_END;
import static ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment.ROTATION_KEY_FROM;
import static ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment.ROTATION_KEY_TO;
import static ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment.ROTATION_SPEED;
import static ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment.ROTATION_START;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_HALLEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_JUPITER;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_NEPTUNE;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_SATURN;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_AUDIO_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_ID_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_NAME_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_PHOTO_KEY;

/**
 * This fragment houses an mp3 player which is used to play the planet text in the form of an mp3.
 * The playback is saved when the user leaves the fragment or the fragment is stopped by the system.
 */
public class AudioPlayerFragment extends Fragment {
    private ObjectAnimator animator = null;
    private float newValueFrom = ROTATION_START;
    private float newValueTo = ROTATION_END;
    private MediaPlayer mediaPlayer = null;

    public AudioPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audio_player, container, false);
        this.setContents(view, savedInstanceState);
        this.prepareAudio(view);
        this.setButtonListeners(view);
        return view;
    }

    /**
     * Set planet image and fragment title based on planet ID in arguments.
     *
     * @param view               The main view which is being modified in onCreateView
     * @param savedInstanceState SavedInstanceState from onCreateView
     */
    private void setContents(View view, Bundle savedInstanceState) {
        // Get planet resources
        Bundle resources = getArguments();
        TextView mainTitle = (TextView) (view.findViewById(R.id.audio_player_title));
        ImageView planetPhoto = (ImageView) (view.findViewById(R.id.audio_player_image_view_photo));

        // Resources must contain planet id
        if (resources != null && resources.containsKey(PLANET_ID_KEY)) {
            // Set photo and title
            mainTitle.setText(getResources().getString(resources.getInt(PLANET_NAME_KEY)));
            planetPhoto.setImageResource(resources.getInt(PLANET_PHOTO_KEY));
            this.newValueFrom = resources.getFloat(ROTATION_KEY_FROM);
            this.newValueTo = resources.getFloat(ROTATION_KEY_TO);

            // Create animator and set animation on the main photo to spin the planet around slowly
            // Restore rotation angle
            if (savedInstanceState != null && savedInstanceState.containsKey(ROTATION_KEY_FROM)) {
                this.newValueFrom = savedInstanceState.getFloat(ROTATION_KEY_FROM);
                this.newValueTo = savedInstanceState.getFloat(ROTATION_KEY_TO);
            }
            this.animator = ObjectAnimator.ofFloat(planetPhoto, "rotation", this.newValueTo, this.newValueFrom);
            animator.setDuration(ROTATION_SPEED);
            animator.setRepeatCount(ObjectAnimator.INFINITE);
            animator.setInterpolator(new LinearInterpolator());
            // Do not animate planets which look weird when revolving
            int planetId = resources.getInt(PLANET_ID_KEY);
            if (planetId == PLANET_ID_JUPITER || planetId == PLANET_ID_HALLEY || planetId == PLANET_ID_SATURN || planetId == PLANET_ID_NEPTUNE) {
                return;
            }
            animator.start();
        }
    }

    /**
     * Loads planet audio file into media player. The file is localized from
     * PlanetResourceCollector. Disables play and stop buttons while the file is being loaded.
     *
     * @param view View from onCreateView
     */
    private void prepareAudio(View view) {
        // Hide pause while the player is not playing anything
        LinearLayout pauseLayout = (LinearLayout) (view.findViewById(R.id.audio_player_linear_layout_pause));
        pauseLayout.setVisibility(View.GONE);
        // Disable buttons until the media player is initialized
        Button playButton = (Button) (view.findViewById(R.id.audio_player_button_play));
        playButton.setClickable(false);
        Button stopButton = (Button) (view.findViewById(R.id.audio_player_button_stop));
        stopButton.setClickable(false);

        // Get audio file resource and load player
        Bundle resources = getArguments();
        if (resources != null && resources.containsKey(PLANET_AUDIO_KEY)) {
            this.mediaPlayer = MediaPlayer.create(getActivity(), resources.getInt(PLANET_AUDIO_KEY));
            this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayback();
                }

            });
            this.mediaPlayer.setWakeMode(getActivity(), PowerManager.PARTIAL_WAKE_LOCK);
        }

        // Enable buttons
        playButton.setClickable(true);
        stopButton.setClickable(true);
    }

    /**
     * Sets up button on click listeners. Because we do not want to handle clicks inside the
     * parent activity.
     *
     * @param view View from onCreateView
     */
    private void setButtonListeners(View view) {
        final Button playButton = (Button) (view.findViewById(R.id.audio_player_button_play));
        final Button pauseButton = (Button) (view.findViewById(R.id.audio_player_button_pause));
        final Button stopButton = (Button) (view.findViewById(R.id.audio_player_button_stop));

        playButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio();
            }
        });

        pauseButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausePlayback();
            }
        });

        stopButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlayback();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Release media player
        this.mediaPlayer.release();
        this.mediaPlayer = null;
    }

    /**
     * Start playback enable stop button and make pause button visible.
     */
    private void playAudio() {
        final LinearLayout playLayout = (LinearLayout) (getActivity().findViewById(R.id.audio_player_linear_layout_play));
        final LinearLayout pauseLayout = (LinearLayout) (getActivity().findViewById(R.id.audio_player_linear_layout_pause));
        final Button stopButton = (Button) (getActivity().findViewById(R.id.audio_player_button_stop));

        // Hide play button, start playback and replace it with pause button
        playLayout.setVisibility(View.GONE);
        mediaPlayer.start();
        pauseLayout.setVisibility(View.VISIBLE);
        stopButton.setClickable(true);
    }

    /**
     * Stop the media player, disable stop button and make play button visible.
     */
    private void stopPlayback() {
        final LinearLayout pauseLayout = (LinearLayout) (getActivity().findViewById(R.id.audio_player_linear_layout_pause));
        final LinearLayout playLayout = (LinearLayout) (getActivity().findViewById(R.id.audio_player_linear_layout_play));
        final Button stopButton = (Button) (getActivity().findViewById(R.id.audio_player_button_stop));

        // Hide pause button, stop playback and replace it with play button
        // Recreate media player. Normal stop() method does not work on android 4.4.4
        pauseLayout.setVisibility(View.GONE);
        stopButton.setClickable(false);
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
        this.mediaPlayer = MediaPlayer.create(getActivity(), getArguments().getInt(PLANET_AUDIO_KEY));
        playLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Pause playback and make play button visible
     */
    private void pausePlayback() {
        final LinearLayout pauseLayout = (LinearLayout) (getActivity().findViewById(R.id.audio_player_linear_layout_pause));
        final LinearLayout playLayout = (LinearLayout) (getActivity().findViewById(R.id.audio_player_linear_layout_play));
        // Hide pause button, stop playback and replace it with play button
        if (mediaPlayer.isPlaying()) {
            pauseLayout.setVisibility(View.GONE);
            mediaPlayer.pause();
            playLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Save planet rotation angle on pause. onSaveInstanceState is called when the
        // app is replaced. We need to save it inside a running app.
        Float rotation = (Float) this.animator.getAnimatedValue();
        this.newValueTo = rotation - 360;
        this.newValueFrom = rotation;
        // Pause audio
        this.pausePlayback();

        // Save current rotation into fragment's arguments, when the fragment resumes operation
        // it reads the rotation position in onCreateView.
        getArguments().putFloat(ROTATION_KEY_FROM, this.newValueFrom);
        getArguments().putFloat(ROTATION_KEY_TO, this.newValueTo);

        // Send rotation values to parent activity in order to resume rotation in audio fragment
        if (getActivity() instanceof AllBoardsActivity) {
            ((AllBoardsActivity) getActivity()).saveRotationValue(newValueFrom, newValueTo);
        }
        if (getActivity() instanceof ScannerActivity) {
            ((ScannerActivity) getActivity()).saveRotationValue(newValueFrom, newValueTo);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save planet rotation angle if activity is killed by the system (changing language,...)
        outState.putFloat(ROTATION_KEY_FROM, this.newValueFrom);
        outState.putFloat(ROTATION_KEY_TO, this.newValueTo);
    }
}
