package com.example.graduproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

public class exerciseNotice extends AppCompatActivity {
    ImageView exerNotice;


    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if(keycode == android.view.KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(exerciseNotice.this, exercise.class));
            finish();
            return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_notice);

        exerNotice = findViewById(R.id.exerNotice);
        //***************이미지 바꾸기*********************
        exerNotice.setBackgroundResource(R.drawable.boongzza_notice);

    }
}