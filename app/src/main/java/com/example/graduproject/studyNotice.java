package com.example.graduproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

public class studyNotice extends AppCompatActivity {
    ImageView studyNotice;

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if(keycode == android.view.KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(studyNotice.this, study.class));
            finish();
            return true;
        }

        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_notice);

        //*************이미지 바꾸기 ************
        studyNotice = findViewById(R.id.studyNotice);
        studyNotice.setBackgroundResource(R.drawable.boongzza_notice);
    }
}