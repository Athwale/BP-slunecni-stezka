package ondrej.mejzlik.suntrail.fragments;


import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.activities.AllBoardsActivity;
import ondrej.mejzlik.suntrail.activities.ScannerActivity;

import static ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment.ROTATION_KEY_FROM;
import static ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment.ROTATION_KEY_TO;
import static ondrej.mejzlik.suntrail.fragments.PlanetMenuFragment.ROTATION_SPEED;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_HALLEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_JUPITER;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_SATURN;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_ID_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_NAME_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_PHOTO_KEY;

/**
 * This fragment houses an mp3 player which is used to play the planet text in the form of an mp3.
 * The playback is saved when the user leaves the fragment or the fragment is stopped by the system.
 */
public class AudioPlayerFragment extends Fragment {
    private ObjectAnimator animator;
    private float newValueFrom = 0;
    private float newValueTo = 0;


    public AudioPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audio_player, container, false);
        this.setContents(view, savedInstanceState);
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
            mainTitle.setText(resources.getString(PLANET_NAME_KEY));
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
            if (planetId == PLANET_ID_JUPITER || planetId == PLANET_ID_HALLEY || planetId == PLANET_ID_SATURN) {
                return;
            }
            animator.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Save planet rotation angle on pause. onSaveInstanceState is called when the
        // app is replaced. We need to save it inide a running app.
        Float rotation = (Float) this.animator.getAnimatedValue();
        this.newValueTo = rotation - 360;
        this.newValueFrom = rotation;

        // Save current rotation into fragment's arguments, when the fragment resumes operation
        // it reads the rotation position in oncreateview.
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
