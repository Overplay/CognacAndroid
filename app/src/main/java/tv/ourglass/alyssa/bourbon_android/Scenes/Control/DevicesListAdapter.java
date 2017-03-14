package tv.ourglass.alyssa.bourbon_android.Scenes.Control;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tv.ourglass.alyssa.bourbon_android.Models.OGConstants;
import tv.ourglass.alyssa.bourbon_android.Models.OGDevice;
import tv.ourglass.alyssa.bourbon_android.R;

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
    public View getView(int position, View view, ViewGroup parent) {
        OGDevice device = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.device_option, parent, false);
        }

        TextView number = (TextView) view.findViewById(R.id.number);
        if (device != null) {
            number.setText(String.format("%02d", position + 1));
        }

        TextView name = (TextView) view.findViewById(R.id.name);
        if (device != null) {
            name.setText(device.name);
        }

        view.setTag(device);

        // Add actions to the options
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OGDevice device = (OGDevice) view.getTag();

                Intent intent = new Intent(context, DeviceViewActivity.class);

                if (device != null) {
                    intent.putExtra(OGConstants.deviceNameExtra, device.name);
                    intent.putExtra(OGConstants.deviceUrlExtra, device.getUrl());
                    context.startActivity(intent);

                } else {
                    Log.e(TAG, "selected a device that is null");
                }
            }
        });

        return view;

    }
}
