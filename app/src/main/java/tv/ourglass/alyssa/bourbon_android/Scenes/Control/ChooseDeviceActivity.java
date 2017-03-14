package tv.ourglass.alyssa.bourbon_android.Scenes.Control;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class ChooseDeviceActivity extends AppCompatActivity {

    String TAG = "ChooseDeviceActivity";

    ArrayList<OGDevice> devices = new ArrayList<>();

    DevicesListAdapter devicesListAdapter;

    Applejack.HttpCallback devicesCallback = new Applejack.HttpCallback() {
        @Override
        public void onFailure(Call call, final IOException e) {
            ChooseDeviceActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ChooseDeviceActivity.this, "Error retrieving devices", Toast.LENGTH_SHORT).show();
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

                    ChooseDeviceActivity.this.devices.add(new OGDevice(name, venueUUID, udid));
                }

                ChooseDeviceActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ChooseDeviceActivity.this.devicesListAdapter.notifyDataSetChanged();
                    }
                });

            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage());
                ChooseDeviceActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChooseDeviceActivity.this, "Error retrieving devices", Toast.LENGTH_SHORT).show();
                    }
                });

            } finally {
                response.body().close();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_view);

        this.devicesListAdapter = new DevicesListAdapter(this, this.devices);

        ListView listView = (ListView) findViewById(R.id.deviceList);
        listView.setAdapter(this.devicesListAdapter);

        TextView venueName = (TextView) findViewById(R.id.venueName);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            venueName.setText(extras.getString(OGConstants.venueNameExtra));
            String venueUUID = extras.getString(OGConstants.venueUUIDExtra);

            Applejack.getInstance().getDevices(this, venueUUID, devicesCallback);
        }
    }
}
