package tv.ourglass.alyssa.bourbon_android.Models;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by atorres on 3/10/17.
 */

public class OGVenue {
    public String name;
    public String address;
    public double latitude;
    public double longitude;
    public Marker marker;
    public String uuid;

    public OGVenue(String name, String address, double latitude, double longitude, String uuid) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.marker = null;
        this.uuid = uuid;
    }
}
