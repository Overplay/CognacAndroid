package tv.ourglass.alyssa.bourbon_android.Scenes.Registration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import tv.ourglass.alyssa.bourbon_android.Models.SharedPrefsManager;
import tv.ourglass.alyssa.bourbon_android.Networking.Applejack;
import tv.ourglass.alyssa.bourbon_android.R;
import tv.ourglass.alyssa.bourbon_android.Scenes.Tabs.MainTabsActivity;

public class CheckAuthActivity extends AppCompatActivity {

    String TAG = "CheckAuthActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_auth);
        checkAuthStatus();
    }

    private void checkAuthStatus() {

        Long expiry = SharedPrefsManager.getUserApplejackJwtExpiry(this);

        if (SharedPrefsManager.getUserApplejackJwt(this) != null &&
                System.currentTimeMillis() + 86400000l < expiry) {  // if JWT is good within the next 24hrs
            Log.d(TAG, "good jwt");
            Intent intent = new Intent(CheckAuthActivity.this, MainTabsActivity.class);
            startActivity(intent);

        } else {  // JWT is bad
            Log.e(TAG, "bad jwt, taking to login/register");
            Intent intent = new Intent(CheckAuthActivity.this, WelcomeActivity.class);
            startActivity(intent);
        }
    }
}
