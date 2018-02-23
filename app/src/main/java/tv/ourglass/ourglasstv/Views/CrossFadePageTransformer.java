package tv.ourglass.ourglasstv.Views;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by atorres on 5/17/17.
 *
 * Applies a cross fade animation when a page is scrolled.
 */
public class CrossFadePageTransformer implements ViewPager.PageTransformer {

    public void transformPage(View view, float position) {
        if (position <= -1.0f || position >= 1.0f) {
            view.setTranslationX(view.getWidth() * position);
            view.setAlpha(0);

        } else if (position == 0.0f) {
            view.setTranslationX(view.getWidth() * position);
            view.setAlpha(1.0f);

        } else {
            view.setTranslationX(view.getWidth() * -position);
            view.setAlpha(1.0f - Math.abs(position));
        }
    }
}
