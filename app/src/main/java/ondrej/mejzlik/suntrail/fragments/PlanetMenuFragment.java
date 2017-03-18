package ondrej.mejzlik.suntrail.fragments;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.activities.AllBoardsActivity;
import ondrej.mejzlik.suntrail.activities.ScannerActivity;
import ondrej.mejzlik.suntrail.utilities.ParametrizedToastOnClickListener;

import static ondrej.mejzlik.suntrail.activities.ScannerActivity.SHOW_GAME_BUTTON;
import static ondrej.mejzlik.suntrail.activities.ScannerActivity.SHOW_GAME_BUTTON_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_HALLEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_JUPITER;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_NEPTUNE;
import static ondrej.mejzlik.suntrail.utilities.PlanetIdentifier.PLANET_ID_SATURN;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_ID_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_NAME_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_PHOTO_AUTHOR_KEY;
import static ondrej.mejzlik.suntrail.utilities.PlanetResourceCollector.PLANET_PHOTO_KEY;


/**
 * This fragment displays a menu after selecting a planet from all boards menu.
 * In this menu the user can select whether to view the text or play it as mp3.
 */
public class PlanetMenuFragment extends Fragment {
    public static final String ROTATION_KEY_FROM = "rotationKeyFrom";
    public static final String ROTATION_KEY_TO = "rotationKeyTo";
    public static final int ROTATION_SPEED = 100000;
    public static final int ROTATION_START = 0;
    public static final int ROTATION_END = 360;
    private ObjectAnimator animator;
    private float newValueFrom = 0;
    private float newValueTo = 0;

    public PlanetMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planet_menu, container, false);
        // Set planet image, title and author based on planet resources in arguments.
        this.setContents(view, savedInstanceState);
        return view;
    }

    /**
     * Set planet photo and fragment title based on planet ID in arguments.
     *
     * @param view               The main view which is being modified in onCreateView
     * @param savedInstanceState SavedInstanceState from onCreateView
     */
    private void setContents(View view, Bundle savedInstanceState) {
        // Get planet resources
        Bundle resources = getArguments();
        TextView mainTitle = (TextView) (view.findViewById(R.id.planet_menu_title));
        ImageView planetPhoto = (ImageView) (view.findViewById(R.id.planet_menu_image_view_photo));
        // This variable will be assigned a value of who shot the planet photo and then is used
        // To set author toast.
        String author;

        // Resources must contain planet id
        if (resources != null && resources.containsKey(PLANET_ID_KEY)) {
            // Set photo and texts
            // Set new title, photo and author
            mainTitle.setText(getResources().getString(resources.getInt(PLANET_NAME_KEY)));
            planetPhoto.setImageResource(resources.getInt(PLANET_PHOTO_KEY));
            author = resources.getString(PLANET_PHOTO_AUTHOR_KEY);
            this.newValueFrom = resources.getFloat(ROTATION_KEY_FROM);
            this.newValueTo = resources.getFloat(ROTATION_KEY_TO);

            // Make game mode button visible if we started this fragment from scanner.
            Button gameButton = (Button) (view.findViewById(R.id.planet_menu_button_play_game));
            if (resources.containsKey(SHOW_GAME_BUTTON_KEY)) {
                if (resources.getString(SHOW_GAME_BUTTON_KEY).equals(SHOW_GAME_BUTTON)) {
                    gameButton.setVisibility(View.VISIBLE);
                }
            } else {
                gameButton.setVisibility(View.GONE);
            }
            // Set on click listener once for photo to show author
            ParametrizedToastOnClickListener listener = new ParametrizedToastOnClickListener();
            listener.setToast(author, getActivity(), Toast.LENGTH_SHORT);
            planetPhoto.setOnClickListener(listener);
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
