package tv.ourglass.alyssa.absinthe_android.Scenes.Settings;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import tv.ourglass.alyssa.absinthe_android.Models.OGConstants;
import tv.ourglass.alyssa.absinthe_android.R;

import static tv.ourglass.alyssa.absinthe_android.Scenes.Registration.RegistrationBaseActivity.isValidEmail;

public class InviteFriendsActivity extends AppCompatActivity {

    private EditText mEmail;

    private ImageView mEmailCheck;

    private TextView mInvite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        mEmail = (EditText)findViewById(R.id.email);

        mEmailCheck = (ImageView)findViewById(R.id.emailCheck);

        mInvite = (TextView)findViewById(R.id.inviteFriend);

        // Set fonts
        Typeface font = Typeface.createFromAsset(getAssets(), OGConstants.regularFont);
        TextView text = (TextView)findViewById(R.id.emailLabel);
        if (text != null) {
            text.setTypeface(font);
        }
        mInvite.setTypeface(font);
        mEmail.setTypeface(font);

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
                    mInvite.animate().alpha(1f).setDuration(OGConstants.fadeInTime).start();

                } else {
                    mEmailCheck.animate().alpha(0f).setDuration(OGConstants.fadeOutTime).start();
                    mInvite.animate().alpha(0f).setDuration(OGConstants.fadeOutTime).start();

                }
            }
        });
    }

    public void invite(View view) {

        // TODO: actually invite

        Toast.makeText(this, "Invite sent!", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}
