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
        inputNickname.setText(originName);

        nameComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickName = inputNickname.getText().toString();
                userProfile user = new userProfile();
                user.setNickName(nickName);
                mDatabaseRef.child("userInfo").child(mUser.getUid()).child("nickName").setValue(nickName);
                startActivity(new Intent(nickNameChange.this,myChange.class));
                finish();

            }
        });
    }
}