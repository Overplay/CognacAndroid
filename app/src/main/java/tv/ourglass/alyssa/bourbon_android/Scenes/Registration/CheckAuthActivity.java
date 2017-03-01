package tv.ourglass.alyssa.bourbon_android.Scenes.Registration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import tv.ourglass.alyssa.bourbon_android.Models.SharedPrefsManager;
import tv.ourglass.alyssa.bourbon_android.Networking.Applejack;
import tv.ourglass.alyssa.bourbon_android.R;
import tv.ourglass.alyssa.bourbon_android.Scenes.Settings.EditAccountActivity;
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
        String email = SharedPrefsManager.getUserEmail(this);
        String pwd = SharedPrefsManager.getUserPassword(this);

        if (email != null && !email.isEmpty() && pwd != null && !pwd.isEmpty()) {

            Applejack.getInstance().login(this, email, pwd,
                    new Applejack.HttpCallback() {

                        @Override
                        public void onFailure(Call call, final IOException e) {  // not authorized
                            CheckAuthActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (e != null) {
                                        Log.d(TAG, e.getLocalizedMessage());
                                    }
                                    Intent intent = new Intent(CheckAuthActivity.this, WelcomeActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }

                        @Override
                        public void onSuccess(Response response) {

                            response.body().close();
                            Applejack.getInstance().getAuthStatus(CheckAuthActivity.this,
                                    new Applejack.HttpCallback() {

                                        @Override
                                        public void onFailure(Call call, final IOException e) { // not authorized
                                            CheckAuthActivity.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (e != null) {
                                                        Log.d(TAG, e.getLocalizedMessage());
                                                    }
                                                    Intent intent = new Intent(CheckAuthActivity.this, WelcomeActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onSuccess(Response response2) {  // authorized!
                                            try {
                                                String jsonData = response2.body().string();
                                                JSONObject json = new JSONObject(jsonData);
                                                SharedPrefsManager.setUserFirstName(CheckAuthActivity.this, json.getString("firstName"));
                                                SharedPrefsManager.setUserLastName(CheckAuthActivity.this, json.getString("lastName"));
                                                SharedPrefsManager.setUserEmail(CheckAuthActivity.this, json.getString("email"));
                                                SharedPrefsManager.setUserId(CheckAuthActivity.this, json.getString("id"));

                                            } catch (Exception e) {
                                                Log.e(TAG, e.getLocalizedMessage());

                                            } finally {
                                                response2.body().close();
                                                CheckAuthActivity.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Intent intent = new Intent(CheckAuthActivity.this, MainTabsActivity.class);
                                                        startActivity(intent);
                                                    }
                                                });
                                            }
                                        }
                                    });
                        }
                    });
        }

        else { // not authorized, take to welcome screen
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
        }
    }
}
