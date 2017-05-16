package tv.ourglass.alyssa.bourbon_android.Views;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;
import android.content.Context;

import tv.ourglass.alyssa.bourbon_android.Model.OGConstants;
import tv.ourglass.alyssa.bourbon_android.R;

/**
 * Created by atorres on 3/1/17.
 */

public class RegularTextView extends TextView {

    public RegularTextView(Context context) {
        super(context);
        setStyle(context);
    }

    public RegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setStyle(context);

    }

    private void setStyle(Context c) {
        setTypeface(Typeface.createFromAsset(c.getAssets(), OGConstants.regularFont));
        setTextColor(ContextCompat.getColor(c, R.color.OGWhite));
    }
}
