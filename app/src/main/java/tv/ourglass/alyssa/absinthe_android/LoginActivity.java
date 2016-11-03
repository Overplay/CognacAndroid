package tv.ourglass.alyssa.absinthe_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText mEmail;
    EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = (EditText)findViewById(R.id.email);
        mPassword = (EditText)findViewById(R.id.password);
    }

    public void login(View view) {
        Log.d("LoginActivity", mEmail.getText().toString());
        Log.d("LoginActivity", mPassword.getText().toString());
    }

    public void goBack(View view) {
        this.finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }
}
