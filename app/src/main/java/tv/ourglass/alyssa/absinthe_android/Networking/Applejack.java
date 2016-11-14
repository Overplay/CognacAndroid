package tv.ourglass.alyssa.absinthe_android.Networking;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tv.ourglass.alyssa.absinthe_android.AbsintheApplication;
import tv.ourglass.alyssa.absinthe_android.Models.OGConstants;

/**
 * Created by atorres on 11/10/16.
 */

public class Applejack {

    public final String TAG = "Applejack";

    private final OkHttpClient client = AbsintheApplication.okclient;

    private static Applejack instance = new Applejack();

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

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
                    cb.onFailure(call, null);
                    response.body().close();

                } else {
                    cb.onSuccess(response);
                    response.body().close();
                }
            }
        });
    }

    private void post(String url, String json, HttpCallback cb) {
        RequestBody body = RequestBody.create(JSON, json);
        Request req = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        request(req, cb);
    }

    public void login(String email, String password, HttpCallback cb) {

        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .add("type", "local")
                .build();

        Request request = new Request.Builder()
                .url(OGConstants.OGCloudBaseURL + OGConstants.loginPath)
                .post(formBody)
                .build();

        request(request, cb);
    }

    public void register(String email, String password, String firstName, String lastName, HttpCallback cb) {
        try {
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("password", password);

            JSONObject user = new JSONObject();
            user.put("firstName", firstName);
            user.put("lastName", lastName);
            json.put("user", user);

            json.put("type", "local");

            post(OGConstants.OGCloudBaseURL + OGConstants.registerPath, json.toString(), cb);

        } catch (JSONException e) {
            e.printStackTrace();
            cb.onFailure(null, null);
        }
    }
}
