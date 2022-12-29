package com.example.graduproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

public class wantClass extends AppCompatActivity {
    ImageView classNotice;

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if(keycode == android.view.KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(wantClass.this, MainActivity.class));
            finish();
            return true;
        }

        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_want_class);

        classNotice = findViewById(R.id.classNotice);
        classNotice.setBackgroundResource(R.drawable.class_what);
    }
}