package tv.ourglass.alyssa.absinthe_android.Models;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by atorres on 11/14/16.
 */

public class SharedPreferencesManager {

    private static final String PREF_FILE = "tv.ourglass.absinthe_ios";

    // properties
    private static final String USER_ID = "USER_ID";
    private static final String USER_FIRST_NAME = "USER_FIRST_NAME";
    private static final String USER_LAST_NAME = "USER_LAST_NAME";
    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String USER_APPLEJACK_JWT = "USER_APPLEJACK_JWT";
    private static final String USER_APPLEJACK_JWT_EXPIRY = "USER_APPLEJACK_JWT_EXPIRY";

    private SharedPreferencesManager() {}

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
    }

    public static String getUserId(Context context) {
        return getSharedPreferences(context).getString(USER_ID, null);
    }

    public static void setUserId(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_ID, newValue);
        editor.apply();
    }

    public static String getUserApplejackJwt(Context context) {
        return getSharedPreferences(context).getString(USER_APPLEJACK_JWT, null);
    }
}
