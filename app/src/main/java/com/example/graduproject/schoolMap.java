package com.example.graduproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

public class schoolMap extends AppCompatActivity {
    ImageView schoolNotice;

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if(keycode == android.view.KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(schoolMap.this, MainActivity.class));
            finish();
            return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_map);

        schoolNotice = findViewById(R.id.schoolNotice);
        schoolNotice.setBackgroundResource(R.drawable.school_what);
    }
}