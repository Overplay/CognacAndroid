package tv.ourglass.ourglasstv.Networking;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import tv.ourglass.ourglasstv.OGTVApplication;
import tv.ourglass.ourglasstv.Model.SharedPrefsManager;

/**
 * Created by mkahn on 5/30/17.
 */

public class OGHeaderInterceptor implements Interceptor {

    public static String sessionCookie;
    //public static String xAuthHeaderValue = "x-ogdevice-1234";
    public static String xAuthHeaderKey = "x-og-authorization"; // to get past weird Nginx issue

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest;

        Request.Builder b = request.newBuilder();

        b.addHeader("User-Agent", "OGMobileDroid");

        String jwt = SharedPrefsManager.getJwt(OGTVApplication.getContext());
        if (jwt!=null){

            b.addHeader(xAuthHeaderKey, "Bearer "+jwt);

        }

        if (sessionCookie!=null){
            b.addHeader("Cookie", sessionCookie);
        }

        return chain.proceed(b.build());
    }
}