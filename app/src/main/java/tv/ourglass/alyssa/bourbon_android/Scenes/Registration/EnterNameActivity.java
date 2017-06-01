package tv.ourglass.alyssa.bourbon_android.Scenes.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import tv.ourglass.alyssa.bourbon_android.Model.Input.InputType;
import tv.ourglass.alyssa.bourbon_android.Model.Input.TextFocusChangeListener;
import tv.ourglass.alyssa.bourbon_android.Model.Input.TextValidator;
import tv.ourglass.alyssa.bourbon_android.Model.OGConstants;
import tv.ourglass.alyssa.bourbon_android.R;

public class EnterNameActivity extends RegistrationBaseActivity {

    EditText mFirstName;
    EditText mLastName;
    ImageButton mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        mNextButton = (ImageButton) findViewById(R.id.nextButton);

        mFirstName = (EditText)findViewById(R.id.firstName);
        mFirstName.setOnFocusChangeListener(TextFocusChangeListener.newInstance(mFirstName, InputType.NONEMPTY));

        mLastName = (EditText)findViewById(R.id.lastName);
        mLastName.setOnFocusChangeListener(TextFocusChangeListener.newInstance(mLastName, InputType.NONEMPTY));

        mFirstName.addTextChangedListener(new TextValidator(mFirstName) {
            @Override
            public void validate(TextView textView, String text) {
                checkFields();
            }
        });

        mLastName.addTextChangedListener(new TextValidator(mLastName) {
            @Override
            public void validate(TextView textView, String text) {
                checkFields();
            }
        });

        mFirstName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    if (mFirstName.getText().toString().trim().isEmpty()) {
                        mFirstName.setError(getString(R.string.req_field));
                    }
                    return true;
                }
                return false;
            }
        });

        mLastName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    if (mLastName.getText().toString().trim().isEmpty()) {
                        mLastName.setError(getString(R.string.req_field));
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void checkFields() {
        if (mFirstName.getText().toString().trim().isEmpty() || mLastName.getText().toString().trim().isEmpty()) {
            mNextButton.animate().alpha(0f).setDuration(OGConstants.fadeOutTime).start();
        } else {
            mNextButton.animate().alpha(1f).setDuration(OGConstants.fadeInTime).start();
        }
    }

    public void next(View view) {
        Boolean firstNameValid = true, lastNameValid = true;

        if (mFirstName.getText().toString().trim().isEmpty()) {
            firstNameValid = false;
            mFirstName.setError(getString(R.string.req_field));
        }
        if (mLastName.getText().toString().trim().isEmpty()) {
            lastNameValid = false;
            mLastName.setError(getString(R.string.req_field));
        }

        if (firstNameValid && lastNameValid) {
            Intent intent = new Intent(this, EnterEmailActivity.class)
                    .putExtra(OGConstants.firstNameExtra, mFirstName.getText().toString())
                    .putExtra(OGConstants.lastNameExtra, mLastName.getText().toString());
            startActivity(intent);
            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
        } else if (firstNameValid && !lastNameValid) {
            mLastName.requestFocus();
        } else {
            mFirstName.requestFocus();
        }
    }
}
