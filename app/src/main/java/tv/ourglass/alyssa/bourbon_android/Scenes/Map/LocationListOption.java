package tv.ourglass.alyssa.bourbon_android.Scenes.Map;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by atorres on 1/11/17.
 */

public class LocationListOption {
    public String name;
    public String address;
    public double latitude;
    public double longitude;
    public Marker marker;

    public LocationListOption(String name, String address, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.marker = null;
    }
}
