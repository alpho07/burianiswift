package com.buriani.poxy.burianiswift;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.buriani.poxy.burianiswift.R;

import java.util.List;

/**
 * Created by Poxy on 12/22/2016.
 */
public class ObituaryAdapter extends RecyclerView.Adapter<ObituaryAdapter.ViewHolder> implements  FragmentClass {
    Context context;
    List<MyData> myData;
    FragmentManager fragmentActivity;


    public ObituaryAdapter(Context context, List<MyData> myData) {
        this.context = context;
        this.myData = myData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent,false);
        return new ViewHolder(view,context,myData);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      //  holder.description.setText(myData.get(position).getDescription());
        holder.title.setText(myData.get(position).getObtitle());
        holder.name.setText(myData.get(position).getTitle());
        holder.dobd.setText(myData.get(position).getDob()+ " : " +myData.get(position).getDod());
        Glide.with(context).load(myData.get(position).getImage_path())
                .crossFade().centerCrop().into(holder.image_path);
    }

    @Override

    public int getItemCount() {
        return myData.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView  title, name, dobd;
        ImageView image_path;
        List<MyData> data;
        Context context;

        public ViewHolder(View view, Context context, List<MyData> data){
            super(view);
            this.data = data;
            this.context = context;
            title = (TextView) view.findViewById(R.id.obtitle);
            dobd = (TextView) view.findViewById(R.id.dobd);
            image_path = (ImageView) view.findViewById(R.id.image_view);
            name = (TextView) view.findViewById(R.id.nametag);
            view.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            MyData holder = this.data.get(position);
            Intent intent = new Intent(context,ObituaryMainDetail.class);
            Log.i("Image_Path", holder.getImage_path());
            intent.putExtra("image",holder.getImage_path()) ;
            intent.putExtra("obid",String.valueOf(holder.getId()));
            intent.putExtra("obtitle",holder.getObtitle());
            intent.putExtra("description",holder.getDescription());
            intent.putExtra("title",holder.getTitle());
            intent.putExtra("dod",holder.getDod());
            intent.putExtra("dob",holder.getDob());
            intent.putExtra("region",holder.getRegion());
            intent.putExtra("dposted",holder.getDate_posted());
            this.context.startActivity(intent);
        }
    }


}

