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

public class PushupFragment extends android.app.Fragment {
    ExerciseListener exl;
    private Sensor lumi;
    private long lastUpdate = 0;
    private TextView data;
    private float lu;
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
        lumi = sensorService.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lumi!=null) {
            sensorService.registerListener(lumiSensorEventListener, lumi,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        View view = inflater.inflate(R.layout.frag_pushup, container,
                false);
        amt = new Random().nextInt(10) + 5;
        TextView tvamt = view.findViewById(R.id.pleasedo);
        data = view.findViewById(R.id.data);
        tvamt.append(" "+amt+" "+getString(R.string.pushs)+".");
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
        String s = getString(R.string.lumi)+" "+lu+".";
        s += "\n"+getString(R.string.youvedone)+" "+amtdone+" ";
        s += amtdone!=1 ? getString(R.string.pushs) : getString(R.string.push);
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

    private SensorEventListener lumiSensorEventListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;

            lu = values[0];

            long actualTime = event.timestamp;
            if (actualTime - lastUpdate < updateDelay) {
                return;
            }
            if (iterations<5) {
                if (lu>base)
                    base=lu;
            } else {
                if (high) {
                    if (lu<=0.3*base) {
                        high = false;
                    }
                } else {
                    if (lu>=0.7*base) {
                        amtdone++;
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