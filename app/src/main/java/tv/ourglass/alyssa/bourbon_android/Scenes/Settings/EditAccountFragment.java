package tv.ourglass.alyssa.bourbon_android.Scenes.Settings;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import tv.ourglass.alyssa.bourbon_android.Models.OGConstants;
import tv.ourglass.alyssa.bourbon_android.Models.SharedPrefsManager;
import tv.ourglass.alyssa.bourbon_android.Networking.Applejack;
import tv.ourglass.alyssa.bourbon_android.R;

import static tv.ourglass.alyssa.bourbon_android.Scenes.Registration.RegistrationBaseActivity.isValidEmail;


public class EditAccountFragment extends Fragment {

    String TAG = "EditAccountFragment";

    EditText mFirstName;
    EditText mLastName;
    EditText mEmail;
    ImageView mEmailCheck;
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
        mEmailCheck = (ImageView) view.findViewById(R.id.emailCheck);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
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
                    mSave.animate().alpha(1f).setDuration(OGConstants.fadeInTime).start();

                } else {
                    mEmailCheck.animate().alpha(0f).setDuration(OGConstants.fadeOutTime).start();
                    mSave.animate().alpha(0f).setDuration(OGConstants.fadeOutTime).start();

                }
            }
        });

        displayUserInfo();

        return view;
    }

    private void displayUserInfo() {
        //progress.show();

        // this is not really doing anything right now, it is just here as a test
        Applejack.getInstance().checkJWT(getActivity(),
                new Applejack.HttpCallback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "bad JWT");
                    }

                    @Override
                    public void onSuccess(Response response) {
                        Log.d(TAG, "good JWT");
                        response.body().close();
                    }
                });

        Applejack.getInstance().getAuthStatus(getActivity(),
                new Applejack.HttpCallback() {

                    @Override
                    public void onFailure(Call call, final IOException e) { // not authorized
                        Log.e(TAG, "not authorized");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mFirstName.setText(SharedPrefsManager.getUserFirstName(getActivity()));
                                mLastName.setText(SharedPrefsManager.getUserLastName(getActivity()));
                                mEmail.setText(SharedPrefsManager.getUserEmail(getActivity()));

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                builder
                                        .setTitle("Uh oh!")
                                        .setMessage("There was an issue getting your account information.")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });

                                progress.dismiss();
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        });
                    }

                    @Override
                    public void onSuccess(Response response) {  // authorized!
                        Log.d(TAG, "authorized");
                        try {
                            String jsonData = response.body().string();
                            JSONObject json = new JSONObject(jsonData);

                            SharedPrefsManager.setUserFirstName(getActivity(), json.getString("firstName"));
                            SharedPrefsManager.setUserLastName(getActivity(), json.getString("lastName"));
                            SharedPrefsManager.setUserId(getActivity(), json.getString("id"));

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mFirstName.setText(SharedPrefsManager.getUserFirstName(getActivity()));
                                    mLastName.setText(SharedPrefsManager.getUserLastName(getActivity()));
                                    mEmail.setText(SharedPrefsManager.getUserEmail(getActivity()));
                                    progress.dismiss();
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            response.body().close();
                        }
                    }
                });
    }

    public void save() {
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
    }

    private void saveAccountInfo() {
        progress.show();

        final String firstName = mFirstName.getText().toString();
        final String lastName = mLastName.getText().toString();
        final String email = mEmail.getText().toString();

        Applejack.getInstance().changeAccountInfo(getActivity(), firstName, lastName, email,
                SharedPrefsManager.getUserId(getActivity()),
                new Applejack.HttpCallback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
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
