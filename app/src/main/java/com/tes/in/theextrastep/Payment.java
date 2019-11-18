package com.tes.in.theextrastep;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class Payment extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    String std,subject,topic,fee,spemail,spphone;
    String full_name,email_id;
    ProgressBar progressBarSubject;
    double file_size=0;
    String file_name;

    String DownloadPath="Android/data/com.tesandroid.mail";

    //----------------------------------------------------------------------------------------------

    RingProgressBar mRingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        full_name= prefs.getString("userkey", "0");
        email_id= prefs.getString("email", "0");

        spemail = prefs.getString("email", "0");
        spphone = prefs.getString("phone", "0");

        mRingProgressBar = (RingProgressBar) findViewById(R.id.progress_bar);  //=================

        progressBarSubject = (ProgressBar) findViewById(R.id.progressBar);

        TextView tvsubject=(TextView)findViewById(R.id.tvsubject);
        TextView tvnote=(TextView)findViewById(R.id.tvnote);
        final TextView tvrs=(TextView)findViewById(R.id.tvrs);
        final TextView tvfee=(TextView)findViewById(R.id.tvfee);
        final Button btnconfirm= (Button)findViewById(R.id.btnconfirm);
        final ImageView ivbackpress = (ImageView)findViewById(R.id.ivbackpress);

        ivbackpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Payment.this,Notes_list2.class);
                startActivity(intent);
            }
        });

        std= prefs.getString("stdkey", "0");
        subject= prefs.getString("subject", "0");
        tvsubject.setText(subject);

        topic = prefs.getString("topic", "0");

        tvnote.setText(topic);
        String topicnew=topic+":%";

     //   Toast.makeText(this, full_name+email_id+topic, Toast.LENGTH_SHORT).show();

        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fee.equals("free")) {
                    String url = "https://diselkamble9.000webhostapp.com/students/notes/" + std + "@" + subject + "-" + topic + "";
                    new DownloadTask().execute(url);
                }
                else {

                    if (!spemail.equals("0") && !spphone.equals("0"))
                    {
                        Intent intent = new Intent(Payment.this,InstamojoActivity.class);
                        intent.putExtra("fee", fee);
                        startActivity(intent);
                    }
                    else
                    {
                        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("flagcheck", "1");
                        editor.commit();
                        Toast.makeText(Payment.this, "fill your details..!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Payment.this,DetailsActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

        //------------------------------------------------------------------------------------------
        if (isNetworkAvailable()) {

            final Response.Listener<String> responseListner = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String result=jsonResponse.getString("topic");
                        String[] separated = result.split(":");
                        fee = separated[1];
                        tvfee.setText(fee);
                        if (fee.equals("free"))
                        {
                            progressBarSubject.setVisibility(View.GONE);
                            btnconfirm.setVisibility(View.VISIBLE);
                            btnconfirm.setText("free");
                            btnconfirm.setBackgroundResource(R.drawable.buttonf);
                        }
                        else {
                            progressBarSubject.setVisibility(View.GONE);
                            tvrs.setVisibility(View.VISIBLE);
                            btnconfirm.setVisibility(View.VISIBLE);
                            btnconfirm.setText("buy");
                            btnconfirm.setBackgroundResource(R.drawable.buttonp);
                        }
                    } catch (JSONException e) {
                        progressBarSubject.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }
            };
            TopicRequest topicRequest = new TopicRequest(std,subject,topicnew, responseListner);
            RequestQueue queue = Volley.newRequestQueue(Payment.this);
            queue.add(topicRequest);

        }
        else
        {
            progressBarSubject.setVisibility(View.GONE);
            Toast.makeText(this, "you are offline !", Toast.LENGTH_SHORT).show();
        }

        //------------------------------------------------------------------------------------------
    }

    private class DownloadTask extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... strings) {
            file_name=strings[0].substring(strings[0].lastIndexOf("/")+1);

            try{
                InputStream input=null;
                OutputStream output=null;
                HttpURLConnection connection=null;
                try{
                    URL url=new URL(strings[0]);
                    connection=(HttpURLConnection)url.openConnection();
                    connection.connect();
                    if (connection.getResponseCode()!= HttpURLConnection.HTTP_OK){
                        return "Server returned HTTP "+connection.getResponseCode()+" "+connection.getResponseMessage();
                    }
                    int file_length=connection.getContentLength();
                    file_size=file_length;

                    input=connection.getInputStream();
                    output=new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+DownloadPath+"/"+file_name);

                    byte data[]=new byte[4096];
                    long total=0;
                    int count;
                    while ((count=input.read(data))!= -1){
                        if (isCancelled()){
                            return null;
                        }
                        total+=count;
                        if (file_length>0){
                            publishProgress((int)(total*100/file_length));
                        }
                        output.write(data,0,count);
                    }
                }catch (Exception e){
                    return e.toString();
                }finally {
                    try {
                        if (output!=null){
                            output.close();
                        }
                        if (input!=null){
                            input.close();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if (connection!=null){
                        connection.disconnect();
                    }
                }
            }finally {

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mRingProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mRingProgressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result!=null){
                Toast.makeText(Payment.this,"Please enable storage permission from settings",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(Payment.this,"Downloaded..Tap on to open",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Payment.this,Notes_list2.class);
                Payment.this.startActivity(intent);
            }
        }
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Payment.this,Notes_list2.class);
        startActivity(intent);
    }
}
