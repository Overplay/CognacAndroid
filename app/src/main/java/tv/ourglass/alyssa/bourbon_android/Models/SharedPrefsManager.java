package tv.ourglass.alyssa.bourbon_android.Models;

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
    private static final String USER_PASSWORD = "USER_PASSWORD";
    private static final String USER_APPLEJACK_JWT = "USER_APPLEJACK_JWT";
    private static final String USER_APPLEJACK_JWT_EXPIRY = "USER_APPLEJACK_JWT_EXPIRY";

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

    public static String getUserPassword(Context context) {
        return getSharedPreferences(context).getString(USER_PASSWORD, null);
    }

    public static void setUserPassword(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_PASSWORD, newValue);
        editor.apply();
    }

    public static String getUserApplejackJwt(Context context) {
        return getSharedPreferences(context).getString(USER_APPLEJACK_JWT, null);
    }

    public static void setUserApplejackJwt(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_APPLEJACK_JWT, newValue);
        editor.apply();
    }

    public static Integer getUserApplejackJwtExpiry(Context context) {
        return getSharedPreferences(context).getInt(USER_APPLEJACK_JWT_EXPIRY, -1);
    }

    public static void setUserApplejackJwtExpiry(Context context, Integer newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(USER_APPLEJACK_JWT_EXPIRY, newValue);
        editor.apply();
    }
}
