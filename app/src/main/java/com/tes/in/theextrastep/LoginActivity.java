package com.tes.in.theextrastep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    ProgressBar progressBarSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBarSubject = (ProgressBar) findViewById(R.id.progressBar);
        final EditText etusername = (EditText) findViewById(R.id.etusername);
        final EditText etpassword = (EditText) findViewById(R.id.etpassword);
        final Button btnlogin= (Button)findViewById(R.id.btnlogin);

        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("wizard","1");
        editor.commit();

        //==========================================================================================
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.startAnimation(buttonClick);
                final String username = etusername.getText().toString();
                final String password = etpassword.getText().toString();

                if (username.equals("") || password.equals(""))
                {
                    Toast.makeText(LoginActivity.this, "fill both fields !", Toast.LENGTH_SHORT).show();
                }else {
                    if (isNetworkAvailable()) {
                        progressBarSubject.setVisibility(View.VISIBLE);

                        final Response.Listener<String> responseListner = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    Boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        String std = jsonResponse.getString("std");
                                        //------------------------------------------------------------------
                                        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putString("loginkey", "1");
                                        editor.putString("stdkey", std);
                                        editor.putString("userkey", username);
                                        editor.putString("passkey", password);
                                        editor.commit();
                                        //------------------------------------------------------------------
                                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                        startActivity(intent);

                                    } else {
                                        progressBarSubject.setVisibility(View.GONE);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                        builder.setMessage("Login Failed")
                                                .setNegativeButton("Retry", null)
                                                .create()
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    progressBarSubject.setVisibility(View.GONE);
                                    e.printStackTrace();
                                }
                            }
                        };
                        LoginRequest loginRequest = new LoginRequest(username, password, responseListner);
                        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                        queue.add(loginRequest);
                    }
                    else
                    {
                        Snackbar.make(view, "you are offline", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        progressBarSubject.setVisibility(View.GONE);
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
}
