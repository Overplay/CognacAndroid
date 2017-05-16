package tv.ourglass.alyssa.bourbon_android.Scenes.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import tv.ourglass.alyssa.bourbon_android.Model.OGConstants;
import tv.ourglass.alyssa.bourbon_android.R;

public class EnterEmailActivity extends RegistrationBaseActivity {

    private EditText mEmail;
    private ImageView mEmailCheck;

    private ImageButton mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_email);

        mEmail = (EditText)findViewById(R.id.email);
        mEmailCheck = (ImageView)findViewById(R.id.emailCheck);

        mNextButton = (ImageButton)findViewById(R.id.nextButton);

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
                    mNextButton.animate().alpha(1f).setDuration(OGConstants.fadeInTime).start();

                } else {
                    mEmailCheck.animate().alpha(0f).setDuration(OGConstants.fadeOutTime).start();
                    mNextButton.animate().alpha(0f).setDuration(OGConstants.fadeOutTime).start();

                }
            }
        });
    }

    public void next(View view) {
        Intent intent = new Intent(this, EnterPasswordActivity.class)
                .putExtra(OGConstants.emailExtra, mEmail.getText().toString())
                .putExtra(OGConstants.firstNameExtra, getIntent().getStringExtra(OGConstants.firstNameExtra))
                .putExtra(OGConstants.lastNameExtra, getIntent().getStringExtra(OGConstants.lastNameExtra));
        startActivity(intent);
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }
}
