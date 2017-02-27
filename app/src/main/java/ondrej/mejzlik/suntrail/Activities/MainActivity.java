package ondrej.mejzlik.suntrail.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ondrej.mejzlik.suntrail.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_layout);
    }

    public void infoButtonHandler(View view) {
        Intent intent = new Intent(this, InfoScreenActivity.class);
        startActivity(intent);
    }
}
