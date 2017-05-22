package com.buriani.poxy.burianiswift;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.buriani.poxy.burianiswift.R;

public class ObituaryMainDetail extends AppCompatActivity {
    ImageView imageView;
    TextView title,name,description,dob, dod;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obituary_main_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        imageView = (ImageView) findViewById(R.id.obituary_single_image);
        title = (TextView) findViewById(R.id.ob_title);
        name = (TextView) findViewById(R.id.name);
        dob = (TextView) findViewById(R.id.dob);
        dod = (TextView) findViewById(R.id.dod);
        description = (TextView) findViewById(R.id.description);
        String image = getIntent().getExtras().getString("image");
        Glide.with(this)
                .load(image)
                .crossFade()
                .centerCrop()
                .override(300,300)
                .fitCenter().
                into(imageView);

        title.setText(getIntent().getStringExtra("obtitle"));
        dob.setText(getIntent().getStringExtra("dob") );
        dod.setText(getIntent().getStringExtra("dod") );
        description.setText(getIntent().getStringExtra("description") );
        name.setText(getIntent().getStringExtra("title"));

          setTitle(getIntent().getStringExtra("obtitle"));

      //  Toast.makeText(this,getIntent().getStringExtra("obid"),Toast.LENGTH_SHORT).show();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.sign);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Guestbook.class);
                intent.putExtra("deceased",getIntent().getStringExtra("title"));
                intent.putExtra("Obid", getIntent().getStringExtra("obid"));
                startActivity(intent);
            }
        });
    }

    public void setTitle(String title){
        getSupportActionBar().setTitle(title);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
