package tv.ourglass.alyssa.bourbon_android.Model.Yelp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import tv.ourglass.alyssa.bourbon_android.Model.OGVenue.OGVenue;

/**
 * Represents a Yelp venue.
 *
 * Created by atorres on 5/20/17.
 */
public class YelpVenue {

    /**
     * Variables that are specific to a Yelp venue.
     */
    public String imageUrl;
    public String distance;
    public String country;

    /**
     * Venue object holding info common between OG venues and Yelp venues.
     */
    public OGVenue ogVenue;

    public YelpVenue(JSONObject venueJson) throws JSONException {
        ogVenue = new OGVenue();

        JSONObject location = venueJson.getJSONObject("location");
        JSONObject coords = venueJson.getJSONObject("coordinates");

        ogVenue.name = venueJson.getString("name");
        ogVenue.address1 = location.getString("address1");
        ogVenue.address2 = location.getString("address2");
        ogVenue.city = location.getString("city");
        ogVenue.state = location.getString("state");
        ogVenue.zip = location.getString("zip_code");
        ogVenue.latitude = coords.getDouble("latitude");
        ogVenue.longitude = coords.getDouble("longitude");
        ogVenue.yelpId = venueJson.getString("id");

        imageUrl = venueJson.getString("image_url");
        double dist = venueJson.getDouble("distance");
        distance = String.format(Locale.ENGLISH, "%.1f km", dist / 1000.0);
        country = location.getString("country");
    }

}
