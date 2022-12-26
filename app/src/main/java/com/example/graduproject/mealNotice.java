package com.example.graduproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

public class mealNotice extends AppCompatActivity {
    ImageView mealNotice;


    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if(keycode == android.view.KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(mealNotice.this, meal.class));
            finish();
            return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_notice);

        mealNotice = findViewById(R.id.mealNotice);
        //***************이미지 바꾸기*********************
        mealNotice.setBackgroundResource(R.drawable.boongzza_notice);


    }
}