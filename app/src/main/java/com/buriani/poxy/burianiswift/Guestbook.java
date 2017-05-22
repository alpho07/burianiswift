package com.buriani.poxy.burianiswift;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.buriani.poxy.burianiswift.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Guestbook extends AppCompatActivity {

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    CommentAdapter adapter;
    List<CData> dataList;
    private static final String Method = "MobileComm";
    private static final String MethodComment = "saveMobileComment";
    Integer Obid;
    TextView title, login, register, oor,topost;
    EditText msg;
    Button postCondolence;
    String Result = "";
    ProgressDialog progressDialog, builder;
    SharedPreferences preferences;
    String Username;
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guestbook);
        postCondolence = (Button) findViewById(R.id.pcond);
        Obid = Integer.parseInt(getIntent().getStringExtra("Obid"));
        title = (TextView) findViewById(R.id.dname);
        login = (TextView) findViewById(R.id.login_c);
        register = (TextView) findViewById(R.id.Register_c);
        topost = (TextView) findViewById(R.id.topost);
        oor = (TextView) findViewById(R.id.oor);
        title.setText(getIntent().getStringExtra("deceased"));
        msg = (EditText) findViewById(R.id.condmsg);
        ActionBar actionBar = getSupportActionBar();
        postCondolence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postCondolece();
            }
        });
         mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
             loadComments();
            }
        });




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(login);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupActivity = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(signupActivity);
            }
        });

        preferences = getSharedPreferences("login.conf", Context.MODE_PRIVATE);


         Username = preferences.getString("username", "");
        String Pass = preferences.getString("password", "");

        if (Username.equals("") && Pass.equals("")) {

            postCondolence.setVisibility(View.INVISIBLE);
            login.setVisibility(View.VISIBLE);
            register.setVisibility(View.VISIBLE);
            oor.setVisibility(View.VISIBLE);
            topost.setVisibility(View.VISIBLE);

        } else {
            postCondolence.setVisibility(View.VISIBLE);
            login.setVisibility(View.INVISIBLE);
            register.setVisibility(View.INVISIBLE);
            oor.setVisibility(View.INVISIBLE);
            topost.setVisibility(View.INVISIBLE);
        }
        //  actionBar.setHomeButtonEnabled(true);
        //  actionBar.setDisplayHomeAsUpEnabled(true);

        loadComments();
    }


    public void loadComments(){
        recyclerView = (RecyclerView) findViewById(R.id.comrecyclerview);
        dataList = new ArrayList<>();
        loadData(Obid);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new CommentAdapter(getApplicationContext(), dataList);
        recyclerView.setAdapter(adapter);
    }


    private void loadData(final int obid) {
        AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(Constant.Home + Method + "/" + obid).build();
                try {
                    Response response = client.newCall(request).execute();
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            CData data = new CData(
                                    obj.getInt("id"),
                                    obj.getString("body"),
                                    obj.getString("date"),
                                    obj.getString("author")
                            );

                            dataList.add(data);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
                //  dismissLoader();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //showLoader();
            }
        };
        task.execute(Obid);
    }

    public void postCondolece() {
        boolean valid = true;

        String message = msg.getText().toString();


        if (message.isEmpty()) {
            msg.setError("Condolence Message Cannot be blank");
            valid = false;
        } else {
            msg.setError(null);
        }


        if (valid) {
             postData(Obid,message,Username);
        } else {
            valid = false;
            Toast.makeText(this, "An error occured", Toast.LENGTH_LONG).show();
        }


    }

    public void showMessageDialog(String Title, String Message) {
        builder = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.setCancelable(false);
        builder.show();
    }

    public void postData(final Integer Obid, final String Consolence, final String Username) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Log.i("START", "request Started");
                OkHttpClient client = new OkHttpClient();
                MediaType type = MediaType.parse("application/json;charset=utf-8");
                JSONObject obj = new JSONObject();
                try {
                    obj.put("obid", Obid);
                    obj.put("body", Consolence);
                    obj.put("author", Username);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(type, obj.toString());
                Request request = new Request.Builder()
                        .url(Constant.COMMENT)
                        .post(body)
                        .build();
                Log.d("REQUESTER", request.toString());
                try {
                    Response response = client.newCall(request).execute();
                    Result = response.body().string();


                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                showMessageDialog("Posting Condolence Message", "Plese Wait...");
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                builder.dismiss();
                super.onPostExecute(aVoid);

                Log.d("RES", Result);
                String Code = "Success";
                if (Code.equals(Result)) {

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            Guestbook.this);
                    // set title
                    alertDialogBuilder.setTitle("Message Received!");
                    alertDialogBuilder.setCancelable(true);
                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Thank you for your Condolence message. It will go through moderation and appear shortly.")
                            .setCancelable(false)
                            .setPositiveButton("Okey", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    try {

                                        loadComments();
                                       msg.setText("");


                                        //so some work
                                    } catch (Exception e) {
                                        //Exception
                                    }
                                }
                            });


                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }
        };
        task.execute();


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
