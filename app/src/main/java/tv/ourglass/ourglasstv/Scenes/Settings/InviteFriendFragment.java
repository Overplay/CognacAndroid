package tv.ourglass.ourglasstv.Scenes.Settings;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import tv.ourglass.ourglasstv.Model.Input.InputType;
import tv.ourglass.ourglasstv.Model.Input.TextFocusChangeListener;
import tv.ourglass.ourglasstv.Model.Input.TextValidator;
import tv.ourglass.ourglasstv.Model.OGConstants;
import tv.ourglass.ourglasstv.Networking.OGCloud;
import tv.ourglass.alyssa.ourglasstv.R;
import tv.ourglass.ourglasstv.Scenes.Registration.RegistrationBaseActivity;


public class InviteFriendFragment extends Fragment {

    EditText mEmail;

    Button mInvite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_invite_friend, container, false);

        mEmail = (EditText) view.findViewById(R.id.email);
        mEmail.setOnFocusChangeListener(TextFocusChangeListener.newInstance(mEmail, InputType.EMAIL));
        mInvite = (Button) view.findViewById(R.id.invite);

        mInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invite();
            }
        });

        mEmail.addTextChangedListener(new TextValidator(mEmail) {
            @Override
            public void validate(TextView textView, String text) {
                if (RegistrationBaseActivity.isValidEmail(text)) {
                    mInvite.animate().alpha(1f).setDuration(OGConstants.fadeInTime).start();
                } else {
                    mInvite.animate().alpha(0f).setDuration(OGConstants.fadeOutTime).start();
                }
            }
        });

        mEmail.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    if (!RegistrationBaseActivity.isValidEmail(mEmail.getText().toString())) {
                        mEmail.setError(getString(R.string.email_not_valid));
                    }
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    public void invite() {

        if (!RegistrationBaseActivity.isValidEmail(mEmail.getText().toString())) {
            mEmail.setError(getString(R.string.email_not_valid));
            mEmail.requestFocus();
            return;
        }

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }

        OGCloud.getInstance().inviteUser(getActivity(), this.mEmail.getText().toString(), new OGCloud.HttpCallback() {
            @Override
            public void onFailure(Call call, final IOException e, OGCloud.OGCloudError error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Unable to send invite", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onSuccess(final Response response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Invite sent!", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }
                });
            }
        });
    }
}
