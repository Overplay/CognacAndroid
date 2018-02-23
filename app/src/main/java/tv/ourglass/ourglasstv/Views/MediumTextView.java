package tv.ourglass.ourglasstv.Views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import tv.ourglass.ourglasstv.Model.OGConstants;

/**
 * Created by atorres on 3/1/17.
 */

public class MediumTextView extends TextView {

    public MediumTextView(Context context) {
        super(context);
        setFont(context);
    }

    public MediumTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    private void setFont(Context c) {
        Typeface font = Typeface.createFromAsset(c.getAssets(), OGConstants.mediumFont);
        setTypeface(font);
    }
}