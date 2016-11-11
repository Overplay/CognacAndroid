package tv.ourglass.alyssa.absinthe_android.Networking;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tv.ourglass.alyssa.absinthe_android.R;
import tv.ourglass.alyssa.absinthe_android.Scenes.Registration.LoginActivity;

/**
 * Created by atorres on 11/10/16.
 */

public class Applejack {

    public final String TAG = "Applejack";

    private static final OkHttpClient client = new OkHttpClient();

    private static Applejack singleton = new Applejack();

    private static final String baseURL = "http://107.170.209.248";

    private Applejack() { }

    public static Applejack getInstance() {
        return singleton;
    }

    public void request(String url) throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected response: " + response);
        }

        Log.d(TAG, response.body().string());
    }

    public void login(String email, String password, Callback cb) {

        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .add("type", "local")
                .build();

        Request request = new Request.Builder()
                .url(baseURL + "/auth/login")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(cb);
    }
}
