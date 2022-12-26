package com.example.graduproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class freeWrite extends AppCompatActivity {
    private RadioGroup radioFree;

    Button register;
    EditText inputTitle, inputContent;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseRef;

    String boardName;

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if(keycode == android.view.KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(freeWrite.this, free.class));
            finish();
            return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_write);

        radioFree = findViewById(R.id.radioFree);
        register = findViewById(R.id.registerFree);
        inputTitle = findViewById(R.id.inputTitleFree);
        inputContent = findViewById(R.id.inputContentFree);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        Intent secondIntent = getIntent();
        boardName = secondIntent.getStringExtra("boardName");

        //글 등록하기
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title, content, writerUid, typeSelect;

                //주제 저장
                int checkedRadio = radioFree.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(checkedRadio);
                typeSelect = rb.getText().toString();

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
                        post.setWriterUid(writerUid);
                        post.setWriteTime(writeTime);
                        post.setBoardName(boardName);

                        post.setType(typeSelect);

                        post.setCommentCount(0);
                        post.setRecruit(true);

                        mDatabaseRef.child("allPosts").child("free").push().setValue(post);
                        mDatabaseRef.child("user-posts").child(mUser.getUid()).push().setValue(post);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {


                    }
                });

                Toast.makeText(freeWrite.this, "게시글이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(freeWrite.this, free.class);
                startActivity(intent);
                finish();

            }
        });
    }
}