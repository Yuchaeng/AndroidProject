package com.example.graduproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class nickNameChange extends AppCompatActivity {
    TextView nameComplete;
    TextInputEditText inputNickname;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name_change);

        nameComplete = findViewById(R.id.nameComplete);
        inputNickname = findViewById(R.id.inputNickname);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();


        Intent secondIntent = getIntent();
        String originName = secondIntent.getStringExtra("nickname");
        if(!originName.equals("닉네임을 설정해주세요.")) {
            inputNickname.setText(originName);
        }

        nameComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickName = inputNickname.getText().toString();
                String message = "곽곽"+(int)(Math.random()*1000);
                userProfile user = new userProfile();
                if(nickName.isEmpty()) {
                    user.setNickName(message);
                    mDatabaseRef.child("userInfo").child(mUser.getUid()).child("nickName").setValue(message);
                }
                else{
                    user.setNickName(nickName);
                    mDatabaseRef.child("userInfo").child(mUser.getUid()).child("nickName").setValue(nickName);
                }
                startActivity(new Intent(nickNameChange.this,myChange.class));
                finish();

            }
        });
    }
}