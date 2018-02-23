package tv.ourglass.ourglasstv.Scenes.Introduction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import tv.ourglass.ourglasstv.Model.OGConstants;
import tv.ourglass.alyssa.ourglasstv.R;
import tv.ourglass.ourglasstv.Scenes.Registration.CheckAuthActivity;

public class IntroSlidesActivity extends AppCompatActivity {

    String TAG = "IntroSlidesActivity";

    // slide image resources
    String[] slideImageURLs = {
            OGConstants.belliniCore + OGConstants.introSlide1Path,
            OGConstants.belliniCore + OGConstants.introSlide2Path,
            OGConstants.belliniCore + OGConstants.introSlide3Path
    };

    ViewPager mSlidesViewPager;

    SlidesPagerAdapter mSlidesPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slides);

        // set up slides view
        mSlidesViewPager = (ViewPager) findViewById(R.id.slides_viewpager);
        mSlidesPagerAdapter = new SlidesPagerAdapter(this);
        mSlidesViewPager.setAdapter(mSlidesPagerAdapter);
        ((TabLayout) findViewById(R.id.slides_tablayout)).setupWithViewPager(mSlidesViewPager);
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
            return slideImageURLs.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View slideView = mLayoutInflater.inflate(R.layout.intro_slide, container, false);

            ImageView imageView = (ImageView) slideView.findViewById(R.id.slide_image_view);
            Picasso.with(mContext)
                    .load(slideImageURLs[position])
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {}

                        @Override
                        public void onError() {
                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(mContext, CheckAuthActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    });

            container.addView(slideView);

            return slideView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }
}