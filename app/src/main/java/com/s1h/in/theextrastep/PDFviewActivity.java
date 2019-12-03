package com.s1h.in.theextrastep;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PDFviewActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    String topic,std,subject;
    String DownloadPath="Android/data/com.tesandroid.mail";
    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);

        pdfView=(PDFView)findViewById(R.id.pdfView);

        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        std= prefs.getString("stdkey", "0");

        Intent intent = getIntent();
        topic =intent.getStringExtra("topic");

     //   Toast.makeText(this, topic, Toast.LENGTH_SHORT).show();

        final File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+DownloadPath+"/" +std+"@"+topic+"");

        if(file.exists())
        {
            pdfView.fromFile(file).load();
        }
        else {
              Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
        }
    }
}
