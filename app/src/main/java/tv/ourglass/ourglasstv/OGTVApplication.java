package tv.ourglass.ourglasstv;

import android.app.Application;
import android.content.Context;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import tv.ourglass.ourglasstv.Networking.OGHeaderInterceptor;

/**
 * Created by atorres on 11/14/16.
 */

public class OGTVApplication extends Application {

    private static OGTVApplication instance;

    //public static OkHttpClient okclient;

    public static Context getContext() {
        return instance;
    }

    public static final OkHttpClient okclient = new OkHttpClient.Builder()
            .addInterceptor(new OGHeaderInterceptor())
//            .cookieJar(new CookieJar() {
//                private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
//
//                @Override
//                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//                    cookieStore.put(url, cookies);
//                }
//
//                @Override
//                public List<Cookie> loadForRequest(HttpUrl url) {
//                    List<Cookie> cookies = cookieStore.get(url);
//                    return cookies != null ? cookies : new ArrayList<Cookie>();
//                }
//            })
            .build();

    @Override
    public void onCreate() {
        super.onCreate();

        CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));

//        okclient = new OkHttpClient.Builder()
//                .cookieJar(cookieJar)
//                .build();



        instance = this;
    }
}
