package tv.ourglass.alyssa.bourbon_android.Model.OGVenue;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tv.ourglass.alyssa.bourbon_android.Model.StateController;
import tv.ourglass.alyssa.bourbon_android.R;

/**
 * List adapter for OGVenues.
 *
 * Created by atorres on 5/13/17.
 */
public class OGVenueListAdapter extends BaseAdapter {

    public interface OnClickVenue {
        void onClick(View view, OGVenue venue);
    }

    String TAG = "OGVenueListAdapter";

    private Context context;

    private OGVenueType type;

    private OnClickVenue onClickVenue;

    private int indicatorVisibility = View.GONE;

    public OGVenueListAdapter(Context context, OGVenueType type, OnClickVenue onClickVenue) {
        this.context = context;
        this.type = type;
        this.onClickVenue = onClickVenue;
    }

    public OGVenueListAdapter(Context context, OGVenueType type, OnClickVenue onClickVenue,
                              int indicatorVisibility) {
        this.context = context;
        this.type = type;
        this.onClickVenue = onClickVenue;
        this.indicatorVisibility = indicatorVisibility;
    }

    @Override
    public int getCount() {
        switch(type) {
            case ALL:
                return StateController.getInstance().getAllVenues().size();

            case MINE:
                int count = 0;
                for (StateController.VenueCollection vc: StateController.getInstance().getMyVenues()) {
                    count += vc.venues.size();
                }
                return count;

            case OWNED:
                return StateController.getInstance().getOwnedVenues().size();

            case MANAGED:
                return StateController.getInstance().getManagedVenues().size();

            default:
                return 0;
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final OGVenue venue = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.ogvenue_option, parent, false);
        }

        TextView name = (TextView) view.findViewById(R.id.name);
        TextView address = (TextView) view.findViewById(R.id.address);
        if (venue != null) {
            name.setText(venue.name);
            address.setText(venue.getAddress());
        }

        // put in section header if we should
        if (type == OGVenueType.MINE) {
            ArrayList<StateController.VenueCollection> myVenues = StateController.getInstance().getMyVenues();
            TextView header = (TextView) view.findViewById(R.id.sectionHeader);

            if (position == 0) {  // first entry of first section
                header.setText(myVenues.get(0).label);
                header.setVisibility(View.VISIBLE);

            } else if (position == myVenues.get(0).venues.size()) {  // first entry of second section
                header.setText(myVenues.get(1).label);
                header.setVisibility(View.VISIBLE);

            } else {
                header.setVisibility(View.GONE);
            }
        }

        // set visibility of arrow indicator
        ImageView indicator = (ImageView) view.findViewById(R.id.indicator);
        indicator.setVisibility(indicatorVisibility);

        view.setTag(venue);

        // set on click listener
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickVenue.onClick(v, venue);
            }
        });

        return view;
    }

    @Override
    public OGVenue getItem(int i) {
        switch(type) {
            case ALL:
                return StateController.getInstance().getAllVenues().get(i);

            case MINE:
                ArrayList<StateController.VenueCollection> myVenues = StateController.getInstance().getMyVenues();
                int venuesSize = myVenues.get(0).venues.size();
                if (i < venuesSize) { // get venue from the first collection
                    return myVenues.get(0).venues.get(i);

                } else { // get venue from second collection
                    return myVenues.get(1).venues.get(i - venuesSize);
                }

            case OWNED:
                return StateController.getInstance().getOwnedVenues().get(i);

            case MANAGED:
               return StateController.getInstance().getManagedVenues().get(i);

            default:
                return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean isEmpty() {
        switch(type) {
            case ALL:
                return StateController.getInstance().getAllVenues().isEmpty();

            case MINE:
                boolean isEmpty = true;
                for (StateController.VenueCollection vc : StateController.getInstance().getMyVenues()) {
                    isEmpty &= vc.venues.isEmpty();
                }
                return isEmpty;

            case OWNED:
                return StateController.getInstance().getOwnedVenues().isEmpty();

            case MANAGED:
                return StateController.getInstance().getManagedVenues().isEmpty();

            default:
                return true;
        }
    }
}
