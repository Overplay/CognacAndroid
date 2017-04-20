package tv.ourglass.alyssa.bourbon_android.Networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

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
import tv.ourglass.alyssa.bourbon_android.BourbonApplication;
import tv.ourglass.alyssa.bourbon_android.Models.OGConstants;
import tv.ourglass.alyssa.bourbon_android.Models.SharedPrefsManager;
import tv.ourglass.alyssa.bourbon_android.Scenes.Registration.WelcomeActivity;

/**
 * Created by atorres on 11/10/16.
 */

public class Applejack {

    public final String TAG = "Applejack";

    private final OkHttpClient client = BourbonApplication.okclient;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public enum ApplejackError {
        authFailure, tokenInvalid, jsonError, defaultError
    }

    private static Applejack instance = new Applejack();

    private Applejack() { }

    public static Applejack getInstance() {
        return instance;
    }

    public static abstract class HttpCallback {

        abstract public void onSuccess(Response response); // this must close the response body or it will leak
        abstract public void onFailure(Call call, IOException e, ApplejackError error);

        /*void onAuthFailure(final Context context) {
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder
                            .setTitle("Uh oh!")
                            .setMessage("You aren't authorized to use this resource.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    builder.create().show();
                }
            });
        }

        void onTokenInvalid(final Context context) {
            Applejack.getInstance().logout(context);
        }*/
    }

