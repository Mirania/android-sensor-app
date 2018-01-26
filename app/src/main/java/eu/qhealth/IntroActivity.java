package eu.qhealth;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

public class IntroActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    public void buttonClick(View v) {
        switch(v.getId()) {
            case R.id.button:
                String name = ((TextView) v.getRootView().findViewById(R.id.name)).getText().toString();
                String pw = ((TextView) findViewById(R.id.pw)).getText().toString();
                String date = ((TextView) findViewById(R.id.date)).getText().toString();
                String weight = ((TextView) findViewById(R.id.weight)).getText().toString();
                String height = ((TextView) findViewById(R.id.height)).getText().toString();
                String body = ((Spinner) findViewById(R.id.body)).getSelectedItem().toString();

                try {
                    AsyncTask<String, Void, Integer> task;
                    if (Globals.getIntromode())
                        task = new NewUserTask(this).execute(name,pw,date,weight,height,body);
                    else
                        task = new UpdateUserTask(this).execute(name,pw,date,weight,height,body);
                    switch (task.get()) {
                        case 0:
                            Intent myIntent = new Intent();
                            if (Globals.getUserType()) myIntent.setClassName("eu.qhealth", "eu.qhealth.TrainerActivity");
                            else myIntent.setClassName("eu.qhealth", "eu.qhealth.MainActivity");
                            startActivity(myIntent);
                            break;
                        case 1:
                            Toast.makeText(this, getString(R.string.introerror),
                                    Toast.LENGTH_LONG).show();
                            break;
                        case 2:
                            Toast.makeText(this, getString(R.string.introerror2),
                                    Toast.LENGTH_LONG).show();
                            break;
                        case 3:
                            Toast.makeText(this, getString(R.string.introerror3),
                                    Toast.LENGTH_LONG).show();
                            break;
                        case 4:
                            Toast.makeText(this, getString(R.string.introerror4),
                                    Toast.LENGTH_LONG).show();
                            break;
                        case 5:
                            Toast.makeText(this, getString(R.string.introerror5),
                                    Toast.LENGTH_LONG).show();
                            break;
                    }

                } catch (Exception e) {
                    Toast.makeText(this, getString(R.string.introerror6),
                            Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    public void removeFocus(View v) {
        InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private class NewUserTask extends AsyncTask<String, Void, Integer> {

        private Context ctx;

        public NewUserTask (Context context){
            ctx = context;
        }

        @Override
        protected Integer doInBackground(String... strings) {
            int age;
            int height;
            float weight;
            DBHandler db = new DBHandler(ctx);

            if (strings[0].equals("") || strings[1].equals("")) {
                return 1;
            }

            if (db.validateUsername(strings[0])) {
                return 5;
            }

            try {
                age = Integer.parseInt(strings[2]);
            } catch (NumberFormatException e) {
                return 2;
            }

            try {
                weight = Float.parseFloat(strings[3]);
            } catch (NumberFormatException e) {
                return 3;
            }

            try {
                height = Integer.parseInt(strings[4]);
            } catch (NumberFormatException e) {
                return 4;
            }

            User u = new User();
            u.setName(strings[0]);
            u.setPword(strings[1]);
            u.setTypeofbody(strings[5]);
            u.setWeight(weight);
            u.setHeight(height);
            u.setAge(age);
            u.setTypeofuser("client");
            db.addUSERS(u);
            Globals.setUsername(strings[0]);
            Globals.setHeight(height);
            Globals.setWeight(weight);

            return 0;
        }
    }

    private class UpdateUserTask extends AsyncTask<String, Void, Integer> {

        private Context ctx;

        public UpdateUserTask (Context context){
            ctx = context;
        }

        @Override
        protected Integer doInBackground(String... strings) {
            int age;
            int height;
            float weight;
            DBHandler db = new DBHandler(ctx);

            if (strings[1].equals("")) {
                return 1;
            }

            try {
                age = Integer.parseInt(strings[2]);
            } catch (NumberFormatException e) {
                return 2;
            }

            try {
                weight = Float.parseFloat(strings[3]);
            } catch (NumberFormatException e) {
                return 3;
            }

            try {
                height = Integer.parseInt(strings[4]);
            } catch (NumberFormatException e) {
                return 4;
            }

            User u = new User();
            u.setName(strings[0]);
            u.setPword(strings[1]);
            u.setTypeofbody(strings[5]);
            u.setWeight(weight);
            u.setHeight(height);
            u.setAge(age);
            db.updateUser(u);
            Globals.setUsername(strings[0]);
            Globals.setHeight(height);
            Globals.setWeight(weight);

            return 0;
        }
    }

}
