package com.s1h.in.theextrastep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

public class Notes_list1 extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    String list_key;
    ListView SubjectListView;
    ProgressBar progressBarSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list1);

        final ImageView ivbackpress = (ImageView)findViewById(R.id.ivbackpress);

        ivbackpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Notes_list1.this,MenuActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        list_key = prefs.getString("stdkey", "0");
     //   Toast.makeText(Notes_list1.this,list_key,Toast.LENGTH_LONG).show();

        SubjectListView = (ListView) findViewById(R.id.listview1);

        progressBarSubject = (ProgressBar) findViewById(R.id.progressBar);

        SubjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = ((TextView) view.findViewById(R.id.textView1)).getText().toString();
            //    Toast.makeText(Notes_list1.this,selected,Toast.LENGTH_LONG).show();

                SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("subject",selected);
                editor.commit();

                Intent intent = new Intent(Notes_list1.this, Notes_list2.class);
            //    intent.putExtra("selected", selected);
                startActivity(intent);
            }
        });

        if (isNetworkAvailable()) {
            final Response.Listener<String> responseListner = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONArray jsonArray = null;
                    List<subjects> subjectsList;
                    Context context = Notes_list1.this;
                    try {
                        jsonArray = new JSONArray(response);
                        JSONObject jsonObject;

                        subjects subjects;

                        subjectsList = new ArrayList<subjects>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            subjects = new subjects();

                            jsonObject = jsonArray.getJSONObject(i);

                            subjects.SubjectName = jsonObject.getString(list_key);

                            subjectsList.add(subjects);
                        }

                        progressBarSubject.setVisibility(View.GONE);
                        SubjectListView.setVisibility(View.VISIBLE);

                        if (subjectsList != null) {
                            ListAdapterClass adapter = new ListAdapterClass(subjectsList, context);

                            SubjectListView.setAdapter(adapter);
                        }
                    } catch (JSONException e1) {
                        progressBarSubject.setVisibility(View.GONE);
                        Toast.makeText(Notes_list1.this, "subjects unavailable !", Toast.LENGTH_SHORT).show();
                        e1.printStackTrace();
                    }
                }
            };
            ListRequest listRequest = new ListRequest(list_key, responseListner);
            RequestQueue queue = Volley.newRequestQueue(Notes_list1.this);
            queue.add(listRequest);
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
        Intent intent = new Intent(Notes_list1.this,MenuActivity.class);
        startActivity(intent);
    }

}
