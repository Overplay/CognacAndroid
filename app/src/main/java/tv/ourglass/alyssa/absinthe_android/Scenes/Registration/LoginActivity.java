package tv.ourglass.alyssa.absinthe_android.Scenes.Registration;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import tv.ourglass.alyssa.absinthe_android.Networking.Applejack;
import tv.ourglass.alyssa.absinthe_android.R;
import tv.ourglass.alyssa.absinthe_android.Scenes.Tabs.MainTabsActivity;

public class LoginActivity extends AppCompatActivity {

    EditText mEmail;
    EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = (EditText)findViewById(R.id.email);
        mPassword = (EditText)findViewById(R.id.password);

        TextView text = (TextView)findViewById(R.id.textView);
        Typeface font = Typeface.createFromAsset(getAssets(), "Poppins-Medium.ttf");
        if (text != null) {
            text.setTypeface(font);
        }

        text = (TextView)findViewById(R.id.emailLabel);
        font = Typeface.createFromAsset(getAssets(), "Poppins-Regular.ttf");
        if (text != null) {
            text.setTypeface(font);
        }

        text = (TextView)findViewById(R.id.passwordLabel);
        if (text != null) {
            text.setTypeface(font);
        }

        mEmail.setTypeface(font);
        mPassword.setTypeface(font);
    }

    public void login(View view) {
        Log.d("LoginActivity", mEmail.getText().toString());
        Log.d("LoginActivity", mPassword.getText().toString());

        Intent intent = new Intent(this, MainTabsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    public void goBack(View view) {
        this.finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }
}
