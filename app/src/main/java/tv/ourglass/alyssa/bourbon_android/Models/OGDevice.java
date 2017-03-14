package tv.ourglass.alyssa.bourbon_android.Models;

/**
 * Created by atorres on 11/8/16.
 */

public class OGDevice {

    public String name;
    public String atVenueUUID;
    public String udid;

    public OGDevice(String name, String atVenueUUID, String udid) {
        this.name = name;
        this.atVenueUUID = atVenueUUID;
        this.udid = udid;
    }

    public OGDevice() {
        this.name = "";
        this.atVenueUUID = "";
        this.udid = "";
    }

    public String getUrl() {
        return OGConstants.newOGCloudBaseURL + OGConstants.deviceControlPath + this.udid;
    }
}
