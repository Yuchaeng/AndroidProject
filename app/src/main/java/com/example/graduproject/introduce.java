package com.example.graduproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class introduce extends AppCompatActivity {
    TextView introduceComplete;
    TextInputEditText inputAboutme;
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);

        introduceComplete = findViewById(R.id.introduceComplete);
        inputAboutme = findViewById(R.id.inputAboutme);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        Intent introIntent = getIntent();
        String originText = introIntent.getStringExtra("oneline");
        inputAboutme.setText(originText);

        introduceComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String aboutMe = inputAboutme.getText().toString();
                String message = "한줄소개를 작성해주세요.";
                userProfile user = new userProfile();
                if(aboutMe.isEmpty()) {
                    user.setIntroduce(message);
                    mDatabaseRef.child("userInfo").child(mUser.getUid()).child("introduce").setValue(message);
                }
                else{
                    user.setIntroduce(aboutMe);
                    mDatabaseRef.child("userInfo").child(mUser.getUid()).child("introduce").setValue(aboutMe);
                }
                startActivity(new Intent(introduce.this,myChange.class));
                finish();
            }
        });

    }
}