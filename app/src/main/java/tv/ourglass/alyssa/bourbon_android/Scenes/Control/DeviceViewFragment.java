package tv.ourglass.alyssa.bourbon_android.Scenes.Control;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import tv.ourglass.alyssa.bourbon_android.Models.OGConstants;
import tv.ourglass.alyssa.bourbon_android.R;

public class DeviceViewFragment extends Fragment {

    String TAG = "DeviceViewFragment";

    String mDeviceName;

    String mDeviceUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        if (args != null) {

            if (args.containsKey(OGConstants.deviceNameExtra)) {
                mDeviceName = args.getString(OGConstants.deviceNameExtra);
            }
            if (args.containsKey(OGConstants.deviceUrlExtra)) {
                mDeviceUrl = args.getString(OGConstants.deviceUrlExtra);
            }

        } else {
            mDeviceName = "";
            mDeviceUrl = "";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_device_view, container, false);

        TextView deviceNameLabel = (TextView) view.findViewById(R.id.deviceName);
        deviceNameLabel.setText(mDeviceName);

        final WebView webview = (WebView) view.findViewById(R.id.webview);

        // Configure web view
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(false);

        // Show progress bar as web view loads URL
        /*getWindow().requestFeature(Window.FEATURE_PROGRESS);

        setContentView(R.layout.activity_device_view);

        final Activity activity = this;

        // show progress
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                activity.setProgress(progress * 100);
            }
        });*/

        // show alert on error loading page
        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                webview.setVisibility(View.INVISIBLE);
                showAlert("Uh oh!", description);
            }
        });

        webview.loadUrl(mDeviceUrl);

        return view;
    }

    public void showAlert(String title, String message) {
        AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getActivity().onBackPressed();
                    }
                });
        alert.show();
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
