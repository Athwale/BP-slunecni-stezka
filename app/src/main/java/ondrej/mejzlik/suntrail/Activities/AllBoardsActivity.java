package ondrej.mejzlik.suntrail.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import ondrej.mejzlik.suntrail.Fragments.BoardsListFragment;
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
}
