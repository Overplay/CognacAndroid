package tv.ourglass.alyssa.bourbon_android.Scenes.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tv.ourglass.alyssa.bourbon_android.Models.OGConstants;
import tv.ourglass.alyssa.bourbon_android.R;


/**
 * Created by atorres on 1/11/17.
 */

public class LocationListAdapter extends ArrayAdapter<LocationListOption> {
    private Context context;

    public LocationListAdapter(Context context, ArrayList<LocationListOption> locations) {
        super(context, 0, locations);
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LocationListOption location = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.location_option, parent, false);
        }

        TextView name = (TextView) view.findViewById(R.id.name);
        if (location != null) {
            name.setText(location.name);
        }

        TextView icon = (TextView) view.findViewById(R.id.address);
        if (location != null) {
            icon.setText(location.address);
        }

        view.setTag(location);

        // Add actions to the options
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation clickAnimation = new AlphaAnimation(1.0f, 0.3f);
                clickAnimation.setDuration(300);
                view.startAnimation(clickAnimation);
                LocationListOption location = (LocationListOption) view.getTag();

                if (location.marker != null) {
                    location.marker.showInfoWindow();
                }
            }
        });

        return view;

    }
}
