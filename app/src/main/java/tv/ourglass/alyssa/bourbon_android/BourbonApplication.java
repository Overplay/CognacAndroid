package tv.ourglass.alyssa.bourbon_android;

import android.app.Application;
import android.content.Context;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;

/**
 * Created by atorres on 11/14/16.
 */

public class BourbonApplication extends Application {

    private static BourbonApplication instance;

    public static OkHttpClient okclient;

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));

        okclient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();

        instance = this;
    }
}
