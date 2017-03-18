package tv.ourglass.alyssa.bourbon_android.Scenes.Tabs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import tv.ourglass.alyssa.bourbon_android.R;
import tv.ourglass.alyssa.bourbon_android.Scenes.Control.ChooseDeviceFragment;

public class MainTabsActivity extends AppCompatActivity {

    String TAG = "MainTabsActivity";

    private TabsPagerAdapter tabsPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private BroadcastReceiver mWifiBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "got network change!");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabs);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        tabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(),
                MainTabsActivity.this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(tabsPagerAdapter);

        viewPager.setOffscreenPageLimit(2); // change this for more tabs to preserve state

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Iterate over all tabs and set the custom view
        if (tabLayout != null) {
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab != null) {
                    tab.setCustomView(tabsPagerAdapter.getTabView(i));
                }
            }
        }

        // start receiving data for network connectivity changes
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(mWifiBroadcastReceiver, intentFilter);
    }

    @Override
    public void onBackPressed() {
        BackStackFragment.handleBackPressed(getSupportFragmentManager());
    }

    public void openNewFragment(Fragment fragment) {
        HostFragment hostFragment = (HostFragment) tabsPagerAdapter.getItem(viewPager.getCurrentItem());
        hostFragment.replaceFragment(fragment, true);
    }
}
