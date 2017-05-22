package com.buriani.poxy.burianiswift;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.buriani.poxy.burianiswift.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment  implements View.OnClickListener {

    Button death, appreciation, memorial;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_home, container, false);
        death = (Button) view.findViewById(R.id.death_burial);
        appreciation = (Button) view.findViewById(R.id.appreciation);
        memorial = (Button) view.findViewById(R.id.memorials);

        death.setOnClickListener(this);
        appreciation.setOnClickListener(this);
        memorial.setOnClickListener(this);

        preferences = getActivity().getSharedPreferences("ob.conf", Context.MODE_PRIVATE);
        editor = preferences.edit();


        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.death_burial:
                Intent db = new Intent(getActivity(), MainActivity.class);
                startActivity(db);
                editor.putString("cat", "29");
                editor.putString("title", "Death/Burial Announcements");
                editor.apply();
                break;
            case R.id.appreciation:
                Intent ap = new Intent(getActivity(), MainActivity.class);
                startActivity(ap);
                editor.putString("cat", "30");
                editor.putString("title", "Appreciation Announcements");
                editor.apply();

                break;
            case R.id.memorials:
                Intent me = new Intent(getActivity(), MainActivity.class);
                startActivity(me);
                editor.putString("cat", "31");
                editor.putString("title", "Memorial Announcements");
                editor.apply();
                break;
            default:

        }

    }
}
