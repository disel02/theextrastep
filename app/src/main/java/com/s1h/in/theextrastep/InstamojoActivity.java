package com.s1h.in.theextrastep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class InstamojoActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    String full_name,email_id,contact_num,room_type,price,url;
    WebView webview;

    Boolean flag=false;

    String std,subject,topic,topic2,topic3;
    ProgressBar progressBarSubject;

    double file_size=0;
    String file_name;

    String DownloadPath="Android/data/com.tesandroid.mail";

    String ResponseString="no";

    //----------------------------------------------------------------------------------------------

    RingProgressBar mRingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instamojo);

        mRingProgressBar = (RingProgressBar) findViewById(R.id.progress_bar);
        progressBarSubject = (ProgressBar) findViewById(R.id.progressBar);
        TextView tvdone=(TextView)findViewById(R.id.tvdone);

        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        full_name= prefs.getString("userkey", "0");
        email_id= prefs.getString("email", "0");
        contact_num= prefs.getString("phone", "0");
        room_type= prefs.getString("topic", "0");
        std= prefs.getString("stdkey", "0");
        subject= prefs.getString("subject", "0");
        topic = prefs.getString("topic", "0");
        topic2=topic.trim().replace(" ","%20");
        topic3=topic.replace(" ","%20");
     //   Toast.makeText(this, topic, Toast.LENGTH_SHORT).show();

        Intent intent = getIntent();
        price =intent.getStringExtra("fee");

    //    Toast.makeText(this, full_name+email_id+topic, Toast.LENGTH_SHORT).show();

        url="http://theextrastep.in/pay/process_payment.php?full_name="+full_name+"&contact_num="+contact_num+"&email_id="+email_id+"&room_type="+room_type+"&price="+price+"&std="+std+"&sub="+subject;

        tvdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (!isNetworkAvailable()) {
                        Toast.makeText(InstamojoActivity.this, "you are offline !", Toast.LENGTH_SHORT).show();
                    } else {
                        new FetchWeatherData().execute();
                        Toast.makeText(InstamojoActivity.this, ResponseString, Toast.LENGTH_SHORT).show();
                        if (ResponseString.trim().equals("yes")) {
                            Toast.makeText(InstamojoActivity.this, "Downloading..", Toast.LENGTH_SHORT).show();
                            String url = "http://disel.site/theextrastep/notes/" + std + "@" + subject + "-" + topic3 + "";
                            new InstamojoActivity.DownloadTask().execute(url);
                        } else {
                            if (!flag) {
                                flag=true;
                                Toast.makeText(InstamojoActivity.this, "click again", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(InstamojoActivity.this, "not paid..pay first", Toast.LENGTH_SHORT).show();
                        }
                    }
            }
        });

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "you are offline !", Toast.LENGTH_SHORT).show();
        }
        else {
            progressBarSubject.setVisibility(View.GONE);
            Toast.makeText(this, "Loading....", Toast.LENGTH_SHORT).show();
            webview = (WebView) findViewById(R.id.webView);
            webview.setWebViewClient(new WebViewClient());
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setDomStorageEnabled(false);
            webview.getSettings().setAllowFileAccess(true);
            webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webview.loadUrl(url);
        }
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

                    String file_name2=file_name.replace("%20"," ");

                    input=connection.getInputStream();
                    output=new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+DownloadPath+"/"+file_name2);

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
                Toast.makeText(InstamojoActivity.this,"Please enable storage permission from settings",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(InstamojoActivity.this,"Downloaded..Tap on to open",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(InstamojoActivity.this,Notes_list2.class);
                InstamojoActivity.this.startActivity(intent);
            }
        }
    }

    public class FetchWeatherData extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try
            {
                HttpClient Client = new DefaultHttpClient();
                String URL = "http://theextrastep.in/pay/check2.php?full_name="+full_name+"&email_id="+email_id+"&topic="+topic2+"&std="+std+"&sub="+subject;
                HttpGet httpget = new HttpGet(URL);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                ResponseString = Client.execute(httpget, responseHandler);
            } catch (IOException e) {
               ResponseString="no";
            }
            return ResponseString;
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }
}
