package com.example.graduproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

public class word extends AppCompatActivity {
    ImageView wordNotice;

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if(keycode == android.view.KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(word.this, MainActivity.class));
            finish();
            return true;
        }

        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        wordNotice = findViewById(R.id.wordNotice);
        wordNotice.setBackgroundResource(R.drawable.word_what);
    }
}