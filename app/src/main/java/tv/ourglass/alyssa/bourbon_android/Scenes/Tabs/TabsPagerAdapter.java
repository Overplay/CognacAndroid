package tv.ourglass.alyssa.bourbon_android.Scenes.Tabs;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tv.ourglass.alyssa.bourbon_android.Scenes.BlankFragment;
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

        public TabOption(String title, Integer icon) {
            this.title = title;
            this.icon = icon;
        }
    }

    private ArrayList<TabOption> tabs = new ArrayList<TabOption>() {{
        add(new TabOption("TV", R.drawable.ic_tv_black_24dp));
        add(new TabOption("Locations", R.drawable.ic_map_black_24dp));
        add(new TabOption("Settings", R.drawable.ic_settings_black_24dp));
    }};

    public TabsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (tabs.get(position).title) {
            case "TV":
                return new ChooseVenueFragment();
            case "Locations":
                return new MapFragment();
            case "Settings":
                return new SettingsFragment();
            default:
                return new BlankFragment();
        }
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = ContextCompat.getDrawable(context, tabs.get(position).icon);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);

        TextView text = (TextView) v.findViewById(R.id.text);
        text.setText(tabs.get(position).title);

        Typeface font = Typeface.createFromAsset(context.getAssets(), "Poppins-Regular.ttf");
        text.setTypeface(font);

        ImageView img = (ImageView) v.findViewById(R.id.image);
        img.setImageResource(tabs.get(position).icon);

        return v;
    }
}
