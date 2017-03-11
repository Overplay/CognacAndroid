package tv.ourglass.alyssa.bourbon_android.Scenes.Introduction;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import tv.ourglass.alyssa.bourbon_android.R;
import tv.ourglass.alyssa.bourbon_android.Scenes.Registration.CheckAuthActivity;

public class IntroSlidesActivity extends AppCompatActivity {

    String TAG = "IntroSlidesActivity";

    // slide image resources
    int [] mResources = {
            R.drawable.ic_add_location_black_36dp,
            R.drawable.ic_chevron_left_black_24dp,
            R.drawable.ic_card_giftcard_black_36dp,
            R.drawable.ic_person_outline_black_36dp
    };

    ViewPager mSlidesViewPager;

    SlidesPagerAdapter mSlidesPagerAdapter;

    TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slides);

        // set up slides view
        mSlidesViewPager = (ViewPager) findViewById(R.id.slides_viewpager);
        mSlidesPagerAdapter = new SlidesPagerAdapter(this);
        mSlidesViewPager.setAdapter(mSlidesPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.slides_tablayout);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(mSlidesViewPager);
        }
    }

    public void cancel(View view) {

        Intent intent = new Intent(this, CheckAuthActivity.class);
        startActivity(intent);
    }

    class SlidesPagerAdapter extends PagerAdapter {

        Context mContext;

        LayoutInflater mLayoutInflater;

        SlidesPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View slideView = mLayoutInflater.inflate(R.layout.intro_slide, container, false);

            ImageView imageView = (ImageView) slideView.findViewById(R.id.slide_image_view);
            imageView.setImageResource(mResources[position]);

            container.addView(slideView);

            return slideView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }
}