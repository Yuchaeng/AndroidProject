package com.example.graduproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class exercise extends AppCompatActivity {
    ImageButton exerImage;
    FloatingActionButton exerWriteBtn;
    ListView exerList;
    postAdapter pa;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser mUser;
    ArrayList<String> exerKey = new ArrayList<String>();
    TextView noPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        exerImage = findViewById(R.id.exerImageView);
        exerWriteBtn = findViewById(R.id.exerWriteBtn);
        exerList = findViewById(R.id.exerList);
        noPost = findViewById(R.id.noPostExer);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        String boardName = "운동게시판";

        pa = new postAdapter();
        exerList.setAdapter(pa);
        getPost();


        exerImage.setBackgroundResource(R.drawable.exer_image);
        exerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(exercise.this,exerciseNotice.class));
                finish();
            }
        });

        //게시판 이름 같이 넘김
        exerWriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(exercise.this, exerciseWrite.class);
                intent.putExtra("boardName",boardName);
                startActivity(intent);
                finish();
            }
        });

        //알아야되는거=키 , 알 수 있는 거=몇번째 글인지 -> 게시글 뿌릴 때 리스트에 키값을 저장해뒀다가 게시글이 눌리면 눌린 인덱스에서 키값을 꺼내서 보내기,
        //게시글 인덱스랑 키 인덱스가 같아야함. 게시글이 거꾸로 저장되므로 키값도 거꾸로 넣기
        exerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent postIntent = new Intent(exercise.this, exercisePostDetail.class);
                String postKey = exerKey.get(position);
                postIntent.putExtra("key",postKey);
                startActivity(postIntent);
                finish();

            }
        });
    }

    public void getPost() {

        DatabaseReference exer = mDatabaseRef.child("allPosts").child("exercise");

        exer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                exerKey.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if(dataSnapshot != null) {
                        noPost.setVisibility(View.GONE);
                    }
                    String data = dataSnapshot.getKey();
                    exerKey.add(0,data);

                    int check = 0;
                    postModel mypostModel = dataSnapshot.getValue(postModel.class);

                    if(!mypostModel.isRecruit()) {
                        check=1;
                    }

                    //필수기입항목
                    String type = mypostModel.getType();
                    String mustData = mypostModel.getDate()+", "+mypostModel.getTime()+", "+mypostModel.getPeople()+", "+mypostModel.getGender();
                    pa.addItem(mypostModel.getPostTitle(), mypostModel.getPostContent(),
                            mustData, mypostModel.getWriterName(),mypostModel.getWriteTime(), mypostModel.getCommentCount(),check, type);


                }
                pa.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}