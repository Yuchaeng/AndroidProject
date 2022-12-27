package com.example.graduproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

public class recommendClub extends AppCompatActivity {
    TextView clubInterest, clubName1;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_club);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        clubInterest = findViewById(R.id.clubInterest);
        clubName1 = findViewById(R.id.clubName);

        //유저의 관심사 가져와서 화면에 표시
        //관심사 비슷한 종류끼리 묶어서 얘네가 몇 개나 있는지 계산
        //계산해서 제일 값이 높은 거에 해당하는 동아리 출력?

        mDatabaseRef.child("userInfo").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userProfile userProfile = snapshot.getValue(com.example.graduproject.userProfile.class);
                if(userProfile.getInterest() != null) {
                    String interest;
                    String[] clubs = new String[10];

                    interest = userProfile.getInterest();
                    clubInterest.setText(interest);

                    List<String> obj= Arrays.asList(interest.split(","));

                    int check=0;

                    if(obj.contains("여행")) {
                        clubs[0] = "유스호스텔\n여행하는 중앙연합동아리";
                        check++;
                    }
                    if(obj.contains("야구")) {
                        clubs[1] = "히어로즈\n야구 중앙동아리";
                        check++;
                    }
                    Random rand = new Random();

                    clubName1.setText(clubs[rand.nextInt(check)]);


                }
                else{
                    clubInterest.setText("관심사를 설정해주세요.");
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
