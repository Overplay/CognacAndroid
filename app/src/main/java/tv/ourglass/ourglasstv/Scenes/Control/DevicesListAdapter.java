package tv.ourglass.ourglasstv.Scenes.Control;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import tv.ourglass.ourglasstv.Model.OGDevice;
import tv.ourglass.alyssa.ourglasstv.R;
import tv.ourglass.ourglasstv.Scenes.Tabs.MainTabsActivity;

/**
 * Created by atorres on 11/8/16.
 */

public class DevicesListAdapter extends ArrayAdapter<OGDevice> {

    String TAG = "DevicesListAdapter";

    private Context context;

    public DevicesListAdapter(Context context, ArrayList<OGDevice> devices) {
        super(context, 0, devices);
        this.context = context;
    }

    @Override
    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        OGDevice device = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.device_option, parent, false);
        }

        if (device != null) {
            ((TextView) view.findViewById(R.id.number)).setText(String.format(Locale.ENGLISH, "%02d", position + 1));
            ((TextView) view.findViewById(R.id.name)).setText(device.name);

            if (device.isActive) {
                ((TextView) view.findViewById(R.id.station)).setText(device.stationName);
            } else {
                ((TextView) view.findViewById(R.id.station)).setText(R.string.offline);
            }
        }

        view.setTag(device);

        // go to device control view when a device is selected
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OGDevice device = (OGDevice) view.getTag();
                if (device != null) {
                    ((MainTabsActivity) context)
                            .openNewFragment(DeviceViewFragment.newInstance(device.name, device.getUrl()));
                } else {
                    Log.e(TAG, "selected a device that is null");
                }
            }
        });

        return view;

    }
}
