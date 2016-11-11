package tv.ourglass.alyssa.absinthe_android.Scenes.Registration;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import tv.ourglass.alyssa.absinthe_android.Networking.Applejack;
import tv.ourglass.alyssa.absinthe_android.R;
import tv.ourglass.alyssa.absinthe_android.Scenes.Tabs.MainTabsActivity;

public class LoginActivity extends AppCompatActivity {

    String TAG = "LoginActivity";

    private EditText mEmail;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);

        TextView text = (TextView) findViewById(R.id.textView);
        Typeface font = Typeface.createFromAsset(getAssets(), "Poppins-Medium.ttf");
        if (text != null) {
            text.setTypeface(font);
        }

        text = (TextView) findViewById(R.id.emailLabel);
        font = Typeface.createFromAsset(getAssets(), "Poppins-Regular.ttf");
        if (text != null) {
            text.setTypeface(font);
        }

        text = (TextView) findViewById(R.id.passwordLabel);
        if (text != null) {
            text.setTypeface(font);
        }

        mEmail.setTypeface(font);
        mPassword.setTypeface(font);
    }

    public void login(View view) {

        Applejack.getInstance().login(mEmail.getText().toString(), mPassword.getText().toString(),
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        } else {
                            Log.d(TAG, response.toString());

                            LoginActivity.this.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Intent intent = new Intent(LoginActivity.this, MainTabsActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                                }
                            });
                        }
                    }
                });
    }

    public void goBack(View view) {
        this.finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }
}
