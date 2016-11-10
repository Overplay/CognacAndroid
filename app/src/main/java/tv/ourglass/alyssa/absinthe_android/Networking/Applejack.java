package tv.ourglass.alyssa.absinthe_android.Networking;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by atorres on 11/10/16.
 */

public class Applejack {

    public final String TAG = "Applejack";
    private final OkHttpClient client = new OkHttpClient();

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
}
