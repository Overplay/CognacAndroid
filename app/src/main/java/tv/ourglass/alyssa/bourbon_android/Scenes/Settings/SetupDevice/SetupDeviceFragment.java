package tv.ourglass.alyssa.bourbon_android.Scenes.Settings.SetupDevice;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import tv.ourglass.alyssa.bourbon_android.Model.OGVenue.OGVenue;
import tv.ourglass.alyssa.bourbon_android.R;
import tv.ourglass.alyssa.bourbon_android.Scenes.Tabs.MainTabsActivity;

/**
 * Created by atorres on 5/17/17.
 */

public class SetupDeviceFragment extends Fragment {
    String TAG = "SetupDeviceFragment";

    OGVenue venue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setup_device, container, false);

        // set up toolbar
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("Setup Device");
        ((MainTabsActivity) getActivity()).setSupportActionBar(toolbar);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);

        // hide menu items we don't want here
        MenuItem add = menu.findItem(R.id.action_add);
        add.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    public static SetupDeviceFragment newInstance(OGVenue venue) {
        SetupDeviceFragment setupDeviceFragment = new SetupDeviceFragment();
        setupDeviceFragment.venue = venue;
        return setupDeviceFragment;
    }
}
