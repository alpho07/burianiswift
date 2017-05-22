package com.buriani.poxy.burianiswift;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.buriani.poxy.burianiswift.R;

public class MainMenu extends AppCompatActivity {
Button Synch, PostList;
    ImageButton Poff;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (!MainActivity.connection(this)) {
            Intent intent = new Intent(this, NetworkStatus.class);
            startActivity(intent);

        }

        if (getIntent().getBooleanExtra("EXIT_FLAG", false)) {
            if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) == 0) {
                finish();
            }
        }
        preferences = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        final String  User = preferences.getString("username", "");
        final String Pass = preferences.getString("password", "");

        Synch = (Button)findViewById(R.id.synchco);
        PostList = (Button)findViewById(R.id.postli);
        Poff = (ImageButton)findViewById(R.id.poff);
        Poff.setVisibility(View.GONE);

        Synch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!User.equals("") && !Pass.equals("")) {
                    Intent newObituary = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(newObituary);
                  //  CodeVerify obituaryFragment = new CodeVerify();
                    //FragmentManager fragmentManager = getSupportFragmentManager();
                  //  fragmentManager.beginTransaction().replace(R.id.relativeLayoutForFragment, obituaryFragment, obituaryFragment.getTag()).commit();
                   // setTitle("Synchronize Contacts");

                } else {

                    Intent newObituary = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(newObituary);
                }
            }
        });

        PostList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!User.equals("") && !Pass.equals("")) {
                    Intent newObituaryLog = new Intent(getApplicationContext(), NewOb.class);
                    startActivity(newObituaryLog);

                } else {

                    Intent newObituary = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(newObituary);
                }
            }
        });

        Poff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();

            }
        });


    }

    @Override
    protected void onDestroy() {
        Process.killProcess(Process.myPid());
        super.onDestroy();
    }





}
