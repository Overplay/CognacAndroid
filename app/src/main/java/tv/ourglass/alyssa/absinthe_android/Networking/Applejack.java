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
import tv.ourglass.alyssa.absinthe_android.Models.OGConstants;

/**
 * Created by atorres on 11/10/16.
 */

public class Applejack {

    public final String TAG = "Applejack";

    private static final OkHttpClient client = new OkHttpClient();

    private static Applejack singleton = new Applejack();

    private Applejack() { }

    public static Applejack getInstance() {
        return singleton;
    }

    public void login(String email, String password, Callback cb) {

        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .add("type", "local")
                .build();

        Request request = new Request.Builder()
                .url(OGConstants.OGCloudBaseURL + "/auth/login")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(cb);
    }
}
