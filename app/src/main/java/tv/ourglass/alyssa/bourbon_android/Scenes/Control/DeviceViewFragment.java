package tv.ourglass.alyssa.bourbon_android.Scenes.Control;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;
import tv.ourglass.alyssa.bourbon_android.BourbonApplication;
import tv.ourglass.alyssa.bourbon_android.Model.OGConstants;
import tv.ourglass.alyssa.bourbon_android.Model.OGVenue.OGVenue;
import tv.ourglass.alyssa.bourbon_android.Model.OGVenue.OGVenueListAdapter;
import tv.ourglass.alyssa.bourbon_android.Model.OGVenue.OGVenueType;
import tv.ourglass.alyssa.bourbon_android.Model.SharedPrefsManager;
import tv.ourglass.alyssa.bourbon_android.R;
import tv.ourglass.alyssa.bourbon_android.Scenes.Tabs.MainTabsActivity;

public class DeviceViewFragment extends WebViewBaseFragment {

    String TAG = "DeviceViewFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        // set target url and display title
        if (args != null) {

            if (args.containsKey(OGConstants.deviceNameExtra)) {
                title = args.getString(OGConstants.deviceNameExtra);
            }
            if (args.containsKey(OGConstants.deviceUrlExtra)) {
                targetUrlString = args.getString(OGConstants.deviceUrlExtra);
            }

        } else {
            title = "";
            targetUrlString = "";
        }

        webViewClient = new WebViewClient() {

            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                view.setVisibility(View.INVISIBLE);
                showAlert("Uh oh!", error.getDescription().toString());
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                view.setVisibility(View.INVISIBLE);
                showAlert("Uh oh!", description);
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();

                if (url.contains(OGConstants.appControlPath)) {
                    ((MainTabsActivity) getActivity())
                            .openNewFragment(AppControlViewFragment.newInstance(url));
                }
                return getNewResponse(url);
            }

            @SuppressWarnings("deprecation")
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (url.contains(OGConstants.appControlPath)) {
                    ((MainTabsActivity) getActivity())
                            .openNewFragment(AppControlViewFragment.newInstance(url));
                }
                return getNewResponse(url);
            }

            // start timing to detect timeout
            @Override
            public void onPageStarted(final WebView view, String url, Bitmap favicon) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(webViewTimeout);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (timeout) {
                            showAlert("Uh oh!", "We were unable to connect to this device.");
                        }
                    }
                }).start();
            }

            // end timeout
            @Override
            public void onPageFinished(WebView view, String url) {
                timeout = false;
            }

        };
    }

    public static DeviceViewFragment newInstance(String deviceName, String deviceUrl) {
        DeviceViewFragment deviceViewFragment = new DeviceViewFragment();

        Bundle args = new Bundle();
        args.putString(OGConstants.deviceNameExtra, deviceName);
        args.putString(OGConstants.deviceUrlExtra, deviceUrl);

        deviceViewFragment.setArguments(args);

        return deviceViewFragment;
    }
}
