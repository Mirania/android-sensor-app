package eu.qhealth;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class ApprovalActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);
    }

    public void buttonClick(View v) {
        Intent myIntent = new Intent();

        switch(v.getId()) {
            case R.id.button:
                myIntent.setClassName("eu.qhealth", "eu.qhealth.TrainerActivity");
                startActivity(myIntent);
                break;
            case R.id.clist:
                myIntent.setClassName("eu.qhealth", "eu.qhealth.ClientListActivity");
                startActivity(myIntent);
                break;
        }
    }
}
