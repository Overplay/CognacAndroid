package tv.ourglass.ourglasstv.Scenes.Control;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import tv.ourglass.ourglasstv.Model.OGConstants;
import tv.ourglass.alyssa.ourglasstv.R;

/**
 * Created by atorres on 4/5/17.
 */

public class AppControlViewFragment extends WebViewBaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        // set target url
        if (args != null) {

            if (args.containsKey(OGConstants.appUrlExtra)) {
                targetUrlString = args.getString(OGConstants.appUrlExtra);
            }

        } else {
            targetUrlString = "";
        }

        // set display title
        if (targetUrlString != null) {
            String[] components = targetUrlString.split("displayName=");

            if (components.length >= 2) {
                try {
                    title = URLDecoder.decode(components[1], "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    title = "";
                }
            } else {
                title = "";
            }

        } else {
            title = "";
        }

        webViewClient = new WebViewClient() {

            @TargetApi(android.os.Build.VERSION_CODES.M)
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                view.setVisibility(View.INVISIBLE);
                showAlert(getString(R.string.uhoh), error.getDescription().toString());
            }

            @SuppressWarnings("deprecation")
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                view.setVisibility(View.INVISIBLE);
                showAlert(getString(R.string.uhoh), description);
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
                            //showAlert(getString(R.string.uhoh), "We were unable to connect to this device.");
                        }
                    }
                }).start();
            }

            // end timeout
            @Override
            public void onPageFinished(WebView view, String url) {
                timeout = false;
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return getNewResponse(request.getUrl().toString());
            }

            @SuppressWarnings("deprecation")
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return getNewResponse(url);
            }
        };
    }

    public static AppControlViewFragment newInstance(String appUrl) {
        AppControlViewFragment appControlViewFragment = new AppControlViewFragment();

        Bundle args = new Bundle();
        args.putString(OGConstants.appUrlExtra, appUrl);
        appControlViewFragment.setArguments(args);

        return appControlViewFragment;
    }
}
