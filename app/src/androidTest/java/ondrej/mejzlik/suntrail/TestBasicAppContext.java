package ondrej.mejzlik.suntrail;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 * Tests that we run the correct application.
 */
@RunWith(AndroidJUnit4.class)
public class TestBasicAppContext {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("ondrej.mejzlik.suntrail", appContext.getPackageName());
    }
}
