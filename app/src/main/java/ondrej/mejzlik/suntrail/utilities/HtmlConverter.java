package ondrej.mejzlik.suntrail.utilities;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by Ondrej Mejzlik on 2/27/17.
 */

public class HtmlConverter {

    /**
     * Converts string from html to a string displayable by TextView
     *
     * @param html Input string. Can be from values/strings if enclosed in <![CDATA[ ... ]]>
     * @return Spanned string for TextView
     */
    // Removes warning for fromHtml which is deprecated in API 24
    @SuppressWarnings("deprecation")
    public Spanned getHtmlForTextView(String html) {
        Spanned output;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            output = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            output = Html.fromHtml(html);
        }
        return output;
    }
}
