package tv.ourglass.alyssa.absinthe_android.Scenes.Control;

import java.util.Locale;

/**
 * Created by atorres on 11/8/16.
 */

public class OGDevice {

    public String systemName;
    public String location;
    public String ipAddress;
    public String venue;
    public int ttl;

    public OGDevice(String systemName, String location, String ipAddress, String venue, int ttl) {
        this.systemName = systemName;
        this.location = location;
        this.ipAddress = ipAddress;
        this.venue = venue;
        this.ttl = ttl;
    }

    public OGDevice() {
        this.systemName = "";
        this.location = "";
        this.ipAddress = "";
        this.venue = "";
        this.ttl = 0;
    }

    public String getUrl() {
        return String.format("http://%s:9090/www/control/index.html", this.ipAddress);
    }

    public String description() {
        return String.format(Locale.US, "systemName: %s\nlocation: %s\nipAddress: %s\nvenue: %s\nttl: %d\n",
                this.systemName, this.location, this.ipAddress, this.venue, this.ttl);
    }
}
