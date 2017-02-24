package ondrej.mejzlik.suntrail;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class GameInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);

        TextView textViewTop = (TextView) (findViewById(R.id.game_info_text_view_top));
        textViewTop.setText(getHtmlForTextView(getString(R.string.game_info_text_top)));

        TextView textViewMiddle = (TextView) (findViewById(R.id.game_info_text_view_bottom));
        textViewMiddle.setText(getHtmlForTextView(getString(R.string.game_info_text_bottom)));
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
}
