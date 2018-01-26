package eu.qhealth;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        if (Globals.checkIfFirstTime) new DBHandler(this).fakeDB();
    }


    public void buttonClick(View v) {
        Intent myIntent = new Intent();

        switch(v.getId()) {
            case R.id.button:
                String user = ((TextView) findViewById(R.id.name)).getText().toString();
                String pw = ((TextView) findViewById(R.id.pw)).getText().toString();
                try {
                    Boolean[] res = new LoginTask(this).execute(user, pw).get();
                    if (res[0]) {
                        if (res[1]) myIntent.setClassName("eu.qhealth", "eu.qhealth.TrainerActivity");
                        else myIntent.setClassName("eu.qhealth", "eu.qhealth.MainActivity");
                        startActivity(myIntent);
                    } else {
                        Toast.makeText(this, getString(R.string.loginerror),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, getString(R.string.loginerror2),
                            Toast.LENGTH_LONG).show();
                    Log.e("e",e.toString());
                }
                break;
            case R.id.button2:
                Globals.setIntromode(true);
                Globals.setUsertype(false);
                myIntent.setClassName("eu.qhealth", "eu.qhealth.IntroActivity");
                startActivity(myIntent);
                break;
        }
    }

    public void removeFocus(View v) {
        InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private class LoginTask extends AsyncTask<String, Void, Boolean[]> {

        private Context ctx;

        public LoginTask (Context context){
            ctx = context;
        }

        @Override
        protected Boolean[] doInBackground(String... strings) {
            //[valid login?, is trainer?]
            DBHandler db = new DBHandler(ctx);
            return db.validateLogin(strings[0],strings[1]);
        }
    }
}
