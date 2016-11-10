package tv.ourglass.alyssa.absinthe_android.Scenes.Devices;

/**
 * Created by atorres on 11/8/16.
 */

public class OGDevice {

    public String systemName;
    public String location;
    public String ipAddress;
    public int ttl;

    public OGDevice(String systemName, String location, String ipAddress, int ttl) {
        this.systemName = systemName;
        this.location = location;
        this.ipAddress = ipAddress;
        this.ttl = ttl;
    }

    public String getUrl() {
        return String.format("http://%s:9090/www/control/index.html", this.ipAddress);
    }
}
