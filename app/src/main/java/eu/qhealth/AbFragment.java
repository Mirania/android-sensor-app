package eu.qhealth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class AbFragment extends android.app.Fragment {
    ExerciseListener exl;
    private Sensor acc;
    private long lastUpdate = 0;
    private TextView data;
    private float ac;
    private int amt;
    private int amtdone = 0;
    private long updateDelay = 200;
    private int iterations = 0;
    private float base = -1;
    private boolean high=true;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SensorManager sensorService = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        acc = sensorService.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (acc!=null) {
            sensorService.registerListener(accSensorEventListener, acc,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        View view = inflater.inflate(R.layout.frag_ab, container,
                false);
        amt = new Random().nextInt(10) + 5;
        TextView tvamt = view.findViewById(R.id.pleasedo);
        data = view.findViewById(R.id.data);
        tvamt.append(" "+amt+" "+getString(R.string.abds)+".");
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
        String s = getString(R.string.ac)+String.format(Locale.US, " %.2f",ac)+" m/s^2.";
        s += "\n"+getString(R.string.youvedone)+" "+amtdone+" ";
        s += amtdone!=1 ? getString(R.string.abds) : getString(R.string.abd);
        s += " "+getString(R.string.sofar)+".";
        s += "\n"+getString(R.string.time)+": "+(iterations/5)+"s";
        data.setText(s);
    }

    public float calcScore() {
        if (amtdone<amt)
            return 0;
        else if (iterations<amt*5)
            return 0;
            //3s- por cada = 10pt
            //15s+ por cada = 0pt
        else {
            float s = (float) (-5.0/6.0*((float)iterations/(5.0*amt))+25.0/2.0);
            if (s>10) s=10;
            if (s<0) s=0;
            return s;
        }
    }

    private SensorEventListener accSensorEventListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            // Movement
            float gx = values[0];
            float gy = values[1];
            float gz = values[2];

            ac = (float) Math.sqrt(gx*gx+gy*gy+gz*gz);

            long actualTime = event.timestamp;
            if (actualTime - lastUpdate < updateDelay) {
                return;
            }
            if (iterations<5) {
                if (ac > base)
                    base = ac;
            } else {
                if (high) {
                    if (ac<=0.8*base) {
                        amtdone++;
                        high = false;
                    }
                } else {
                    if (ac>=1.2*base) {
                        high = true;
                    }
                }
            }
            lastUpdate = actualTime;
            if (amtdone<amt) iterations++;
            if (getActivity()!=null) update();
        }
    };
}