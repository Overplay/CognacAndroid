package tv.ourglass.alyssa.bourbon_android.Scenes.Settings.SetupDevice;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;
import tv.ourglass.alyssa.bourbon_android.Model.Yelp.YelpVenue;
import tv.ourglass.alyssa.bourbon_android.Model.Yelp.YelpVenueListAdapter;
import tv.ourglass.alyssa.bourbon_android.Networking.OGCloud;
import tv.ourglass.alyssa.bourbon_android.Networking.SingleShotLocationProvider;
import tv.ourglass.alyssa.bourbon_android.R;
import tv.ourglass.alyssa.bourbon_android.Scenes.Tabs.MainTabsActivity;

/**
 * Performs a Yelp search and displays the results.
 *
 * Created by atorres on 5/17/17.
 */
public class FindYelpVenueFragment extends Fragment {

    static String TAG = "FindYelpVenueFragment";

    String searchTerm;

    String searchLocation;

    Boolean useCurrentLocation = false;

    ProgressBar mProgressIndicator;

    ListView mListView;

    TextView mEmptyView;

    ArrayList<YelpVenue> mVenues = new ArrayList<>();

    YelpVenueListAdapter mVenueAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_find_yelp_venue, container, false);

        // set up toolbar
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("Find venue");
        ((MainTabsActivity) getActivity()).setSupportActionBar(toolbar);

        // set up progress spinner
        mProgressIndicator = (ProgressBar) rootView.findViewById(R.id.progress);
        mProgressIndicator.setVisibility(View.VISIBLE);

        // set up Yelp venue list
        mVenueAdapter = new YelpVenueListAdapter(getActivity(), mVenues, new YelpVenueListAdapter.OnClickVenue() {
            @Override
            public void onClick(View view, YelpVenue venue) {
                ((MainTabsActivity) getActivity()).setSelectedYelpVenue(venue);
                getActivity().onBackPressed();
            }
        });
        mListView = (ListView) rootView.findViewById(R.id.yelpVenueList);
        mListView.setAdapter(mVenueAdapter);
        mEmptyView = (TextView) rootView.findViewById(R.id.empty);

        // find yelp venues
        if (searchTerm != null && searchLocation != null) {
            OGCloud.getInstance().yelpSearch(getActivity(), searchTerm, searchLocation, yelpSearchCb);

        } else if (searchTerm != null && useCurrentLocation) {
            SingleShotLocationProvider.requestSingleUpdate(getActivity(),
                    new SingleShotLocationProvider.LocationCallback() {
                @Override
                public void onLocationAvailable(Location location) {
                    OGCloud.getInstance().yelpSearch(getActivity(), searchTerm,
                            location.getLatitude(), location.getLongitude(), yelpSearchCb);
                }

                @Override
                public void onLocationFailure() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressIndicator.setVisibility(View.GONE);
                            mListView.setEmptyView(mEmptyView);
                        }
                    });
                }
            });

        } else {
            mProgressIndicator.setVisibility(View.GONE);
            mListView.setEmptyView(mEmptyView);
        }

        return rootView;
    }

    OGCloud.HttpCallback yelpSearchCb = new OGCloud.HttpCallback() {
        @Override
        public void onSuccess(Response response) {
            try {
                JSONArray venues = (new JSONObject(response.body().string())).getJSONArray("businesses");
                for (int i = 0; i < venues.length(); i++) {
                    mVenues.add(new YelpVenue(venues.getJSONObject(i)));
                }

            } catch (IOException | JSONException e) {
                Log.e(TAG, e.getLocalizedMessage());

            } finally {
                response.body().close();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressIndicator.setVisibility(View.GONE);
                        mListView.setEmptyView(mEmptyView);
                        mVenueAdapter.notifyDataSetChanged();
                    }
                });
            }
        }

        @Override
        public void onFailure(Call call, IOException e, OGCloud.OGCloudError error) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressIndicator.setVisibility(View.GONE);
                    mListView.setEmptyView(mEmptyView);
                    mVenueAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    public static FindYelpVenueFragment newInstance(String searchTerm, String location) {
        FindYelpVenueFragment findYelpVenueFragment = new FindYelpVenueFragment();
        findYelpVenueFragment.searchTerm = searchTerm;
        findYelpVenueFragment.searchLocation = location;
        return findYelpVenueFragment;
    }

    public static FindYelpVenueFragment newInstance(String searchTerm) {
        FindYelpVenueFragment findYelpVenueFragment = new FindYelpVenueFragment();
        findYelpVenueFragment.searchTerm = searchTerm;
        findYelpVenueFragment.useCurrentLocation = true;
        return findYelpVenueFragment;
    }
}