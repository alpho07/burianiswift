package com.buriani.poxy.burianiswift;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Poxy on 1/4/2017.
 */



public class Constant extends AppCompatActivity {
    ProgressDialog progressDialog;
    ProgressDialog builder;
    public  static final String BASE_URL  = "https://buriani.co.ke/";
    public  static final String URL  = BASE_URL+"Mobile/";
    public  static final String Home  = BASE_URL+"Home/";
    public  static final String NEWOB  = BASE_URL+"home/mobileobpost/";
    public  static final String REGISTER  = BASE_URL+"Register/mobile_newuser/";
    public  static final String POSTOB  = BASE_URL+"Home/uploadMobile/";
    public  static final String COMMENT  = BASE_URL+"Mobile/saveMobileComment/";
    public  static final String VERIFYEMAIL  = BASE_URL+"Mobile/verifyUser/email/";
    public  static final String VERIFYPHONE  = BASE_URL+"Mobile/verifyUser/phone/";

    public Constant(){


    }

    public void showLoader(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading. Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public void dismissLoader(){
        progressDialog.dismiss();
    }



}
