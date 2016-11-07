package tv.ourglass.alyssa.absinthe_android;

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

/**
 * Created by alyssa on 11/6/16.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private String[] tabTitles = {"TV", "Locations", "Settings"};
    private int[] tabIcons = {
            R.drawable.ic_tv_black_24dp,
            R.drawable.ic_map_black_24dp,
            R.drawable.ic_settings_black_24dp
    };

    public TabsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (tabTitles[position]) {
            case "TV":
                return new DevicesFragment();
            case "Locations":
                return new LocationsFragment();
            case "Settings":
                return new SettingsFragment();
            default:
                return new BlankFragment();
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = ContextCompat.getDrawable(context, tabIcons[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);

        TextView text = (TextView) v.findViewById(R.id.text);
        text.setText(tabTitles[position]);

        Typeface font = Typeface.createFromAsset(context.getAssets(), "Poppins-Regular.ttf");
        text.setTypeface(font);

        ImageView img = (ImageView) v.findViewById(R.id.image);
        img.setImageResource(tabIcons[position]);

        return v;
    }
}
