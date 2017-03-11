package tv.ourglass.alyssa.bourbon_android.Scenes.Control;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;
import tv.ourglass.alyssa.bourbon_android.Models.OGVenue;
import tv.ourglass.alyssa.bourbon_android.Networking.Applejack;
import tv.ourglass.alyssa.bourbon_android.R;
import tv.ourglass.alyssa.bourbon_android.Scenes.Map.LocationListAdapter;


public class ChooseVenueFragment extends Fragment {

    String TAG = "ChooseVenueFragment";

    ArrayList<OGVenue> venues = new ArrayList<>();

    VenueListAdapter venueListAdapter;

    Applejack.HttpCallback venueCallback = new Applejack.HttpCallback() {
        @Override
        public void onFailure(Call call, final IOException e) {
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
                String jsonStr = response.body().string();
                JSONArray locationArray = new JSONArray(jsonStr);

                Log.d(TAG, String.format("%d venues found!", locationArray.length()));

                for (int i = 0; i < locationArray.length(); i++) {
                    JSONObject o = locationArray.getJSONObject(i);

                    // Get name
                    String name = o.getString("name");

                    // Get address
                    JSONObject addr = o.getJSONObject("address");
                    String location = String.format("%s, %s, %s %s", addr.getString("street"),
                            addr.getString("city"), addr.getString("state"), addr.getString("zip"));

                    // Get uuid
                    String uuid = o.getString("uuid");

                    // Get geolocation
                    try {
                        JSONObject geoLoc = o.getJSONObject("geolocation");
                        double lat = geoLoc.getDouble("latitude");
                        double lng = geoLoc.getDouble("longitude");

                        // Add to array
                        venues.add(new OGVenue(name, location, lat, lng, uuid));

                    } catch (Exception e) {
                        Log.e(TAG, "found venue with no geolocation, filtering out");
                    }
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        venueListAdapter.notifyDataSetChanged();
                    }
                });

            } catch (Exception e) {
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
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_venues, container, false);

        this.venueListAdapter = new VenueListAdapter(getActivity(), venues);

        // Attach the adapter to a ListView
        ListView listView = (ListView) rootView.findViewById(R.id.venueList);
        listView.setAdapter(this.venueListAdapter);

        Applejack.getInstance().getVenues(getActivity(), venueCallback);

        return rootView;
    }
}
