package com.example.graduproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

public class freeNotice extends AppCompatActivity {
    ImageView freeNotice;

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if(keycode == android.view.KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(freeNotice.this, free.class));
            finish();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_notice);

        //*************이미지 바꾸기 ************
        freeNotice = findViewById(R.id.freeNotice);
        freeNotice.setBackgroundResource(R.drawable.boongzza_notice);
    }
}