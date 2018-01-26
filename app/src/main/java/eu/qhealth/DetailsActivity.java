package eu.qhealth;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends FragmentActivity {
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        user = (User) getIntent().getSerializableExtra("user");
    }

    public void buttonClick(View v) {
        Intent myIntent = new Intent();

        switch (v.getId()) {
            case R.id.button:
                myIntent.setClassName("eu.qhealth", "eu.qhealth.TrainerActivity");
                startActivity(myIntent);
                break;
            case R.id.clist:
                myIntent.setClassName("eu.qhealth", "eu.qhealth.ClientListActivity");
                startActivity(myIntent);
                break;
            case R.id.confirm:
                TextView infotv = findViewById(R.id.editText);
                Spinner catsp = findViewById(R.id.category);
                String info = infotv.getText().toString();
                String cat = catsp.getSelectedItem().toString();

                try {
                    switch (new NewExTask(this).execute(info,cat).get()) {
                        case 0:
                            Toast.makeText(this, getString(R.string.detailsyes),
                                    Toast.LENGTH_LONG).show();
                            infotv.setText("");
                            break;
                        case 1:
                            Toast.makeText(this, getString(R.string.detailserror),
                                    Toast.LENGTH_LONG).show();
                            break;
                    }

                } catch (Exception e) {
                    Toast.makeText(this, getString(R.string.detailserror2),
                            Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    private class NewExTask extends AsyncTask<String, Void, Integer> {

        private Context ctx;

        public NewExTask (Context context){
            ctx = context;
        }

        @Override
        protected Integer doInBackground(String... strings) {
            DBHandler db = new DBHandler(ctx);

            if (strings[0].equals("")) {
                return 1;
            }

            Exercises e = new Exercises(strings[1],strings[0]);
            db.addAndLinkExercise(e,user);
            return 0;
        }
    }

    public void removeFocus(View v) {
        InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}
