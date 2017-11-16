package ondrej.mejzlik.suntrail;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ondrej.mejzlik.suntrail.activities.MainMenuActivity;
import ondrej.mejzlik.suntrail.utilities.HtmlConverter;

import static junit.framework.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 * Testing resource collection
 */
@RunWith(AndroidJUnit4.class)
public class HTMLConverterTest {
    @Rule
    public ActivityTestRule<MainMenuActivity> mActivityRule = new ActivityTestRule<>(
            MainMenuActivity.class);
    MainMenuActivity activity = null;

    @Before
    public void setUp() {
        this.activity = mActivityRule.getActivity();
    }

    @Test
    public void TestHTMLConversion() throws Exception {
        HtmlConverter converter = new HtmlConverter();
        String html = "<b>test string</b>";
        CharSequence output = converter.getHtmlForTextView(html);

        assertEquals(output.toString(), "test string");
    }
}
