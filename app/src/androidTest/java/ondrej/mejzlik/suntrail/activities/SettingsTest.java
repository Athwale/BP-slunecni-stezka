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
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ondrej.mejzlik.suntrail.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SettingsTest {

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
    public void settingsTest() {
        ViewInteraction button = onView(
                allOf(withId(R.id.main_menu_button_settings), withText("Settings"),
                        withParent(allOf(withId(R.id.main_menu_activity_relative_layout),
                                withParent(withId(R.id.main_menu_fragment_container)))),
                        isDisplayed()));
        button.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.settings_text_view_title), withText("Settings"),
                        childAtPosition(
                                allOf(withId(R.id.settings_activity_relative_layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Settings")));

        ViewInteraction view = onView(
                allOf(withId(R.id.settings_view_spacer),
                        childAtPosition(
                                allOf(withId(R.id.settings_activity_relative_layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        view.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.settings_text_view_data_info), withText("Game has not been started"),
                        childAtPosition(
                                allOf(withId(R.id.settings_activity_relative_layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        textView2.check(matches(withText("Game has not been started")));

        ViewInteraction checkBox = onView(
                allOf(withId(R.id.settings_checkbox_clear_data),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.linearLayout4),
                                        0),
                                0),
                        isDisplayed()));
        checkBox.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.settings_button_clear_data),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.linearLayout4),
                                        1),
                                0),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction checkBox2 = onView(
                allOf(withId(R.id.settings_checkbox_use_flash),
                        childAtPosition(
                                allOf(withId(R.id.settings_activity_relative_layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        checkBox2.check(matches(isDisplayed()));

        ViewInteraction relativeLayout = onView(
                allOf(withId(R.id.settings_activity_relative_layout),
                        childAtPosition(
                                allOf(withId(android.R.id.content),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                0),
                        isDisplayed()));
        relativeLayout.check(matches(isDisplayed()));

    }
}
