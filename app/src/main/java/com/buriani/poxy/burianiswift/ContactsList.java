package com.buriani.poxy.burianiswift;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.buriani.poxy.burianiswift.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ContactsList extends AppCompatActivity {
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
    SharedPreferences.Editor editor;
    final static int CONTACTS_PERMISSION_CODE =2;


    String url = Constant.BASE_URL+"Mobile/getResp/";
    String Method="getContacts/";


    ListView lv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getPermissionToReadUserContacts();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //====================================================================
        //ListView to Display Contact Names and on click Phone Number in Toast.
        //====================================================================
        lv1 = (ListView) findViewById(R.id.listView1);

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {


            //Read Contact Name
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            //Read Phone Number
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            if(name!=null)
            {
                namecsv += name + ",";
                phonecsv += phoneNumber + ",";
            }


        }
        phones.close();


        //==============================================
        // Convert csvstrimg into array
        //==============================================
        namearray = namecsv.split(",");
        phonearray = phonecsv.split(",");

        postData(namecsv,phonecsv);

        //postData(namearray,phonearray);

     //   Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();





        //Create Array Adapter and Pass ArrayOfValues to it.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,android.R.id.text1,namearray);

        //BindAdpater with our Actual ListView
       lv1.setAdapter(adapter);

        //Do something on click on ListView Click on Items
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {


                //============================================
                // Display number of contact on click.
                //===========================================
                String msg = phonearray[arg2];
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });



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


   public void  postData(final String Name, final String Phone){
       preferences = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
       Intent intent = getIntent();
       Obid = intent.getStringExtra("OBID");
       User = preferences.getString("username", "");
       AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>() {
           @Override
           protected Void doInBackground(Void... params) {
               Log.i("START","request Started");
               OkHttpClient client = new OkHttpClient();
               MediaType type = MediaType.parse("application/json;charset=utf-8");
               JSONObject obj = new JSONObject();
               try {

                   obj.put("name",Name);
                   obj.put("phone",Phone);
                   obj.put("client_id",User);
                   obj.put("obid",Obid);
                   Log.d("OBJS",obj.toString());
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
                   Response  response = client.newCall(request).execute();
                   Log.d("WINYO", response.body().string());
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
              executeDataSanitizer(User);
               //alert = new AlertDialog.Builder(this);


           }

           @Override
           protected void onProgressUpdate(Void... values) {
               super.onProgressUpdate(values);
           }
       };
       task.execute();


   }


    private void executeDataSanitizer(final String email) {
        AsyncTask<Integer, Void,Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(Constant.URL + Method + email).build();

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
                builder.dismiss();

                Intent intent = new Intent(getApplicationContext(), Dispatch.class);
                intent.putExtra("CONTACTS", counter);
                intent.putExtra("OID", Obid);
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

    public void showMessageDialog(String Title, String Message) {
        builder = new ProgressDialog(this);
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.setCancelable(false);
        builder.show();
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



