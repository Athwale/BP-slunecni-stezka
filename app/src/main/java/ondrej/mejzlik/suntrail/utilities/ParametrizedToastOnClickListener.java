package ondrej.mejzlik.suntrail.utilities;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import ondrej.mejzlik.suntrail.R;

/**
 * Created by Ondrej Mejzlik on 3/14/17.
 * This class is used in PlanetMenuFragment to display a toast with planet photo author's name.
 * Because we have to set onClickListener programmatically inside the fragment and anonymous
 * classes can not read non-final outer variables, we have to use this and set the author with a
 * method.
 */
public class ParametrizedToastOnClickListener implements View.OnClickListener {
    private String string = "";
    private Activity activity = null;

    /**
     * Set the toast string along with which activity does the toast belong to.
     *
     * @param string   Toast string
     * @param activity Toast activity
     */
    public void setToast(String string, Activity activity) {
        this.string = string;
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        // If activity is null, no toast will appear.
        if (activity == null) {
            return;
        }
        String toast = activity.getResources().getString(R.string.toast_author) + " " + string;
        Toast.makeText(activity, toast, Toast.LENGTH_SHORT).show();
    }
}
