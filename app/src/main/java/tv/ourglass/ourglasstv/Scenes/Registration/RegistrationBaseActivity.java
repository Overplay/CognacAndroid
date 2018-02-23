package tv.ourglass.ourglasstv.Scenes.Registration;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tv.ourglass.ourglasstv.Model.OGConstants;
import tv.ourglass.alyssa.ourglasstv.R;

/**
 * Created by atorres on 11/11/16.
 */

public class RegistrationBaseActivity extends AppCompatActivity {

    String TAG = "RegBaseActivity";

    public static boolean isValidEmail(String email) {
        return isMatch(email, OGConstants.emailRegEx);
    }

    public static boolean isValidPassword(String pwd) {
        return isMatch(pwd, OGConstants.passwordRegEx);
    }

    public static boolean isMatch(String s, String regEx) {
        try {
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(s);
            return matcher.matches();

        } catch (RuntimeException e) {
            return false;
        }
    }

    public void goBack(View view) {
        this.finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    public void showAlert(String title, String message) {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alert.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }
}
