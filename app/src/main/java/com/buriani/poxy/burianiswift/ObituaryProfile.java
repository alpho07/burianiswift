package com.buriani.poxy.burianiswift;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.buriani.poxy.burianiswift.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ObituaryProfile extends Fragment {

    ImageView imageView;
    TextView title;
    public ObituaryProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_obituary_profile, container, false);
        imageView = (ImageView) view.findViewById(R.id.obituary_single_image);
        title = (TextView) view.findViewById(R.id.ob_title);
        String image = getArguments().getString("image");
        Glide.with(this)
                .load(image)
                .crossFade()
                .centerCrop()
                .override(300,300)
                .fitCenter().
                into(imageView);

        title.setText(getArguments().getString("title"));

        return view;
    }

}
