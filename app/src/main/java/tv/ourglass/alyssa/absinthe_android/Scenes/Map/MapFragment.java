package tv.ourglass.alyssa.absinthe_android.Scenes.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
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
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tv.ourglass.alyssa.absinthe_android.AbsintheApplication;
import tv.ourglass.alyssa.absinthe_android.Models.OGConstants;
import tv.ourglass.alyssa.absinthe_android.Networking.Applejack;
import tv.ourglass.alyssa.absinthe_android.R;

public class MapFragment extends Fragment {

    String TAG = "MapFragment";

    ArrayList<LocationListOption> locationList = new ArrayList<>();

    MapView mMapView;

    private GoogleMap googleMap;

    LocationListAdapter locationListAdapter;

    OkHttpClient okclient = AbsintheApplication.okclient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_locations, container, false);

        this.locationListAdapter = new LocationListAdapter(getActivity(), locationList);

        // TODO: if user connects to internet, try to get venues

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

                     Log.d(TAG, String.format("%d venues found!", locationArray.length()));

                     for (int i = 0; i < locationArray.length(); i++) {
                         JSONObject o = locationArray.getJSONObject(i);

                         // Get name
                         String name = o.getString("name");

                         // Get location
                         JSONObject addr = (JSONObject)o.get("address");
                         String location = String.format("%s, %s, %s %s", addr.getString("street"),
                                 addr.getString("city"), addr.getString("state"), addr.getString("zip"));

                         // Get coordinates
                         /*JSONObject geoloc = (JSONObject)o.get("geolocation");
                         long latitude = geoloc.getLong("latitude");
                         long longitude = geoloc.getLong("longitude");*/

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

                response.body().close();
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

                // Show markers for found venues
                for (final LocationListOption loc : locationList) {
                    getGeolocation(loc.address, new Applejack.HttpCallback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e(TAG, e.getLocalizedMessage());
                        }

                        @Override
                        public void onSuccess(Response response) {
                            try {
                                String jsonStr = response.body().string();
                                JSONObject responseObj = new JSONObject(jsonStr);
                                JSONObject result = responseObj.getJSONArray("results").getJSONObject(0);
                                final JSONObject location = result.getJSONObject("geometry").getJSONObject("location");

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        try {
                                            LatLng markerLoc = new LatLng(location.getDouble("lat"), location.getDouble("lng"));
                                            googleMap.addMarker(new MarkerOptions()
                                                    .position(markerLoc)
                                                    .title(loc.name)
                                                    .snippet(loc.address));

                                        } catch (JSONException e) {
                                            Log.e(TAG, e.getLocalizedMessage());
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                Log.e(TAG, e.getLocalizedMessage());
                            }

                            response.body().close();
                        }
                    });
                }

                // Zoom into current location
                /*LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
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
                    }

                } catch (SecurityException e) {
                    Log.d(TAG, e.getLocalizedMessage());
                }*/
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

    private void getGeolocation(String address, final Applejack.HttpCallback cb) {
        String formattedAddr = address.replaceAll("\\s", "+");

        Request req = new Request.Builder()
                .url(OGConstants.googleGeocodingRequestBase + formattedAddr
                        + "&key=" + OGConstants.googleGeocodingAPIKey)
                .get()
                .build();

        okclient.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                cb.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (!response.isSuccessful()) {
                    Log.e(TAG, "geocoding request not successful");
                    cb.onFailure(call, null);
                    response.body().close();

                } else {
                    cb.onSuccess(response);
                }
            }
        });
    }
}
