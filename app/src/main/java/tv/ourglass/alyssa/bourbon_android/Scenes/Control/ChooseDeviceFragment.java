package tv.ourglass.alyssa.bourbon_android.Scenes.Control;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;
import tv.ourglass.alyssa.bourbon_android.Model.OGConstants;
import tv.ourglass.alyssa.bourbon_android.Model.OGDevice;
import tv.ourglass.alyssa.bourbon_android.Networking.OGCloud;
import tv.ourglass.alyssa.bourbon_android.R;
import tv.ourglass.alyssa.bourbon_android.Scenes.Tabs.MainTabsActivity;

public class ChooseDeviceFragment extends Fragment {

    String TAG = "ChooseDeviceFragment";

    ArrayList<OGDevice> mDevices = new ArrayList<>();

    DevicesListAdapter devicesListAdapter;

    View mEmptyView;

    ListView mDevicesList;

    ProgressBar mProgressSpinner;

    String mVenueName;
    String mVenueUUID;

    OGCloud.HttpCallback devicesCallback = new OGCloud.HttpCallback() {

        @Override
        public void onFailure(Call call, final IOException e, OGCloud.OGCloudError error) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDevicesList.setEmptyView(mEmptyView);
                    mProgressSpinner.setVisibility(View.GONE);
                }
            });

            switch (error) {
                case authFailure:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder
                                    .setTitle("Uh oh!")
                                    .setMessage("You are not authorized to access this resource.")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });
                    break;

                case tokenInvalid:
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
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });
                    break;

                default:
                    break;
            }
        }

        @Override
        public void onSuccess(final Response response) {

            try {
                final ArrayList<OGDevice> devices = new ArrayList<>();

                String jsonStr = response.body().string();
                JSONArray deviceArray = new JSONArray(jsonStr);

                Log.d(TAG, String.format("%d devices found!", deviceArray.length()));

                for (int i = 0; i < deviceArray.length(); i++) {
                    devices.add(new OGDevice(deviceArray.getJSONObject(i)));
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDevices.clear();
                        mDevices.addAll(devices);
                        devicesListAdapter.notifyDataSetChanged();
                        mProgressSpinner.setVisibility(View.GONE);
                        mDevicesList.setEmptyView(mEmptyView);
                    }
                });

            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDevicesList.setEmptyView(mEmptyView);
                        mProgressSpinner.setVisibility(View.GONE);
                    }
                });

            } finally {
                response.body().close();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        if (args != null) {

            if (args.containsKey(OGConstants.venueNameExtra)) {
                mVenueName = args.getString(OGConstants.venueNameExtra);
            }
            if (args.containsKey(OGConstants.venueUUIDExtra)) {
                mVenueUUID = args.getString(OGConstants.venueUUIDExtra);
            }

        } else {
            mVenueName = "";
            mVenueUUID = "";
        }

        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_choose_device, container, false);

        // set up top toolbar
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(mVenueName);
        ((MainTabsActivity) getActivity()).setSupportActionBar(toolbar);

        // set up devices list
        devicesListAdapter = new DevicesListAdapter(getActivity(), mDevices);
        mDevicesList = (ListView) rootView.findViewById(R.id.deviceList);
        mDevicesList.setAdapter(this.devicesListAdapter);
        mEmptyView = rootView.findViewById(R.id.empty);
        mProgressSpinner = (ProgressBar) rootView.findViewById(R.id.progress);
        mProgressSpinner.setVisibility(View.VISIBLE);

        OGCloud.getInstance().getDevices(getActivity(), mVenueUUID, devicesCallback);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_add).setVisible(false);
    }

    public static ChooseDeviceFragment newInstance(String venueName, String venueUUID) {
        ChooseDeviceFragment chooseDeviceFragment = new ChooseDeviceFragment();

        Bundle args = new Bundle();
        args.putString(OGConstants.venueNameExtra, venueName);
        args.putString(OGConstants.venueUUIDExtra, venueUUID);

        chooseDeviceFragment.setArguments(args);

        return chooseDeviceFragment;
    }
}
