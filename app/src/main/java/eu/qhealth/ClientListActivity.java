package eu.qhealth;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

public class ClientListActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clist);
    }

    public void buttonClick(View v) {
        Intent myIntent = new Intent();

        switch(v.getId()) {
            case R.id.button:
                myIntent.setClassName("eu.qhealth", "eu.qhealth.TrainerActivity");
                startActivity(myIntent);
                break;
            case R.id.approval:
                myIntent.setClassName("eu.qhealth", "eu.qhealth.ApprovalActivity");
                startActivity(myIntent);
                break;
        }
    }

}
