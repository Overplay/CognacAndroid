package tv.ourglass.ourglasstv.Model.Yelp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tv.ourglass.alyssa.ourglasstv.R;

/**
 * List adapter for Yelp venues.
 *
 * Created by atorres on 5/20/17.
 */
public class YelpVenueListAdapter extends ArrayAdapter<YelpVenue> {
    String TAG = "YelpVenueListAdapter";

    private Context context;

    private OnClickVenue onClickVenue;

    public interface OnClickVenue {
        void onClick(View view, YelpVenue venue);
    }

    public YelpVenueListAdapter(Context context, ArrayList<YelpVenue> venues, OnClickVenue onClickVenue) {
        super(context, 0, venues);
        this.context = context;
        this.onClickVenue = onClickVenue;
    }

    @Override @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        final YelpVenue venue = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.yelp_venue, parent, false);
        }

        ((TextView) view.findViewById(R.id.name)).setText(venue.ogVenue.name);

        ((TextView) view.findViewById(R.id.address)).setText(venue.ogVenue.getAddress());

        ((TextView) view.findViewById(R.id.distance)).setText(venue.distance);

        ImageView image = (ImageView) view.findViewById(R.id.venue_image);
        int imgDim = (int) getContext().getResources().getDimension(R.dimen.yelp_image_size);

        if (!venue.imageUrl.trim().isEmpty()) {
            Picasso.with(getContext())
                    .load(venue.imageUrl)
                    .resize(imgDim, imgDim)
                    .onlyScaleDown()
                    .centerCrop()
                    .into(image);
        }

        view.setTag(venue);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickVenue.onClick(view, venue);
            }
        });

        return view;
    }
}
