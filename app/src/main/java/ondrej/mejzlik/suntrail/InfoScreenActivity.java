package ondrej.mejzlik.suntrail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class InfoScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_screen_layout);

        TextView textViewTop = (TextView) (findViewById(R.id.info_screen_text_view_top));
        textViewTop.setText(getHtmlForTextView(getString(R.string.sun_path_info_top)));
        textViewTop.setMovementMethod(LinkMovementMethod.getInstance());

        TextView textViewMiddle = (TextView) (findViewById(R.id.info_screen_text_view_middle));
        textViewMiddle.setText(getHtmlForTextView(getString(R.string.sun_path_info_middle)));
        textViewMiddle.setMovementMethod(LinkMovementMethod.getInstance());

        TextView textViewBottom = (TextView) (findViewById(R.id.info_screen_text_view_bottom));
        textViewBottom.setText(getHtmlForTextView(getString(R.string.sun_path_info_bottom)));
        textViewBottom.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * Converts string from html to a string displayable by TextView
     * @param html Input string. Can be from values/strings if enclosed in <![CDATA[ ... ]]>
     * @return Spanned string for TextView
     */
    // Removes warning for fromHtml which is deprecated in API 24
    @SuppressWarnings("deprecation")
    public static Spanned getHtmlForTextView(String html){
        Spanned output;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            output = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            output = Html.fromHtml(html);
        }
        return output;
    }

    public void mapButtonHandler(View view) {
        Intent intent = new Intent(this, ZoomableWebViewActivity.class);
        startActivity(intent);
    }

    public void howToPlayButtonHandler(View view) {
        Intent intent = new Intent(this, GameInfoActivity.class);
        startActivity(intent);
    }
}
