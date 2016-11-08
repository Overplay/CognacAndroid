package tv.ourglass.alyssa.absinthe_android.Registration;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import tv.ourglass.alyssa.absinthe_android.R;
import tv.ourglass.alyssa.absinthe_android.Registration.EnterEmailActivity;

public class EnterNameActivity extends AppCompatActivity {

    EditText mFirstName;
    EditText mLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        mFirstName = (EditText)findViewById(R.id.firstName);
        mLastName = (EditText)findViewById(R.id.lastName);

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
    }

    public void next(View view) {
        Intent intent = new Intent(this, EnterEmailActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    public void goBack(View view) {
        this.finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }
}
