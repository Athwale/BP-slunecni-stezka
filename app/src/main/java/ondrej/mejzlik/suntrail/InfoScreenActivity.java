package ondrej.mejzlik.suntrail;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class InfoScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_screen_layout);
        WebView webView = (WebView) findViewById(R.id.info_screen_web_view);
        // Set the webview to have transparent background
        webView.setBackgroundColor(0x00000000);
    }
}
