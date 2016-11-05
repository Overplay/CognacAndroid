package tv.ourglass.alyssa.absinthe_android;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EnterEmailActivity extends AppCompatActivity {

    EditText mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_email);

        mEmail = (EditText)findViewById(R.id.email);

        TextView text = (TextView)findViewById(R.id.textView);
        Typeface font = Typeface.createFromAsset(getAssets(), "Poppins-Medium.ttf");
        if (text != null) {
            text.setTypeface(font);
        }

        font = Typeface.createFromAsset(getAssets(), "Poppins-Regular.ttf");
        text = (TextView)findViewById(R.id.emailLabel);
        if (text != null) {
            text.setTypeface(font);
        }

        mEmail.setTypeface(font);
    }

    public void next(View view) {
        Intent intent = new Intent(this, EnterPasswordActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    public void goBack(View view) {
        this.finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }
}
