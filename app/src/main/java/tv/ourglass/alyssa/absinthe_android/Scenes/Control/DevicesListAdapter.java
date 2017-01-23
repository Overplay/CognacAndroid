package tv.ourglass.alyssa.absinthe_android.Scenes.Control;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tv.ourglass.alyssa.absinthe_android.Models.OGConstants;
import tv.ourglass.alyssa.absinthe_android.R;

/**
 * Created by atorres on 11/8/16.
 */

public class DevicesListAdapter extends ArrayAdapter<OGDevice> {

    Context context;

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
            name.setText(device.systemName);
        }

        TextView location = (TextView) view.findViewById(R.id.location);
        if (device != null) {
            location.setText(device.location);
        }

        TextView ipAddress = (TextView) view.findViewById(R.id.ipAddress);
        if (device != null) {
            ipAddress.setText(device.ipAddress);
        }

        Typeface font = Typeface.createFromAsset(context.getAssets(), OGConstants.semiBoldFont);
        name.setTypeface(font);
        number.setTypeface(font);

        font = Typeface.createFromAsset(context.getAssets(), OGConstants.lightFont);
        location.setTypeface(font);
        ipAddress.setTypeface(font);

        view.setTag(device);

        // Add actions to the options
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OGDevice device = (OGDevice) view.getTag();
                Log.d("Device", "Chose device");
                Intent intent = new Intent(context, DeviceViewActivity.class);
                intent.putExtra(OGConstants.deviceUrlExtra, device.getUrl());
                intent.putExtra(OGConstants.deviceNameExtra, device.systemName);
                context.startActivity(intent);
            }
        });

        return view;

    }

    private void goTo(Class dest) {
        Intent intent = new Intent(context, dest);
        context.startActivity(intent);
    }
}
