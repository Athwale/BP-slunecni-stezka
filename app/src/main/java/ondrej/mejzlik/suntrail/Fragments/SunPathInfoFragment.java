package ondrej.mejzlik.suntrail.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.Utilities.HtmlConverter;

/**
 * Fragment class for general Sun path information.
 * Shows general information about the Sun Path.
 */
public class SunPathInfoFragment extends Fragment {
    private HtmlConverter htmlConverter = new HtmlConverter();


    public SunPathInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sun_path_info, container, false);
        this.fillText(view);
        return view;
    }

    /**
     * Fills text views of this fragment with required strings.
     * This can not be done from xml since xml does not support using html markup in strings.
     *
     * @param view The main view of this fragment
     */
    private void fillText(View view) {
        // Load strings into textviews
        TextView textViewTop = (TextView) (view.findViewById(R.id.sun_path_info_text_view_top));
        textViewTop.setText(htmlConverter.getHtmlForTextView(getString(R.string.sun_path_info_top)));
        textViewTop.setMovementMethod(LinkMovementMethod.getInstance());

        TextView textViewMiddle = (TextView) (view.findViewById(R.id.sun_path_info_text_view_middle));
        textViewMiddle.setText(htmlConverter.getHtmlForTextView(getString(R.string.sun_path_info_middle)));
        textViewMiddle.setMovementMethod(LinkMovementMethod.getInstance());

        TextView textViewBottom = (TextView) (view.findViewById(R.id.sun_path_info_text_view_bottom));
        textViewBottom.setText(htmlConverter.getHtmlForTextView(getString(R.string.sun_path_info_bottom)));
        textViewBottom.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
