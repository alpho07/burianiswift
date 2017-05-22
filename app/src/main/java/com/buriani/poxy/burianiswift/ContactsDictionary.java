package com.buriani.poxy.burianiswift;

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
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class ContactsDictionary extends AppCompatActivity {

    private ListView lv;
    private ArrayList<Model> modelArrayList;
    private CustomAdapter customAdapter;
    private Button btnselect, btndeselect, btnnext;
    private String Numbers = "";
    String namearray[];
    String phonearray[];
    String temparray[];
    ProgressDialog builder;
    AlertDialog.Builder alert;
    SharedPreferences preferences;
    String counter = "";
    String User;
    String post_res;
    String Obid;
    EditText search;
    CheckBox all;
    SharedPreferences.Editor editor;
    long total;
    Double smscost = 5.80;
    String code;
    final static int CONTACTS_PERMISSION_CODE = 2;


    String url = Constant.BASE_URL + "Mobile/getResp/";
    String Method = "getCount/";

    Intent in;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_dictionary);
        getPermissionToReadUserContacts();
        in = getIntent();
        // all = (CheckBox)findViewById(R.id.selectAll);
        code = in.getStringExtra("OBIDD");


        lv = (ListView) findViewById(R.id.lv);
        btnselect = (Button) findViewById(R.id.select);
        btndeselect = (Button) findViewById(R.id.deselect);
        btnnext = (Button) findViewById(R.id.next);
        search = (EditText) findViewById(R.id.searchContact);

        modelArrayList = getModel(false);
        customAdapter = new CustomAdapter(this, modelArrayList);
        lv.setAdapter(customAdapter);

        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelArrayList = getModel(true);
                customAdapter = new CustomAdapter(ContactsDictionary.this, modelArrayList);
                lv.setAdapter(customAdapter);
            }
        });
        btndeselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelArrayList = getModel(false);
                customAdapter = new CustomAdapter(ContactsDictionary.this, modelArrayList);
                lv.setAdapter(customAdapter);

            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<Model> list = new ArrayList<Model>();
                for (Model st : modelArrayList) {
                    if (st.getName().toLowerCase().contains(search.getText().toString().toLowerCase())) {
                        list.add(st);
                    }

                    customAdapter = new CustomAdapter(ContactsDictionary.this, list);
                    lv.setAdapter(customAdapter);
                    customAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Numbers = "";
                for (int i = 0; i < CustomAdapter.modelArrayList.size(); i++) {
                    if (CustomAdapter.modelArrayList.get(i).getSelected()) {
                        Numbers += CustomAdapter.modelArrayList.get(i).getNumbber() + ",";
                    }
                }


                if (Numbers.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Select atleast one contact to continue", Toast.LENGTH_LONG).show();

                } else {

                    String w = Numbers.substring(0, Numbers.length() - 1);
                    temparray = w.split(",");
                    String number1 = String.valueOf(temparray.length);
                    if (Integer.parseInt(number1) > 500) {
                        Toast.makeText(getApplicationContext(), "Please Select select a maximum of 500 contacts to continue. Currently selected is " + number1, Toast.LENGTH_LONG).show();
                    } else {
                        String s = Numbers.substring(0, Numbers.length() - 1);
                        phonearray = s.split(",");
                        final String Phones = s;
                        String number = String.valueOf(phonearray.length);
                        total = Math.round(Double.parseDouble(number) * smscost);
                        final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                                ContactsDictionary.this);
                        // set title
                        alertDialogBuilder.setTitle("Contact Selection");
                        alertDialogBuilder.setCancelable(true);
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("You have selected " + number + " contacts to be notified. We shall clean the contacts and show you cumulative SMS cost next. Do you want to proceed? ")
                                .setCancelable(true)
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

                                    Toast.makeText(getApplicationContext(), "Starting Synchronization...", Toast.LENGTH_LONG).show();
                                    postData(Phones);
                                    // executeDataSanitizer("alpho07@gmail.com","BUR21");
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
            }
        });


    }

    private ArrayList<Model> getModel(boolean isSelect) {
        ArrayList<Model> list = new ArrayList<>();

        try {
            Cursor phones = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);
            try {
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

                Model model = new Model();
                model.setSelected(isSelect);
                model.setName(phoneName);
                model.setNumbber(phoneNumber);
                list.add(model);

            }
            phones.close();


        } catch (Exception e) {
            e.printStackTrace();
        }


        return list;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem search = menu.findItem(R.id.searchContact);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Model> list = new ArrayList<Model>();
                for (Model st : modelArrayList) {
                    if (st.getName().toLowerCase().contains(newText.toLowerCase())) {
                        list.add(st);
                    }

                    customAdapter = new CustomAdapter(ContactsDictionary.this, list);
                    lv.setAdapter(customAdapter);
                    customAdapter.notifyDataSetChanged();
                }

                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    public void postData(final String Phone) {
        preferences = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        User = preferences.getString("username", "");
        Toast.makeText(getApplicationContext(), "Synchronization Started..", Toast.LENGTH_SHORT).show();
        showMessageDialog("Synchronizing....", "Please wait as contacts synch....");
        APIService service = ApiClient.getClient().create(APIService.class);
        //User user = new User(name, email, password);


        Call<MSG> userCall = service.userData("BURCLIENT", Phone, User, code);

        userCall.enqueue(new Callback<MSG>() {
            @Override
            public void onResponse(Call<MSG> call, retrofit2.Response<MSG> response) {
              //  Toast.makeText(getApplicationContext(), "Responded", Toast.LENGTH_SHORT).show();
                //onSignupSuccess();
              //  Log.d("onResponse", "" + response.body().getMessage());
                builder.dismiss();

                if (response.body().getSuccess() == 1) {
                    Toast.makeText(getApplicationContext(), "Synchronization completed successfully...", Toast.LENGTH_LONG).show();
                    builder.dismiss();
                    executeDataSanitizer();
                } else {
                }
            }

            @Override
            public void onFailure(Call<MSG> call, Throwable t) {
                builder.dismiss();
                Log.d("onFailure", t.toString());
            }
        });
    }


    private void executeDataSanitizer() {
        Toast.makeText(getApplicationContext(), "Contacts Prepration Started..", Toast.LENGTH_SHORT).show();
        showMessageDialog("Starting Contact preparations......", "Please wait as contacts are cleaned and prepared....");
        APIService service = ApiClient.getClient().create(APIService.class);

        Call<MSG> userCall = service.cleanData(User, code);

        userCall.enqueue(new Callback<MSG>() {
            @Override
            public void onResponse(Call<MSG> call, retrofit2.Response<MSG> response) {
               // Toast.makeText(getApplicationContext(), "Responded", Toast.LENGTH_SHORT).show();
                //Log.d("onResponse", "" + response.body().getMessage());
                builder.dismiss();

                if (response.body().getSuccess() >= 0) {
                    Intent Dispatcher = new Intent(getApplicationContext(),Dispatch.class);
                    Dispatcher.putExtra("CONTACTS",response.body().getSuccess().toString());
                    Dispatcher.putExtra("OIDD",code);
                    startActivity(Dispatcher);

                } else {
                }
            }

            @Override
            public void onFailure(Call<MSG> call, Throwable t) {
                builder.dismiss();
                Log.d("onFailure", t.toString());
            }
        });
    }





       /* AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Log.i("START", "request Started");
                OkHttpClient client = new OkHttpClient();
                MediaType type = MediaType.parse("application/json;charset=utf-8");
                JSONObject obj = new JSONObject();
                try {

                    obj.put("name", "BURCLIENT");
                    obj.put("phone", Phone);
                    obj.put("client_id", User);
                    obj.put("obid", code);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(type, obj.toString());
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    post_res=response.body().toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                showMessageDialog("Synchronizing....", "Please wait as contacts synch....");
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(post_res.equals("Complete")){
                    Log.d("TIEKO",post_res);
                    builder.dismiss();
                }else{
                    Toast.makeText(getApplicationContext(),"An Error Occured",Toast.LENGTH_LONG).show();
                }


               // executeDataSanitizer(User, code);
                //alert = new AlertDialog.Builder(this);


            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }
        };
        task.execute();*/


    /*private void executeDataSanitizer(final String email, final String obid) {
        AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(Constant.URL + Method + email + "/" + obid).build();

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
            protected void onPostExecute(Void aVoid) {
                builder.dismiss();
                Log.d("OCHYD", Constant.URL + Method + email + "/" + obid);
                Intent intent = new Intent(getApplicationContext(), Dispatch.class);
                intent.putExtra("CONTACTS", counter);
                intent.putExtra("OIDD", code);
                startActivity(intent);
            }

            @Override
            protected void onPreExecute() {
                showMessageDialog("Finalizing", "Calculating Cost...");
                super.onPreExecute();
                //showLoader();
            }
        };
        task.execute();
    }*/

    public void getPermissionToReadUserContacts() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            return;
        } else {
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
        builder.setCancelable(true);
        builder.show();
    }


}
