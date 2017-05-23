package tv.ourglass.alyssa.bourbon_android.Model.OGVenue;

import com.google.android.gms.maps.model.Marker;

/**
 * Represents an Ourglass venue.
 *
 * Created by atorres on 3/10/17.
 */
public class OGVenue {
    public String name;
    public String address1;
    public String address2 = "";
    public String city;
    public String state;
    public String zip;
    public double latitude;
    public double longitude;
    public String uuid;
    public String yelpId = "";

    public Marker marker;

    public String getAddress() {
        String s2 = (address2.trim().isEmpty()) ? address2 : " " + address2;
        return String.format("%s%s, %s, %s %s", address1, s2, city, state, zip);
    }

    public OGVenue(String name, String address1, String city, String state, String zip,
                   double latitude, double longitude, String uuid) {
        this.name = name;
        this.address1 = address1;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.latitude = latitude;
        this.longitude = longitude;
        this.marker = null;
        this.uuid = uuid;
    }

    public OGVenue(String name, String address1, String address2,
                   String city, String state, String zip,
                   double latitude, double longitude, String uuid) {
        this.name = name;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.latitude = latitude;
        this.longitude = longitude;
        this.marker = null;
        this.uuid = uuid;
    }

    public OGVenue() {
        this.name = "";
        this.address1 = "";
        this.city = "";
        this.state = "";
        this.zip = "";
        this.latitude = 0;
        this.longitude = 0;
        this.marker = null;
        this.uuid = "";
    }
}
