package ondrej.mejzlik.suntrail.activities;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ondrej.mejzlik.suntrail.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class HowToPlayTest {

    @Rule
    public ActivityTestRule<MainMenuActivity> mActivityTestRule = new ActivityTestRule<>(MainMenuActivity.class);

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @Test
    public void howToPlayTest() {
        ViewInteraction button = onView(
                allOf(withId(R.id.main_menu_button_how_to_play), withText("How to play"),
                        withParent(allOf(withId(R.id.main_menu_linear_layout_how_to_play),
                                withParent(withId(R.id.main_menu_linear_layout_top)))),
                        isDisplayed()));
        button.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.game_info_text_view_top), withText("You can start the game by scanning an NFC tag or a QR code off a board on the Sun trail with a scanner from this app. The game can be started at any board except the first general information board. \n\nIn the game mode you can buy and sell items at each stop along the trail. You earn credits for trading these goods.\nBefore you finish the game at the last stop, try to earn as much as you can. \n\nIn the beginning you will be given a basic space ship. A trustworthy Ikarus M and a small amount of space credits Which are used as money. \n\nAt each stop you must scan the code of the planet. Then you can enter the shop and trade items."),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.game_info_scroll_view),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("You can start the game by scanning an NFC tag or a QR code off a board on the Sun trail with a scanner from this app. The game can be started at any board except the first general information board.   In the game mode you can buy and sell items at each stop along the trail. You earn credits for trading these goods. Before you finish the game at the last stop, try to earn as much as you can.   In the beginning you will be given a basic space ship. A trustworthy Ikarus M and a small amount of space credits Which are used as money.   At each stop you must scan the code of the planet. Then you can enter the shop and trade items.")));

        ViewInteraction imageView = onView(
                allOf(withId(R.id.game_info_image_view_infographic), withContentDescription("Cartoon image displaying game play"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.game_info_scroll_view),
                                        0),
                                1),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.game_info_text_view_bottom), withText("Items can be sold at on planet. The price of each ware is not fixed, it may rise or fall on the next planet. But the price will only move on a not yet visited planet. \n\nDuring the game you will have a chance to buy better, larger space ships. Those will allow you to transport larger amount of goods. The old ship will be sold when buying a new one. \n\nBased on how much credits you earn at the end of the game, you will be rewarded with a space fleet officer rank. \n\nHave fun."),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.game_info_scroll_view),
                                        0),
                                2),
                        isDisplayed()));
        textView2.check(matches(withText("Items can be sold at on planet. The price of each ware is not fixed, it may rise or fall on the next planet. But the price will only move on a not yet visited planet.   During the game you will have a chance to buy better, larger space ships. Those will allow you to transport larger amount of goods. The old ship will be sold when buying a new one.   Based on how much credits you earn at the end of the game, you will be rewarded with a space fleet officer rank.   Have fun.")));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.game_info_button_scanner),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.game_info_scroll_view),
                                        0),
                                3),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

    }
}
