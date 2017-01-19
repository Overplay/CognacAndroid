package tv.ourglass.alyssa.absinthe_android.Scenes.Control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import tv.ourglass.alyssa.absinthe_android.Models.OGConstants;
import tv.ourglass.alyssa.absinthe_android.Networking.NetUtils;
import tv.ourglass.alyssa.absinthe_android.Networking.OGDPBroadcastReceiver;
import tv.ourglass.alyssa.absinthe_android.Networking.OGDPService;
import tv.ourglass.alyssa.absinthe_android.R;

public class DevicesFragment extends Fragment {

    String TAG = "DevicesFragment";

    ArrayList<OGDevice> devices = new ArrayList<>();

    DevicesListAdapter devicesListAdapter;

    private BroadcastReceiver mBroadcastRcvr = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            devices = OGDPService.getInstance().devices;
            devicesListAdapter.notifyDataSetChanged();
        }
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

        devicesListAdapter = new DevicesListAdapter(getActivity(), devices);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mBroadcastRcvr,
                new IntentFilter("ogdp"));

        // Attach the adapter to a ListView
        ListView listView = (ListView) view.findViewById(R.id.devicesList);
        listView.setAdapter(devicesListAdapter);

        // Attach text to display when list is empty
        TextView empty = (TextView) view.findViewById(R.id.empty);
        font = Typeface.createFromAsset(getActivity().getAssets(), OGConstants.lightFont);
        empty.setTypeface(font);
        listView.setEmptyView(empty);

        return view;
    }
}