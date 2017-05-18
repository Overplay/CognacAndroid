package tv.ourglass.alyssa.bourbon_android.Scenes.Tabs;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tv.ourglass.alyssa.bourbon_android.Scenes.Control.ChooseVenueFragment;
import tv.ourglass.alyssa.bourbon_android.Scenes.Map.MapFragment;
import tv.ourglass.alyssa.bourbon_android.R;
import tv.ourglass.alyssa.bourbon_android.Scenes.Settings.SettingsFragment;

/**
 * Created by alyssa on 11/6/16.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    private class TabOption {
        String title;
        Integer icon;

        TabOption(String title, Integer icon) {
            this.title = title;
            this.icon = icon;
        }
    }

    private ArrayList<TabOption> tabOptions = new ArrayList<TabOption>() {{
        add(new TabOption("TV", R.drawable.ic_tv_black_24dp));
        add(new TabOption("Map", R.drawable.ic_map_black_24dp));
        add(new TabOption("Settings", R.drawable.ic_settings_black_24dp));
    }};

    private List<Fragment> tabs = new ArrayList<>();

    public TabsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        initializeTabs();
    }

    private void initializeTabs() {
        tabs.add(HostFragment.newInstance(new ChooseVenueFragment()));
        tabs.add(HostFragment.newInstance(new MapFragment()));
        tabs.add(HostFragment.newInstance(new SettingsFragment()));
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = ContextCompat.getDrawable(context, tabOptions.get(position).icon);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    /**
     * Returns the view with an icon and a title below for the tab at position.
     *
     * @param position tab position
     * @return the tab view
     */
    View getTabView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);

        TextView text = (TextView) v.findViewById(R.id.text);
        text.setText(tabOptions.get(position).title);

        Typeface font = Typeface.createFromAsset(context.getAssets(), "Poppins-Regular.ttf");
        text.setTypeface(font);

        ImageView img = (ImageView) v.findViewById(R.id.image);
        img.setImageResource(tabOptions.get(position).icon);

        return v;
    }
}
