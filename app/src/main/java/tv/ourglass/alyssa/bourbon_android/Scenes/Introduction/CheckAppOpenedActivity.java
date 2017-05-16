package tv.ourglass.alyssa.bourbon_android.Scenes.Introduction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tv.ourglass.alyssa.bourbon_android.Model.OGConstants;
import tv.ourglass.alyssa.bourbon_android.Model.SharedPrefsManager;
import tv.ourglass.alyssa.bourbon_android.R;
import tv.ourglass.alyssa.bourbon_android.Scenes.Registration.CheckAuthActivity;

public class CheckAppOpenedActivity extends AppCompatActivity {

    String TAG = "CheckAppOpenedActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_app_opened);

        boolean appOpened = SharedPrefsManager.getAppOpened(this);

        // check if we should show intro slides or not, and navigate appropriately
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
