package com.buriani.poxy.burianiswift;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.buriani.poxy.burianiswift.R;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ContactsAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    CheckBox AllChecker;
    private List<ContactObject> mainDataList = null;
    private ArrayList<ContactObject> arraylist;
    public ContactsAdapter(Context context, List<ContactObject> mainDataList) {

        mContext = context;
        this.mainDataList = mainDataList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<ContactObject>();
        this.arraylist.addAll(mainDataList);
        //this.AllChecker = allChecker;


    }
    static class ViewHolder {
        protected TextView name;
        protected TextView number;
        protected CheckBox check;
        protected Button all;
        protected ImageView image;
    }
    @Override
    public int getCount() {
        return mainDataList.size();
    }
    @Override
    public ContactObject getItem(int position) {
        return mainDataList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_row, null);
            holder.name = (TextView) view.findViewById(R.id.contactname);
            holder.number = (TextView) view.findViewById(R.id.contactno);
            holder.check = (CheckBox) view.findViewById(R.id.contactcheck);
            holder.image = (ImageView) view.findViewById(R.id.contactimage);
            holder.all = (Button)view.findViewById(R.id.selectall);
            view.setTag(holder);
            view.setTag(R.id.contactname, holder.name);
            view.setTag(R.id.contactno, holder.number);
            view.setTag(R.id.contactcheck, holder.check);
          //  view.setTag(R.id.selectAll,holder.all);
            holder.check
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton vw,
                                                     boolean isChecked) {
                            int getPosition = (Integer) vw.getTag();
                            mainDataList.get(getPosition).setSelected(
                                    vw.isChecked());
                        }
                    });





        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.check.setTag(position);

        holder.name.setText(mainDataList.get(position).getName());
        holder.number.setText(mainDataList.get(position).getNumber());

        if(getByteContactPhoto(mainDataList.get(position).getImage())==null){
            holder.image.setImageResource(R.mipmap.ic_phone);
        }else{
            holder.image.setImageBitmap(getByteContactPhoto(mainDataList.get(position).getImage()));
        }
       // holder.check.setChecked(mainDataList.get(position).isSelected());
        if(mainDataList.get(position).isSelected()){
            mainDataList.get(position).setSelected(false);
        }else {
            mainDataList.get(position).setSelected(true);
        }
        return view;
    }



    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mainDataList.clear();
        if (charText.length() == 0) {
            mainDataList.addAll(arraylist);
        } else {
            for (ContactObject wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    mainDataList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
    public Bitmap getByteContactPhoto(String contactId) {
        Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, Long.parseLong(contactId));
        Uri photoUri = Uri.withAppendedPath(contactUri, Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = mContext.getContentResolver().query(photoUri,
                new String[] {Contacts.Photo.DATA15}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return BitmapFactory.decodeStream( new ByteArrayInputStream(data));
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }

}