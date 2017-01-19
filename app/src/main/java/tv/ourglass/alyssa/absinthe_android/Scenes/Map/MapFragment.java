package tv.ourglass.alyssa.absinthe_android.Scenes.Map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import tv.ourglass.alyssa.absinthe_android.Models.SharedPrefsManager;
import tv.ourglass.alyssa.absinthe_android.R;
import tv.ourglass.alyssa.absinthe_android.Scenes.Settings.SettingsListAdapter;
import tv.ourglass.alyssa.absinthe_android.Scenes.Settings.SettingsListOption;

public class MapFragment extends Fragment {

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
                googleMap.setMyLocationEnabled(true);

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
}
