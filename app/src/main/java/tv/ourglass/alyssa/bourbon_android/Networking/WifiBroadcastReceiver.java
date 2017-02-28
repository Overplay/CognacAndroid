package tv.ourglass.alyssa.bourbon_android.Networking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by atorres on 1/23/17.
 */

public class WifiBroadcastReceiver extends BroadcastReceiver {

    String TAG = "WifiBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        /*String action = intent.getAction();

        if (action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
            if (intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE) == SupplicantState.COMPLETED) {
                // Wifi is associated
                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

            }
        }*/
        Log.d(TAG, "network connectivity changed");

        /*ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();

        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            Log.d(TAG, "have wifi");
        } else {
            Log.d(TAG, "don't have wifi");
        }*/
    }
}
