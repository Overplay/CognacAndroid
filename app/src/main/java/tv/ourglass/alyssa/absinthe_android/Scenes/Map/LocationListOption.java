package tv.ourglass.alyssa.absinthe_android.Scenes.Map;

/**
 * Created by atorres on 1/11/17.
 */

public class LocationListOption {
    public String name;
    public String address;
    public long latitude;
    public long longitude;

    public LocationListOption(String name, String address, long latitude, long longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
