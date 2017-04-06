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

import tv.ourglass.alyssa.bourbon_android.Models.OGConstants;
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

        if (Build.VERSION.SDK_INT >= 21) {
            webViewClient = new WebViewClient() {
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    view.setVisibility(View.INVISIBLE);
                    showAlert("Uh oh!", error.toString());
                }

                @TargetApi(21)
                public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                    String newUrl = request.getUrl().toString();

                    if (newUrl.contains(OGConstants.appControlPath)) {
                        ((MainTabsActivity) getActivity())
                                .openNewFragment(AppControlViewFragment.newInstance(newUrl));
                    }

                    return null;
                }
            };

        } else {
            webViewClient = new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    view.setVisibility(View.INVISIBLE);
                    showAlert("Uh oh!", description);
                }

                public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                    if (url.contains(OGConstants.appControlPath)) {
                        ((MainTabsActivity) getActivity())
                                .openNewFragment(AppControlViewFragment.newInstance(url));
                    }

                    return null;
                }
            };
        }
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
