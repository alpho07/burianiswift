package com.buriani.poxy.burianiswift;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.buriani.poxy.burianiswift.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.Bind;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    Constant constant = new Constant();
    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_mobile) EditText _mobileText;
   // @Bind(R.id.input_password) EditText _passwordText;
   // @Bind(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;
    String Result = "";
    ProgressDialog progressDialog,builder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }







    public void signup() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
      //  String password = _passwordText.getText().toString();
        //String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

       /* if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        })*/

        if(valid) {
            postData(name, email, mobile);
        }else{
            valid = false;
            Toast.makeText(this,"An error occured",Toast.LENGTH_LONG).show();
        }


    }

    public void showMessageDialog(String Title, String Message) {
        builder = new ProgressDialog(this,  R.style.AppTheme_Dark_Dialog);
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.setCancelable(false);
        builder.show();
    }


    public void  postData(final String Name, final String Email,final String Phone){
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
                    obj.put("email",Email);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(type,obj.toString());
                Request request = new Request.Builder()
                        .url(Constant.REGISTER)
                        .post(body)
                        .build();
                Log.d("REQUESTER", request.toString());
                try {
                    Response response = client.newCall(request).execute();
                    Result= response.body().string();


                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
               showMessageDialog("Creating Account","Plese Wait...");
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                builder.dismiss();
                super.onPostExecute(aVoid);

               Log.d("RES",Result);
                String Code = "Success";
                if (Code.equals(Result)) {

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            SignupActivity.this);
                    // set title
                    alertDialogBuilder.setTitle("Registration successful!");
                    alertDialogBuilder.setCancelable(true);
                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Thank you for registering, you can now login to start posting...")
                            .setCancelable(false)
                            .setPositiveButton("Okey", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    try {
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                        finish();

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


}