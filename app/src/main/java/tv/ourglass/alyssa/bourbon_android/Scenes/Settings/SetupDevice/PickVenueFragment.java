package tv.ourglass.alyssa.bourbon_android.Scenes.Settings.SetupDevice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import tv.ourglass.alyssa.bourbon_android.Model.BourbonNotification;
import tv.ourglass.alyssa.bourbon_android.Model.OGVenue.OGVenue;
import tv.ourglass.alyssa.bourbon_android.Model.OGVenue.OGVenueListAdapter;
import tv.ourglass.alyssa.bourbon_android.Model.OGVenue.OGVenueType;
import tv.ourglass.alyssa.bourbon_android.Model.StateController;
import tv.ourglass.alyssa.bourbon_android.Networking.Applejack;
import tv.ourglass.alyssa.bourbon_android.R;
import tv.ourglass.alyssa.bourbon_android.Scenes.Tabs.MainTabsActivity;

/**
 * Created by atorres on 5/10/17.
 */

public class PickVenueFragment extends Fragment {
    final String TAG = "PickVenueFragment";

    OGVenueListAdapter mVenueListAdapter;

    BroadcastReceiver mBroadcastReceiver;

    Applejack.HttpCallback venueCallback = new Applejack.HttpCallback() {

        @Override
        public void onSuccess(final Response response) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mVenueListAdapter.notifyDataSetChanged();
                }
            });
            response.body().close();
        }

        @Override
        public void onFailure(Call call, final IOException e, Applejack.ApplejackError error) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "Error retrieving venues", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mVenueListAdapter.notifyDataSetChanged();
                    }
                });
            }
        };

        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(BourbonNotification.myVenuesUpdated.name()));

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pick_venue, container, false);

        // set up top toolbar
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("Pick a venue");
        ((MainTabsActivity) getActivity()).setSupportActionBar(toolbar);

        // set up venue list
        mVenueListAdapter = new OGVenueListAdapter(getActivity(), OGVenueType.OWNED,
                new OGVenueListAdapter.OnClickVenue() {
                    @Override
                    public void onClick(View view, OGVenue venue) {
                        ((MainTabsActivity) getActivity()).openNewFragment(
                                SetupDeviceFragment.newInstance(venue));
                    }
                }, View.VISIBLE);
        ListView listView = (ListView) rootView.findViewById(R.id.venueList);
        listView.setAdapter(mVenueListAdapter);

        // set empty view for list
        TextView empty = (TextView) rootView.findViewById(R.id.empty);
        listView.setEmptyView(empty);

        // get venues
        StateController.getInstance().findMyVenues(venueCallback);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                ((MainTabsActivity) getActivity()).openNewFragment(new CreateVenueFragment());
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
