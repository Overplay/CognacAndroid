package tv.ourglass.alyssa.bourbon_android.Scenes.Settings;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import tv.ourglass.alyssa.bourbon_android.Model.OGConstants;
import tv.ourglass.alyssa.bourbon_android.Networking.Applejack;
import tv.ourglass.alyssa.bourbon_android.R;

import static tv.ourglass.alyssa.bourbon_android.Scenes.Registration.RegistrationBaseActivity.isValidEmail;


public class InviteFriendFragment extends Fragment {

    private EditText mEmail;

    private ImageView mEmailCheck;

    private TextView mInvite;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_invite_friend, container, false);

        mEmail = (EditText) view.findViewById(R.id.email);
        mEmailCheck = (ImageView) view.findViewById(R.id.emailCheck);
        mInvite = (TextView) view.findViewById(R.id.inviteFriend);

        mInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invite();
            }
        });


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

        return view;
    }

    public void invite() {

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }

        Applejack.getInstance().inviteUser(getActivity(), this.mEmail.getText().toString(), new Applejack.HttpCallback() {
            @Override
            public void onFailure(Call call, final IOException e, Applejack.ApplejackError error) {
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
