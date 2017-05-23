package tv.ourglass.alyssa.bourbon_android.Model.Input;

import android.view.View;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tv.ourglass.alyssa.bourbon_android.BourbonApplication;
import tv.ourglass.alyssa.bourbon_android.Model.OGConstants;
import tv.ourglass.alyssa.bourbon_android.R;

/**
 * Class used to validate input in a text view when focus is lost, as opposed to after every change.
 * Created by atorres on 5/20/17.
 */

public abstract class TextFocusChangeListener implements View.OnFocusChangeListener {
    private final TextView textView;

    public TextFocusChangeListener(TextView textView) {
        this.textView = textView;
    }

    public abstract void validate(TextView textView);

    public static TextFocusChangeListener newInstance(TextView textView, final InputType inputType) {
        return new TextFocusChangeListener(textView) {
            @Override
            public void validate(TextView textView) {
                String text = textView.getText().toString();

                switch (inputType) {
                    case EMAIL:
                        if (isMatch(text, OGConstants.emailRegEx)) {
                            textView.setError(null);
                        } else {
                            textView.setError(BourbonApplication.getContext()
                                    .getString(R.string.email_not_valid));
                        }
                        break;

                    case PASSWORD:
                        if (isMatch(text, OGConstants.passwordRegEx)) {
                            textView.setError(null);
                        } else {
                            textView.setError(BourbonApplication.getContext()
                                    .getString(R.string.pwd_not_valid));
                        }
                        break;

                    case NONEMPTY:
                        if (text.isEmpty() || text.trim().isEmpty()) {
                            textView.setError(BourbonApplication.getContext()
                                    .getString(R.string.req_field));
                        } else {
                            textView.setError(null);
                        }
                        break;
                }
            }
        };
    }

    @Override
    final public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus) {
            validate(textView);
        }
    }

    private static boolean isMatch(String s, String regEx) {
        try {
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(s);
            return matcher.matches();

        } catch (RuntimeException e) {
            return false;
        }
    }

}
