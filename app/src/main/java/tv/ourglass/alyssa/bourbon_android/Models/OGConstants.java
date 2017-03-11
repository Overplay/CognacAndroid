package tv.ourglass.alyssa.bourbon_android.Models;

/**
 * Created by atorres on 11/11/16.
 */

public class OGConstants {

    // Google Geocoding API
    public static final String googleGeocodingRequestBase = "https://maps.googleapis.com/maps/api/geocode/json?address=";

    // Dev mode
    public static final boolean devMode = false;  // if true, login isn't really done
    public static final boolean alwaysShowIntro = false;

    // OG Cloud paths
    public static final String OGCloudBaseURL = "http://107.170.209.248";
    public static final String getTokenPath = "/user/jwt";
    public static final String registerPath = "/auth/addUser";
    public static final String loginPath = "/auth/login";
    public static final String changePwdPath = "/auth/changePwd";
    public static final String getVenuesPath = "/api/v1/venue";
    public static final String getAuthStatusPath = "/auth/status";
    public static final String logoutPath = "/auth/logoutPage";
    public static final String changeAccountPath = "/api/v1/user/";
    public static final String inviteUserPath = "/user/inviteNewUser";

    // New OG Cloud paths
    public static final String newOGCloudBaseURL = "http://138.68.230.239";
    public static final String newVenuesPath = ":2000/api/v1/venue";
    public static final String devicesPath = ":2001/venue/devices?atVenueUUID=";

    // UDP constants
    public static final Integer udpDiscoveryPort = 9091;
    public static final Integer maxTTL = 6;
    public static final Integer broadcastInterval = 10000; // ms
    public static final long ttlInterval = 10000; // ms

    // Regex constants
    public static final String emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}";
    public static final String passwordRegEx = ".{5,}";

    // Intent extras
    public static final String firstNameExtra = "INTENT_EXTRA_FIRST_NAME";
    public static final String lastNameExtra = "INTENT_EXTRA_LAST_NAME";
    public static final String emailExtra = "INTENT_EXTRA_EMAIL";
    public static final String deviceUrlExtra = "INTENT_EXTRA_DEVICE_URL";
    public static final String deviceNameExtra = "INTENT_EXTRA_DEVICE_NAME";
    public static final String venueUUIDExtra = "INTENT_EXTRA_VENUE_UUID";
    public static final String venueNameExtra = "INTENT_EXTRA_VENUE_NAME";

    // Animations
    public static final int fadeInTime = 350;  // ms
    public static final int fadeOutTime = 350; // ms

    // Fonts
    public static final String regularFont = "Poppins-Regular.ttf";
    public static final String lightFont = "Poppins-Light.ttf";
    public static final String boldFont = "Poppins-Bold.ttf";
    public static final String mediumFont = "Poppins-Medium.ttf";
    public static final String semiBoldFont = "Poppins-SemiBold.ttf";
}
