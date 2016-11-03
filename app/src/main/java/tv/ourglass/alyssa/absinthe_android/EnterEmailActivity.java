package tv.ourglass.alyssa.absinthe_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class EnterEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_email);
    }

    public void next(View view) {
        Intent intent = new Intent(this, EnterPasswordActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    public void goBack(View view) {
        this.finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }
}
