package tv.ourglass.alyssa.bourbon_android.Views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import tv.ourglass.alyssa.bourbon_android.Model.OGConstants;

/**
 * Created by atorres on 3/1/17.
 */

public class LightTextView extends TextView {

    public LightTextView(Context context) {
        super(context);
        setFont(context);
    }

    public LightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    private void setFont(Context c) {
        Typeface font = Typeface.createFromAsset(c.getAssets(), OGConstants.lightFont);
        setTypeface(font);
    }
}
