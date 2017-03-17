package tv.ourglass.alyssa.bourbon_android.Scenes.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tv.ourglass.alyssa.bourbon_android.BourbonApplication;
import tv.ourglass.alyssa.bourbon_android.Models.OGConstants;
import tv.ourglass.alyssa.bourbon_android.Models.OGVenue;
import tv.ourglass.alyssa.bourbon_android.Networking.Applejack;
import tv.ourglass.alyssa.bourbon_android.R;

public class MapFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener {

    String TAG = "MapFragment";

    ArrayList<OGVenue> locationList = new ArrayList<>();

    MapView mMapView;

    private GoogleMap googleMap;

    LocationListAdapter locationListAdapter;

    private BroadcastReceiver mWifiBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "got network change!");

            if (!intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
                Applejack.getInstance().getVenues(getActivity(), venueCallback);
            }
        }
    };

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
                    final String name = o.getString("name");

                    // Get address
                    JSONObject addr = o.getJSONObject("address");
                    final String location = String.format("%s, %s, %s %s", addr.getString("street"),
                            addr.getString("city"), addr.getString("state"), addr.getString("zip"));

                    // Get uuid
                    final String uuid = o.getString("uuid");

                    // Get geolocation
                    try {
                        JSONObject geoLoc = o.getJSONObject("geolocation");
                        final double lat = geoLoc.getDouble("latitude");
                        final double lng = geoLoc.getDouble("longitude");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Add to array
                                locationList.add(new OGVenue(name, location, lat, lng, uuid));
                                locationListAdapter.notifyDataSetChanged();
                            }
                        });

                    } catch (Exception e) {
                        Log.e(TAG, "found venue with no geolocation, filtering out");
                    }
                }

                displayVenuesOnMap();

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
        View rootView = inflater.inflate(R.layout.fragment_locations, container, false);

        // get notified when network connectivity changes
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(mWifiBroadcastReceiver, intentFilter);

        this.locationListAdapter = new LocationListAdapter(getActivity(), locationList);

        // Attach the adapter to a ListView
        ListView listView = (ListView) rootView.findViewById(R.id.locationList);
        listView.setAdapter(this.locationListAdapter);

        // Set up map
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        // TODO: pop up alert if google maps not available

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Applejack.getInstance().getVenues(getActivity(), venueCallback);

        return rootView;
    }

    public void displayVenuesOnMap() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap mMap) {
                        googleMap = mMap;

                        googleMap.setOnInfoWindowClickListener(MapFragment.this);

                        // For showing a move to my location button
                        try {
                            googleMap.setMyLocationEnabled(true);
                        } catch (SecurityException e) {
                            Log.d(TAG, e.getLocalizedMessage());
                        }

                        // Show markers for found venues
                        for (OGVenue loc : locationList) {

                            LatLng markerLoc = new LatLng(loc.latitude, loc.longitude);
                            loc.marker = googleMap.addMarker(new MarkerOptions()
                                    .position(markerLoc)
                                    .title(loc.name)
                                    .snippet(loc.address));

                            locationListAdapter.notifyDataSetChanged(); // because we added the marker
                        }

                        // try to center map on current location
                        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                        Criteria criteria = new Criteria();

                        try {
                            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

                            if (location != null) {
                                LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());

                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(pos)
                                        .zoom(12)
                                        .build();

                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            } else {
                                // center map on US
                                LatLng pos = new LatLng(37.0902, -95.7129);

                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(pos)
                                        .zoom(3.5f)
                                        .build();

                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            }

                        } catch (SecurityException e) {
                            Log.e(TAG, e.getLocalizedMessage());

                            // center map on US
                            LatLng pos = new LatLng(37.0902, -95.7129);

                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(pos)
                                    .zoom(3.5f)
                                    .build();

                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                    }
                });
            }
        });
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

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.d(TAG, "marker clicked");
        LatLng pos = marker.getPosition();

        Uri mapIntentUri = Uri.parse(String.format("google.navigation:q=%f,%f", pos.latitude, pos.longitude));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            showAlert("Uh oh!", "We couldn't find a navigation app installed on your device.");
        }
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
