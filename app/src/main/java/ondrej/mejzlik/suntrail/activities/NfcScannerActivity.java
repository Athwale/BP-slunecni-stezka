package ondrej.mejzlik.suntrail.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import ondrej.mejzlik.suntrail.R;

public class NfcScannerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_scanner);

        // Animate text to fade out
        TextView sensorsActivated = (TextView) findViewById(R.id.nfc_scanner_text_view_scanner_activated);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(sensorsActivated, "alpha", 0.3f, 1f);
        fadeIn.setDuration(1000);
        fadeIn.setRepeatCount(ObjectAnimator.INFINITE);
        fadeIn.start();

        // Set planet revolutions
        ImageView animationPlanet = (ImageView) findViewById(R.id.nfc_scanner_image_view_animation_planet);
        ObjectAnimator animatorPlanet = ObjectAnimator.ofFloat(animationPlanet, "rotation", 360, 0);
        animatorPlanet.setDuration(100000);
        animatorPlanet.setRepeatCount(ObjectAnimator.INFINITE);
        animatorPlanet.setInterpolator(new LinearInterpolator());
        animatorPlanet.start();

        // Make the ship orbit the planet in opposite direction
        ImageView animationShip = (ImageView) findViewById(R.id.nfc_scanner_image_view_animation_ship);
        ObjectAnimator animatorShip = ObjectAnimator.ofFloat(animationShip, "rotation", 0, 360);
        animatorShip.setDuration(10000);
        animatorShip.setRepeatCount(ObjectAnimator.INFINITE);
        animatorShip.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorShip.start();
    }
}
