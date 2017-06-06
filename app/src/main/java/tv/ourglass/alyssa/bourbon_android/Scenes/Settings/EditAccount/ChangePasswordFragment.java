package tv.ourglass.alyssa.bourbon_android.Scenes.Settings.EditAccount;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import tv.ourglass.alyssa.bourbon_android.Model.SharedPrefsManager;
import tv.ourglass.alyssa.bourbon_android.Networking.OGCloud;
import tv.ourglass.alyssa.bourbon_android.R;
import tv.ourglass.alyssa.bourbon_android.Scenes.Registration.RegistrationBaseActivity;

/**
 * Created by atorres on 6/1/17.
 */

public class ChangePasswordFragment extends Fragment {

    String TAG = "ChangePasswordFragment";

    EditText mCurrentPassword, mNewPassword, mRepeatNewPassword;

    ProgressDialog mProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        mProgress = new ProgressDialog(getActivity());

        // set up top toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.tb_change_password));

        // get password edit texts
        mCurrentPassword = (EditText) view.findViewById(R.id.currentPassword);
        mNewPassword = (EditText) view.findViewById(R.id.newPassword);
        mRepeatNewPassword = (EditText) view.findViewById(R.id.repeatNewPassword);

        (view.findViewById(R.id.save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNewPassword();
            }
        });

        return view;
    }

    private void saveNewPassword() {
        mProgress.show();

        // hide keyboard
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) { // keyboard not open
        }

        // check if new password is valid
        if (!RegistrationBaseActivity.isValidPassword(mNewPassword.getText().toString())) {
            mNewPassword.setError(getString(R.string.passwordRequirements));
            mNewPassword.requestFocus();
            mProgress.dismiss();
            return;
        }

        // check if new passwords match
        if (!mNewPassword.getText().toString().equals(mRepeatNewPassword.getText().toString())) {
            mRepeatNewPassword.setError(getString(R.string.passwords_must_match));
            mRepeatNewPassword.requestFocus();
            mProgress.dismiss();
            return;
        }

        final String email = SharedPrefsManager.getUserEmail(getActivity());
        final String newPassword = mNewPassword.getText().toString();

        OGCloud.getInstance().loginOnly(getActivity(), email, mCurrentPassword.getText().toString(),
                new OGCloud.HttpCallback() {
                    @Override
                    public void onSuccess(Response response) {
                        response.body().close();
                        // password is correct, so now we change it
                        OGCloud.getInstance().changePassword(getActivity(), email, newPassword,
                                new OGCloud.HttpCallback() {
                                    @Override
                                    public void onSuccess(Response response) {
                                        response.body().close();
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mProgress.dismiss();
                                                Toast.makeText(getActivity(), getString(R.string.password_saved), Toast.LENGTH_SHORT).show();
                                                getActivity().onBackPressed();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(Call call, IOException e, OGCloud.OGCloudError error) {
                                        Log.d(TAG, error.name());
                                        Log.d(TAG, SharedPrefsManager.getJwt(getActivity()));
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mProgress.dismiss();
                                                AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
                                                alert.setTitle(getString(R.string.uhoh));
                                                alert.setMessage(getString(R.string.password_save_error));
                                                alert.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.dismiss();
                                                            }
                                                        });
                                                alert.show();
                                            }
                                        });
                                    }
                                });
                    }

                    @Override
                    public void onFailure(Call call, IOException e, OGCloud.OGCloudError error) {
                        // the password entered is incorrect
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mCurrentPassword.setError(getString(R.string.password_wrong));
                                mCurrentPassword.requestFocus();
                                mProgress.dismiss();
                            }
                        });
                    }
                });
    }

    //private OGCloud.HttpCallback
}
