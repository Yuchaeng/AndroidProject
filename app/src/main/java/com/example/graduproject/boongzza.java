package com.example.graduproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class boongzza extends AppCompatActivity {
    FloatingActionButton writeBtn;
    ListView boongzzaList;
    postAdapter pa;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser mUser;
    ArrayList<String> allTitles = new ArrayList<String>();
    ArrayList<String> allContents = new ArrayList<String>();
    ArrayList<String> allTimes = new ArrayList<String>();
    //ArrayList<String> nickName = new ArrayList<String>();
    ArrayList<String> mustText = new ArrayList<String>();
    ArrayList<Integer> commentCounts = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boongzza);



        //setTheme(com.google.android.material.R.style.Theme_MaterialComponents);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        boongzzaList = findViewById(R.id.boongzzaList);

        String boardName = "붕짜게시판";


        writeBtn = (FloatingActionButton) findViewById(R.id.writeBtn);
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

        boongzzaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(boongzza.this,position+"클릭",Toast.LENGTH_SHORT).show();
                mDatabaseRef.child("allPosts").child("boongzza").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String key = snapshot.getKey();
                            Log.d("tag",key);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });



    }

    public void getBoongPost() {
        DatabaseReference boong = mDatabaseRef.child("allPosts").child("boongzza");
        boong.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if(dataSnapshot == null) {
                        TextView textView = new TextView(getApplicationContext());
                        textView.setText("아직 게시글이 없어요.");
                        break;
                    }
                    postModel mypostModel = dataSnapshot.getValue(postModel.class);
                    pa.addItem(mypostModel.getPostTitle(), mypostModel.getPostContent(),
                            mypostModel.getMustKeep(), mypostModel.getWriterName(),mypostModel.getWriteTime(), mypostModel.getCommentCount());

                    if(!mypostModel.isRecruit()) {
                        pa.setBtn();
                    }

                }
                pa.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}