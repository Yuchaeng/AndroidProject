package com.example.graduproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

public class boongzza_notice extends AppCompatActivity {
    ImageView boongNotice;

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if(keycode == android.view.KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(boongzza_notice.this, boongzza.class));
            finish();
            return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boongzza_notice);

        boongNotice = findViewById(R.id.boongNotice);

        boongNotice.setBackgroundResource(R.drawable.boongzza_notice);


    }
}