package tv.ourglass.alyssa.bourbon_android.Scenes.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import tv.ourglass.alyssa.bourbon_android.Model.Input.InputType;
import tv.ourglass.alyssa.bourbon_android.Model.Input.TextFocusChangeListener;
import tv.ourglass.alyssa.bourbon_android.Model.Input.TextValidator;
import tv.ourglass.alyssa.bourbon_android.Model.OGConstants;
import tv.ourglass.alyssa.bourbon_android.R;

public class EnterEmailActivity extends RegistrationBaseActivity {

    EditText mEmail;

    ImageButton mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_email);

        mNextButton = (ImageButton) findViewById(R.id.nextButton);

        mEmail = (EditText)findViewById(R.id.email);
        mEmail.setOnFocusChangeListener(TextFocusChangeListener.newInstance(mEmail, InputType.EMAIL));

        mEmail.addTextChangedListener(new TextValidator(mEmail) {
            @Override
            public void validate(TextView textView, String text) {
                if (isValidEmail(text)) {
                    mNextButton.animate().alpha(1f).setDuration(OGConstants.fadeInTime).start();
                } else {
                    mNextButton.animate().alpha(0f).setDuration(OGConstants.fadeOutTime).start();
                }
            }
        });

        mEmail.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    if (!isValidEmail(mEmail.getText().toString())) {
                        mEmail.setError(getString(R.string.email_not_valid));
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void next(View view) {
        if (!isValidEmail(mEmail.getText().toString())) {
            mEmail.setError(getString(R.string.email_not_valid));
            mEmail.requestFocus();
            return;
        }

        Intent intent = new Intent(this, EnterPasswordActivity.class)
                .putExtra(OGConstants.emailExtra, mEmail.getText().toString())
                .putExtra(OGConstants.firstNameExtra, getIntent().getStringExtra(OGConstants.firstNameExtra))
                .putExtra(OGConstants.lastNameExtra, getIntent().getStringExtra(OGConstants.lastNameExtra));
        startActivity(intent);
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }
}
