package com.example.graduproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class clubInformation extends AppCompatActivity {
    ImageView clubNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_information);

        clubNotice = findViewById(R.id.clubNotice);
        clubNotice.setBackgroundResource(R.drawable.club_what);
    }
}