package ondrej.mejzlik.suntrail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
