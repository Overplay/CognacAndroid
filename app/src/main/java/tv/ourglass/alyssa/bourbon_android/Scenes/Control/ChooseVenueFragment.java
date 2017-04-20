package tv.ourglass.alyssa.bourbon_android.Scenes.Control;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import okhttp3.Call;
import okhttp3.Response;
import tv.ourglass.alyssa.bourbon_android.Models.OGVenue;
import tv.ourglass.alyssa.bourbon_android.Networking.Applejack;
import tv.ourglass.alyssa.bourbon_android.R;


public class ChooseVenueFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    String TAG = "ChooseVenueFragment";

    ArrayList<OGVenue> mVenues = new ArrayList<>();

    VenueListAdapter mVenueListAdapter;

    GoogleApiClient mLocationClient;  // used to get user's location

    Location mLastLocation;

    Applejack.HttpCallback venueCallback = new Applejack.HttpCallback() {

        @Override
        public void onFailure(Call call, final IOException e, Applejack.ApplejackError error) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "Error retrieving venues", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onSuccess(final Response response) {
            try {
                String venueStr = response.body().string();
                JSONArray venues = new JSONArray(venueStr);
                processVenues(venues);

            } catch (IOException | JSONException e) {
                Log.e(TAG, e.getLocalizedMessage());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Error retrieving venues", Toast.LENGTH_SHORT).show();
                    }
                });

            } finally {
                response.body().close();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (mLocationClient == null) {
            mLocationClient = new GoogleApiClient.Builder(this.getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_venues, container, false);

        mVenueListAdapter = new VenueListAdapter(getActivity(), mVenues);

        // Attach the adapter to a ListView
        ListView listView = (ListView) rootView.findViewById(R.id.venueList);
        listView.setAdapter(mVenueListAdapter);

        this.getVenues();

        return rootView;
    }

    @Override
    public void onStart() {
        mLocationClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        if (mLocationClient != null) {
            mLocationClient.disconnect();
        }
        super.onStop();
    }

    private void getVenues() {
        Applejack.getInstance().getVenues(getActivity(), venueCallback);
    }

    private void sortByLocationAndReload() {
        if (mLastLocation != null) {
            Collections.sort(mVenues, new Comparator<OGVenue>() {
                @Override
                public int compare(OGVenue lhs, OGVenue rhs) {
                    Location a = new Location(LocationManager.GPS_PROVIDER);
                    Location b = new Location(LocationManager.GPS_PROVIDER);

                    a.setLatitude(lhs.latitude);
                    a.setLongitude(lhs.longitude);
                    b.setLatitude(rhs.latitude);
                    b.setLongitude(rhs.longitude);

                    Float distA = a.distanceTo(mLastLocation);
                    Float distB = b.distanceTo(mLastLocation);

                    return distA.compareTo(distB);
                }
            });
        }

        mVenueListAdapter.notifyDataSetChanged();
    }

    private void processVenues(JSONArray venues) {
        final ArrayList<OGVenue> venueList = new ArrayList<>();
        for (int i = 0; i < venues.length(); i++) {
            try {
                JSONObject o = venues.getJSONObject(i);
                String name = o.getString("name");

                JSONObject addr = o.getJSONObject("address");
                String location = String.format("%s, %s, %s %s", addr.getString("street"),
                        addr.getString("city"), addr.getString("state"), addr.getString("zip"));

                String uuid = o.getString("uuid");

                JSONObject geoLoc = o.getJSONObject("geolocation");
                double lat = geoLoc.getDouble("latitude");
                double lng = geoLoc.getDouble("longitude");

                venueList.add(new OGVenue(name, location, lat, lng, uuid));

            } catch (JSONException e) {
                Log.e(TAG, "found venue with missing info, filtering out");
            }
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mVenues.clear();
                mVenues.addAll(venueList);
                sortByLocationAndReload();
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mLocationClient);
            sortByLocationAndReload();

        } catch (SecurityException e) {
            Log.d(TAG, e.getLocalizedMessage());
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        this.getVenues();
    }
}
