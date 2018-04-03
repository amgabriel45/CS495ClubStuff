package crimsonclubs.uacs.android.crimsonclubs;

import android.content.Intent;
import android.support.annotation.Keep;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

@Keep
public class SplashActivity extends AppCompatActivity { //Just a splash screen that displays logo until MainActivity is loaded

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
