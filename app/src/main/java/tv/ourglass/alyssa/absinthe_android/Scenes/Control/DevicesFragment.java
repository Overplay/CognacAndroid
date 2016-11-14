package tv.ourglass.alyssa.absinthe_android.Scenes.Control;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import tv.ourglass.alyssa.absinthe_android.Models.OGConstants;
import tv.ourglass.alyssa.absinthe_android.Networking.NetUtils;
import tv.ourglass.alyssa.absinthe_android.R;

public class DevicesFragment extends Fragment {

    private String[] names = {
            "Over bar",
            "Patio",
            "Back room",
            "Main bar",
            "Pool table"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_devices, container, false);

        // Set fonts
        TextView text = (TextView)view.findViewById(R.id.networkLabel);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), OGConstants.regularFont);
        if (text != null) {
            text.setTypeface(font);
        }

        text = (TextView)view.findViewById(R.id.networkName);
        font = Typeface.createFromAsset(getActivity().getAssets(), OGConstants.semiBoldFont);
        if (text != null) {
            text.setTypeface(font);
            text.setText(NetUtils.getCurrentSSID(getContext()));
        }

        // Construct the data source
        ArrayList<OGDevice> devices = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            devices.add(new OGDevice(names[i], "location", "0.0.0.0", 1));
        }

        // Create the adapter to convert the array to views
        DevicesListAdapter adapter = new DevicesListAdapter(getActivity(), devices);

        // Attach the adapter to a ListView
        ListView listView = (ListView) view.findViewById(R.id.devicesList);
        listView.setAdapter(adapter);

        // Attach text to display when list is empty
        TextView empty = (TextView) view.findViewById(R.id.empty);
        font = Typeface.createFromAsset(getActivity().getAssets(), OGConstants.lightFont);
        empty.setTypeface(font);
        listView.setEmptyView(empty);

        return view;
    }
}