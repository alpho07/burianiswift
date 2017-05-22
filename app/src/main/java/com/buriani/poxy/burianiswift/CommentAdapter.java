package com.buriani.poxy.burianiswift;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buriani.poxy.burianiswift.R;

import java.util.List;

/**
 * Created by Poxy on 12/22/2016.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>  {
    Context context;
    List<CData> myData;



    public CommentAdapter(Context context, List<CData> myData) {
        this.context = context;
        this.myData = myData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comcardview,parent,false);
        return new ViewHolder(view,context,myData);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      //  holder.description.setText(myData.get(position).getDescription());
        holder.name.setText(myData.get(position).getName());
        holder.date.setText(myData.get(position).getDate());
        holder.comment.setText(myData.get(position).getComment());

    }

    @Override

    public int getItemCount() {
        return myData.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView  name, date, comment;
        List<CData> data;
        Context context;

        public ViewHolder(View view, Context context, List<CData> data){
            super(view);
            this.data = data;
            this.context = context;
            name = (TextView) view.findViewById(R.id.comname);
            date = (TextView) view.findViewById(R.id.comdate);
            comment = (TextView) view.findViewById(R.id.comcomments);
            view.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {


        }
    }


}

