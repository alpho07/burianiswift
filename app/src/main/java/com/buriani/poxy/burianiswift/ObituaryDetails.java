package com.buriani.poxy.burianiswift;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.buriani.poxy.burianiswift.R;

public class ObituaryDetails extends AppCompatActivity {

    ImageView imageView;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.obituary_details);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        imageView = (ImageView) findViewById(R.id.obituary_single_image);
        title = (TextView) findViewById(R.id.ob_title);
        String image = getIntent().getExtras().getString("image");
        Glide.with(this)
                .load(image)
                .crossFade()
                .centerCrop()
                .override(300,300)
                .fitCenter().
                into(imageView);

        title.setText(getIntent().getStringExtra("title"));
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
