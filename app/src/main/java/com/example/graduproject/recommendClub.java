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


        mDatabaseRef.child("userInfo").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                userProfile userProfile = snapshot.getValue(com.example.graduproject.userProfile.class);
                if(!userProfile.getInterest().equals("") && !userProfile.getInterest().equals("관심사 및 특징을 설정해주세요.")) {
                    String interest;
                    ArrayList<String> clubs = new ArrayList<>();


                    interest = userProfile.getInterest();
                    clubInterest.setText(interest);

                    List<String> obj= Arrays.asList(interest.split(","));

                    int check=0;

                    if(obj.contains("여행")||obj.contains("친구사귀기")||obj.contains("연애")||obj.contains("술")) {
                        clubs.add("유스호스텔\n여행하는 중앙연합동아리");
                    }
                    if(obj.contains("야구")||obj.contains("운동")) {
                        clubs.add("히어로즈\n야구 중앙동아리");
                    }
                    if(obj.contains("축구")||obj.contains("운동")) {
                        clubs.add("FC seoultech,FC CTRL\n축구 중앙동아리");
                        clubs.add("FC CTRL\n축구 중앙동아리");
                    }
                    if(obj.contains("농구")||obj.contains("운동")) {
                        clubs.add("스파바\n농구 중앙동아리");
                    }
                    if(obj.contains("노래")) {
                        clubs.add("소리사랑\n보컬 중앙동아리");
                        clubs.add("통일아침\n밴드 중앙동아리");
                        clubs.add("그레이무드\n밴드 중앙동아리");
                        clubs.add("세마치\n밴드 중앙동아리");
                    }
                    if(obj.contains("춤")) {
                        clubs.add("열혈무군\n스트릿댄스 중앙동아리");
                        clubs.add("아이엠\n스트릿댄스 중앙동아리");
                    }
                    if(obj.contains("보드")) {
                        clubs.add("타보타\n보드 중앙동아리");
                    }
                    if(obj.contains("어학")) {
                        clubs.add("일본어 회화 동아리\n중앙동아리");
                    }
                    if(obj.contains("악기연주")) {
                        clubs.add("통일아침\n밴드 중앙동아리");
                        clubs.add("그레이무드\n밴드 중앙동아리");
                        clubs.add("세마치\n밴드 중앙동아리");
                        clubs.add("SNUTO\n오케스트라 중앙동아리");
                    }
                    if(obj.contains("배드민턴")) {
                        clubs.add("STAB\n배드민턴 중앙동아리");
                    }
                    if(obj.contains("볼링")||obj.contains("운동")) {
                        clubs.add("KOBO\n볼링 중앙동아리");
                    }
                    if(obj.contains("어학")) {
                        clubs.add("ECC\n영어 중앙동아리");
                    }
                    if(obj.contains("그림그리기")) {
                        clubs.add("그림랑\n그림 중앙동아리");
                    }
                    if(obj.contains("운동")) {
                        clubs.add("라이더스\n자전거 중앙동아리");
                    }


                    if(clubs.size()==0) {
                        clubName1.setText("추천 동아리를\n찾지못했어요..");
                    }
                    else{
                        Random rand = new Random();
                        int ranNum = rand.nextInt(clubs.size());
                        clubName1.setText(clubs.get(ranNum));
                    }

                }
                else{
                    clubInterest.setText("관심사가 없어요!");
                    clubName1.setText("관심사를 설정해주세요.");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
