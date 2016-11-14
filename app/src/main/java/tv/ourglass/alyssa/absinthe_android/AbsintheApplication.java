package tv.ourglass.alyssa.absinthe_android;

import android.app.Application;

import okhttp3.OkHttpClient;

/**
 * Created by atorres on 11/14/16.
 */

public class AbsintheApplication extends Application {

    public static final OkHttpClient okclient = new OkHttpClient();

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
