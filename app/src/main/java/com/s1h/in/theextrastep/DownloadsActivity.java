package com.s1h.in.theextrastep;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadsActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    String std,topic;
    ListView SubjectListView;
    String DownloadPath="Android/data/com.tesandroid.mail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloads);

        final ImageView ivbackpress = (ImageView)findViewById(R.id.ivbackpress);
        SubjectListView = (ListView) findViewById(R.id.listview1);

        ivbackpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DownloadsActivity.this,MenuActivity.class);
                startActivity(intent);
            }
        });

        SubjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                topic = ((TextView) view.findViewById(R.id.textView1)).getText().toString();

                SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                std= prefs.getString("stdkey", "0");

                    final File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+DownloadPath+"/" +std+"@"+topic+"");

                    if(file.exists())
                    {
                        Intent intent = new Intent(DownloadsActivity.this, PDFviewActivity.class);
                        intent.putExtra("topic", topic);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(DownloadsActivity.this, "File not found", Toast.LENGTH_SHORT).show();
                    }
            }
        });

        SubjectListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                topic = ((TextView) view.findViewById(R.id.textView1)).getText().toString();

                SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                std= prefs.getString("stdkey", "0");

                final File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+DownloadPath+"/" +std+"@"+topic+"");

                if(file.exists())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DownloadsActivity.this);
                    builder.setMessage("Are you want to delete ?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                        file.delete();
                                    Toast.makeText(DownloadsActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(DownloadsActivity.this, DownloadsActivity.class);
                                    startActivity(intent);
                                }})
                            .create()
                            .show();
                }
                else
                {
                    Toast.makeText(DownloadsActivity.this, "File not found", Toast.LENGTH_SHORT).show();
                }
                                return true;
            }
        });

        //------------------------------------------------------------------------------------------

        List<subjects> subjectsList;
        Context context = DownloadsActivity.this;

        try {
              File dir=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+DownloadPath);

                File[] filelist = dir.listFiles();

            subjects subjects;

            subjectsList = new ArrayList<subjects>();

            for (int i = 0; i < filelist.length; i++) {
                subjects = new subjects();

                String result=filelist[i].getName();
                String[] separated = result.split("@");
                subjects.SubjectName = separated[1];
                subjectsList.add(subjects);
            }

            SubjectListView.setVisibility(View.VISIBLE);

            if (subjectsList != null) {
                ListAdapterClass2 adapter = new ListAdapterClass2(subjectsList, context);
                SubjectListView.setAdapter(adapter);
            }
            else
                Toast.makeText(DownloadsActivity.this, "not yet download !", Toast.LENGTH_SHORT).show();
        } catch (Exception e1) {
            Toast.makeText(DownloadsActivity.this, "not yet download !", Toast.LENGTH_SHORT).show();
        }
        //------------------------------------------------------------------------------------------
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DownloadsActivity.this,MenuActivity.class);
        startActivity(intent);
    }
}
