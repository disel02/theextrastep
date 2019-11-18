package com.tes.in.theextrastep;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class Wizard1 extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance("TheExtraStep","This application is to Provide students All important study related documents, notes, Videos etc. for learning.",R.drawable.classroom,Color.parseColor("#41366e")));
        addSlide(AppIntroFragment.newInstance("Notes","quick learning notes",R.drawable.notepad2, Color.parseColor("#41366e")));
        addSlide(AppIntroFragment.newInstance("Videos","easily understandable videos",R.drawable.youtube2, Color.parseColor("#41366e")));
        addSlide(AppIntroFragment.newInstance("Chat","chat with teacher to solve query's",R.drawable.chat2, Color.parseColor("#41366e")));
        showStatusBar(true);
        setBarColor(Color.parseColor("#333639"));
        setSeparatorColor(Color.parseColor("#333639"));
    }

    @Override
    public void onDonePressed() {
        super.onDonePressed();
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}
