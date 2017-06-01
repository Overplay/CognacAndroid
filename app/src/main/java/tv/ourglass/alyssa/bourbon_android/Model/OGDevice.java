package tv.ourglass.alyssa.bourbon_android.Model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import tv.ourglass.alyssa.bourbon_android.BourbonApplication;

/**
 * Ourglass device.
 * Created by atorres on 11/8/16.
 */

public class OGDevice {

    public String name = "";
    public String atVenueUUID = "";
    public String udid = "";
    public String stationName = "";
    public Boolean isActive = false;
    public Date lastContact = null;

    public OGDevice(JSONObject deviceJson) throws JSONException {
        this.name = deviceJson.getString("name");
        this.atVenueUUID = deviceJson.getString("atVenueUUID");
        this.udid = deviceJson.getString("deviceUDID");
        this.stationName = deviceJson.getJSONObject("currentProgram").getString("networkName");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
        Log.d("OGDevice", deviceJson.getString("lastContact"));
        try {
            this.lastContact = dateFormat.parse(deviceJson.getString("lastContact"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long interval = this.lastContact == null ? 1000000 : System.currentTimeMillis() - this.lastContact.getTime();
        this.isActive = interval < TimeUnit.SECONDS.toMillis(300);
    }

    public String getUrl() {
        return OGConstants.belliniDM + OGConstants.deviceControlPath + this.udid + "&jwt=" +
                SharedPrefsManager.getJwt(BourbonApplication.getContext());
    }
}
