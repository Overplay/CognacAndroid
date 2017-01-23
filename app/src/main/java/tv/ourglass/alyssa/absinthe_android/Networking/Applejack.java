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

                Log.d(TAG, response.toString());

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

    /* Logs in with given credentials and gets the JWT. Returns a success if both login and get
       token are successful, failure if not.
    */
    public void login(final Context context, final String email, final String password, final HttpCallback cb) {
        try {
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("password", password);
            json.put("type", "local");

            HttpCallback loginCb = new HttpCallback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    cb.onFailure(call, e);
                }

                @Override
                public void onSuccess(Response response) {
                    SharedPrefsManager.setUserEmail(context, email);
                    SharedPrefsManager.setUserPassword(context, password);

                    // TODO: get and set user id?

                    Applejack.getInstance().getToken(context, new Applejack.HttpCallback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            cb.onFailure(call, e);
                        }

                        @Override
                        public void onSuccess(Response response) {
                            try {
                                String jsonData = response.body().string();
                                JSONObject json = new JSONObject(jsonData);
                                SharedPrefsManager.setUserApplejackJwt(context, json.getString("token"));
                                SharedPrefsManager.setUserApplejackJwtExpiry(context, json.getInt("expires"));
                                cb.onSuccess(response);

                            } catch (Exception e) {
                                e.printStackTrace();
                                cb.onFailure(null, null);
                            }
                        }
                    });
                }
            };

            post(context, OGConstants.OGCloudBaseURL + OGConstants.loginPath, json.toString(), loginCb);

        } catch (JSONException e) {
            e.printStackTrace();
            cb.onFailure(null, null);
        }
    }

    public void register(final Context context, final String email, final String password, final String firstName,
                         final String lastName, final HttpCallback cb) {
        try {
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("password", password);
            json.put("type", "local");

            JSONObject user = new JSONObject();
            user.put("firstName", firstName);
            user.put("lastName", lastName);
            json.put("user", user);

            HttpCallback registerCb = new HttpCallback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    cb.onFailure(call, e);
                }

                @Override
                public void onSuccess(Response response) {
                    SharedPrefsManager.setUserFirstName(context, firstName);
                    SharedPrefsManager.setUserLastName(context, lastName);
                    SharedPrefsManager.setUserEmail(context, email);
                    SharedPrefsManager.setUserPassword(context, password);

                    Applejack.getInstance().getToken(context, new Applejack.HttpCallback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            cb.onFailure(call, e);
                        }

                        @Override
                        public void onSuccess(Response response) {
                            try {
                                String jsonData = response.body().string();
                                JSONObject json = new JSONObject(jsonData);
                                SharedPrefsManager.setUserApplejackJwt(context, json.getString("token"));
                                SharedPrefsManager.setUserApplejackJwtExpiry(context, json.getInt("expires"));
                                cb.onSuccess(response);

                            } catch (Exception e) {
                                e.printStackTrace();
                                cb.onFailure(null, null);
                            }
                        }
                    });
                }
            };


            post(context, OGConstants.OGCloudBaseURL + OGConstants.registerPath, json.toString(), registerCb);

        } catch (JSONException e) {
            e.printStackTrace();
            cb.onFailure(null, null);
        }
    }

    public void changePassword(final Context context, String email, final String newPassword, final HttpCallback cb) {
        try {
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("newpass", newPassword);

            HttpCallback changePwdCb = new HttpCallback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    cb.onFailure(call, e);
                }

                @Override
                public void onSuccess(Response response) {
                    SharedPrefsManager.setUserPassword(context, newPassword);
                    cb.onSuccess(response);
                }
            };

            post(context, OGConstants.OGCloudBaseURL + OGConstants.changePwdPath, json.toString(), changePwdCb);

        } catch (JSONException e) {
            e.printStackTrace();
            cb.onFailure(null, null);
        }
    }

    public void changeAccountInfo(final Context context, final String firstName, final String lastName, final String email,
                                  String userId, final HttpCallback cb) {
        try {
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("firstName", firstName);
            json.put("lastName", lastName);

            HttpCallback changeActCb = new HttpCallback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    cb.onFailure(call, e);
                }

                @Override
                public void onSuccess(Response response) {
                    SharedPrefsManager.setUserFirstName(context, firstName);
                    SharedPrefsManager.setUserLastName(context, lastName);
                    SharedPrefsManager.setUserEmail(context, email);
                    cb.onSuccess(response);
                }
            };

            put(context, OGConstants.OGCloudBaseURL + OGConstants.changeAccountPath + userId, json.toString(),
                    changeActCb);

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
