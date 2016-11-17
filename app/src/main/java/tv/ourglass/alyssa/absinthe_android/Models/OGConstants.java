package tv.ourglass.alyssa.absinthe_android.Models;

/**
 * Created by atorres on 11/11/16.
 */

public class OGConstants {

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

    // UDP constants
    public static final Integer udpDiscoveryPort = 9091;

    // Regex constants
    public static final String emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}";
    public static final String passwordRegEx = ".{5,}";

    // Intent extras
    public static final String firstNameExtra = "INTENT_EXTRA_FIRST_NAME";
    public static final String lastNameExtra = "INTENT_EXTRA_LAST_NAME";
    public static final String emailExtra = "INTENT_EXTRA_EMAIL";

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
