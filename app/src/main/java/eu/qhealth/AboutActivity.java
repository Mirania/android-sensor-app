package eu.qhealth;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

public class AboutActivity extends FragmentActivity {
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        user = (User) getIntent().getSerializableExtra("user");
    }

    public void buttonClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                Intent myIntent = new Intent();
                if (user.getTypeofuser().equals("trainer")) myIntent.setClassName("eu.qhealth", "eu.qhealth.TrainerActivity");
                else myIntent.setClassName("eu.qhealth", "eu.qhealth.MainActivity");
                startActivity(myIntent);
                break;
        }
    }
}
