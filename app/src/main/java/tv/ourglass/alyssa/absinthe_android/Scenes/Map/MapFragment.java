package tv.ourglass.alyssa.absinthe_android.Scenes.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;
import tv.ourglass.alyssa.absinthe_android.Models.SharedPrefsManager;
import tv.ourglass.alyssa.absinthe_android.Networking.Applejack;
import tv.ourglass.alyssa.absinthe_android.R;
import tv.ourglass.alyssa.absinthe_android.Scenes.Registration.LoginActivity;
import tv.ourglass.alyssa.absinthe_android.Scenes.Settings.SettingsListAdapter;
import tv.ourglass.alyssa.absinthe_android.Scenes.Settings.SettingsListOption;
import tv.ourglass.alyssa.absinthe_android.Scenes.Tabs.MainTabsActivity;

public class MapFragment extends Fragment {

    String TAG = "MapFragment";

    private String[] names = {
            "Lola's",
            "Paddy's Pub",
            "Novo",
            "New York Lounge",
            "The Drunkard"
    };

    private String[] addresses = {
            "1234 Main St, San Jose, CA 95120",
            "24 Main St, New York City, NY 12345",
            "88 Abc Lane, Boston, MA 12335",
            "334 Higuera St, San Luis Obispo, CA 93405",
            "6789 24th St, Chicago IL, 12345"
    };

    MapView mMapView;

    private GoogleMap googleMap;

    ProgressDialog progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_locations, container, false);

        // Set up list of venues
        ArrayList<LocationListOption> locations = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            locations.add(new LocationListOption(names[i], addresses[i]));
        }

        progress = ProgressDialog.show(getActivity(), "Finding venues...", "", true);

        // TODO: this doesn't work
        Applejack.getInstance().getVenues(getActivity(), new Applejack.HttpCallback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        Toast.makeText(getActivity(), "Error retrieving venues", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onSuccess(final Response response) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        Log.d(TAG, response.toString());

                        try {
                            Log.d(TAG, response.body().string());
                            String jsonStr = response.body().string();
                            Log.d(TAG, jsonStr);
                            //JSONArray venueArray = new JSONArray(jsonStr);

                            /*for (int i = 0; i < venueArray.length(); i++) {
                                JSONObject venue = venueArray.getJSONObject(i);
                                Log.d(TAG, venue.getString("name"));
                            }*/

                        } catch (Exception e) {
                            Log.e(TAG, "error reading json " + e.getLocalizedMessage());
                            Toast.makeText(getActivity(), "Error retrieving venues", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // Create the adapter to convert the array to views
        LocationListAdapter adapter = new LocationListAdapter(getActivity(), locations);

        // Attach the adapter to a ListView
        ListView listView = (ListView) rootView.findViewById(R.id.locationList);
        listView.setAdapter(adapter);

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

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
