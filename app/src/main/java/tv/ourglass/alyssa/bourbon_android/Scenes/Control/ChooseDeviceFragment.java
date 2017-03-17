package tv.ourglass.alyssa.bourbon_android.Scenes.Control;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;
import tv.ourglass.alyssa.bourbon_android.Models.OGConstants;
import tv.ourglass.alyssa.bourbon_android.Models.OGDevice;
import tv.ourglass.alyssa.bourbon_android.Networking.Applejack;
import tv.ourglass.alyssa.bourbon_android.R;

public class ChooseDeviceFragment extends Fragment {

    String TAG = "ChooseDeviceActivity";

    ArrayList<OGDevice> devices = new ArrayList<>();

    DevicesListAdapter devicesListAdapter;

    Applejack.HttpCallback devicesCallback = new Applejack.HttpCallback() {
        @Override
        public void onFailure(Call call, final IOException e) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "Error retrieving devices", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onSuccess(final Response response) {

            try {
                String jsonStr = response.body().string();
                JSONArray deviceArray = new JSONArray(jsonStr);

                Log.d(TAG, String.format("%d devices found!", deviceArray.length()));

                for (int i = 0; i < deviceArray.length(); i++) {
                    JSONObject device = deviceArray.getJSONObject(i);

                    // Get device info
                    String name = device.getString("name");
                    String venueUUID = device.getString("atVenueUUID");
                    String udid = device.getString("deviceUDID");

                    devices.add(new OGDevice(name, venueUUID, udid));
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        devicesListAdapter.notifyDataSetChanged();
                    }
                });

            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Error retrieving devices", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_choose_device, container, false);

        devicesListAdapter = new DevicesListAdapter(getActivity(), devices);

        ListView listView = (ListView) rootView.findViewById(R.id.deviceList);
        listView.setAdapter(this.devicesListAdapter);

        TextView venueName = (TextView) rootView.findViewById(R.id.venueName);
        Bundle extras = getActivity().getIntent().getExtras();

        if (extras != null) {
            venueName.setText(extras.getString(OGConstants.venueNameExtra));
            String venueUUID = extras.getString(OGConstants.venueUUIDExtra);

            Applejack.getInstance().getDevices(getActivity(), venueUUID, devicesCallback);
        }

        return rootView;
    }
}
