package tv.ourglass.alyssa.absinthe_android.Networking;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tv.ourglass.alyssa.absinthe_android.AbsintheApplication;
import tv.ourglass.alyssa.absinthe_android.Models.OGConstants;
import tv.ourglass.alyssa.absinthe_android.Models.SharedPrefsManager;

/**
 * Created by atorres on 11/10/16.
 */

public class Applejack {

    public final String TAG = "Applejack";

    private final OkHttpClient client = AbsintheApplication.okclient;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static Applejack instance = new Applejack();

    private Applejack() { }

    public static Applejack getInstance() {
        return instance;
    }

    public interface HttpCallback {
        void onFailure(Call call, IOException e);
        void onSuccess(Response response);
    }

    private void request(Request request, final HttpCallback cb) {

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                cb.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (!response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    cb.onFailure(call, null);
                    response.body().close();

                } else {
                    cb.onSuccess(response);
                    response.body().close();
                }
            }
        });
    }

    private void post(Context context, String url, String json, HttpCallback cb) {
        RequestBody body = RequestBody.create(JSON, json);
        Request req;
        String jwt = SharedPrefsManager.getUserApplejackJwt(context);

        if (jwt != null) {
            req = new Request.Builder()
                    .addHeader("Authorization", "Bearer: " + jwt)
                    .url(url)
                    .post(body)
                    .build();
        } else {
            req = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
        }

        request(req, cb);
    }

    private void get(Context context, String url, HttpCallback cb) {
        Request req;
        String jwt = SharedPrefsManager.getUserApplejackJwt(context);

        if (jwt != null) {
            req = new Request.Builder()
                    .addHeader("Authorization", "Bearer: " + jwt)
                    .url(url)
                    .build();
        } else {
            req = new Request.Builder()
                    //.get()
                    .url(url)
                    .build();
        }

        request(req, cb);
    }

    private void put(Context context, String url, String json, HttpCallback cb) {
        RequestBody body = RequestBody.create(JSON, json);
        Request req;
        String jwt = SharedPrefsManager.getUserApplejackJwt(context);

        if (jwt != null) {
            req = new Request.Builder()
                    .addHeader("Authorization", "Bearer: " + jwt)
                    .url(url)
                    .put(body)
                    .build();
        } else {
            req = new Request.Builder()
                    .url(url)
                    .put(body)
                    .build();
        }

        request(req, cb);
    }

    public void login(Context context, String email, String password, HttpCallback cb) {
        try {
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("password", password);
            json.put("type", "local");

            post(context, OGConstants.OGCloudBaseURL + OGConstants.loginPath, json.toString(), cb);

        } catch (JSONException e) {
            e.printStackTrace();
            cb.onFailure(null, null);
        }
    }

    public void register(Context context, String email, String password, String firstName,
                         String lastName, HttpCallback cb) {
        try {
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("password", password);
            json.put("type", "local");

            JSONObject user = new JSONObject();
            user.put("firstName", firstName);
            user.put("lastName", lastName);
            json.put("user", user);

            post(context, OGConstants.OGCloudBaseURL + OGConstants.registerPath, json.toString(), cb);

        } catch (JSONException e) {
            e.printStackTrace();
            cb.onFailure(null, null);
        }
    }

    public void changePassword(Context context, String email, String newPassword, HttpCallback cb) {
        try {
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("newpass", newPassword);

            post(context, OGConstants.OGCloudBaseURL + OGConstants.changePwdPath, json.toString(), cb);

        } catch (JSONException e) {
            e.printStackTrace();
            cb.onFailure(null, null);
        }
    }

    public void changeAccountInfo(Context context, String firstName, String lastName, String email,
                                  String userId, HttpCallback cb) {
        try {
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("firstName", firstName);
            json.put("lastName", lastName);

            put(context, OGConstants.OGCloudBaseURL + OGConstants.changeAccountPath, json.toString(), cb);

        } catch (JSONException e) {
            e.printStackTrace();
            cb.onFailure(null, null);
        }
    }

    public void inviteUser(Context context, String email, HttpCallback cb) {
        try {
            JSONObject json = new JSONObject();
            json.put("email", email);

            post(context, OGConstants.OGCloudBaseURL + OGConstants.inviteUserPath, json.toString(), cb);

        } catch (JSONException e) {
            e.printStackTrace();
            cb.onFailure(null, null);
        }
    }

    public void getToken(Context context, HttpCallback cb) {
        get(context, OGConstants.OGCloudBaseURL + OGConstants.getTokenPath, cb);
    }

    public void getVenues(Context context, HttpCallback cb) {
        get(context, OGConstants.OGCloudBaseURL + OGConstants.getVenuesPath, cb);
    }

    public void getAuthStatus(Context context, HttpCallback cb) {
        get(context, OGConstants.OGCloudBaseURL + OGConstants.getAuthStatusPath, cb);
    }

    public void logout(Context context, HttpCallback cb) {
        get(context, OGConstants.OGCloudBaseURL + OGConstants.logoutPath, cb);
    }
}
