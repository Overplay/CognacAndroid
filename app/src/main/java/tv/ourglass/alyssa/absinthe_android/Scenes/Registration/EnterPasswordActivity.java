package tv.ourglass.alyssa.absinthe_android.Scenes.Registration;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import tv.ourglass.alyssa.absinthe_android.R;
import tv.ourglass.alyssa.absinthe_android.Scenes.Tabs.MainTabsActivity;

public class EnterPasswordActivity extends RegistrationBaseActivity {

    private EditText mPassword;
    private ImageView mPasswordCheck;

    private ImageButton mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);

        mPassword = (EditText)findViewById(R.id.password);
        mPasswordCheck = (ImageView)findViewById(R.id.passwordCheck);

        mNextButton = (ImageButton)findViewById(R.id.nextButton);

        // Set fonts
        TextView text = (TextView)findViewById(R.id.textView);
        Typeface font = Typeface.createFromAsset(getAssets(), "Poppins-Medium.ttf");
        if (text != null) {
            text.setTypeface(font);
        }

        font = Typeface.createFromAsset(getAssets(), "Poppins-Light.ttf");
        text = (TextView)findViewById(R.id.passwordRequirements);
        if (text != null) {
            text.setTypeface(font);
        }

        font = Typeface.createFromAsset(getAssets(), "Poppins-Regular.ttf");
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
                    mPasswordCheck.animate().alpha(1f).setDuration(350).start();
                    mNextButton.animate().alpha(1f).setDuration(350).start();

                } else {
                    mPasswordCheck.animate().alpha(0f).setDuration(350).start();
                    mNextButton.animate().alpha(0f).setDuration(350).start();

                }
            }
        });
    }

    public void next(View view) {
        Intent intent = new Intent(this, MainTabsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }
}
