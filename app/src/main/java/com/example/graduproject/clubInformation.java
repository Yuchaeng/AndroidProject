package com.example.graduproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

public class clubInformation extends AppCompatActivity {
    ImageView clubNotice;

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if(keycode == android.view.KeyEvent.KEYCODE_BACK) {
           
            startActivity(new Intent(clubInformation.this, MainActivity.class));
            finish();
            return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_information);

        clubNotice = findViewById(R.id.clubNotice);
        clubNotice.setBackgroundResource(R.drawable.club_what);
    }
}