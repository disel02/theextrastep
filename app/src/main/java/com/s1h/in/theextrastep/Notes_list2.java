package com.s1h.in.theextrastep;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Notes_list2 extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    ListView SubjectListView;
    ProgressBar progressBarSubject;
    String topic,std,subject,url;

    private static final int MY_PERMISSION=1;
    ProgressDialog mPogressDialog;
    double file_size=0;
    String file_name;
    boolean flag=false;

    String DownloadPath="Android/data/com.tesandroid.mail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list2);

        final ImageView ivbackpress = (ImageView)findViewById(R.id.ivbackpress);

        ivbackpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Notes_list2.this,MenuActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        std= prefs.getString("stdkey", "0");
        subject=prefs.getString("subject", "0");
    //    Toast.makeText(Notes_list2.this,std,Toast.LENGTH_LONG).show();

     //   Intent intent = getIntent();
      //  subject =intent.getStringExtra("selected");
     //   Toast.makeText(Notes_list2.this,subject,Toast.LENGTH_LONG).show();

        //==========================================================================================

        if(Build.VERSION.SDK_INT >Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSION);
        }
        else {
            //create folder
            File dir=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+DownloadPath+"/");
            try{
                dir.mkdir();
           //     flag=true;
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(Notes_list2.this,"cannot create folder",Toast.LENGTH_SHORT).show();
            }
        }

        //==========================================================================================

        SubjectListView = (ListView) findViewById(R.id.listview1);

        progressBarSubject = (ProgressBar) findViewById(R.id.progressBar);

        SubjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                topic = ((TextView) view.findViewById(R.id.textView1)).getText().toString();
            //    Toast.makeText(Notes_list2.this,topic,Toast.LENGTH_LONG).show();
                
                url="http://disel.site/theextrastep/notes/"+std+"@"+subject+"-"+topic+"";

                //======================Download====================================================
                if(!flag){  // flag not working
                    final File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+DownloadPath+"/" +std+"@"+subject+"-"+topic+"");

                    if(file.exists())
                    {
                        String topicnew =subject+"-"+topic;
                        Intent intent = new Intent(Notes_list2.this, PDFviewActivity.class);
                        intent.putExtra("topic", topicnew);
                        startActivity(intent);
                    }
                    else
                    {
                        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("topic", topic);
                        editor.commit();
                        Intent intent = new Intent(Notes_list2.this, Payment.class);
                        startActivity(intent);
                      //  new DownloadTask().execute(url);
                    }
                }
                else
                    Toast.makeText(Notes_list2.this,"something wrong",Toast.LENGTH_SHORT).show();
                //======================end=========================================================
            }
        });

        //==========================================================================================

        if (isNetworkAvailable()) {
            final Response.Listener<String> responseListner = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONArray jsonArray = null;
                    List<subjects> subjectsList;
                    Context context = Notes_list2.this;
                    try {
                        jsonArray = new JSONArray(response);
                        JSONObject jsonObject;

                        subjects subjects;

                        subjectsList = new ArrayList<subjects>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            subjects = new subjects();

                            jsonObject = jsonArray.getJSONObject(i);

                            String result = jsonObject.getString(subject);
                            String[] separated = result.split(":");
                            subjects.SubjectName = separated[0];
                            subjectsList.add(subjects);
                        }

                        progressBarSubject.setVisibility(View.GONE);
                        SubjectListView.setVisibility(View.VISIBLE);

                        if (subjectsList != null) {
                            ListAdapterClass2 adapter = new ListAdapterClass2(subjectsList, context);

                            SubjectListView.setAdapter(adapter);
                        }
                        else
                            Toast.makeText(Notes_list2.this, "notes unavailable", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e1) {
                        progressBarSubject.setVisibility(View.GONE);
                        Toast.makeText(Notes_list2.this, "notes unavailable !", Toast.LENGTH_SHORT).show();
                        e1.printStackTrace();
                    }
                }
            };
            ListRequest2 listRequest2 = new ListRequest2(std,subject, responseListner);
            RequestQueue queue = Volley.newRequestQueue(Notes_list2.this);
            queue.add(listRequest2);
        }
        else
        {
            progressBarSubject.setVisibility(View.GONE);
            Toast.makeText(this, "you are offline !", Toast.LENGTH_SHORT).show();
        }
    }

    //==========================Download============================================================


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSION:{
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show();
                    File dir=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+DownloadPath+"/");
                    try{
                        dir.mkdir();
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(Notes_list2.this,"cannot create folder",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this,"Permission not granted",Toast.LENGTH_SHORT).show();
                }
            }
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
            mPogressDialog=new ProgressDialog(Notes_list2.this);
            mPogressDialog.setTitle("Downloading..");
            mPogressDialog.setMessage("File Size: 0 MB");
            mPogressDialog.setIndeterminate(true);
            mPogressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mPogressDialog.setCancelable(true);

            mPogressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Toast.makeText(Notes_list2.this,"Download Canceled",Toast.LENGTH_SHORT).show();
                    File dir=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+DownloadPath+"/"+file_name);
                    try{
                        dir.delete();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            mPogressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mPogressDialog.setIndeterminate(true);
            mPogressDialog.setMax(100);
            mPogressDialog.setProgress(values[0]);
            mPogressDialog.setMessage("File size: "+new DecimalFormat("##.##").format(file_size/1000000)+" MB");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mPogressDialog.dismiss();
            if (result!=null){
                Toast.makeText(Notes_list2.this,"Error : "+result,Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(Notes_list2.this,"Downloaded..Tap on to open",Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(Notes_list2.this,Notes_list1.class);
        startActivity(intent);
    }
}
