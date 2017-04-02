package ondrej.mejzlik.suntrail.utilities;

import android.text.Html;
import android.text.Spanned;


/**
 * This class provides a support method to convert html from string resources into spanned text
 * which shows correctly in Text Views.
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
    public CharSequence getHtmlForTextView(String html) {
        Spanned output;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            output = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            output = Html.fromHtml(html);
        }
        return this.trim(output);
    }

    /**
     * This method removes any white characters from the beginning and then from the end of the
     * passed char sequence. Because html conversion always puts two \n below a <p> tag which
     * we do not want there.
     *
     * @param text input text from Html.fromHtml()
     * @return trimmed text
     */
    private CharSequence trim(CharSequence text) {
        int start = 0;
        int end = text.length();
        while (start < end && Character.isWhitespace(text.charAt(start))) {
            start++;
        }
        while (end > start && Character.isWhitespace(text.charAt(end - 1))) {
            end--;
        }
        return text.subSequence(start, end);
    }
}
