package ondrej.mejzlik.suntrail.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import ondrej.mejzlik.suntrail.fragments.BoardsListFragment;
import ondrej.mejzlik.suntrail.R;

public class AllBoardsActivity extends Activity {
    private BoardsListFragment boardsListFragment = new BoardsListFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_boards);

        if (findViewById(R.id.all_boards_fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            // Add the fragment to the fragment_container
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.all_boards_fragment_container, boardsListFragment);
            transaction.commit();
        }
    }

    /**
     * Handles clicks from buttons in all boards list.
     * Launches a new fragment corresponding to selected button.
     *
     * @param planetButton The button that has been clicked
     */
    public void planetButtonsHandler(View planetButton) {
        switch (planetButton.getId()) {
            case R.id.all_boards_button_ceres:
            {
                break;
            }
            case R.id.all_boards_button_earth:
            {
                break;
            }
            case R.id.all_boards_button_halley:
            {
                break;
            }
            case R.id.all_boards_button_jupiter:
            {
                break;
            }
            case R.id.all_boards_button_mars:
            {
                break;
            }
            case R.id.all_boards_button_mercury:
            {
                break;
            }
            case R.id.all_boards_button_moon:
            {
                break;
            }
            case R.id.all_boards_button_neptune:
            {
                break;
            }
            case R.id.all_boards_button_saturn:
            {
                break;
            }
            case R.id.all_boards_button_sun:
            {
                break;
            }
            case R.id.all_boards_button_uranus:
            {
                break;
            }
            case R.id.all_boards_button_venus:
            {
                break;
            }
        }
    }
}
