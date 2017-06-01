package tv.ourglass.alyssa.bourbon_android.Scenes.Settings.EditAccount;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import tv.ourglass.alyssa.bourbon_android.Model.Input.InputType;
import tv.ourglass.alyssa.bourbon_android.Model.Input.TextFocusChangeListener;
import tv.ourglass.alyssa.bourbon_android.Model.Input.TextValidator;
import tv.ourglass.alyssa.bourbon_android.Model.OGConstants;
import tv.ourglass.alyssa.bourbon_android.Model.SharedPrefsManager;
import tv.ourglass.alyssa.bourbon_android.Networking.OGCloud;
import tv.ourglass.alyssa.bourbon_android.R;
import tv.ourglass.alyssa.bourbon_android.Scenes.Tabs.MainTabsActivity;

import static tv.ourglass.alyssa.bourbon_android.Scenes.Registration.RegistrationBaseActivity.isValidEmail;


public class EditAccountFragment extends Fragment {
    final String TAG = "EditAccountFragment";

    EditText mFirstName;
    EditText mLastName;
    EditText mEmail;

    Button mSave;

    ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_account, container, false);

        progress = new ProgressDialog(getActivity());

        mSave = (Button) view.findViewById(R.id.save);
        mFirstName = (EditText) view.findViewById(R.id.firstName);
        mLastName = (EditText) view.findViewById(R.id.lastName);
        mEmail = (EditText) view.findViewById(R.id.email);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        // Add text change listeners
        mEmail.addTextChangedListener(new TextValidator(mEmail) {
            @Override
            public void validate(TextView textView, String text) {
                if (isValidEmail(text)) {
                    mSave.animate().alpha(1f).setDuration(OGConstants.fadeInTime).start();
                } else {
                    mSave.animate().alpha(0f).setDuration(OGConstants.fadeOutTime).start();

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

        mEmail.setOnFocusChangeListener(TextFocusChangeListener.newInstance(mEmail, InputType.EMAIL));

        displayUserInfo();

        return view;
    }

    private void displayUserInfo() {
        //progress.show();
        OGCloud.getInstance().checkJWT(getActivity(),
                new OGCloud.HttpCallback() {
                    @Override
                    public void onFailure(Call call, IOException ex, OGCloud.OGCloudError error) {
                        Log.e(TAG, "bad JWT");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                builder
                                        .setTitle("Uh oh!")
                                        .setMessage("It looks like your session has expired. Please log back in.")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                OGCloud.getInstance().logout(getActivity());
                                            }
                                        });

                                progress.dismiss();
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        });
                    }

                    @Override
                    public void onSuccess(Response response) {
                        Log.d(TAG, "good JWT");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mFirstName.setText(SharedPrefsManager.getUserFirstName(getActivity()));
                                mLastName.setText(SharedPrefsManager.getUserLastName(getActivity()));
                                mEmail.setText(SharedPrefsManager.getUserEmail(getActivity()));
                                progress.dismiss();
                            }
                        });

                        response.body().close();

                    }
                });
    }

    public void save() {
        if (isValidEmail(mEmail.getText().toString())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder
                    .setTitle("Save changes")
                    .setMessage("Are you sure you want to save changes to your account information?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveAccountInfo();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
        } else {
            mEmail.setError(getString(R.string.email_not_valid));
        }
    }

    private void saveAccountInfo() {
        progress.show();

        final String firstName = mFirstName.getText().toString();
        final String lastName = mLastName.getText().toString();
        final String email = mEmail.getText().toString();

        OGCloud.getInstance().changeAccountInfo(getActivity(), firstName, lastName, email,
                SharedPrefsManager.getUserId(getActivity()),
                new OGCloud.HttpCallback() {

                    @Override
                    public void onFailure(Call call, IOException e, OGCloud.OGCloudError error) {
                        Log.e(TAG, "save info failed");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                progress.dismiss();

                                builder
                                        .setTitle("Uh oh!")
                                        .setMessage("There was a problem saving your new account information.")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });

                                builder.create().show();
                            }
                        });
                    }

                    @Override
                    public void onSuccess(Response response) {
                        response.body().close();

                        SharedPrefsManager.setUserFirstName(getActivity(), firstName);
                        SharedPrefsManager.setUserLastName(getActivity(), lastName);
                        SharedPrefsManager.setUserEmail(getActivity(), email);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mFirstName.setText(firstName);
                                mLastName.setText(lastName);
                                mEmail.setText(email);
                                progress.dismiss();
                            }
                        });
                    }
                });
    }
}
