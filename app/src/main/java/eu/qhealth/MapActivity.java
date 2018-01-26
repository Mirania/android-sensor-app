package eu.qhealth;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.widget.Toast;

public class MapActivity extends FragmentActivity implements MapFragment.ExerciseListener {
    User user;
    Exercises ex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

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
        MapFragment f = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment1);
        return f.result();
    }

    public String resultString() {
        MapFragment f = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment1);
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

















/*package eu.qhealth;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MapActivity extends FragmentActivity {

    private static SensorManager sensorService;
    private Sensor sensor;
    private Sensor lumi;
    private long lastUpdate = 0;
    private TextView tv;
    private float gx = 0, gy = 0, gz = 0, lu = 0;
    private long updateDelay = 200;
    private LocationManager locationManager;
    private double longt = 0, lat = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        tv = findViewById(R.id.maptext);

        update();

        sensorService = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorService.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lumi = sensorService.getDefaultSensor(Sensor.TYPE_LIGHT);
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria locationCriteria = new Criteria();
        locationManager.getBestProvider(locationCriteria, true);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0, 0, locationListener);


        if (sensor != null && lumi!=null) {
            sensorService.registerListener(mySensorEventListener, sensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
            sensorService.registerListener(lumiSensorEventListener, lumi,
                    SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "Sensor not found",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void update() {
        tv.setText("X-axis acceleration: "+gx+
                "\nY-axis acceleration: "+gy+
                "\nZ-axis acceleration: "+gz+
                "\n\nLuminosity: "+lu+
                "\n\nLatitude: "+lat+
                "\nLongitude: "+longt);
    }

    private SensorEventListener mySensorEventListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            // Movement
            gx = values[0];
            gy = values[1];
            gz = values[2];

            long actualTime = event.timestamp;
            if (actualTime - lastUpdate < updateDelay) {
                return;
            }
            lastUpdate = actualTime;
            update();
        }
    };

    private SensorEventListener lumiSensorEventListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            // Movement
            lu = values[0];

            long actualTime = event.timestamp;
            if (actualTime - lastUpdate < updateDelay) {
                return;
            }
            lastUpdate = actualTime;
            update();
        }
    };

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location loc) {
            longt = loc.getLongitude();
            lat = loc.getLatitude();

            update();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }

    };


    public void buttonClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                Intent myIntent = new Intent();
                myIntent.setClassName("eu.qhealth", "eu.qhealth.MainActivity");
                startActivity(myIntent);
                break;
        }
    }

}*/
