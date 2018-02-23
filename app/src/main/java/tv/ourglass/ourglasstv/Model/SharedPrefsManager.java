package tv.ourglass.ourglasstv.Model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by atorres on 11/14/16.
 */

public class SharedPrefsManager {

    private static final String PREF_FILE = "tv.ourglass.absinthe_ios";

    // properties
    private static final String APP_OPENED = "APP_OPENED";
    private static final String USER_ID = "USER_ID";
    private static final String USER_FIRST_NAME = "USER_FIRST_NAME";
    private static final String USER_LAST_NAME = "USER_LAST_NAME";
    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String USER_BELLINI_JWT = "USER_BELLINI_JWT";
    private static final String USER_BELLINI_JWT_EXPIRY = "USER_BELLINI_JWT_EXPIRY";

    private SharedPrefsManager() {}

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
    }

    public static boolean getAppOpened(Context context) {
        return getSharedPreferences(context).getBoolean(APP_OPENED, false);
    }

    public static void setAppOpened(Context context, boolean newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(APP_OPENED, newValue);
        editor.apply();
    }

    public static String getUserId(Context context) {
        return getSharedPreferences(context).getString(USER_ID, null);
    }

    public static void setUserId(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_ID, newValue);
        editor.apply();
    }

    public static String getUserFirstName(Context context) {
        return getSharedPreferences(context).getString(USER_FIRST_NAME, null);
    }

    public static void setUserFirstName(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_FIRST_NAME, newValue);
        editor.apply();
    }

    public static String getUserLastName(Context context) {
        return getSharedPreferences(context).getString(USER_LAST_NAME, null);
    }

    public static void setUserLastName(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_LAST_NAME, newValue);
        editor.apply();
    }

    public static String getUserEmail(Context context) {
        return getSharedPreferences(context).getString(USER_EMAIL, null);
    }

    public static void setUserEmail(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_EMAIL, newValue);
        editor.apply();
    }

    public static String getJwt(Context context) {
        return getSharedPreferences(context).getString(USER_BELLINI_JWT, null);
    }

    public static void setJwt(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_BELLINI_JWT, newValue);
        editor.apply();
    }

    public static Long getJwtExpiry(Context context) {
        return getSharedPreferences(context).getLong(USER_BELLINI_JWT_EXPIRY, 0l);
    }

    public static void setJwtExpiry(Context context, Long newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putLong(USER_BELLINI_JWT_EXPIRY, newValue);
        editor.apply();
    }
}
