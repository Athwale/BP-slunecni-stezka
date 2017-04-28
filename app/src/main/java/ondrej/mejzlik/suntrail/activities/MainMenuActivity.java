package ondrej.mejzlik.suntrail.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.fragments.EndGameFragment;
import ondrej.mejzlik.suntrail.fragments.InventoryFragment;
import ondrej.mejzlik.suntrail.fragments.ItemInfoFragment;
import ondrej.mejzlik.suntrail.fragments.MainMenuFragment;
import ondrej.mejzlik.suntrail.fragments.ShipInfoFragment;
import ondrej.mejzlik.suntrail.game.ItemModel;

import static ondrej.mejzlik.suntrail.activities.GameActivity.END_GAME_PREFERENCE_KEY;
import static ondrej.mejzlik.suntrail.activities.GameActivity.PREFERENCES_KEY;
import static ondrej.mejzlik.suntrail.activities.GameActivity.SPACESHIP_NAME_KEY;
import static ondrej.mejzlik.suntrail.fragments.ItemInfoFragment.ITEM_KEY;

/**
 * This is the main activity which is launched after after launching the app.
 * Displays main menu.
 */
public class MainMenuActivity extends Activity {
    // Used in fragments to save scroll position
    public static final String SCROLL_POSITION_KEY = "scrollPosition";
    // Used in main menu in infoButtonsHandler to start either general info or game info fragment
    // in InfoScreen activity
    public static final String INFO_BUTTON_INTENT_KEY = "StartFragment";
    public static final int INFO_BUTTON_GENERAL = 1;
    public static final int INFO_BUTTON_GAME = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.main_menu_fragment_container) != null) {

            // If we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            MainMenuFragment mainMenuFragment = new MainMenuFragment();
            // Add the fragment to the fragment_container in FrameLayout
            transaction.add(R.id.main_menu_fragment_container, mainMenuFragment);
            transaction.commit();
        }
    }

    /**
     * Handles clicks from settings button in main menu.
     * Launches a new activity with settings screen.
     *
     * @param view The button that has been clicked
     */
    public void settingsButtonHandler(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Handles clicks from scanner button in main menu.
     * Launches a new activity with scanner screen.
     *
     * @param view The button that has been clicked
     */
    public void scannerButtonHandler(View view) {
        Intent intent = new Intent(this, ScannerActivity.class);
        startActivity(intent);
    }

    /**
     * Handles clicks from all boards button in main menu.
     * Launches a new activity with scanner screen.
     *
     * @param view The button that has been clicked
     */
    public void allBoardsButtonHandler(View view) {
        Intent intent = new Intent(this, AllBoardsActivity.class);
        startActivity(intent);
    }

    /**
     * Handles clicks from info button and game info button in main menu and inventory fragments.
     * Launches a new activity with info screen.
     *
     * @param button The button that has been clicked
     */
    public void infoButtonsHandler(View button) {
        Intent intent = new Intent(this, InfoScreenActivity.class);
        switch (button.getId()) {
            case R.id.main_menu_button_info: {
                intent.putExtra(INFO_BUTTON_INTENT_KEY, INFO_BUTTON_GENERAL);
                break;
            }
            case R.id.main_menu_button_how_to_play: {
                intent.putExtra(INFO_BUTTON_INTENT_KEY, INFO_BUTTON_GAME);
                break;
            }
        }
        startActivity(intent);
    }

    /**
     * Handles clicks from Inventory button in main menu fragment. The fragment it self checks
     * the existence of a game database and in case there is no database, a message saying that
     * game has to be started first is displayed. If game has ended, displays end game fragment.
     *
     * @param view The button that has been clicked
     */
    public void InventoryButtonHandlerManiMenu(View view) {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_KEY, 0);
        boolean gameEnded = preferences.getBoolean(END_GAME_PREFERENCE_KEY, false);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (gameEnded) {
            EndGameFragment endGameFragment = new EndGameFragment();
            transaction.replace(R.id.main_menu_fragment_container, endGameFragment);
        } else {
            InventoryFragment inventoryFragment = new InventoryFragment();
            transaction.replace(R.id.main_menu_fragment_container, inventoryFragment);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Handles clicks from ship info button in inventory fragment.
     * Launches a new fragment with information about the ship.
     * Which ship info is displayed is based on the ship name string resource id.
     *
     * @param view The button that has been clicked
     */
    public void showShipInfoButtonHandler(View view) {
        // The button contains a tag which holds the name of the ship which is then passed to the
        // ship info fragment. The ship info fragment identifies which spaceship info to display by
        // the spaceship name string resource id.
        ImageButton shipButton = (ImageButton) view;

        Bundle spaceshipArguments = new Bundle();
        spaceshipArguments.putInt(SPACESHIP_NAME_KEY, (int) shipButton.getTag());

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Pass space ship name string resource id to fragment
        ShipInfoFragment shipInfoFragment = new ShipInfoFragment();
        shipInfoFragment.setArguments(spaceshipArguments);
        transaction.replace(R.id.main_menu_fragment_container, shipInfoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * This method opens an ItemInfoFragment and passes the item data into it.
     * It is used from the inventory or shop.
     *
     * @param item The game item for which we want to see the info.
     */
    public void openItemInfoFragment(ItemModel item) {
        FragmentManager fragmentManager = getFragmentManager();
        ItemInfoFragment itemInfoFragment = new ItemInfoFragment();

        Bundle arguments = new Bundle();
        arguments.putParcelable(ITEM_KEY, item);
        itemInfoFragment.setArguments(arguments);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_menu_fragment_container, itemInfoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
