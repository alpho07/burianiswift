package com.buriani.poxy.burianiswift;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.buriani.poxy.burianiswift.R;

import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Dispatch extends AppCompatActivity implements View.OnClickListener {
    EditText mpesa;
    String Result = "";
    String Verify = "verifyPayment/";
    String Verify1 = "sendMessages2/";
    String OBID = "";
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    double total;
    Double smscost = 5.80;
    private static final int MY_NOTIFICATION_ID = 1;
    NotificationManager notificationManager;
    Notification myNotification;
    ProgressDialog builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView contacts;
        TextView cost;
        Button finish, toolkit1;

        contacts = (TextView) findViewById(R.id.contacts);
        cost = (TextView) findViewById(R.id.TotalCost);
        mpesa = (EditText) findViewById(R.id.mpesa);
        finish = (Button) findViewById(R.id.finish);
        toolkit1 = (Button) findViewById(R.id.simtoolkit);


        contacts.setText(getIntent().getStringExtra("CONTACTS"));
        OBID = getIntent().getStringExtra("OIDD");
        Double Product = Double.parseDouble(getIntent().getStringExtra("CONTACTS"));
        total = Product * smscost;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        cost.setText("KES. " + df.format(total));

        finish.setOnClickListener(this);
        toolkit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm = getPackageManager();
                Intent appStartIntent = pm.getLaunchIntentForPackage("com.android.stk");
                startActivity(appStartIntent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        // builder.dismiss();

        Intent intent = new Intent(this, CodeVerify.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.finish:
                if (mpesa.getText().length() <= 0) {
                    mpesa.setError("Please Activation Enter Code!");
                } else {

                    getResponse();

                }

             break;

        }
    }

    public void getResponse() {
        final String TexcCode = mpesa.getText().toString();

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showMessageDialog("Payment Update..", "Please wait as we update your payment details..");
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                builder.dismiss();
                super.onPostExecute(aVoid);
                Log.d("OCHY",Result);
                String Code = "Yes";
                if (Code.equals(Result)) {

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            Dispatch.this);
                    // set title
                    alertDialogBuilder.setTitle("Update Finished");
                    alertDialogBuilder.setCancelable(true);
                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Thank you for choosing Buriani")
                            .setCancelable(true)
                            .setPositiveButton("Most Welcome", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    try {
                                        // getFinalResponse();
                                        Intent intent = new Intent(getApplicationContext(), Finish.class);
                                        startActivity(intent);
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

                } else {
                    mpesa.setError("Error Occurred. Hit finish again!");
                    Toast.makeText(getApplicationContext(), "An error occurred. Hit Finish to try again!", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected Void doInBackground(Void... params) {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(Constant.URL + Verify + OBID + "/" + TexcCode).build();
                try {
                    Response response = client.newCall(request).execute();
                    Result = response.body().string();


                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };
        task.execute();


    }


    public void getFinalResponse() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                   builder.dismiss();
                super.onPostExecute(aVoid);

                String Code = "Success";
                if (Code.equals(Result)) {
                    SharedPreferences settings = getApplicationContext().getSharedPreferences("cost.conf", Context.MODE_PRIVATE);
                    settings.edit().clear().commit();

                    Intent myIntent = new Intent(getApplicationContext(), Finish.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(
                            getApplicationContext(),
                            0,
                            myIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    myNotification = new NotificationCompat.Builder(getApplicationContext())
                            .setContentTitle("Buriani")
                            .setContentText("Condolence messages successfully sent to the retrieved family and friends.")
                            .setTicker("Buriani Notification!")
                            .setWhen(System.currentTimeMillis())
                            .setContentIntent(pendingIntent)
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setAutoCancel(true)
                            .setSmallIcon(R.mipmap.ic_buriani_cool)
                            .build();

                    notificationManager =
                            (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(MY_NOTIFICATION_ID, myNotification);


                } else {
                    Toast.makeText(getApplicationContext(), "An error occured while sending messages", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected Void doInBackground(Void... params) {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(Constant.URL + Verify1 + OBID).build();
                Log.i("THEURL", request.toString());
                try {
                    Response response = client.newCall(request).execute();
                    Result = response.body().string();
                    Log.i("TAG2", Result);


                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };
        task.execute();


    }


    public void showMessageDialog(String Title, String Message) {
        builder = new ProgressDialog(this);
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.setCancelable(true);
        builder.show();
    }

}
