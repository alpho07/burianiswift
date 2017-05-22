package com.buriani.poxy.burianiswift;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.buriani.poxy.burianiswift.R;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    HashMap data;
    private static final String Method = "Authenticate_user";
    ProgressDialog progressDialog;
    Button death, appreciation, memorial;
    String User, Pass;

    NavigationView navigationView;
    Menu nav_Menu;
    TextView counter, cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (!connection(this)) {
            Intent intent = new Intent(this, NetworkStatus.class);
            startActivity(intent);

        }

        preferences = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        User = preferences.getString("username", "");
        Pass = preferences.getString("password", "");


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        nav_Menu = navigationView.getMenu();

        //nav_Menu.findItem(R.id.dashboard).setVisible(false);
        nav_Menu.findItem(R.id.logout).setVisible(true);
        nav_Menu.findItem(R.id.settings).setVisible(true);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();




        navigationView.setNavigationItemSelectedListener(this);
        preferences = getSharedPreferences("ob.conf", Context.MODE_PRIVATE);
        String title = preferences.getString("title", "");

        //Intent finish = new Intent(this, Finish.class);
        // startActivity(finish);
        Intent obituaryFragment = new Intent(getApplicationContext(),CodeVerify.class);
        startActivity(obituaryFragment);
        setTitle(title);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.sync) {
            preferences = getSharedPreferences("cost.conf", Context.MODE_PRIVATE);
            String OBID = preferences.getString("OBID", "");
            String COUNTER = preferences.getString("COUNTER", "");
            String COST = preferences.getString("COST", "");
            // if (!OBID.equals("") && !COUNTER.equals("")) {
            //    Intent intent = new Intent(this, Dispatch.class);
            // //   startActivity(intent);

            //} else {

            Intent codeVerify = new Intent(getApplicationContext(),CodeVerify.class);
            startActivity(codeVerify);
            setTitle("Contacts Synchronization");
            // }


        } else if (id == R.id.login) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);

            //} else if (id == R.id.categories) {
            //   setTitle("Categories");

            // } else if (id == R.id.dashboard) {
            //    setTitle("Dashboard");
        } else if (id == R.id.settings) {
            setTitle("Settings ");
        } else if (id == R.id.logout) {

            editor.remove("username").remove("password").clear().commit();
            nav_Menu.findItem(R.id.login).setVisible(false);
            //  nav_Menu.findItem(R.id.dashboard).setVisible(false);
            nav_Menu.findItem(R.id.logout).setVisible(false);
            nav_Menu.findItem(R.id.sync).setVisible(false);
            nav_Menu.findItem(R.id.settings).setVisible(false);
            TextView loggedin = (TextView) findViewById(R.id.loggedinemail);
            loggedin.setText("Get all you need");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void postLoginData(final String Username, final String Password) {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        // progressDialog.show();
        data = new HashMap();
        data.put("username", Username);
        data.put("password", Password);

        PostResponseAsyncTask asyncTask = new PostResponseAsyncTask(MainActivity.this, data, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (s.contains("Pass")) {
                    TextView loggedin = (TextView) findViewById(R.id.loggedinemail);
                    loggedin.setText(preferences.getString("username", ""));
                    //  Toast.makeText(getApplicationContext(),preferences.getString("username",""),Toast.LENGTH_LONG).show();

                } else {
                    Log.d("SUPU", s);
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }
        });
        asyncTask.execute(Constant.URL + Method);


    }


    public static boolean connection(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


}
