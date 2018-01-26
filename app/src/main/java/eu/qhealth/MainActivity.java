package eu.qhealth;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends FragmentActivity {
    MainFragment f;
    User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        f = (MainFragment) getFragmentManager().findFragmentById(R.id.fragment1);
        try {
            u = new GetEverythingTask(this).execute().get();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.usererror),
                    Toast.LENGTH_LONG).show();
        }
    }


    public void buttonClick(View v) {
        Intent myIntent = new Intent();

        switch(v.getId()) {
            case R.id.button:
                Globals.setIntromode(false);
                myIntent.setClassName("eu.qhealth", "eu.qhealth.IntroActivity");
                startActivity(myIntent);
                break;
            case R.id.about:
                try {
                    myIntent.setClassName("eu.qhealth", "eu.qhealth.AboutActivity");
                    if (u==null) u = new GetEverythingTask(this).execute().get();
                    myIntent.putExtra("user", u);
                    startActivity(myIntent);
                } catch (Exception e) {
                    Toast.makeText(this, getString(R.string.usererror),
                            Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.plan:
                try {
                    myIntent.setClassName("eu.qhealth", "eu.qhealth.PlanActivity");
                    myIntent.putExtra("user", new GetScoreTask(this).execute().get());
                    startActivity(myIntent);
                } catch (Exception e) {
                    Toast.makeText(this, getString(R.string.mainerror),
                            Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.logout:
                myIntent.setClassName("eu.qhealth", "eu.qhealth.LoginActivity");
                startActivity(myIntent);
                break;
            case R.id.buttonimg:
                f.swapImage();
        }

    }

    private class GetScoreTask extends AsyncTask<Void, Void, User> {

        private Context ctx;

        public GetScoreTask (Context context){
            ctx = context;
        }

        @Override
        protected User doInBackground(Void... voids) {
            DBHandler db = new DBHandler(ctx);
            return db.getUserStats(Globals.getUsername());
        }
    }

    private class GetEverythingTask extends AsyncTask<Void, Void, User> {

        private Context ctx;

        public GetEverythingTask (Context context){
            ctx = context;
        }

        @Override
        protected User doInBackground(Void... voids) {
            DBHandler db = new DBHandler(ctx);
            return db.getAllUserStats(Globals.getUsername());
        }
    }



}

