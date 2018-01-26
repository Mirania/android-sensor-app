package eu.qhealth;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_about, container,
                false);
        ImageView eu = view.findViewById(R.id.eu);
        eu.setImageResource(R.drawable.me);
        ImageView tu = view.findViewById(R.id.tu);
        tu.setImageResource(R.drawable.notme);
        return view;
    }
}