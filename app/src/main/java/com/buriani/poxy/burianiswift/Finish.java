package com.buriani.poxy.burianiswift;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.buriani.poxy.burianiswift.R;

public class Finish extends AppCompatActivity implements View.OnClickListener {

    Button home, exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        home = (Button) findViewById(R.id.goHome);
        exit = (Button) findViewById(R.id.Exit);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                if (id == R.id.goHome) {
                    Intent home = new Intent(getApplicationContext(), MainMenu.class);
                    startActivity(home);
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exit();
                moveTaskToBack(false);
                finish();


            }
        });


    }
    public void exit() {
        startActivity(new Intent(this, MainMenu.class).
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK).putExtra("EXIT_FLAG", true));
    }


    @Override
    protected void onDestroy() {
        Process.killProcess(Process.myPid());
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();


    }

    @Override
    public void onBackPressed() {

        Process.killProcess(Process.myPid());
    }
}
