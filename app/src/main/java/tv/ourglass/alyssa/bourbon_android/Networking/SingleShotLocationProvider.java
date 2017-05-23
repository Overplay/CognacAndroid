package tv.ourglass.alyssa.bourbon_android.Networking;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Gets the current location.
 *
 * Created by atorres on 5/20/17.
 */
public class SingleShotLocationProvider {

    static String TAG = "SingleShotLocationProv";

    public interface LocationCallback {
        void onLocationAvailable(Location location);
        void onLocationFailure();
    }

    public static void requestSingleUpdate(Context context, final LocationCallback cb) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);

            try {
                locationManager.requestSingleUpdate(criteria, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        cb.onLocationAvailable(location);
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {
                    }

                    @Override
                    public void onProviderEnabled(String s) {
                    }

                    @Override
                    public void onProviderDisabled(String s) {
                        cb.onLocationFailure();
                    }
                }, null);

            } catch (SecurityException e) {
                Log.e(TAG, e.getLocalizedMessage());
                cb.onLocationFailure();
            }

        } else {
            cb.onLocationFailure();
        }
    }
}
