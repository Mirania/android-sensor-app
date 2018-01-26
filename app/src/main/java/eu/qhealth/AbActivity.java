package eu.qhealth;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.AsyncTask;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.widget.Toast;

public class AbActivity extends FragmentActivity implements AbFragment.ExerciseListener {
    User user;
    Exercises ex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ab);

        user = (User) getIntent().getSerializableExtra("user");
        ex = (Exercises) getIntent().getSerializableExtra("ex");
    }

    public void buttonClick(View v) {
        switch(v.getId()) {
            case R.id.button:
                Toast.makeText(this, resultString(),
                        Toast.LENGTH_LONG).show();
                user.incAmt();
                user.addScore(result());
                try {
                    new UpdateScoreTask(this).execute(user).get();
                    new RemoveExTask(this).execute(ex.getId()).get();
                    notifyUser();
                } catch (Exception e) {
                    Toast.makeText(this, getString(R.string.aberror),
                            Toast.LENGTH_LONG).show();
                }
                Intent myIntent= new Intent();
                myIntent.putExtra("user",user);
                myIntent.setClassName("eu.qhealth", "eu.qhealth.PlanActivity");
                startActivity(myIntent);
                break;
        }
    }

    @Override
    public float result() {
        AbFragment f = (AbFragment) getFragmentManager().findFragmentById(R.id.fragment1);
        return f.result();
    }

    public String resultString() {
        AbFragment f = (AbFragment) getFragmentManager().findFragmentById(R.id.fragment1);
        return f.resultString();
    }

    private class UpdateScoreTask extends AsyncTask<User, Void, Boolean> {

        private Context ctx;

        public UpdateScoreTask (Context context){
            ctx = context;
        }

        @Override
        protected Boolean doInBackground(User... users) {
            DBHandler db = new DBHandler(ctx);
            db.updateScore(users[0]);
            return true;
        }
    }

    private class RemoveExTask extends AsyncTask<Integer, Void, Boolean> {

        private Context ctx;

        public RemoveExTask (Context context){
            ctx = context;
        }

        @Override
        protected Boolean doInBackground(Integer... ints) {
            DBHandler db = new DBHandler(ctx);
            db.deleteExercise(ints[0]);
            return true;
        }
    }

    private void notifyUser() {
        if (user.getAmt()%10==0) {
            buildNotification(getString(R.string.everyten1)+" "
                    +user.getAmt()+getString(R.string.everyten2));
        }
        if (user.getRating()>=9.5) {
            buildNotification(getString(R.string.threshold1)+" "
                    +user.getRatingString()+" "+getString(R.string.threshold2));
        }
    }

    private void buildNotification(String msg) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notif)
                        .setContentTitle("Score update!")
                        .setContentText(msg);
        Intent resultIntent = new Intent(this, LoginActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(LoginActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
