package com.buriani.poxy.burianiswift;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.buriani.poxy.burianiswift.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Synchronize extends AppCompatActivity implements View.OnClickListener {
    Button fromPhone, fromSim;
    Button fromPhone2;
    Animation Rotate;
    String code;
    Intent in;

    public Synchronize() {
        // Required empty public constructor
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sync);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fromPhone = (Button) findViewById(R.id.syphone);
        Rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        fromPhone.startAnimation(Rotate);
        //  fromSim.setOnClickListener(this);
        fromPhone.setOnClickListener(this);
        in = getIntent();
        code = in.getStringExtra("OBBID");
        Toast.makeText(this,code,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {


        if (v == fromPhone) {
            Intent intent = new Intent(this, ContactsDictionary.class);
            intent.putExtra("OBIDD", code);
            startActivity(intent);


        }
    }
}
