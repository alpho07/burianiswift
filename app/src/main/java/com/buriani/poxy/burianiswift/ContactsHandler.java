package com.buriani.poxy.burianiswift;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.buriani.poxy.burianiswift.R;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ContactsHandler extends Activity {
    Context context = null;
    ContactsAdapter objAdapter;
    ListView lv = null;
    EditText edtSearch = null;
    LinearLayout llContainer = null;
    Button btnOK = null;
    RelativeLayout rlPBContainer = null;
    String namecsv="";
    String phonecsv="";

    String namearray[];
    String phonearray[];
    ProgressDialog builder;
    AlertDialog.Builder  alert;
    SharedPreferences preferences;
    String counter ="";
    String User;
    String Obid;
    CheckBox all;
    SharedPreferences.Editor editor;
    long total;
    Double smscost=5.80;
    String code;
    final static int CONTACTS_PERMISSION_CODE =2;


    String url = Constant.BASE_URL+"Mobile/getResp/";
    String Method="getContacts/";

    Intent in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_contacts_handler);
        rlPBContainer = (RelativeLayout) findViewById(R.id.pbcontainer);
        edtSearch = (EditText) findViewById(R.id.input_search);
        llContainer = (LinearLayout) findViewById(R.id.data_container);
        btnOK = (Button) findViewById(R.id.ok_button);
        getPermissionToReadUserContacts();
        in = getIntent();
       // all = (CheckBox)findViewById(R.id.selectAll);
        code = in.getStringExtra("OBIDD");
        Toast.makeText(this,code,Toast.LENGTH_LONG).show();
        btnOK.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                getSelectedContacts();
            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                // When user changed the Text
                String text = edtSearch.getText().toString()
                        .toLowerCase(Locale.getDefault());
                objAdapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        addContactsInList();
    }

    private void getSelectedContacts() {
        // TODO Auto-generated method stub
        StringBuffer sb = new StringBuffer();
        for (ContactObject bean : ContactsListClass.phoneList) {
            if (bean.isSelected()) {
                sb.append(bean.getNumber());
                sb.append(",");
            }
        }
        String s = sb.toString().trim();
        if (TextUtils.isEmpty(s)) {
            Toast.makeText(context, "Select atleast one Contact",
                    Toast.LENGTH_SHORT).show();
        } else {
            s = s.substring(0, s.length() - 1);
            phonearray = s.split(",");
            final String Phones = s;
            String number = String.valueOf(phonearray.length);
            total = Math.round(Double.parseDouble(number) * smscost);
            final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                    ContactsHandler.this);
            // set title
            alertDialogBuilder.setTitle("Contact Selection");
            alertDialogBuilder.setCancelable(true);
            // set dialog message
            alertDialogBuilder
                    .setMessage("You have selected "+number+" contacts to be notified. We shall clean the contacts and show you cumulative SMS cost next. Do you want to proceed? ")
                    .setCancelable(false)
                    .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {

                              //  Toast.makeText(getApplicationContext(),"Back Selected",Toast.LENGTH_LONG).show();
                                //so some work
                            } catch (Exception e) {
                                //Exception
                            }
                        }
                    }).setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    try {

                        Toast.makeText(getApplicationContext(),"Starting Synchronization...",Toast.LENGTH_LONG).show();
                        postData(Phones);
                        //so some work
                    } catch (Exception e) {
                        //Exception
                    }
                }
            });


            // create alert dialog
            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }

    private void addContactsInList() {
        // TODO Auto-generated method stub

        try {
            Cursor phones = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);
            try {
                ContactsListClass.phoneList.clear();
            } catch (Exception e) {
            }
            while (phones.moveToNext()) {
                String phoneName = phones
                        .getString(phones
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones
                        .getString(phones
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String phoneImage = phones
                        .getString(phones
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));

                ContactObject cp = new ContactObject();


                cp.setName(phoneName);
                cp.setNumber(phoneNumber);
                cp.setImage(phoneImage);
                ContactsListClass.phoneList.add(cp);
            }
            phones.close();
            lv = new ListView(context);
            lv.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    llContainer.addView(lv);
                }
            });
            Collections.sort(ContactsListClass.phoneList,
                    new Comparator<ContactObject>() {
                        @Override
                        public int compare(ContactObject lhs,
                                           ContactObject rhs) {
                            return lhs.getName().compareTo(
                                    rhs.getName());
                        }
                    });
            objAdapter = new ContactsAdapter(ContactsHandler.this,
                    ContactsListClass.phoneList);
            lv.setAdapter(objAdapter);
            lv.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent,
                                        View view, int position, long id) {
                    CheckBox chk = (CheckBox) view
                            .findViewById(R.id.contactcheck);
                    ContactObject bean = ContactsListClass.phoneList
                            .get(position);
                    if (bean.isSelected()) {
                        bean.setSelected(false);
                        chk.setChecked(false);
                    } else {
                        bean.setSelected(true);
                        chk.setChecked(true);
                    }
                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public void  postData(final String Phone){
        preferences = getSharedPreferences("login.conf", Context.MODE_PRIVATE);

        User = preferences.getString("username", "");
        AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Log.i("START","request Started");
                OkHttpClient client = new OkHttpClient();
                MediaType type = MediaType.parse("application/json;charset=utf-8");
                JSONObject obj = new JSONObject();
                try {

                    obj.put("name","BURCLIENT");
                    obj.put("phone",Phone);
                    obj.put("client_id",User);
                    obj.put("obid",code);
                  //  Log.d("OBJS",obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(type,obj.toString());
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Log.d("REQUESTER", request.toString());
                try {
                    Response response = client.newCall(request).execute();
                    counter = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                showMessageDialog("Contact Synch","Please Wait Synchronizing...");
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                builder.dismiss();

                Intent intent = new Intent(getApplicationContext(), Dispatch.class);
                intent.putExtra("CONTACTS", counter);
                intent.putExtra("OIDD", code);
                startActivity(intent);
               // executeDataSanitizer(User,code);
                //alert = new AlertDialog.Builder(this);


            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }
        };
        task.execute();


    }

    private void executeDataSanitizer(final String email,final String obid) {
        AsyncTask<Integer, Void,Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(Constant.URL + Method + email+"/"+obid).build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    counter = response.body().string();

                } catch (IOException e) {
                    e.printStackTrace();
                }



                return null;

            }


            @Override
            protected void onPostExecute(Void aVoid){
                Log.d("OCHERA",counter);
                builder.dismiss();

                Intent intent = new Intent(getApplicationContext(), Dispatch.class);
                intent.putExtra("CONTACTS", counter);
                intent.putExtra("OIDD", code);
                startActivity(intent);
            }

            @Override
            protected void onPreExecute()  {
                showMessageDialog("Processing","Cleaning contacts, please wait...");
                super.onPreExecute();
                //showLoader();
            }
        };
        task.execute();
    }

    public void getPermissionToReadUserContacts() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            return;
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, CONTACTS_PERMISSION_CODE);
        }
    }

    // Callback with the request from calling requestPermissions(...)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == CONTACTS_PERMISSION_CODE) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "Read Contacts permission granted", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    public void showMessageDialog(String Title, String Message) {
        builder = new ProgressDialog(this);
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.setCancelable(false);
        builder.show();
    }


}