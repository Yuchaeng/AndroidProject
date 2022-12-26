package com.example.graduproject;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class interest extends AppCompatActivity implements View.OnClickListener {
    Button[] itemButton = new Button[64];
    TextView interestComplete;
    int getId;
    String selectResult;
    ArrayList<String> selectArray;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser mUser;

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if(keycode ==KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(interest.this, myChange.class));
            finish();
            return true;
        }

        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        interestComplete = findViewById(R.id.interestComplete);
        selectArray = new ArrayList<String>();

        //버튼 아이디 연결
        for(int i=0;i<itemButton.length;i++) {
            getId = getResources().getIdentifier("inter_"+i,"id","com.example.graduproject");
            itemButton[i]= (Button) findViewById(getId);
            itemButton[i].setOnClickListener(this);
        }

        interestComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectResult = String.join(",",selectArray);
                userProfile user = new userProfile();
                user.setInterest(selectResult);
                mDatabaseRef.child("userInfo").child(mUser.getUid()).child("interest").setValue(selectResult);

                startActivity(new Intent(interest.this,myChange.class));
                finish();

            }
        });



    }

    //뒤로가기하면 프로필 편집 화면 나오도록 추가해야함

    @Override
    public void onClick(View view) {
        Button newButton = (Button) view;
        view.setSelected(!view.isSelected());
        String buttonText;

        for(Button tempButton : itemButton) {
            if(tempButton == newButton && tempButton.isSelected()) {
                buttonText = tempButton.getText().toString();
                selectArray.add(buttonText);

            }
            if(tempButton == newButton && !tempButton.isSelected()) {
                buttonText = tempButton.getText().toString();
                selectArray.remove(buttonText);
            }

           // Log.d(TAG,"출력"+selectArray);
        }
    }




}