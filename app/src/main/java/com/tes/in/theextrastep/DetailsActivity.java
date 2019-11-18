package com.tes.in.theextrastep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailsActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    String email,phone,spemail,spphone,flagcheck;
   // String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        final EditText etemail = (EditText) findViewById(R.id.etemail);
        final EditText etphone = (EditText) findViewById(R.id.etphone);
        final Button btnconfirm= (Button)findViewById(R.id.btnconfirm);

        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        spemail = prefs.getString("email", "0");
        spphone = prefs.getString("phone", "0");
        flagcheck = prefs.getString("flagcheck", "0");

        if (!spemail.equals("0") || !spphone.equals("0"))
        {
            etemail.setText(spemail);
            etphone.setText(spphone);
        }

        final ImageView ivbackpress = (ImageView)findViewById(R.id.ivbackpress);

        ivbackpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flagcheck.equals("1")) {
                    SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = prefs.edit();
                    editor2.putString("flagcheck", "0");
                    editor2.commit();
                    Intent intent = new Intent(DetailsActivity.this, Payment.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(DetailsActivity.this, MenuActivity.class);
                    startActivity(intent);
                }
            }
        });

        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 email = etemail.getText().toString();
                 phone = etphone.getText().toString();

                if (email.equals("") || phone.equals("") )
                {
                    Toast.makeText(DetailsActivity.this, "fill both fields !", Toast.LENGTH_SHORT).show();
                }else {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && android.util.Patterns.PHONE.matcher(phone).matches() && phone.length() == 10 )
                    {
                    SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("email", email);
                    editor.putString("phone", phone);
                    editor.commit();
                    Toast.makeText(DetailsActivity.this, "..Saved", Toast.LENGTH_SHORT).show();
                    if (flagcheck.equals("1")) {
                        SharedPreferences.Editor editor2 = prefs.edit();
                        editor2.putString("flagcheck", "0");
                        editor2.commit();
                        Intent intent = new Intent(DetailsActivity.this, Payment.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(DetailsActivity.this, DetailsActivity.class);
                        startActivity(intent);
                    }
                    }
                    else
                        Toast.makeText(DetailsActivity.this, "invalid email or phone number !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (flagcheck.equals("1")) {
            SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor2 = prefs.edit();
            editor2.putString("flagcheck", "0");
            editor2.commit();
            Intent intent = new Intent(DetailsActivity.this, Payment.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(DetailsActivity.this, MenuActivity.class);
            startActivity(intent);
        }
    }

}
