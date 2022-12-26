package com.example.graduproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

public class boongzza extends AppCompatActivity {
    ImageButton imageView;
    FloatingActionButton writeBtn;
    ListView boongzzaList;
    postAdapter pa;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser mUser;
    ArrayList<String> key = new ArrayList<String>();
    TextView noPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boongzza);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        boongzzaList = findViewById(R.id.boongzzaList);
        imageView = findViewById(R.id.imageView);
        noPost = findViewById(R.id.noPostBoong);

        imageView.setBackgroundResource(R.drawable.boongzza_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(boongzza.this,boongzza_notice.class));
                finish();
            }
        });

        String boardName = "붕짜게시판";


        writeBtn = (FloatingActionButton) findViewById(R.id.writeBtn);
        //게시판 이름 같이 넘김
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(boongzza.this, boongzzawrite.class);
                intent.putExtra("boardName",boardName);
                startActivity(intent);
                finish();
            }
        });


        pa = new postAdapter();
        boongzzaList.setAdapter(pa);
        getBoongPost();

        //알아야되는거=키 , 알 수 있는 거=몇번째 글인지 -> 게시글 뿌릴 때 리스트에 키값을 저장해뒀다가 게시글이 눌리면 눌린 인덱스에서 키값을 꺼내서 보내기,
        //게시글 인덱스랑 키 인덱스가 같아야함. 게시글이 거꾸로 저장되므로 키값도 거꾸로 넣기
        boongzzaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent postIntent = new Intent(boongzza.this, boongWriteDetail.class);
                String postKey = key.get(position);
                postIntent.putExtra("key",postKey);
                startActivity(postIntent);
                finish();


            }
        });


    }

    public void getBoongPost() {
        DatabaseReference boong = mDatabaseRef.child("allPosts").child("boongzza");

        boong.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                key.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if(dataSnapshot != null) {
                        noPost.setVisibility(View.GONE);
                    }
                    String data = dataSnapshot.getKey();
                    key.add(0,data);

                    int check = 0;
                    postModel mypostModel = dataSnapshot.getValue(postModel.class);

                    if(!mypostModel.isRecruit()) {
                        check=1;
                    }

                    String mustData = mypostModel.getDate()+", "+mypostModel.getTime()+", "+mypostModel.getPeople()+", "+mypostModel.getGender();
                    pa.addItem(mypostModel.getPostTitle(), mypostModel.getPostContent(),
                            mustData, mypostModel.getWriterName(),mypostModel.getWriteTime(), mypostModel.getCommentCount(),check, null);


                }
                pa.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}