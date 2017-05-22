package com.buriani.poxy.burianiswift;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.buriani.poxy.burianiswift.R;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class CodeVerify extends AppCompatActivity implements View.OnClickListener {
    Button bver;
    EditText code;
    String Verify = "codeverify/";
    String Result = "";
    CodeGetter getter;
    String TexcCode = "";
    ProgressDialog builder;

    public CodeVerify() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_verify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bver = (Button) findViewById(R.id.bver);
        code = (EditText) findViewById(R.id.code);
        bver.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.bver:
                if (code.getText().length() <= 0) {
                    code.setError("Please Enter Code!");
                } else {

                    getResponse();

                }
                break;


        }
    }
    @Override
    public void onBackPressed(){

       Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        finish();
    }

    public void getResponse() {
        TexcCode = code.getText().toString();
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showMessageDialog("Veryfying...","Pease wait as we verify your code...");
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                  builder.dismiss();

                String Code = "Yes";
                if (Code.equals(Result)) {
                    Intent synchronize = new Intent(getApplicationContext(), Synchronize.class);
                    synchronize.putExtra("OBBID", TexcCode);
                    startActivity(synchronize);
                } else {
                    code.setError("Invalid or Used Code!");
                    Log.d("VCODE",Result);
                    Toast.makeText(getApplicationContext(), "Invalid or Used Code!", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);

            }

            @Override
            protected Void doInBackground(Void... params) {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(Constant.URL + Verify + TexcCode).build();
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
        builder.setCancelable(false);
        builder.show();
    }
}






