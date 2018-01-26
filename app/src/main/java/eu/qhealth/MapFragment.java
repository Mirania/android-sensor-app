package eu.qhealth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;
import java.util.Random;

public class MapFragment extends android.app.Fragment {
    ExerciseListener exl;
    private LocationManager locationManager;
    private double longt = 0, lat = 0;
    private long start = 0;
    private long current = 0;
    private TextView data;
    private float amt;
    private float amtdone = 0;
    private long updateDelay = 900;
    private double time = 0;
    private float base = -1;

    public interface ExerciseListener {
        public float result();
        public String resultString();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            exl = (ExerciseListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " has no exl.");
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        Criteria locationCriteria = new Criteria();
        locationManager.getBestProvider(locationCriteria, true);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        View view = inflater.inflate(R.layout.frag_map, container,
                false);
        amt = (float) ((new Random().nextInt(20) + 10) * 0.1);
        TextView tvamt = view.findViewById(R.id.pleaserun);
        data = view.findViewById(R.id.data);
        tvamt.append(" "+amt+" km.");
        update();
        return view;
    }

    public float result() {
        return calcScore();
    }

    public String resultString() {
        return String.format(Locale.US, getString(R.string.score)+" %.1f.",calcScore());
    }

    public void update() {
        String s = getString(R.string.latitude)+String.format(Locale.US, " %.2f", lat)+".";
        s += "\n"+getString(R.string.longitude)+String.format(Locale.US, " %.2f", longt)+".";
        s += "\n"+getString(R.string.youverun)+String.format(Locale.US, " %.1f", amtdone*1000)+"m "+getString(R.string.sofar)+".";
        s += "\n"+getString(R.string.time)+": "+String.format(Locale.US, "%.0f", time)+"s";
        data.setText(s);
    }

    public float calcScore() {
        float kmh = (float) ((amt/1000.0) / (time/60.0/60.0));

        if (amtdone<amt)
            return 0;
        else if (kmh>20)
            return 0;
            //7km/h+ = 10pt
            //1km/h- = 0pt
        else {
            float s = (float) (5.0/3.0*kmh-5.0/3.0);
            if (s>10) s=10;
            if (s<0) s=0;
            return s;
        }
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location loc) {
            if (start==0) start = loc.getTime();
            current = loc.getTime();

            if (current - start < updateDelay) {
                return;
            }
            double newlongt = loc.getLongitude();
            double newlat = loc.getLatitude();

            double dist = calcDistance(newlongt,newlat);
            if (dist<0.2) amtdone += dist; //due to the data's unreliability

            longt = newlongt;
            lat = newlat;

            if (longt!=0 || lat!=0) time = (current-start)/1000.0;
            if (getActivity()!=null) update();
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

    private float calcDistance(double newlongt, double newlat) {
        double r = 6371e3;
        double l1 = lat * Math.PI/180.0;
        double l2 = newlat * Math.PI/180.0;
        double latdiff = (newlat-lat) * Math.PI/180.0;
        double longdiff = (newlongt-longt) * Math.PI/180.0;

        double a = Math.sin(latdiff/2) * Math.sin(latdiff/2) +
                Math.cos(l1) * Math.cos(l2) *
                        Math.sin(longdiff/2) * Math.sin(longdiff/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = r * c;

        return (float) d;
    }

}