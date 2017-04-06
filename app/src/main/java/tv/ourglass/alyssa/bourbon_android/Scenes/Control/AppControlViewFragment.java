package tv.ourglass.alyssa.bourbon_android.Scenes.Control;

import android.annotation.TargetApi;
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
import java.nio.charset.Charset;

import tv.ourglass.alyssa.bourbon_android.Models.OGConstants;

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


        if (Build.VERSION.SDK_INT >= 21) {
            webViewClient = new WebViewClient() {
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    view.setVisibility(View.INVISIBLE);
                    showAlert("Uh oh!", error.toString());
                }
            };

        } else {
            webViewClient = new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    view.setVisibility(View.INVISIBLE);
                    showAlert("Uh oh!", description);
                }
            };
        }
    }

    public static AppControlViewFragment newInstance(String appUrl) {
        AppControlViewFragment appControlViewFragment = new AppControlViewFragment();

        Bundle args = new Bundle();
        args.putString(OGConstants.appUrlExtra, appUrl);
        appControlViewFragment.setArguments(args);

        return appControlViewFragment;
    }
}
