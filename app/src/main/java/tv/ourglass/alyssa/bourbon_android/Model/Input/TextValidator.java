package tv.ourglass.alyssa.bourbon_android.Model.Input;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tv.ourglass.alyssa.bourbon_android.BourbonApplication;
import tv.ourglass.alyssa.bourbon_android.Model.OGConstants;
import tv.ourglass.alyssa.bourbon_android.R;

/**
 * Text validation class that validates text after it changes.
 *
 * Created by atorres on 5/20/17.
 */
public abstract class TextValidator implements TextWatcher {
    private final TextView textView;

    public TextValidator(TextView textView) {
        this.textView = textView;
    }

    public abstract void validate(TextView textView, String text);

    public static TextValidator newInstance(TextView textView, final InputType inputType) {
        return new TextValidator(textView) {
            @Override
            public void validate(TextView textView, String text) {
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
    final public void afterTextChanged(Editable s) {
        String text = textView.getText().toString();
        validate(textView, text);
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) { }

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
