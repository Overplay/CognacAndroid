package tv.ourglass.alyssa.bourbon_android.Scenes.Settings.SetupDevice;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.test.suitebuilder.TestMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Response;
import tv.ourglass.alyssa.bourbon_android.Model.Input.InputType;
import tv.ourglass.alyssa.bourbon_android.Model.Input.TextFocusChangeListener;
import tv.ourglass.alyssa.bourbon_android.Model.OGVenue.OGVenue;
import tv.ourglass.alyssa.bourbon_android.Networking.Applejack;
import tv.ourglass.alyssa.bourbon_android.R;
import tv.ourglass.alyssa.bourbon_android.Scenes.Tabs.MainTabsActivity;

/**
 * Created by atorres on 5/17/17.
 */

public class SetupDeviceFragment extends Fragment {
    String TAG = "SetupDeviceFragment";

    OGVenue venue;

    EditText regCode, deviceName;

    Button createDeviceButton;

    ProgressBar progressSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setup_device, container, false);

        // set up toolbar
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("Setup Device");
        ((MainTabsActivity) getActivity()).setSupportActionBar(toolbar);

        // setup progress spinner
        progressSpinner = (ProgressBar) rootView.findViewById(R.id.progress);
        progressSpinner.setVisibility(View.GONE);

        // set text to indicate the selected venue
        ((TextView) rootView.findViewById(R.id.venueName)).setText(venue.name);
        ((TextView) rootView.findViewById(R.id.venueAddress)).setText(venue.getAddress());

        // set button on click listeners
        createDeviceButton = (Button) rootView.findViewById(R.id.createDevice);
        createDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDevice();
            }
        });

        // get inputs
        regCode = (EditText) rootView.findViewById(R.id.regCode);
        regCode.setOnFocusChangeListener(
                TextFocusChangeListener.newInstance(regCode, InputType.NONEMPTY));

        deviceName = (EditText) rootView.findViewById(R.id.deviceName);
        deviceName.setOnFocusChangeListener(
                TextFocusChangeListener.newInstance(deviceName, InputType.NONEMPTY));

        return rootView;
    }

    /**
     * Create a new device and add it to the selected venue.
     */
    private void createDevice() {
        createDeviceButton.setEnabled(false);
        progressSpinner.setVisibility(View.VISIBLE);

        // hide keyboard
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) { // keyboard not open
        }

        // remove focus from the EditText's
        getView().findViewById(R.id.layout).requestFocus();

        // check inputs for error
        ArrayList<EditText> inputs = new ArrayList<>(Arrays.asList(regCode, deviceName));

        boolean inputValid = true;

        for (EditText input : inputs) {
            if (input.getError() != null || input.getText().toString().trim().isEmpty()) {
                inputValid = false;
                input.setError(getString(R.string.req_field));
            }
        }

        if (inputValid) {
            final Context context = getActivity();

            final String name = deviceName.getText().toString();
            final String venueUuid = venue.uuid;

            Applejack.getInstance().findByRegCode(context, regCode.getText().toString(),
                    new Applejack.HttpCallback() {
                        @Override
                        public void onSuccess(Response response) {
                            String deviceUdid = null;
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                deviceUdid = json.getString("deviceUDID");

                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            } finally {
                                response.body().close();
                            }

                            // if we got a udid, change the device's name
                            if (deviceUdid != null) {
                                final String udid = deviceUdid;
                                Applejack.getInstance().changeDeviceName(context, deviceUdid, name,
                                        new Applejack.HttpCallback() {
                                            @Override
                                            public void onSuccess(Response response) {
                                                response.body().close();

                                                // now associate the device with the venue
                                                Applejack.getInstance().associate(context, udid, venueUuid,
                                                        new Applejack.HttpCallback() {
                                                            @Override
                                                            public void onSuccess(Response response) {
                                                                response.body().close();
                                                                getActivity().runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Toast.makeText(getActivity(), "Device created!", Toast.LENGTH_SHORT).show();
                                                                        progressSpinner.setVisibility(View.GONE);
                                                                        createDeviceButton.setEnabled(true);
                                                                    }
                                                                });
                                                            }

                                                            @Override
                                                            public void onFailure(Call call, IOException e, Applejack.ApplejackError error) {
                                                                createDeviceFailure("Something went wrong and we were unable to create your device.");
                                                            }
                                                        });
                                            }

                                            @Override
                                            public void onFailure(Call call, IOException e, Applejack.ApplejackError error) {
                                                createDeviceFailure("Something went wrong and we were unable to create your device.");
                                            }
                                        });

                            } else {  // we did not get a udid
                                Log.d(TAG, "code invalid from JSON");
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        regCode.setError("Code invalid.");
                                        createDeviceButton.setEnabled(true);
                                        progressSpinner.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call call, IOException e, Applejack.ApplejackError error) {
                            if (error == Applejack.ApplejackError.defaultError) { // reg code was invalid
                                Log.d(TAG, "code invalid onFailure "+ regCode.getText().toString());
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        regCode.setError("Code invalid.");
                                        createDeviceButton.setEnabled(true);
                                        progressSpinner.setVisibility(View.GONE);
                                    }
                                });

                            } else {
                                createDeviceFailure("Something went wrong and we were unable to create your device.");
                            }
                        }
                    });

        } else {
            createDeviceButton.setEnabled(true);
            progressSpinner.setVisibility(View.GONE);
        }
    }

    public void createDeviceFailure(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createDeviceButton.setEnabled(true);
                progressSpinner.setVisibility(View.GONE);
                showAlert("Uh oh!", message);
            }
        });
    }

    private void showAlert(String title, String message) {
        AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alert.show();
    }

    public static SetupDeviceFragment newInstance(OGVenue venue) {
        SetupDeviceFragment setupDeviceFragment = new SetupDeviceFragment();
        setupDeviceFragment.venue = venue;
        return setupDeviceFragment;
    }
}
