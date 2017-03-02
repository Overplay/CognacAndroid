package tv.ourglass.alyssa.bourbon_android.Views;

import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import android.content.Context;

import tv.ourglass.alyssa.bourbon_android.Models.OGConstants;

/**
 * Created by atorres on 3/1/17.
 */

public class RegularTextView extends TextView {

    public RegularTextView(Context context) {
        super(context);
        setFont(context);
    }

    public RegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    private void setFont(Context c) {
        Typeface font = Typeface.createFromAsset(c.getAssets(), OGConstants.regularFont);
        setTypeface(font);
    }
}
