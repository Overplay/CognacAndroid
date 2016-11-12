package tv.ourglass.alyssa.absinthe_android.Scenes.Registration;

import android.content.Intent;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import tv.ourglass.alyssa.absinthe_android.R;

public class EnterNameActivity extends RegistrationBaseActivity {

    private EditText mFirstName;
    private EditText mLastName;

    private ImageButton mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        mFirstName = (EditText)findViewById(R.id.firstName);
        mLastName = (EditText)findViewById(R.id.lastName);

        mNextButton = (ImageButton)findViewById(R.id.nextButton);

        // Set fonts
        TextView text = (TextView)findViewById(R.id.textView);
        Typeface font = Typeface.createFromAsset(getAssets(), "Poppins-Medium.ttf");
        if (text != null) {
            text.setTypeface(font);
        }

        font = Typeface.createFromAsset(getAssets(), "Poppins-Regular.ttf");
        text = (TextView)findViewById(R.id.firstNameLabel);
        if (text != null) {
            text.setTypeface(font);
        }

        text = (TextView)findViewById(R.id.lastNameLabel);
        if (text != null) {
            text.setTypeface(font);
        }

        mFirstName.setTypeface(font);
        mLastName.setTypeface(font);

        // Add text change listeners
        mFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkFields();
            }
        });

        mLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkFields();
            }
        });
    }

    private void checkFields() {
        if ((mFirstName.getText().toString().isEmpty() && mLastName.getText().toString().isEmpty())) {
            mNextButton.animate().alpha(0f).setDuration(350).start();
        } else {
            mNextButton.animate().alpha(1f).setDuration(350).start();
        }
    }

    public void next(View view) {
        Intent intent = new Intent(this, EnterEmailActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }
}
