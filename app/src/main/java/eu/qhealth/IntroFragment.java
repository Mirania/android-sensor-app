package eu.qhealth;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class IntroFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_intro, container,
                false);
        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.think);
        ImageView img2 = view.findViewById(R.id.userPic);
        img2.setImageResource(R.drawable.notebook);

        if (!Globals.getIntromode()) {
            try {
                String[] s = new UpdateTask(this.getActivity()).execute().get();

                if (s==null) {
                    Toast.makeText(this.getActivity(), getString(R.string.infoerror),
                            Toast.LENGTH_LONG).show();
                    return view;
                }

                TextView nametv = view.findViewById(R.id.name);
                nametv.setText(s[0]);
                nametv.setFocusable(false); //can't update username
                nametv.setLongClickable(false);
                nametv.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }
                    public void onDestroyActionMode(ActionMode mode) {
                    }
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        return false;
                    }
                });
                TextView pwtv = view.findViewById(R.id.pw);
                pwtv.setText(s[1]);
                TextView datetv = view.findViewById(R.id.date);
                datetv.setText(s[2]);
                TextView weighttv = view.findViewById(R.id.weight);
                weighttv.setText(s[3]);
                TextView heighttv = view.findViewById(R.id.height);
                heighttv.setText(s[4]);
                Spinner bodysp = view.findViewById(R.id.body);
                if (Locale.getDefault().getLanguage().equals("pt")) {
                    if (s[5].equals("Musculado")) {
                        bodysp.setSelection(0);
                    } else {
                        bodysp.setSelection(1);
                    }
                } else {
                    if (s[5].equals("Fit")) {
                        bodysp.setSelection(0);
                    } else {
                        bodysp.setSelection(1);
                    }
                }

            } catch (Exception e) {
                Toast.makeText(this.getActivity(), getString(R.string.infoerror),
                        Toast.LENGTH_LONG).show();
            }
        }
        return view;
    }

    private class UpdateTask extends AsyncTask<Void, Void, String[]> {

        private Context ctx;

        public UpdateTask (Context context){
            ctx = context;
        }

        @Override
        protected String[] doInBackground(Void... voids) {
            DBHandler db = new DBHandler(ctx);
            return db.getUserInfo(Globals.getUsername());
        }
    }
}