package eu.qhealth;

import android.*;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class PlanFragment extends Fragment {
    int amt;
    float score;
    User auxUser;
    Exercises auxEx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_plan, container,
                false);

        final User user = (User) getActivity().getIntent().getSerializableExtra("user");
        amt = user.getAmt();
        score = user.getScore();

        ExpandableListView exp = view.findViewById(R.id.planlistview);

        try {
            final ArrayList<Exercises> ex = new GetExTask(getActivity()).execute().get();
            final ExerciseAdapter adp = new ExerciseAdapter(getActivity(), ex);

            exp.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            final int groupPosition, int childPosition, long id) {
                    String type = ex.get(groupPosition).getType();
                    if (type.equals("Diet") || type.equals("Dieta")) {
                        DialogInterface.OnClickListener dlist = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int op) {
                                switch (op) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        user.incAmt();
                                        user.addScore(10);
                                        amt = user.getAmt();
                                        score = user.getScore();
                                        try {
                                            if (new UpdateScoreTask(getActivity()).execute(user).get() &&
                                                    new RemoveExTask(getActivity()).execute(ex.get(groupPosition).getId()).get()) {
                                                adp.update(groupPosition);
                                                Toast.makeText(getActivity(), getString(R.string.score)+" 10.0.",
                                                        Toast.LENGTH_LONG).show();
                                                notifyUser();
                                            }

                                        } catch (Exception e) {
                                            Toast.makeText(getActivity(), getString(R.string.planerror),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(getString(R.string.completion))
                                .setPositiveButton(getString(R.string.yes), dlist)
                                .setNegativeButton(getString(R.string.no), dlist).show();

                    } else if (type.equals("Sleep") || type.equals("Dormir")) {
                        DialogInterface.OnClickListener slist = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int op) {
                                switch (op) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        user.incAmt();
                                        user.addScore(10);
                                        amt = user.getAmt();
                                        score = user.getScore();
                                        try {
                                            if (new UpdateScoreTask(getActivity()).execute(user).get() &&
                                                    new RemoveExTask(getActivity()).execute(ex.get(groupPosition).getId()).get()) {
                                                adp.update(groupPosition);
                                                Toast.makeText(getActivity(), getString(R.string.score)+" 10.0.",
                                                        Toast.LENGTH_LONG).show();
                                                notifyUser();
                                            }
                                        } catch (Exception e) {
                                            Toast.makeText(getActivity(), getString(R.string.planerror),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder sbuilder = new AlertDialog.Builder(getActivity());
                        sbuilder.setMessage(getString(R.string.completion))
                                .setPositiveButton(getString(R.string.yes), slist)
                                .setNegativeButton(getString(R.string.no), slist).show();
                    } else if (type.equals("Abdominals") || type.equals("Abdominais")) {
                        Intent ia = new Intent(getActivity(), AbActivity.class);
                        ia.putExtra("user", user);
                        ia.putExtra("ex", ex.get(groupPosition));
                        startActivity(ia);
                    } else if (type.equals("Push-ups") || type.equals("Flexões")) {
                        Intent ip = new Intent(getActivity(), PushupActivity.class);
                        ip.putExtra("user", user);
                        ip.putExtra("ex", ex.get(groupPosition));
                        startActivity(ip);
                    } else if (type.equals("Jogging") || type.equals("Corrida")) {
                        auxUser = user;
                        auxEx = ex.get(groupPosition);
                        confirmPermissions();
                    }
                    return true;
                }
            });
            exp.setAdapter(adp);
            return view;

        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.planerror2),
                    Toast.LENGTH_LONG).show();
            ExerciseAdapter empty = new ExerciseAdapter(getActivity(), new ArrayList<Exercises>());

            exp.setAdapter(empty);
            return view;
        }
    }

    private class GetExTask extends AsyncTask<Void, Void, ArrayList<Exercises>> {

        private Context ctx;

        public GetExTask (Context context){
            ctx = context;
        }

        @Override
        protected ArrayList<Exercises> doInBackground(Void... voids) {
            DBHandler db = new DBHandler(ctx);
            return db.getExercises(Globals.getUsername());
        }
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
        if (amt%10==0) {
            buildNotification(getActivity().getString(R.string.everyten1)+" "
            +amt+getActivity().getString(R.string.everyten2));
        }
        if (score/(float)amt>=9.5) {
            buildNotification(getActivity().getString(R.string.threshold1)+" "
            +String.format(Locale.US, "%.1f",score/(float)amt)+" "+getActivity().getString(R.string.threshold2));
        }
    }

    private void buildNotification(String msg) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.drawable.notif)
                        .setContentTitle("Score update!")
                        .setContentText(msg);
        Intent resultIntent = new Intent(getActivity(), LoginActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        stackBuilder.addParentStack(LoginActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    private void confirmPermissions() {

        if ( ContextCompat.checkSelfPermission( getActivity(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission( getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ){
            requestPermissions
                    (new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                    android.Manifest.permission.ACCESS_FINE_LOCATION  }, 123);
        } else {
            Intent ij = new Intent(getActivity(), MapActivity.class);
            ij.putExtra("user", auxUser);
            ij.putExtra("ex", auxEx);
            startActivity(ij);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 123: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent ij = new Intent(getActivity(), MapActivity.class);
                    ij.putExtra("user", auxUser);
                    ij.putExtra("ex", auxEx);
                    startActivity(ij);

                } else {
                    Toast.makeText(getActivity(), getString(R.string.noperms),
                            Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}
