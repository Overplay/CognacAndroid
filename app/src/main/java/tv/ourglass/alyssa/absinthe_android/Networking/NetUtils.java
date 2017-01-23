package tv.ourglass.alyssa.absinthe_android.Networking;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by atorres on 11/11/16.
 */

public class NetUtils {

    public static String getCurrentSSID(Context context) {

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID().replaceAll("^\"|\"$", "");
    }
}
