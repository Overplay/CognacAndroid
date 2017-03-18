package tv.ourglass.alyssa.bourbon_android.Scenes.Control;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tv.ourglass.alyssa.bourbon_android.Models.OGVenue;
import tv.ourglass.alyssa.bourbon_android.R;
import tv.ourglass.alyssa.bourbon_android.Scenes.Tabs.MainTabsActivity;

/**
 * Created by atorres on 3/10/17.
 */

public class VenueListAdapter extends ArrayAdapter<OGVenue> {

    String TAG = "VenueListAdapter";

    private Context context;

    public VenueListAdapter(Context context, ArrayList<OGVenue> venues) {
        super(context, 0, venues);
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final OGVenue venue = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.location_option, parent, false);
        }

        TextView name = (TextView) view.findViewById(R.id.name);
        if (venue != null) {
            name.setText(venue.name);
        }

        TextView icon = (TextView) view.findViewById(R.id.address);
        if (venue != null) {
            icon.setText(venue.address);
        }

        view.setTag(venue);

        // show list of devices for venue when it is clicked
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (venue != null) {
                    ((MainTabsActivity) context)
                            .openNewFragment(ChooseDeviceFragment.newInstance(venue.name, venue.uuid));
                } else {
                    Log.e(TAG, "selected a venue that is null");
                }
            }
        });

        return view;

    }
}
