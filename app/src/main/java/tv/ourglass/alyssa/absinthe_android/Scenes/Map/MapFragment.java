package tv.ourglass.alyssa.absinthe_android.Scenes.Map;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;
import tv.ourglass.alyssa.absinthe_android.Networking.Applejack;
import tv.ourglass.alyssa.absinthe_android.R;

public class MapFragment extends Fragment {

    String TAG = "MapFragment";

    ArrayList<LocationListOption> locationList = new ArrayList<>();

    MapView mMapView;

    private GoogleMap googleMap;

    LocationListAdapter locationListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_locations, container, false);

        this.locationListAdapter = new LocationListAdapter(getActivity(), locationList);

        // TODO: if user connects to internet, try to get venues

        // TODO: this doesn't work
        Applejack.getInstance().getVenues(getActivity(), new Applejack.HttpCallback() {
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

                     Log.d(TAG, jsonStr);

                     Log.d(TAG, String.format("%d venues found!", locationArray.length()));

                     for (int i = 0; i < locationArray.length(); i++) {
                         JSONObject o = locationArray.getJSONObject(i);

                         // Get name
                         String name = o.getString("name");

                         // Get location
                         JSONObject addr = (JSONObject)o.get("address");
                         String location = String.format("%s, %s, %s, %s", addr.getString("street"),
                                 addr.getString("city"), addr.getString("state"), addr.getString("zip"));

                         // Get coordinates
                         /*JSONObject geoloc = (JSONObject)o.get("geolocation");
                         long latitude = geoloc.getLong("latitude");
                         long longitude = geoloc.getLong("longitude");*/
                         //Log.d(TAG, o.)

                         // Add to array
                         locationList.add(new LocationListOption(name, location, 0, 0));
                     }

                     getActivity().runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             locationListAdapter.notifyDataSetChanged();
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
                 }
            }
        });

        // Attach the adapter to a ListView
        ListView listView = (ListView) rootView.findViewById(R.id.locationList);
        listView.setAdapter(this.locationListAdapter);

        // Set up map
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        // TDOD: pop up alert if google maps not available

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                try {
                    googleMap.setMyLocationEnabled(true);
                } catch (SecurityException e) {
                    Log.d(TAG, e.getLocalizedMessage());
                }

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(-34, 151);
                googleMap.addMarker(new MarkerOptions()
                        .position(sydney).title("Marker Title").snippet("Marker Description"));

            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public void showAlert(String title, String message) {
        AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alert.show();
    }
}
