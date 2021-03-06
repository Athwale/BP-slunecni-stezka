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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainMenuActivityTest {

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
    public void mainMenuActivityTest() {
        ViewInteraction button = onView(
                allOf(withId(R.id.main_menu_button_how_to_play),
                        childAtPosition(
                                allOf(withId(R.id.main_menu_linear_layout_how_to_play),
                                        childAtPosition(
                                                withId(R.id.main_menu_linear_layout_top),
                                                0)),
                                0),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.main_menu_button_all_boards),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_menu_linear_layout_top),
                                        1),
                                0),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction button3 = onView(
                allOf(withId(R.id.main_menu_button_info),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_menu_linear_layout_top),
                                        2),
                                0),
                        isDisplayed()));
        button3.check(matches(isDisplayed()));

        ViewInteraction button4 = onView(
                allOf(withId(R.id.main_menu_button_scan),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout3),
                                        childAtPosition(
                                                withId(R.id.main_menu_activity_relative_layout),
                                                3)),
                                0),
                        isDisplayed()));
        button4.check(matches(isDisplayed()));

        ViewInteraction button5 = onView(
                allOf(withId(R.id.main_menu_button_inventory),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout2),
                                        childAtPosition(
                                                withId(R.id.main_menu_activity_relative_layout),
                                                4)),
                                0),
                        isDisplayed()));
        button5.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.main_menu_text_view_no_scanner), withText("Scan code from a board"),
                        childAtPosition(
                                allOf(withId(R.id.main_menu_activity_relative_layout),
                                        childAtPosition(
                                                withId(R.id.main_menu_fragment_container),
                                                0)),
                                5),
                        isDisplayed()));
        textView.check(matches(withText("Scan code from a board")));

        ViewInteraction button6 = onView(
                allOf(withId(R.id.main_menu_button_settings),
                        childAtPosition(
                                allOf(withId(R.id.main_menu_activity_relative_layout),
                                        childAtPosition(
                                                withId(R.id.main_menu_fragment_container),
                                                0)),
                                6),
                        isDisplayed()));
        button6.check(matches(isDisplayed()));

    }
}
