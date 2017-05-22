package com.buriani.poxy.burianiswift;

import android.app.DatePickerDialog;
import android.app.Dialog;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.buriani.poxy.burianiswift.R;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadStatusDelegate;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewOb extends AppCompatActivity {
    EditText dob;
    EditText dod;
    EditText title;
    EditText name;
    EditText decname;
    EditText bio;
    EditText moreinfo;
    EditText cont1;
    EditText cont2;
    EditText password;
    EditText phone;
    EditText email;
    EditText place;
    EditText image;
    private static final int STORAGE_PERMISSION_CODE = 111;
    private static final int PICK_IMAGE_REQUEST = 222;
    private static final int PICK_ID_REQUEST = 333;
    Spinner category;
    Button post, imgChooser, imgidChooser;
    ImageView potrait, potrait2;
    ProgressDialog builder;
    String Result;
    int Year, Month, Day;
    static final int DIALOG_ID = 0;
    static final int DIALOG_ID2 = 1;
    private Uri filepath, filepath2;
    private Bitmap bitmap, bitmap2;
    String UserEmail;
    final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ob);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getPermissions();
        final Calendar Cal = Calendar.getInstance();
        Year = Cal.get(Calendar.YEAR);
        Month = Cal.get(Calendar.MONTH);
        Day = Cal.get(Calendar.DAY_OF_MONTH);


        title = (EditText) findViewById(R.id.obtitle);
        decname = (EditText) findViewById(R.id.obdecname);
        category = (Spinner) findViewById(R.id.obcategories);
        dob = (EditText) findViewById(R.id.dob);
        dod = (EditText) findViewById(R.id.dod);
        bio = (EditText) findViewById(R.id.obio);
        moreinfo = (EditText) findViewById(R.id.obmoreinfo);
        cont1 = (EditText) findViewById(R.id.obcont1);
        cont2 = (EditText) findViewById(R.id.obcont2);
        place = (EditText) findViewById(R.id.obplace);
        post = (Button) findViewById(R.id.btn_postob);
        imgChooser = (Button) findViewById(R.id.imageChooser);
        imgidChooser = (Button) findViewById(R.id.imageIDChooser);
        potrait2 = (ImageView) findViewById(R.id.idpotrait);
        potrait = (ImageView) findViewById(R.id.potrait);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
        dod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID2);
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveObituary();
            }
        });
        imgChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        imgidChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser2();
            }
        });


    }

    private void getPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return;
        } else {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //  Toast.makeText(this, "Permission Grandted", Toast.LENGTH_LONG).show();
            } else {
                //  Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();

            }
        }

    }

    private void showFileChooser2() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_ID_REQUEST);
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null & data.getData() != null) {
            filepath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                potrait.setImageBitmap(bitmap);
            } catch (IOException e) {

            }
        } else if (requestCode == PICK_ID_REQUEST && resultCode == RESULT_OK && data != null & data.getData() != null) {
            filepath2 = data.getData();
            try {

                bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath2);
                potrait2.setImageBitmap(bitmap2);
            } catch (IOException e) {

            }
        }
    }


    private String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document = cursor.getString(0);
        document = document.substring(document.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + "=?", new String[]{document}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    public void uploadMultipart(final Context context, final String Title, final String Decname, final String Category,
                                final String Dob, final String Dod,
                                final String Bio, final String Moreinfo, final String Cont1,
                                final String Cont2, final String Place) {

        UploadService.getTaskList();
        String path = getPath(filepath);
        String path2 = getPath(filepath2);
        preferences = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        UserEmail = preferences.getString("username", "");

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, Constant.POSTOB)
                    .addFileToUpload(path, "file")
                    .addFileToUpload(path2, "idcopy")
                    .addParameter("title", Title)
                    .addParameter("email", UserEmail)
                    .addParameter("decname", Decname)
                    .addParameter("category", Category)
                    .addParameter("dob", Dob)
                    .addParameter("dod", Dod)
                    .addParameter("bio", Bio)
                    .addParameter("moreinfo", Moreinfo)
                    .addParameter("cont1", Cont1)
                    .addParameter("cont2", Cont2)
                    .addParameter("region", Place)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                            showMessageDialog("Posting", "Plese Wait...");
                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, Exception exception) {
                            Log.e("UPLOAD_ERROR", exception.toString());
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            builder.dismiss();
                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    NewOb.this);
                            // set title
                            alertDialogBuilder.setTitle("Obituary Created Successfully");
                            alertDialogBuilder.setCancelable(true);
                            // set dialog message
                            alertDialogBuilder
                                    .setMessage("Listing posted. Moderation ongoin, you will be notified shortly once completed. If it passes you will receive synch code to send bulk SMS notification to friends and family members. www.buriani.co.ke")
                                    .setCancelable(false)
                                    .setPositiveButton("Okey", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            try {
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            // your code here
                        }
                    })
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void checkEmail(final Context context, final String Title, final String Decname, final String Category,
                           final String Dob, final String Dod,
                           final String Bio, final String Moreinfo, final String Cont1,
                           final String Cont2, final String Place) {
        //final String tEmail = email.getText().toString();
        //final String Phone = phone.getText().toString();
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);


                String Code = "Yes";
                if (Code.equals(Result)) {
                    email.setError("Error: Email in Use!");
                    Toast.makeText(getApplicationContext(), "Error: Email in Use!", Toast.LENGTH_LONG).show();

                } else {
                    checkPhoneAndPost(getApplicationContext(), Title, Decname, Category, Dob, Dod, Bio, Moreinfo, Cont1, Cont2, Place);
                }

            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected Void doInBackground(Void... params) {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(Constant.VERIFYEMAIL + UserEmail).build();
                ///  Log.i("THEURL", request.toString());
                try {
                    Response response = client.newCall(request).execute();
                    Result = response.body().string();
                    //   Log.i("TAG2", Result);


                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };
        task.execute();


    }

    public void checkPhoneAndPost(final Context context, final String Title, final String Decname, final String Category,
                                  final String Dob, final String Dod,
                                  final String Bio, final String Moreinfo, final String Cont1,
                                  final String Cont2, final String Place) {
        //final String tPhone = phone.getText().toString();
        //final String Phone = phone.getText().toString();
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                String Code = "Yes";
                if (Code.equals(Result)) {
                    phone.setError("Error: Phone Number in Use!");
                    Toast.makeText(getApplicationContext(), "Error: Phone Number in Use!", Toast.LENGTH_LONG).show();

                } else {
                    uploadMultipart(getApplicationContext(), Title, Decname, Category, Dob, Dod, Bio, Moreinfo, Cont1, Cont2, Place);
                    Toast.makeText(getApplicationContext(), "We Are so good to go", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected Void doInBackground(Void... params) {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(Constant.VERIFYPHONE).build();
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


    public void saveObituary() {
        boolean valid = true;

        String title1 = title.getText().toString();
        String deceased1 = decname.getText().toString();
        String dob1 = dob.getText().toString();
        String dod1 = dod.getText().toString();
        String bio1 = bio.getText().toString();
        String moreinfo1 = moreinfo.getText().toString();
        String cont11 = cont1.getText().toString();
        String cont21 = cont2.getText().toString();
        String place1 = place.getText().toString();
        String category1 = category.getSelectedItem().toString();


        if (title1.isEmpty() ) {
            title.setError("Cannot be blank");
            valid = false;
        } else {
            title.setError(null);
        }

        if (deceased1.isEmpty()) {
            decname.setError("Deceased name cannot be left blank");
            valid = false;
        } else {
            decname.setError(null);
        }

        if (dob1.isEmpty()) {
            dob.setError("Date of birth cannot be blank");
            valid = false;
        } else {
            dob.setError(null);
        }

        if (dod1.isEmpty()) {
            dod.setError("Date of passing on cannot be blank");
            valid = false;
        } else {
            dod.setError(null);
        }

        if (bio1.isEmpty()) {
            bio.setError("Cannot be left blank");
            valid = false;
        } else {
            bio.setError(null);
        }


        if (cont11.isEmpty()) {
            cont1.setError("Contact 1 should not be blank");
            valid = false;
        } else {
            cont1.setError(null);
        }

        if (cont21.isEmpty()) {
            cont2.setError("Contact 2 should not be blank");
            valid = false;
        } else {
            cont2.setError(null);
        }


        if (place1.isEmpty() ) {
            place.setError("Area of posting cannot be blank");
            valid = false;
        } else {
            place.setError(null);
        }


        if (valid) {
            if (potrait.getDrawable() == null) {
                Toast.makeText(getApplicationContext(), "Image Error: No image has been selected", Toast.LENGTH_LONG).show();
            } else if (potrait2.getDrawable() == null) {
                Toast.makeText(getApplicationContext(), "ID Error: No Scanned ID image has been selected", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Strarting...", Toast.LENGTH_LONG).show();
                uploadMultipart(getApplicationContext(), title1, deceased1, category1, dob1, dod1, bio1, moreinfo1, cont11, cont21, place1);
            }


        } else {
            Toast.makeText(this, "Encountered Error: Stopping...", Toast.LENGTH_LONG).show();

        }


    }

    public void showMessageDialog(String Title, String Message) {
        builder = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.setCancelable(false);
        builder.show();
    }


    public void postData(
            final String Title, final String Decname, final String Category,
            final String Password, final String Dob, final String Dod,
            final String Bio, final String Moreinfo, final String Cont1,
            final String Cont2, final String Name,
            final String Phone, final String Email, final String Place) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Log.i("START", "request Started");
                String filename = getPath(filepath).substring(getPath(filepath).lastIndexOf("/") + 1);
                OkHttpClient client = new OkHttpClient();
                //  JSONObject obj = new JSONObject();
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addPart(
                                Headers.of("Content-Disposition", "form-data; name=\"imtitle\""),
                                RequestBody.create(null, filename))
                        .addPart(
                                Headers.of("Content-Disposition", "form-data; name=\"file\""),
                                RequestBody.create(MEDIA_TYPE_PNG, new File(getPath(filepath))))
                        //.addFormDataPart("file", filename, RequestBody.create(MEDIA_TYPE_PNG, new File(getPath(filepath))))
                        .addFormDataPart("title", Title)
                        .addFormDataPart("fullname", Name)
                        .addFormDataPart("phone", Phone)
                        .addFormDataPart("email", Email)
                        .addFormDataPart("password", Password)
                        .addFormDataPart("decname", Decname)
                        .addFormDataPart("category", Category)
                        .addFormDataPart("dob", Dob)
                        .addFormDataPart("dod", Dod)
                        .addFormDataPart("bio", Bio)
                        .addFormDataPart("moreinfo", Moreinfo)
                        .addFormDataPart("cont1", Cont1)
                        .addFormDataPart("cont2", Cont2)
                        .addFormDataPart("region", Place)
                        .build();
                Request request = new Request.Builder()
                        .url(Constant.POSTOB)
                        .post(requestBody)
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
                showMessageDialog("Posting", "Plese Wait...");
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                builder.dismiss();
                super.onPostExecute(aVoid);

                Log.d("ONYINJO", Result);
                String Code = "Success";
                if (Code.equals(Result)) {

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            NewOb.this);
                    // set title
                    alertDialogBuilder.setTitle("Posting Obituary successful!");
                    alertDialogBuilder.setCancelable(true);
                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Thank you for Posting, you can now login to view your post")
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


    @Override
    public Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            DatePickerDialog dp = new DatePickerDialog(this, dPick, Year, Month, Day);
            dp.getDatePicker().setMaxDate(System.currentTimeMillis());
            return dp;
        } else if (id == DIALOG_ID2) {
            DatePickerDialog dp = new DatePickerDialog(this, dPick2, Year, Month, Day);
            dp.getDatePicker().setMaxDate(System.currentTimeMillis());
            return dp;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dPick = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            Year = year;
            Month = month + 1;
            Day = dayOfMonth;

            dob.setText(Day + "/" + Month + "/" + Year);
        }
    };

    private DatePickerDialog.OnDateSetListener dPick2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            Year = year;
            Month = month + 1;
            Day = dayOfMonth;

            dod.setText(Day + "/" + Month + "/" + Year);
        }
    };


}
