package tv.ourglass.alyssa.bourbon_android.Scenes.Control;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import tv.ourglass.alyssa.bourbon_android.R;

/**
 * Created by atorres on 4/5/17.
 */

public class WebViewBaseFragment extends Fragment {

    String targetUrlString;
    String title;
    WebViewClient webViewClient;
    Boolean timeout;
    Long webViewTimeout = 15000l;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_device_view, container, false);

        TextView deviceNameLabel = (TextView) view.findViewById(R.id.title);
        deviceNameLabel.setText(title);

        final WebView webview = (WebView) view.findViewById(R.id.webview);

        // Configure web view
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(false);
        timeout = true;


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
        webview.setWebViewClient(webViewClient);

        webview.loadUrl(targetUrlString);

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
}
