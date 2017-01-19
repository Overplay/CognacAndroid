package tv.ourglass.alyssa.absinthe_android.Scenes.Tabs;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;

import tv.ourglass.alyssa.absinthe_android.Networking.OGDPBroadcastReceiver;
import tv.ourglass.alyssa.absinthe_android.Networking.OGDPService;
import tv.ourglass.alyssa.absinthe_android.R;

public class MainTabsActivity extends AppCompatActivity {

    String TAG = "MainTabsActivity";

    OGDPBroadcastReceiver mBroadcastRcvr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabs);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        TabsPagerAdapter pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(),
                MainTabsActivity.this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        if (viewPager != null) {
            viewPager.setAdapter(pagerAdapter);
        }

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }

        // Iterate over all tabs and set the custom view
        if (tabLayout != null) {
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab != null) {
                    tab.setCustomView(pagerAdapter.getTabView(i));
                }
            }
        }

        Intent di = new Intent(this, OGDPService.class);
        startService(di);
    }

    @Override
    public void onBackPressed() {
        // make back button do nothing
    }

    @Override
    protected void onResume(){
        super.onResume();
        registerOGDPResponse();
    }

    @Override
    protected void onDestroy(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastRcvr);
        super.onDestroy();
    }

    public void registerOGDPResponse(){

        mBroadcastRcvr = new OGDPBroadcastReceiver(new OGDPBroadcastReceiver.OGDPBroadcastReceiverListener() {
            @Override
            public void foundOGs(final HashMap<String, JSONObject> devices) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Set<String> ipSet = devices.keySet();
                        String ipString = "...";
                        for (String ipAddr: ipSet){
                            ipString = ipString + ipAddr+ "...";
                        }
                        Log.d(TAG, ipString);
                    }
                });
            }

            @Override
            public void ogSearchFail(String errString, Exception e) {

            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastRcvr,
                new IntentFilter("ogdp"));


    }
}
