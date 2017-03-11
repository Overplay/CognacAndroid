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

public class DevicesViewActivity extends AppCompatActivity {

    String TAG = "DevicesViewActivity";

    ArrayList<OGDevice> devices = new ArrayList<>();

    DevicesListAdapter devicesListAdapter;

    Applejack.HttpCallback devicesCallback = new Applejack.HttpCallback() {
        @Override
        public void onFailure(Call call, final IOException e) {
            DevicesViewActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(DevicesViewActivity.this, "Error retrieving devices", Toast.LENGTH_SHORT).show();
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

                    // Get name
                    String name = device.getString("name");
                    DevicesViewActivity.this.devices.add(new OGDevice(name));
                }

                DevicesViewActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DevicesViewActivity.this.devicesListAdapter.notifyDataSetChanged();
                    }
                });

            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage());
                DevicesViewActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DevicesViewActivity.this, "Error retrieving venues", Toast.LENGTH_SHORT).show();
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

        // set venue name at top
        TextView venueName = (TextView) findViewById(R.id.venueName);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            venueName.setText(extras.getString(OGConstants.venueNameExtra));
        }

        // get devices
        if (extras != null) {
            String venueUUID = extras.getString(OGConstants.venueUUIDExtra);

            Applejack.getInstance().getDevices(this, venueUUID, devicesCallback);
        }
    }
}
