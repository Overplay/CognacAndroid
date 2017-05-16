package tv.ourglass.alyssa.bourbon_android.Model;

/**
 * Created by atorres on 11/11/16.
 */

public class OGConstants {

    // Dev mode
    public static final boolean devMode = false;  // if true, login isn't really done
    public static final boolean alwaysShowIntro = false;

    // OG Cloud paths
    public static final String ourglassCloudBaseUrl = "http://138.68.230.239";
    public static final String ourglassBasePort = ":2000";
    public static final String getTokenPath = ourglassBasePort + "/user/jwt";
    public static final String registerPath = ourglassBasePort + "/auth/addUser";
    public static final String loginPath = ourglassBasePort + "/auth/login";
    public static final String changePwdPath = ourglassBasePort + "/auth/changePwd";
    public static final String getAuthStatusPath = ourglassBasePort + "/auth/status";
    public static final String changeAccountPath = ourglassBasePort + "/user/";
    public static final String inviteUserPath = ourglassBasePort + "/user/inviteNewUser";
    public static final String checkJWTPath = ourglassBasePort + "/user/checkjwt";

    public static final String venuesPath = ourglassBasePort + "/venue/all";
    public static final String userVenuesPath = ourglassBasePort + "/venue/myvenues";
    public static final String devicesPath = ":2001/venue/devices?atVenueUUID=";
    public static final String deviceControlPath = ":2001/blueline/control/?deviceUDID=";
    public static final String appControlPath = "app/control/index.html";
    public static final String findByRegCodePath = ":2001/ogdevice/findByRegCode?regCode=";
    public static final String changeDeviceNamePath = ":2001/ogdevice/changeName";
    public static final String associateWithVenuePath = ":2001/ogdevice/associateWithVenue";
    public static final String addVenuePath = ourglassBasePort + "/venue";
    public static final String yelpSearchPath = ourglassBasePort + "/venue/yelpSearch";

    // Regex constants
    public static final String emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}";
    public static final String passwordRegEx = ".{8,}";

    // Intent extras
    public static final String firstNameExtra = "INTENT_EXTRA_FIRST_NAME";
    public static final String lastNameExtra = "INTENT_EXTRA_LAST_NAME";
    public static final String emailExtra = "INTENT_EXTRA_EMAIL";
    public static final String deviceUrlExtra = "INTENT_EXTRA_DEVICE_URL";
    public static final String deviceNameExtra = "INTENT_EXTRA_DEVICE_NAME";
    public static final String venueUUIDExtra = "INTENT_EXTRA_VENUE_UUID";
    public static final String venueNameExtra = "INTENT_EXTRA_VENUE_NAME";
    public static final String appUrlExtra = "INTENT_EXTRA_APP_URL";

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
