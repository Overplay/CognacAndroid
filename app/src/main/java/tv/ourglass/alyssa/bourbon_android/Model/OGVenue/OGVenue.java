package tv.ourglass.alyssa.bourbon_android.Model.OGVenue;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by atorres on 3/10/17.
 */

public class OGVenue {
    public String name;
    public String street;
    public String street2 = "";
    public String city;
    public String state;
    public String zip;
    public double latitude;
    public double longitude;
    public String uuid;
    public String yelpId = "";

    public Marker marker;

    public String getAddress() {
        String s2 = (street2 == "") ? street2 : " " + street2;
        return String.format("%s%s, %s, %s %s", street, s2, city, state, zip);
    }

    public OGVenue(String name, String street, String city, String state, String zip,
                   double latitude, double longitude, String uuid) {
        this.name = name;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.latitude = latitude;
        this.longitude = longitude;
        this.marker = null;
        this.uuid = uuid;
    }

    public OGVenue(String name, String street, String street2,
                   String city, String state, String zip,
                   double latitude, double longitude, String uuid) {
        this.name = name;
        this.street = street;
        this.street2 = street2;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.latitude = latitude;
        this.longitude = longitude;
        this.marker = null;
        this.uuid = uuid;
    }
}
