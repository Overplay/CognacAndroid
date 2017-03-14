package tv.ourglass.alyssa.bourbon_android.Scenes.Control;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import tv.ourglass.alyssa.bourbon_android.Models.OGConstants;
import tv.ourglass.alyssa.bourbon_android.Networking.Applejack;
import tv.ourglass.alyssa.bourbon_android.R;


public class DeviceViewActivity extends AppCompatActivity {

    String mDeviceName;
    String mDeviceUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Show progress bar as web view loads URL
        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        setContentView(R.layout.activity_device_view);

        // set venue name at top
        TextView deviceName = (TextView) findViewById(R.id.deviceName);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mDeviceName = extras.getString(OGConstants.deviceNameExtra);
            deviceName.setText(mDeviceName);

            mDeviceUrl = extras.getString(OGConstants.deviceUrlExtra);
        }

        final WebView webview = (WebView)findViewById(R.id.webview);

        // Configure web view
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(false);

        final Activity activity = this;

        // show progress
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                activity.setProgress(progress * 1000);
            }
        });

        // show alert on error loading page
        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                webview.setVisibility(View.INVISIBLE);
                showAlert("Uh oh!", description);
            }
        });

        webview.loadUrl(mDeviceUrl);
    }

    public void showAlert(String title, String message) {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        DeviceViewActivity.super.onBackPressed();
                    }
                });
        alert.show();
    }
}
