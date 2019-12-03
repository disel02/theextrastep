package com.s1h.in.theextrastep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

import java.util.ArrayList;
import java.util.List;

public class Video_list extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    String std_video,video_name,list_key,url;
    ListView SubjectListView;
    ProgressBar progressBarSubject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        final ImageView ivbackpress = (ImageView)findViewById(R.id.ivbackpress);

        ivbackpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Video_list.this,MenuActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        list_key = prefs.getString("stdkey", "0");
        std_video=list_key+"videos";
        //   Toast.makeText(Notes_list1.this,list_key,Toast.LENGTH_LONG).show();

        SubjectListView = (ListView) findViewById(R.id.listview1);

        progressBarSubject = (ProgressBar) findViewById(R.id.progressBar);

        SubjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                video_name = ((TextView) view.findViewById(R.id.textView1)).getText().toString();
                Toast.makeText(Video_list.this,video_name,Toast.LENGTH_LONG).show();

                progressBarSubject.setVisibility(View.VISIBLE);

                final Response.Listener<String> responseListner = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            Boolean success = jsonResponse.getBoolean("success");
                            url=jsonResponse.getString("url");
                            if(success){
                                progressBarSubject.setVisibility(View.GONE);
                                Intent intent = new Intent(Video_list.this, VideoActivity.class);
                                intent.putExtra("url", url);
                                startActivity(intent);
                            }else{
                                progressBarSubject.setVisibility(View.GONE);
                                AlertDialog.Builder builder = new AlertDialog.Builder(Video_list.this);
                                builder.setMessage("Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            progressBarSubject.setVisibility(View.GONE);
                            Toast.makeText(Video_list.this, "something wrong", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                };
                FindVideoRequest findVideoRequest= new FindVideoRequest(std_video,video_name,responseListner);
                RequestQueue queue= Volley.newRequestQueue(Video_list.this);
                queue.add(findVideoRequest);
            }
        });

        if (isNetworkAvailable()) {
            final Response.Listener<String> responseListner = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONArray jsonArray = null;
                    List<subjects> subjectsList;
                    Context context = Video_list.this;
                    try {
                        jsonArray = new JSONArray(response);
                        JSONObject jsonObject;

                        subjects subjects;

                        subjectsList = new ArrayList<subjects>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            subjects = new subjects();

                            jsonObject = jsonArray.getJSONObject(i);

                            subjects.SubjectName = jsonObject.getString("videos");

                            subjectsList.add(subjects);
                        }

                        progressBarSubject.setVisibility(View.GONE);
                        SubjectListView.setVisibility(View.VISIBLE);

                        if (subjectsList != null) {
                            ListAdapterClass3 adapter = new ListAdapterClass3(subjectsList, context);

                            SubjectListView.setAdapter(adapter);
                        }
                    } catch (JSONException e1) {
                        progressBarSubject.setVisibility(View.GONE);
                        Toast.makeText(Video_list.this, "videos unavailable !", Toast.LENGTH_SHORT).show();
                        e1.printStackTrace();
                    }
                }
            };
            VideoRequest videoRequest = new VideoRequest(std_video, responseListner);
            RequestQueue queue = Volley.newRequestQueue(Video_list.this);
            queue.add(videoRequest);
        }
        else
        {
            progressBarSubject.setVisibility(View.GONE);
            Toast.makeText(this, "you are offline !", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(Video_list.this,MenuActivity.class);
        startActivity(intent);
    }

}
