package tv.ourglass.ourglasstv.Scenes.Introduction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tv.ourglass.ourglasstv.Model.OGConstants;
import tv.ourglass.ourglasstv.Model.SharedPrefsManager;
import tv.ourglass.alyssa.ourglasstv.R;
import tv.ourglass.ourglasstv.Scenes.Registration.CheckAuthActivity;

public class CheckAppOpenedActivity extends AppCompatActivity {

    String TAG = "CheckAppOpenedActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_app_opened);

        boolean appOpened = SharedPrefsManager.getAppOpened(this);

        if ( OGConstants.alwaysSkipInto || appOpened ){
            Intent intent = new Intent(this, CheckAuthActivity.class);
            startActivity(intent);
        }


        // check if we should show intro slides or not, and navigate appropriately
        if (!appOpened || OGConstants.alwaysShowIntro) {
            SharedPrefsManager.setAppOpened(this, true);
            Intent intent = new Intent(this, IntroSlidesActivity.class);
            startActivity(intent);
        }

    }
}
