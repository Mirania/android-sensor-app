package eu.qhealth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class DetailsFragment extends Fragment {
    View view;
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        user = (User) getActivity().getIntent().getSerializableExtra("user");

        view = inflater.inflate(R.layout.frag_details, container,
                false);
        showImage();
        TextView tvname = view.findViewById(R.id.clientname);
        TextView tvbmi = view.findViewById(R.id.bmidetails);
        TextView tvinfo = view.findViewById(R.id.clientinfo);
        tvname.setText(user.getName());
        tvbmi.append(" "+user.getBMIstring());
        tvinfo.setText(user.getInfo());

        return view;
    }

    public void showImage() {
        ImageView imageView = view.findViewById(R.id.imageView);
        Integer i = null;
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

    private class GetImageTask extends AsyncTask<Void, Void, Integer> {

        private Context ctx;

        public GetImageTask (Context context){
            ctx = context;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            DBHandler db = new DBHandler(ctx);
            return db.getImage(user.getName());
        }
    }
}