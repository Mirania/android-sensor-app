package eu.qhealth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MainFragment extends android.app.Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_main, container,
                false);
        decideIcon();
        showImage();
        TextView tv = view.findViewById(R.id.textView);
        TextView bmitv = view.findViewById(R.id.bmi);
        TextView avgtv = view.findViewById(R.id.avg);
        TextView tiptv = view.findViewById(R.id.tip);

        if (Globals.getUsername()!=null) {
            try {
                User u = new GetScoreTask(getActivity()).execute().get();
                tv.append(", " + Globals.getUsername() + "!");
                bmitv.append(" "+Globals.getBMIstring());
                avgtv.append(" "+u.getRatingString());
            } catch (Exception e) {
                Toast.makeText(getActivity(), getString(R.string.infoerror),
                        Toast.LENGTH_LONG).show();
                tv.append("!");
                bmitv.append(" "+getString(R.string.unknown));
                avgtv.append(" "+getString(R.string.unknown));
            }
        } else {
            tv.append("!");
            bmitv.append(" "+getString(R.string.unknown));
            avgtv.append(" "+getString(R.string.unknown));
        }
        int choice = new Random().nextInt((3 - 1) + 1) + 1;
        switch (choice) {
            case 1:
                tiptv.setText(R.string.tip1);
                break;
            case 2:
                tiptv.setText(R.string.tip2);
                break;
            case 3:
                tiptv.setText(R.string.tip3);
                break;
        }




        return view;
    }

    public void decideIcon() {
        ImageView bmi = view.findViewById(R.id.bmiImage);
        float i = Globals.getBMI();
        if (i<18) bmi.setImageResource(R.drawable.low);
        else if (i<25) bmi.setImageResource(R.drawable.normal);
        else if (i<30) bmi.setImageResource(R.drawable.high);
        else bmi.setImageResource(R.drawable.veryhigh);
    }

    public void showImage() {
        ImageView imageView = view.findViewById(R.id.imageView);
        Integer i = 1;
        try {
            i = new GetImageTask(getActivity()).execute().get();
        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.imgerror),
                    Toast.LENGTH_LONG).show();
            imageView.setImageResource(R.drawable.ic_launcher);
            return;
        }
        switch (i) {
            case 1: imageView.setImageResource(R.drawable.p1);
                break;
            case 2: imageView.setImageResource(R.drawable.p2);
                break;
            case 3: imageView.setImageResource(R.drawable.p3);
                break;
        }
    }

    public void swapImage() {
        ImageView imageView = view.findViewById(R.id.imageView);
        Integer i = 1;
        try {
            i = new SwapImageTask(getActivity()).execute().get();
        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.imgerror),
                    Toast.LENGTH_LONG).show();
            imageView.setImageResource(R.drawable.ic_launcher);
            return;
        }
        switch (i) {
            case 1: imageView.setImageResource(R.drawable.p1);
            break;
            case 2: imageView.setImageResource(R.drawable.p2);
            break;
            case 3: imageView.setImageResource(R.drawable.p3);
            break;
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

    private class GetImageTask extends AsyncTask<Void, Void, Integer> {

        private Context ctx;
        public GetImageTask (Context context){
            ctx = context;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            DBHandler db = new DBHandler(ctx);
            return db.getImage(Globals.getUsername());
        }
    }

    private class SwapImageTask extends AsyncTask<Void, Void, Integer> {

        private Context ctx;

        public SwapImageTask (Context context){
            ctx = context;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            DBHandler db = new DBHandler(ctx);
            int i = db.getImage(Globals.getUsername());
            if (i==3) {
                db.setImage(Globals.getUsername(), 1);
                return 1;
            }
            else {
                db.setImage(Globals.getUsername(), i+1);
                return i+1;
            }
        }
    }
}