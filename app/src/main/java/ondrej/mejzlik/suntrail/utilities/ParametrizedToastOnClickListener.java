package ondrej.mejzlik.suntrail.utilities;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

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
    private int duration = 2000;
    private int toastLenght = Toast.LENGTH_SHORT;

    /**
     * Set the toast string along with which activity does the toast belong to and the lenght.
     * Lenght can be Toast.LENGTH_SHORT or Toast.LENGTH_LONG. In case of wrong arguments,
     * Toast.LENGTH_SHORT is used. The view click action will be paused while the toast is visible.
     *
     * @param string   Toast string
     * @param activity Toast activity
     * @param length   Toast.LENGTH_SHORT or Toast.LENGTH_LONG
     */
    public void setToast(String string, Activity activity, int length) {
        this.string = string;
        this.activity = activity;
        if (length == Toast.LENGTH_LONG) {
            this.duration = 3500;
            this.toastLenght = length;
        } else if (length == Toast.LENGTH_SHORT) {
            this.duration = 2000;
            this.toastLenght = length;
        }
    }

    @Override
    public void onClick(final View view) {
        // If activity is null, no toast will appear.
        if (activity == null) {
            return;
        }
        // These are used to disable the button while a toast with author name is shown.
        final Handler handler = new Handler();
        Timer timer = new Timer();

        String toast = activity.getResources().getString(R.string.toast_author) + " " + string;
        Toast.makeText(activity, toast, this.toastLenght).show();
        // Disable showing more than one toast at a time. Enable clicking after the toast has
        // vanished.
        view.setClickable(false);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        view.setClickable(true);
                    }
                });
            }
            // Toast.LENGTH_SHORT = 2000ms
        }, this.duration);
    }
}
