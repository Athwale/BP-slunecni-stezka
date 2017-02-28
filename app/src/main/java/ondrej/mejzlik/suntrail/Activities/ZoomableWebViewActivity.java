package ondrej.mejzlik.suntrail.Activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;

import ondrej.mejzlik.suntrail.R;

public class ZoomableWebViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoomable_web_view);

        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        WebView webView = (WebView) (findViewById(R.id.zoomable_webview));
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setBackgroundColor(Color.TRANSPARENT);

        String data = "<body style='margin:0;padding:0;'>" + "<img src=\"overall_map_full_size.png\"/></body>";
        webView.loadDataWithBaseURL("file:///android_asset/",data , "text/html", "utf-8",null);
    }

}
