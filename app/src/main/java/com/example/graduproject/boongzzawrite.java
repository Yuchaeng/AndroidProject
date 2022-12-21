package com.example.graduproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class boongzzawrite extends AppCompatActivity {

    private TextView dateText;
    private DatePickerDialog datePickerDialog;
    private RadioButton selectDate,noSelect;
    private Spinner timeSpinner, peopleSpinner, mfSpinner;
    private String[] timeItem = {"오전","오후"};
    private String[] people = {"2명","3명","4명","5명","6명이상","무관"};
    private String[] genderItem = {"무관","여성만","남성만"};

    Button register;
    EditText inputTime, inputTitle, inputContent;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseRef;

    String boardName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boongzzawrite);

        dateText = findViewById(R.id.dateText);
        selectDate = findViewById(R.id.selectDate);
        noSelect = findViewById(R.id.noSelect);
        timeSpinner = findViewById(R.id.timeSpinner);
        peopleSpinner = findViewById(R.id.countSpinner);
        mfSpinner = findViewById(R.id.mfSpinner);
        register = findViewById(R.id.register);
        inputTime = findViewById(R.id.inputTime);
        inputTitle = findViewById(R.id.inputTitle);
        inputContent =findViewById(R.id.inputContent);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        Intent secondIntent = getIntent();
        switch (secondIntent.getStringExtra("boardName")) {
            case "붕짜게시판" :
                boardName = "붕짜게시판";
                break;
            case "학식게시판" :
                boardName = "학식게시판";
                break;
            case "운동게시판" :
                boardName = "운동게시판";
                break;
            case "스터디게시판" :
                boardName = "스터디게시판";
                break;
            case "자유게시판" :
                boardName = "자유게시판";
                break;
        }


        //날짜 선택
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateText.setVisibility(View.VISIBLE);
                Calendar calendar = Calendar.getInstance();
                int pYear = calendar.get(Calendar.YEAR);
                int pMonth = calendar.get(Calendar.MONTH);
                int pDay = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(boongzzawrite.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        dateText.setText(year+"/"+(month+1)+"/"+dayOfMonth);
                    }
                }, pYear, pMonth, pDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
            }
        });

        noSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateText.setVisibility(View.GONE);
            }
        });

        //시간 선택 스피너
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,timeItem);
        timeAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        //스피너 객체에 어댑터 넣어주기
        timeSpinner.setAdapter(timeAdapter);

        //인원수 스피너
        ArrayAdapter<String> peopleAdapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,people);
        peopleAdapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item);
        peopleSpinner.setAdapter(peopleAdapter);

        //성별 스피너
        ArrayAdapter<String> mfAdapter = new ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,genderItem);
        mfAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        mfSpinner.setAdapter(mfAdapter);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateSelect, timeSelect, peopleSelect, genderSelect, mustSelect, title, content,
                        writerUid, writerName;

                //날짜 저장
                if(selectDate.isChecked()) {
                    dateSelect = dateText.getText().toString();
                }
                else {
                    dateSelect = "날짜 무관/미정";
                }
                timeSelect = timeSpinner.getSelectedItem().toString() + inputTime.getText().toString();
                peopleSelect = peopleSpinner.getSelectedItem().toString();
                genderSelect = mfSpinner.getSelectedItem().toString();

                //필수기입항목
                mustSelect = dateSelect+", "+timeSelect+", "+peopleSelect+", "+genderSelect;
                title = inputTitle.getText().toString();
                content = inputContent.getText().toString();

                //작성자 uid
                writerUid = mUser.getUid();

                //현재 시간
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String writeTime = dateFormat.format(date);


//postModel post = new postModel을 밖에서 하고 닉네임 제외 나머지를 다 밖에서 하면 닉넴 저장이 안됨

                mDatabaseRef.child("userInfo").child(writerUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userProfile user = snapshot.getValue(userProfile.class);
                        postModel post = new postModel();
                        post.setWriterName(user.getNickName());

                        post.setPostTitle(title);
                        post.setPostContent(content);
                        post.setMustKeep(mustSelect);
                        post.setWriterUid(writerUid);
                        post.setWriteTime(writeTime);
                        post.setBoardName(boardName);

                        post.setCommentCount(0);
                        post.setRecruit(true);

                        mDatabaseRef.child("allPosts").child("boongzza").push().setValue(post);
                        mDatabaseRef.child("user-posts").child(mUser.getUid()).push().setValue(post);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Intent intent = new Intent(boongzzawrite.this, boongzza.class);
                startActivity(intent);
                finish();

            }
        });


    }



}