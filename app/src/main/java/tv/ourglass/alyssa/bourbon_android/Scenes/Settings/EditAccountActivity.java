package tv.ourglass.alyssa.bourbon_android.Scenes.Settings;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import tv.ourglass.alyssa.bourbon_android.Models.OGConstants;
import tv.ourglass.alyssa.bourbon_android.Models.SharedPrefsManager;
import tv.ourglass.alyssa.bourbon_android.Networking.Applejack;
import tv.ourglass.alyssa.bourbon_android.R;

import static tv.ourglass.alyssa.bourbon_android.Scenes.Registration.RegistrationBaseActivity.isValidEmail;

public class EditAccountActivity extends AppCompatActivity {

    String TAG = "EditAccountActivity";

    EditText mFirstName;
    EditText mLastName;
    EditText mEmail;
    ImageView mEmailCheck;
    Button mSave;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        progress = new ProgressDialog(EditAccountActivity.this);

        mSave = (Button) findViewById(R.id.save);
        mFirstName = (EditText) findViewById(R.id.firstName);
        mLastName = (EditText) findViewById(R.id.lastName);
        mEmail = (EditText) findViewById(R.id.email);
        mEmailCheck = (ImageView) findViewById(R.id.emailCheck);

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
    }

    private void displayUserInfo() {
        progress.show();

        Applejack.getInstance().getAuthStatus(this,
                new Applejack.HttpCallback() {

                    @Override
                    public void onFailure(Call call, final IOException e) { // not authorized
                        EditAccountActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mFirstName.setText(SharedPrefsManager.getUserFirstName(EditAccountActivity.this));
                                mLastName.setText(SharedPrefsManager.getUserLastName(EditAccountActivity.this));
                                mEmail.setText(SharedPrefsManager.getUserEmail(EditAccountActivity.this));

                                AlertDialog.Builder builder = new AlertDialog.Builder(EditAccountActivity.this);

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
                        try {
                            String jsonData = response.body().string();
                            JSONObject json = new JSONObject(jsonData);
                            SharedPrefsManager.setUserFirstName(EditAccountActivity.this, json.getString("firstName"));
                            SharedPrefsManager.setUserLastName(EditAccountActivity.this, json.getString("lastName"));
                            SharedPrefsManager.setUserEmail(EditAccountActivity.this, json.getJSONObject("auth").getString("email"));
                            SharedPrefsManager.setUserId(EditAccountActivity.this, json.getString("id"));

                            EditAccountActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mFirstName.setText(SharedPrefsManager.getUserFirstName(EditAccountActivity.this));
                                    mLastName.setText(SharedPrefsManager.getUserLastName(EditAccountActivity.this));
                                    mEmail.setText(SharedPrefsManager.getUserEmail(EditAccountActivity.this));
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

    public void save(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
                .setTitle("Save changes")
                .setMessage("Are you sure you want to save changes to your account information?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //progress.show();
                        saveAccountInfo();

                        //TODO: actually save
                        Log.d(TAG, "clicked save, placeholder");
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

        Applejack.getInstance().changeAccountInfo(this, firstName, lastName, email, SharedPrefsManager.getUserId(this),
                new Applejack.HttpCallback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "save info failed");
                        EditAccountActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(EditAccountActivity.this);

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

                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        });
                    }

                    @Override
                    public void onSuccess(Response response) {
                        response.body().close();

                        SharedPrefsManager.setUserFirstName(EditAccountActivity.this, firstName);
                        SharedPrefsManager.setUserLastName(EditAccountActivity.this, lastName);
                        SharedPrefsManager.setUserEmail(EditAccountActivity.this, email);

                        EditAccountActivity.this.runOnUiThread(new Runnable() {
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
