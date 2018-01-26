package eu.qhealth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ClientListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_clist, container,
                false);

        ExpandableListView exp = view.findViewById(R.id.clistview);

        try {
            final ArrayList<User> us = new GetClientsTask(getActivity()).execute().get();
            final ExpandableAdapter adp = new ExpandableAdapter(getActivity(), us);
            exp.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            final int groupPosition, int childPosition, long id) {
                    Intent i = new Intent(getActivity(), DetailsActivity.class);
                    i.putExtra("user", us.get(groupPosition));
                    startActivity(i);
                    return true;
                }
            });
            exp.setAdapter(adp);
            return view;

        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.clisterror),
                    Toast.LENGTH_LONG).show();
            ExpandableAdapter empty = new ExpandableAdapter(getActivity(), new ArrayList<User>());
            exp.setAdapter(empty);
            return view;
        }

    }

    private class GetClientsTask extends AsyncTask<Void, Void, ArrayList<User>> {

        private Context ctx;

        public GetClientsTask (Context context){
            ctx = context;
        }

        @Override
        protected ArrayList<User> doInBackground(Void... voids) {
            DBHandler db = new DBHandler(ctx);
            return db.getClients(Globals.getUsername());
        }
    }

}