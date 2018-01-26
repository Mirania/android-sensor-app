package eu.qhealth;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class ApprovalFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_approval, container,
                false);

        ExpandableListView exp = view.findViewById(R.id.approvallistview);

        try {
            final ArrayList<User> us = new GetFreeClientsTask(getActivity()).execute().get();
            final ExpandableAdapter adp = new ExpandableAdapter(getActivity(), us);

            exp.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            final int groupPosition, int childPosition, long id) {

                    DialogInterface.OnClickListener dlist = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int op) {
                            switch (op){
                                case DialogInterface.BUTTON_POSITIVE:
                                    try {
                                        if (new ApproveClientTask(getActivity()).execute(us.get(groupPosition)).get())
                                            adp.update(groupPosition);
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), getString(R.string.aperror),
                                                Toast.LENGTH_LONG).show();
                                    }
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    if (Locale.getDefault().getLanguage().equals("en")) {
                        builder.setMessage(getString(R.string.become) + " " + us.get(groupPosition).getName() + getString(R.string.strainer))
                                .setPositiveButton(getString(R.string.yes), dlist)
                                .setNegativeButton(getString(R.string.no), dlist).show();
                    } else {
                        builder.setMessage(getString(R.string.become) + " " + us.get(groupPosition).getName() + " " + getString(R.string.strainer))
                                .setPositiveButton(getString(R.string.yes), dlist)
                                .setNegativeButton(getString(R.string.no), dlist).show();
                    }

                    return false;
                }
            });

            exp.setAdapter(adp);
            return view;

        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.aperror2),
                    Toast.LENGTH_LONG).show();
            ExpandableAdapter empty = new ExpandableAdapter(getActivity(), new ArrayList<User>());
            exp.setAdapter(empty);
            return view;
        }
    }

    private class GetFreeClientsTask extends AsyncTask<Void, Void, ArrayList<User>> {

        private Context ctx;

        public GetFreeClientsTask (Context context){
            ctx = context;
        }

        @Override
        protected ArrayList<User> doInBackground(Void... voids) {
            DBHandler db = new DBHandler(ctx);
            return db.getFreeClients();
        }
    }

    private class ApproveClientTask extends AsyncTask<User, Void, Boolean> {

        private Context ctx;

        public ApproveClientTask (Context context){
            ctx = context;
        }

        @Override
        protected Boolean doInBackground(User... users) {
            DBHandler db = new DBHandler(ctx);
            return db.approveUser(users[0]);
        }
    }
}