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
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BasicInfoTest {

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
    public void basicInfoTest() {
        ViewInteraction button = onView(
                allOf(withId(R.id.main_menu_button_info), withText("Help"), isDisplayed()));
        button.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.sun_path_info_title), withText("Basic information"),
                        childAtPosition(
                                allOf(withId(R.id.sun_path_info_fragment_relative_layout),
                                        childAtPosition(
                                                withId(R.id.info_screen_fragment_container),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Basic information")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.sun_path_info_text_view_one), withText("The Sun trail\n\nThe Sun trail of Křtiny is a model of our Solar system scaled to 1:1 milliard. All models of planets and the Sun and the distances you have to walk on the trail are also scaled as written above. \n\nAt each station you will find a sphere representing the given planet. You can read the information about the planet on the nearby board. \n\nOur Sun trail was inspired by . Planetary trail build by the observatory in Hradec Králové, but it is not just a plain copy. The realization of this trail was originally supposed to be self aided. But thanks to the support of ŠLP Křtiny and the South moravian district it was possible to build a more elaborate version of this trail. The main part was being built in the year 2014 and opened int the year 2015. \n\nIf you follow this trail you will be able to try on your own how close or far away the planets in our Solar system are. At the same time you will experience the nice landscape of Křtiny."),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sun_path_info_scroll_view),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("The Sun trail  The Sun trail of Křtiny is a model of our Solar system scaled to 1:1 milliard. All models of planets and the Sun and the distances you have to walk on the trail are also scaled as written above.   At each station you will find a sphere representing the given planet. You can read the information about the planet on the nearby board.   Our Sun trail was inspired by . Planetary trail build by the observatory in Hradec Králové, but it is not just a plain copy. The realization of this trail was originally supposed to be self aided. But thanks to the support of ŠLP Křtiny and the South moravian district it was possible to build a more elaborate version of this trail. The main part was being built in the year 2014 and opened int the year 2015.   If you follow this trail you will be able to try on your own how close or far away the planets in our Solar system are. At the same time you will experience the nice landscape of Křtiny.")));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.sun_path_info_button_all_boards),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sun_path_info_scroll_view),
                                        0),
                                1),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.sun_path_info_text_view_two), withText("The path\n\nThe Sun trail can be walked through from both ends. You can start at the Sun in Křtiny and finish at the Neptune near ŠLP Křtiny arboretum. Then you can continue and follow the yellow marking towards Jedovnice or get on the IDS JMK number 201 (Křtiny, Hájovna na lukách) bus.\nOther than that you can approach the Sun from the other end of the trail and begin at Neptune. \n\nThe trail follows well marked path mostly inside a forrest and has a length of about 4.5 km. For better orientation you can use the provided map. If you are going to return to Křtiny on foot double the distance. You will only need casual footwear for this trail."),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sun_path_info_scroll_view),
                                        0),
                                2),
                        isDisplayed()));
        textView3.check(matches(withText("The path  The Sun trail can be walked through from both ends. You can start at the Sun in Křtiny and finish at the Neptune near ŠLP Křtiny arboretum. Then you can continue and follow the yellow marking towards Jedovnice or get on the IDS JMK number 201 (Křtiny, Hájovna na lukách) bus. Other than that you can approach the Sun from the other end of the trail and begin at Neptune.   The trail follows well marked path mostly inside a forrest and has a length of about 4.5 km. For better orientation you can use the provided map. If you are going to return to Křtiny on foot double the distance. You will only need casual footwear for this trail.")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.sun_path_info_text_view_two), withText("The path\n\nThe Sun trail can be walked through from both ends. You can start at the Sun in Křtiny and finish at the Neptune near ŠLP Křtiny arboretum. Then you can continue and follow the yellow marking towards Jedovnice or get on the IDS JMK number 201 (Křtiny, Hájovna na lukách) bus.\nOther than that you can approach the Sun from the other end of the trail and begin at Neptune. \n\nThe trail follows well marked path mostly inside a forrest and has a length of about 4.5 km. For better orientation you can use the provided map. If you are going to return to Křtiny on foot double the distance. You will only need casual footwear for this trail."),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sun_path_info_scroll_view),
                                        0),
                                2),
                        isDisplayed()));
        textView4.check(matches(withText("The path  The Sun trail can be walked through from both ends. You can start at the Sun in Křtiny and finish at the Neptune near ŠLP Křtiny arboretum. Then you can continue and follow the yellow marking towards Jedovnice or get on the IDS JMK number 201 (Křtiny, Hájovna na lukách) bus. Other than that you can approach the Sun from the other end of the trail and begin at Neptune.   The trail follows well marked path mostly inside a forrest and has a length of about 4.5 km. For better orientation you can use the provided map. If you are going to return to Křtiny on foot double the distance. You will only need casual footwear for this trail.")));

        ViewInteraction button3 = onView(
                allOf(withId(R.id.sun_path_info_button_map),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sun_path_info_scroll_view),
                                        0),
                                3),
                        isDisplayed()));
        button3.check(matches(isDisplayed()));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.sun_path_info_text_view_four), withText("You can also take a shorter variant of the trail which ends at Saturn. After that you will return from Viligrunt. (Saturn is about 1.5km from the beginning of the trail). \n\nTranslations of the information boards into English and German can be found in this app.\nYou can also play a game during which you will use your phone or tablet. \n\nThe Solar system\n\nThe following picture shows a scheme of the orbits of planets and other astronomical objects in our Solar system. These are those you will encounter on the Sun trail. The inner part of the Solar system is enlarged 10 times for clarity.\nThe gauge is in astronomical units.\n(1 AU = 149 597 870,691 km)\nThe guidelines mark the positions of planets, (1) Ceres and comet 1P/Halley on September 1st 2003."),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sun_path_info_scroll_view),
                                        0),
                                4),
                        isDisplayed()));
        textView5.check(matches(withText("You can also take a shorter variant of the trail which ends at Saturn. After that you will return from Viligrunt. (Saturn is about 1.5km from the beginning of the trail).   Translations of the information boards into English and German can be found in this app. You can also play a game during which you will use your phone or tablet.   The Solar system  The following picture shows a scheme of the orbits of planets and other astronomical objects in our Solar system. These are those you will encounter on the Sun trail. The inner part of the Solar system is enlarged 10 times for clarity. The gauge is in astronomical units. (1 AU = 149 597 870,691 km) The guidelines mark the positions of planets, (1) Ceres and comet 1P/Halley on September 1st 2003.")));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.sun_path_info_button_schema), withContentDescription("Solar system diagram"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sun_path_info_scroll_view),
                                        0),
                                5),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction imageButton2 = onView(
                allOf(withId(R.id.sun_path_info_button_schema), withContentDescription("Solar system diagram"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sun_path_info_scroll_view),
                                        0),
                                5),
                        isDisplayed()));
        imageButton2.check(matches(isDisplayed()));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.sun_path_info_text_view_five), withText("News and contact\n\nIf you liked the trail, please drop us a line at kontakt@slunecnistezka.cz. And read what others have said. \n\nIf you want to know about any news, like us on Facebook or Twitter.\nIf you have any ideas or questions, please contact us at the e-mail address mentioned above.\nA lot of people already helped us, we will be happy to hear from you! \n\nThe Sun trail is a community project and is run under the management of Křtiny. More at The Sun trail This application was created as a part of a bachelor thesis at FI MU. More at Git hub. \n\nPlease also have a look at photos from building the trail. \n\nAuthors of photos\n\nSun: NASA/Goddard/SDO\nMercury: NASA/Messenger\nVenus: NASA/JPL/Magellan\nEarth: NASA/Goddard\nMoon: Fred Locklear\nMars: NASA/Hubble\nCeres: NASA/JPL-Caltech\nJupiter: NASA/JPL/Voyager 1\nHalley: Giotto Project, ESA\nSaturn: NASA/ESA/JPL/Cassini\nUranus: NASA/JPL/Voyager 2/Joe Ruhinski\nNeptune: NASA/JPL/Voyager 2 \n\nSponsors"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sun_path_info_scroll_view),
                                        0),
                                6),
                        isDisplayed()));
        textView6.check(matches(withText("News and contact  If you liked the trail, please drop us a line at kontakt@slunecnistezka.cz. And read what others have said.   If you want to know about any news, like us on Facebook or Twitter. If you have any ideas or questions, please contact us at the e-mail address mentioned above. A lot of people already helped us, we will be happy to hear from you!   The Sun trail is a community project and is run under the management of Křtiny. More at The Sun trail This application was created as a part of a bachelor thesis at FI MU. More at Git hub.   Please also have a look at photos from building the trail.   Authors of photos  Sun: NASA/Goddard/SDO Mercury: NASA/Messenger Venus: NASA/JPL/Magellan Earth: NASA/Goddard Moon: Fred Locklear Mars: NASA/Hubble Ceres: NASA/JPL-Caltech Jupiter: NASA/JPL/Voyager 1 Halley: Giotto Project, ESA Saturn: NASA/ESA/JPL/Cassini Uranus: NASA/JPL/Voyager 2/Joe Ruhinski Neptune: NASA/JPL/Voyager 2   Sponsors")));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.sun_path_info_text_view_five), withText("News and contact\n\nIf you liked the trail, please drop us a line at kontakt@slunecnistezka.cz. And read what others have said. \n\nIf you want to know about any news, like us on Facebook or Twitter.\nIf you have any ideas or questions, please contact us at the e-mail address mentioned above.\nA lot of people already helped us, we will be happy to hear from you! \n\nThe Sun trail is a community project and is run under the management of Křtiny. More at The Sun trail This application was created as a part of a bachelor thesis at FI MU. More at Git hub. \n\nPlease also have a look at photos from building the trail. \n\nAuthors of photos\n\nSun: NASA/Goddard/SDO\nMercury: NASA/Messenger\nVenus: NASA/JPL/Magellan\nEarth: NASA/Goddard\nMoon: Fred Locklear\nMars: NASA/Hubble\nCeres: NASA/JPL-Caltech\nJupiter: NASA/JPL/Voyager 1\nHalley: Giotto Project, ESA\nSaturn: NASA/ESA/JPL/Cassini\nUranus: NASA/JPL/Voyager 2/Joe Ruhinski\nNeptune: NASA/JPL/Voyager 2 \n\nSponsors"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sun_path_info_scroll_view),
                                        0),
                                6),
                        isDisplayed()));
        textView7.check(matches(withText("News and contact  If you liked the trail, please drop us a line at kontakt@slunecnistezka.cz. And read what others have said.   If you want to know about any news, like us on Facebook or Twitter. If you have any ideas or questions, please contact us at the e-mail address mentioned above. A lot of people already helped us, we will be happy to hear from you!   The Sun trail is a community project and is run under the management of Křtiny. More at The Sun trail This application was created as a part of a bachelor thesis at FI MU. More at Git hub.   Please also have a look at photos from building the trail.   Authors of photos  Sun: NASA/Goddard/SDO Mercury: NASA/Messenger Venus: NASA/JPL/Magellan Earth: NASA/Goddard Moon: Fred Locklear Mars: NASA/Hubble Ceres: NASA/JPL-Caltech Jupiter: NASA/JPL/Voyager 1 Halley: Giotto Project, ESA Saturn: NASA/ESA/JPL/Cassini Uranus: NASA/JPL/Voyager 2/Joe Ruhinski Neptune: NASA/JPL/Voyager 2   Sponsors")));

        ViewInteraction imageView = onView(
                allOf(withContentDescription("Sponsor logo"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sun_path_info_scroll_view),
                                        0),
                                7),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction imageView2 = onView(
                allOf(withContentDescription("Sponsor logo"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sun_path_info_scroll_view),
                                        0),
                                8),
                        isDisplayed()));
        imageView2.check(matches(isDisplayed()));

        ViewInteraction imageView3 = onView(
                allOf(withContentDescription("Sponsor logo"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sun_path_info_scroll_view),
                                        0),
                                9),
                        isDisplayed()));
        imageView3.check(matches(isDisplayed()));

        ViewInteraction imageView4 = onView(
                allOf(withContentDescription("Sponsor logo"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sun_path_info_scroll_view),
                                        0),
                                10),
                        isDisplayed()));
        imageView4.check(matches(isDisplayed()));

        ViewInteraction imageView5 = onView(
                allOf(withContentDescription("Sponsor logo"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sun_path_info_scroll_view),
                                        0),
                                11),
                        isDisplayed()));
        imageView5.check(matches(isDisplayed()));

        ViewInteraction imageView6 = onView(
                allOf(withContentDescription("Sponsor logo"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sun_path_info_scroll_view),
                                        0),
                                11),
                        isDisplayed()));
        imageView6.check(matches(isDisplayed()));

    }
}
