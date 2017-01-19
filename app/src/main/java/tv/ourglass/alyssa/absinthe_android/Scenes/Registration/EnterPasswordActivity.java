package tv.ourglass.alyssa.absinthe_android.Scenes.Registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import tv.ourglass.alyssa.absinthe_android.Models.OGConstants;
import tv.ourglass.alyssa.absinthe_android.Models.SharedPrefsManager;
import tv.ourglass.alyssa.absinthe_android.Networking.Applejack;
import tv.ourglass.alyssa.absinthe_android.R;
import tv.ourglass.alyssa.absinthe_android.Scenes.Tabs.MainTabsActivity;

public class EnterPasswordActivity extends RegistrationBaseActivity {

    public String TAG = "EnterPasswordActivity";

    private EditText mPassword;
    private ImageView mPasswordCheck;

    private ImageButton mNextButton;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);

        mPassword = (EditText)findViewById(R.id.password);
        mPasswordCheck = (ImageView)findViewById(R.id.passwordCheck);

        mNextButton = (ImageButton)findViewById(R.id.nextButton);

        // Set fonts
        TextView text = (TextView)findViewById(R.id.textView);
        Typeface font = Typeface.createFromAsset(getAssets(), OGConstants.mediumFont);
        if (text != null) {
            text.setTypeface(font);
        }

        font = Typeface.createFromAsset(getAssets(), OGConstants.lightFont);
        text = (TextView)findViewById(R.id.passwordRequirements);
        if (text != null) {
            text.setTypeface(font);
        }

        font = Typeface.createFromAsset(getAssets(), OGConstants.regularFont);
        text = (TextView)findViewById(R.id.passwordLabel);
        if (text != null) {
            text.setTypeface(font);
        }

        mPassword.setTypeface(font);

        // Add text change listeners
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
                    mNextButton.animate().alpha(1f).setDuration(OGConstants.fadeInTime).start();

                } else {
                    mPasswordCheck.animate().alpha(0f).setDuration(OGConstants.fadeOutTime).start();
                    mNextButton.animate().alpha(0f).setDuration(OGConstants.fadeOutTime).start();

                }
            }
        });
    }

    public void next(View view) {
        Intent prev = getIntent();

        progress = ProgressDialog.show(this, "Signing up...", "", true);

        final String email = prev.getStringExtra(OGConstants.emailExtra);
        final String firstName = prev.getStringExtra(OGConstants.firstNameExtra);
        final String lastName = prev.getStringExtra(OGConstants.lastNameExtra);
        final String password = mPassword.getText().toString();

        Applejack.getInstance().register(this, email, password, firstName, lastName,
                new Applejack.HttpCallback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        EnterPasswordActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                                showAlert("Uh oh!", "There was a problem signing you up. Do you already have an account with that email?");
                            }
                        });
                    }

                    @Override
                    public void onSuccess(Response response) {

                        EnterPasswordActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Intent intent = new Intent(EnterPasswordActivity.this, MainTabsActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                            }
                        });
                    }
                });
    }
}
