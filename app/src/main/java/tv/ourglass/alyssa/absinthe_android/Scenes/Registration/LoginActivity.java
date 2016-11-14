package tv.ourglass.alyssa.absinthe_android.Scenes.Registration;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import tv.ourglass.alyssa.absinthe_android.Models.OGConstants;
import tv.ourglass.alyssa.absinthe_android.Networking.Applejack;
import tv.ourglass.alyssa.absinthe_android.R;
import tv.ourglass.alyssa.absinthe_android.Scenes.Tabs.MainTabsActivity;

public class LoginActivity extends RegistrationBaseActivity {

    private final String TAG = "LoginActivity";

    private EditText mEmail;
    private ImageView mEmailCheck;

    private EditText mPassword;
    private ImageView mPasswordCheck;

    private ImageButton mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = (EditText) findViewById(R.id.email);
        mEmailCheck = (ImageView) findViewById(R.id.emailCheck);

        mPassword = (EditText) findViewById(R.id.password);
        mPasswordCheck = (ImageView) findViewById(R.id.passwordCheck);

        mLoginButton = (ImageButton) findViewById(R.id.loginButton);

        // Set fonts
        TextView text = (TextView) findViewById(R.id.textView);
        Typeface font = Typeface.createFromAsset(getAssets(), OGConstants.mediumFont);
        if (text != null) {
            text.setTypeface(font);
        }

        text = (TextView) findViewById(R.id.emailLabel);
        font = Typeface.createFromAsset(getAssets(), OGConstants.regularFont);
        if (text != null) {
            text.setTypeface(font);
        }

        text = (TextView) findViewById(R.id.passwordLabel);
        if (text != null) {
            text.setTypeface(font);
        }

        mEmail.setTypeface(font);
        mPassword.setTypeface(font);

        // Add text change listeners
        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isValidEmail(mEmail.getText().toString())) {
                    mEmailCheck.animate().alpha(1f).setDuration(OGConstants.fadeInTime).start();

                } else {
                    mEmailCheck.animate().alpha(0f).setDuration(OGConstants.fadeOutTime).start();
                }
                checkFields();
            }
        });

        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isValidPassword(mPassword.getText().toString())) {
                    mPasswordCheck.animate().alpha(1f).setDuration(OGConstants.fadeInTime).start();

                } else {
                    mPasswordCheck.animate().alpha(0f).setDuration(OGConstants.fadeOutTime).start();
                }
                checkFields();
            }
        });
    }

    private void checkFields() {
        if (isValidEmail(mEmail.getText().toString()) && isValidPassword(mPassword.getText().toString())) {
            mLoginButton.animate().alpha(1f).setDuration(OGConstants.fadeInTime).start();
        } else {
            mLoginButton.animate().alpha(0f).setDuration(OGConstants.fadeOutTime).start();
        }
    }

    public void login(View view) {

        Applejack.getInstance().login(mEmail.getText().toString(), mPassword.getText().toString(),
                new Applejack.HttpCallback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showAlert("Uh oh!", "There was a problem logging in.");
                            }
                        });
                    }

                    @Override
                    public void onSuccess(Response response) {
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
                });
    }
}
