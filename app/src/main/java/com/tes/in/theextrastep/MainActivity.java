package com.tes.in.theextrastep;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
//    Animation frombottom,fromtop;
    final int SPLASH_DISPLAY_LENGTH = 2400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0,0);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 19)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

/*
        ImageView ballon=(ImageView)findViewById(R.id.ballon);
        TextView appname=(TextView)findViewById(R.id.appname);
        Button btnstart=(Button)findViewById(R.id.btnstart);
        LinearLayout btnstartly=(LinearLayout)findViewById(R.id.btnstartly);

        frombottom= AnimationUtils.loadAnimation(this,R.anim.frombottom);
        fromtop= AnimationUtils.loadAnimation(this,R.anim.fromtop);

        ballon.setAnimation(fromtop);
        appname.setAnimation(fromtop);
     //   btnstart.setAnimation(frombottom);
        btnstartly.setAnimation(frombottom);
*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                String log = prefs.getString("loginkey", "0");
                String wizard = prefs.getString("wizard", "0");
                if (wizard.equals("0") && Build.VERSION.SDK_INT >= 24) {   // 0 to enable wizard
                    Intent mainIntent = new Intent(MainActivity.this, Wizard1.class);
                    MainActivity.this.startActivity(mainIntent);
                    MainActivity.this.finish();
                }
                else if (log.equals("0")) {  // Login screen
                    Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
                    MainActivity.this.startActivity(mainIntent);
                    MainActivity.this.finish();
                }
                else {
                    Intent mainIntent = new Intent(MainActivity.this, MenuActivity.class);
                    MainActivity.this.startActivity(mainIntent);
                    MainActivity.this.finish();
                }
            }
    }, SPLASH_DISPLAY_LENGTH);

    }
}
