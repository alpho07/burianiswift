package com.buriani.poxy.burianiswift;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.buriani.poxy.burianiswift.R;

public class Home extends AppCompatActivity implements View.OnClickListener {
    Button death, appreciation, memorial;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        death = (Button) findViewById(R.id.death_burial);
        appreciation = (Button) findViewById(R.id.appreciation);
        memorial = (Button) findViewById(R.id.memorials);

        death.setOnClickListener(this);
        appreciation.setOnClickListener(this);
        memorial.setOnClickListener(this);

        preferences = getSharedPreferences("ob.conf", Context.MODE_PRIVATE);
        editor = preferences.edit();



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.death_burial:
                Intent db = new Intent(this, MainActivity.class);
                startActivity(db);
                editor.putString("cat","29");
                editor.putString("title","Death/Burial Announcements");
                editor.apply();
                setTitle("Death/Burial Announcements");
                break;
            case R.id.appreciation:
                Intent ap = new Intent(this, MainActivity.class);
                startActivity(ap);
                editor.putString("cat","30");
                editor.putString("title","Appreciation Announcements");
                editor.apply();

                break;
            case R.id.memorials:
                Intent me = new Intent(this, MainActivity.class);
                startActivity(me);
                editor.putString("cat","31");
                editor.putString("title","Memorial Announcements");
                editor.apply();
                break;
            default:

        }
    }
}
