package tv.ourglass.alyssa.bourbon_android.Scenes.Introduction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import tv.ourglass.alyssa.bourbon_android.Models.OGConstants;
import tv.ourglass.alyssa.bourbon_android.Models.SharedPrefsManager;
import tv.ourglass.alyssa.bourbon_android.R;
import tv.ourglass.alyssa.bourbon_android.Scenes.Registration.CheckAuthActivity;

public class CheckAppOpenedActivity extends AppCompatActivity {

    String TAG = "CheckAppOpenedActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_app_opened);

        boolean appOpened = SharedPrefsManager.getAppOpened(this);

        Log.d(TAG, String.valueOf(appOpened));

        if (!appOpened || OGConstants.alwaysShowIntro) {
            SharedPrefsManager.setAppOpened(this, true);
            Intent intent = new Intent(this, IntroSlidesActivity.class);
            startActivity(intent);

        } else {
            Intent intent = new Intent(this, CheckAuthActivity.class);
            startActivity(intent);
        }
    }
}
