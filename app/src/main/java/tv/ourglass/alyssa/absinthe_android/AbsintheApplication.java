package tv.ourglass.alyssa.absinthe_android;

import android.app.Application;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;

/**
 * Created by atorres on 11/14/16.
 */

public class AbsintheApplication extends Application {

    public static OkHttpClient okclient;

    @Override
    public void onCreate() {
        super.onCreate();

        CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));

        okclient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();
    }
}
