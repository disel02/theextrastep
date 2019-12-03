package com.s1h.in.theextrastep;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String MyPREFERENCES = "MyPrefs" ;
    String phoneNumber="8806554656";
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0,0);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //==========================================================================================

        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String std = prefs.getString("stdkey", "0");
        String username = prefs.getString("userkey", "name");

        View header = navigationView.getHeaderView(0);
        TextView tvstd=(TextView)header.findViewById(R.id.tvstd);
        TextView tvname=(TextView)header.findViewById(R.id.tvname);
        tvstd.setText(std);
        tvname.setText(username);

        CardView btnnotes=(CardView) findViewById(R.id.btnnotes);
        CardView btnvideos=(CardView)findViewById(R.id.btnvideos);
        CardView btnchat=(CardView)findViewById(R.id.btnchat);

        btnnotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                Intent intent = new Intent(MenuActivity.this, Notes_list1.class);
                startActivity(intent);
            }
        });

        btnvideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                Intent intent = new Intent(MenuActivity.this, Video_list.class);
                startActivity(intent);
            }
        });

        btnchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                Intent intent = new Intent(MenuActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });
        //==========================================================================================
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (exit) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Press Back again to Exit.",Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);
            }
        }
  //      super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("loginkey", "0");
            editor.commit();
            Intent intent=new Intent(MenuActivity.this,LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_downloads) {
            Intent intent=new Intent(MenuActivity.this,DownloadsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_chpassword) {
            Intent intent=new Intent(MenuActivity.this,ChpasswordActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_details) {
            Intent intent=new Intent(MenuActivity.this,DetailsActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_call) {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
