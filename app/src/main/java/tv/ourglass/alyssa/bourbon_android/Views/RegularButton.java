package tv.ourglass.alyssa.bourbon_android.Views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import tv.ourglass.alyssa.bourbon_android.Model.OGConstants;

/**
 * Created by atorres on 3/1/17.
 */

public class RegularButton extends AppCompatButton {

    public RegularButton(Context context) {
        super(context);
        setFont(context);
    }

    public RegularButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    private void setFont(Context c) {
        Typeface font = Typeface.createFromAsset(c.getAssets(), OGConstants.regularFont);
        setTypeface(font);
    }
}
