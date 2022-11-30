package com.example.graduproject;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.List;

public class myChange extends AppCompatActivity {

    TextView picChange, whatName, whatBirth, whatGender, deptId, nickName, whenEmpty,
    whatInterest, writeIntroduce;
    Button signoutBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mychage);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mUser = mAuth.getCurrentUser();

        whatName = findViewById(R.id.whatName);
        whatBirth = findViewById(R.id.whatBrith);
        whatGender = findViewById(R.id.whatGender);
        deptId = findViewById(R.id.deptId);
        nickName = findViewById(R.id.nickName);
        whenEmpty = findViewById(R.id.whenEmpty);
        whatInterest = findViewById(R.id.whatInterest);
        writeIntroduce = findViewById(R.id.writeIntroduce);

        signoutBtn = findViewById(R.id.signoutBtn);
        picChange = findViewById(R.id.picChange);
        //프로필 사진 변경
        picChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(myChange.this,login.class));
            }
        });

        mDatabaseRef.child("userInfo").child(mUser.getUid()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userProfile profile = snapshot.getValue(userProfile.class);
                        String name = profile.getName();
                        String birth = profile.getBirth();
                        String gender = profile.getGender();
                        String dept = profile.getDept();
                        String studentId = profile.getStudentId();
                        String whatnickName = profile.getNickName();
                        String emptyString = profile.getEmptyTime();
                        String interestString = profile.getInterest();
                        String introduce = profile.getIntroduce();

                        whatName.setText(name);
                        whatBirth.setText(birth);
                        whatGender.setText(gender);
                        deptId.setText(dept + "/" + studentId);
                        nickName.setText(whatnickName);
                        whenEmpty.setText(emptyString);
                        whatInterest.setText(interestString);
                        writeIntroduce.setText(introduce);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w(TAG, "loadPost:onCancelled", error.toException());
                    }
                }
        );


    } //oncreate 끝






}