    private void request(Request request, final HttpCallback cb, final Context context) {

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                cb.onFailure(call, e, ApplejackError.defaultError);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {

                if (!response.isSuccessful()) {

                    // if error 403, then perform checks for authorization
                    if (response.code() == 403) {
                        Applejack.getInstance().checkJWT(context, new HttpCallback() {
                            @Override
                            public void onFailure(Call jwtCall, IOException e, ApplejackError error) {
                                Log.e(TAG, "JWT is invalid");
                                response.body().close();
                                cb.onFailure(jwtCall, e, ApplejackError.tokenInvalid);
                            }

                            @Override
                            public void onSuccess(Response jwtResponse) {
                                Log.e(TAG, "resource is not allowed for this user");
                                jwtResponse.body().close();
                                response.body().close();
                                cb.onFailure(call, null, ApplejackError.authFailure);
                            }
                        });

                    } else {
                        cb.onFailure(call, null, ApplejackError.defaultError);
                        response.body().close();
                    }

                } else {
                    cb.onSuccess(response);
                }
            }
        });
    }

    private void getWithoutAuthCheck(Context context, String url, final HttpCallback cb) {
        Request req;
        String jwt = SharedPrefsManager.getUserApplejackJwt(context);

        if (jwt != null) {
            req = new Request.Builder()
                    .addHeader("Authorization", "Bearer " + jwt)
                    .url(url)
                    .get()
                    .build();
        } else {
            req = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
        }

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                cb.onFailure(call, e, ApplejackError.defaultError);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    cb.onFailure(call, null, ApplejackError.defaultError);
                    response.body().close();
                } else {
                    cb.onSuccess(response);
                }
            }
        });
    }

    public void post(Context context, String url, String json, HttpCallback cb) {
        RequestBody body = RequestBody.create(JSON, json);
        Request req;
        String jwt = SharedPrefsManager.getUserApplejackJwt(context);

        if (jwt != null) {
            req = new Request.Builder()
                    .addHeader("Authorization", "Bearer " + jwt)
                    .url(url)
                    .post(body)
                    .build();
        } else {
            req = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
        }

        request(req, cb, context);
    }

    public void get(Context context, String url, HttpCallback cb) {
        Request req;
        String jwt = SharedPrefsManager.getUserApplejackJwt(context);

        if (jwt != null) {
            req = new Request.Builder()
                    .addHeader("Authorization", "Bearer " + jwt)
                    .url(url)
                    .get()
                    .build();
        } else {
            req = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
        }

        request(req, cb, context);
    }

    private void put(Context context, String url, String json, HttpCallback cb) {
        RequestBody body = RequestBody.create(JSON, json);
        Request req;
        String jwt = SharedPrefsManager.getUserApplejackJwt(context);

        if (jwt != null) {
            req = new Request.Builder()
                    .addHeader("Authorization", "Bearer " + jwt)
                    .url(url)
                    .put(body)
                    .build();
        } else {
            req = new Request.Builder()
                    .url(url)
                    .put(body)
                    .build();
        }

        request(req, cb, context);
    }

    public void login(final Context context, final String email, final String password, final HttpCallback cb) {
        try {
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("password", password);
            json.put("type", "local");

            HttpCallback loginCb = new HttpCallback() {

                @Override
                public void onFailure(Call call, IOException e, ApplejackError error) {
                    cb.onFailure(call, e, error);
                }

                @Override
                public void onSuccess(final Response loginResponse) {
                    SharedPrefsManager.setUserEmail(context, email);

                    Applejack.getInstance().getToken(context, new Applejack.HttpCallback() {

                        @Override
                        public void onFailure(Call call, IOException e, ApplejackError error) {
                            cb.onFailure(call, e, error);
                        }

                        @Override
                        public void onSuccess(Response tokenResponse) {
                            try {
                                String jsonData = tokenResponse.body().string();
                                JSONObject json = new JSONObject(jsonData);
                                SharedPrefsManager.setUserApplejackJwt(context, json.getString("token"));
                                SharedPrefsManager.setUserApplejackJwtExpiry(context, json.getLong("expires"));
                                tokenResponse.body().close();

                                cb.onSuccess(loginResponse);

                            } catch (Exception e) {
                                e.printStackTrace();
                                cb.onFailure(null, null, ApplejackError.tokenInvalid);
                            }
                        }
                    });
                }
            };

            post(context, OGConstants.ourglassCloudBaseUrl + OGConstants.loginPath, json.toString(), loginCb);

        } catch (JSONException e) {
            e.printStackTrace();
            cb.onFailure(null, null, ApplejackError.jsonError);
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
                public void onFailure(Call call, IOException e, ApplejackError error) {
                    cb.onFailure(call, e, error);
                }

                @Override
                public void onSuccess(final Response regResponse) {
                    SharedPrefsManager.setUserFirstName(context, firstName);
                    SharedPrefsManager.setUserLastName(context, lastName);
                    SharedPrefsManager.setUserEmail(context, email);

                    Applejack.getInstance().getToken(context, new Applejack.HttpCallback() {

                        @Override
                        public void onFailure(Call call, IOException e, ApplejackError error) {
                            cb.onFailure(call, e, error);
                        }

                        @Override
                        public void onSuccess(Response tokenResponse) {
                            try {
                                String jsonData = tokenResponse.body().string();
                                JSONObject json = new JSONObject(jsonData);
                                SharedPrefsManager.setUserApplejackJwt(context, json.getString("token"));
                                SharedPrefsManager.setUserApplejackJwtExpiry(context, json.getLong("expires"));
                                tokenResponse.body().close();

                                cb.onSuccess(regResponse);

                            } catch (Exception e) {
                                e.printStackTrace();
                                cb.onFailure(null, null, ApplejackError.tokenInvalid);
                            }
                        }
                    });
                }
            };


            post(context, OGConstants.ourglassCloudBaseUrl + OGConstants.registerPath, json.toString(), registerCb);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "error creating JSON object for registration");
            cb.onFailure(null, null, ApplejackError.jsonError);
        }
    }

    public void changePassword(final Context context, String email, final String newPassword, final HttpCallback cb) {
        try {
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("newpass", newPassword);

            HttpCallback changePwdCb = new HttpCallback() {

                @Override
                public void onFailure(Call call, IOException e, ApplejackError error) {
                    cb.onFailure(call, e, error);
                }

                @Override
                public void onSuccess(Response response) {
                    cb.onSuccess(response);
                }
            };

            post(context, OGConstants.ourglassCloudBaseUrl + OGConstants.changePwdPath, json.toString(), changePwdCb);

        } catch (JSONException e) {
            e.printStackTrace();
            cb.onFailure(null, null, ApplejackError.jsonError);
        }
    }

    public void changeAccountInfo(final Context context, final String firstName, final String lastName, final String email,
                                  final String userId, final HttpCallback cb) {
        try {
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("firstName", firstName);
            json.put("lastName", lastName);

            HttpCallback changeActCb = new HttpCallback() {

                @Override
                public void onFailure(Call call, IOException e, ApplejackError error) {
                    cb.onFailure(call, e, error);
                }

                @Override
                public void onSuccess(Response response) {
                    SharedPrefsManager.setUserFirstName(context, firstName);
                    SharedPrefsManager.setUserLastName(context, lastName);
                    SharedPrefsManager.setUserEmail(context, email);
                    cb.onSuccess(response);
                }
            };

            put(context, OGConstants.ourglassCloudBaseUrl + OGConstants.changeAccountPath + userId, json.toString(),
                    changeActCb);

        } catch (JSONException e) {
            e.printStackTrace();
            cb.onFailure(null, null, ApplejackError.jsonError);
        }
    }

    public void inviteUser(Context context, String email, HttpCallback cb) {
        try {
            JSONObject json = new JSONObject();
            json.put("email", email);

            post(context, OGConstants.ourglassCloudBaseUrl + OGConstants.inviteUserPath, json.toString(), cb);

        } catch (JSONException e) {
            e.printStackTrace();
            cb.onFailure(null, null, ApplejackError.jsonError);
        }
    }

    public void checkJWT(Context context, HttpCallback cb) {
        // cannot use same get() as everyone else or we might end up in a loop
        getWithoutAuthCheck(context, OGConstants.ourglassCloudBaseUrl + OGConstants.checkJWTPath, cb);
    }

    public void getToken(Context context, HttpCallback cb) {
        get(context, OGConstants.ourglassCloudBaseUrl + OGConstants.getTokenPath, cb);
    }

    public void getVenues(Context context, HttpCallback cb) {
        get(context, OGConstants.ourglassCloudBaseUrl + OGConstants.newVenuesPath, cb);
    }

    public void getDevices(Context context, String venueUUID, HttpCallback cb) {
        get(context, OGConstants.ourglassCloudBaseUrl + OGConstants.devicesPath + venueUUID, cb);
    }

    public void getAuthStatus(Context context, HttpCallback cb) {
        get(context, OGConstants.ourglassCloudBaseUrl + OGConstants.getAuthStatusPath, cb);
    }

    // TODO: is there more we need to do here?
    public void logout(final Context context) {
        SharedPrefsManager.setUserApplejackJwt(context, null);
        SharedPrefsManager.setUserApplejackJwtExpiry(context, 0l);
        SharedPrefsManager.setUserId(context, null);

        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Logged out!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, WelcomeActivity.class);
                context.startActivity(intent);
            }
        });
    }
}
