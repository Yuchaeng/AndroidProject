package com.example.graduproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.List;

public class boongzza extends AppCompatActivity {
    private FloatingActionButton writeBtn;
    private BoardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boongzza);

        RecyclerView boongzza_recycle = findViewById(R.id.boongzzaRecycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        boongzza_recycle.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
        boongzza_recycle.addItemDecoration(dividerItemDecoration);
        adapter = new BoardAdapter();
        boongzza_recycle.setAdapter(adapter);
        getData();



        writeBtn = (FloatingActionButton) findViewById(R.id.writeBtn);
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), boongzzawrite.class);
                startActivity(intent);
            }
        });


    }
    private void getData() {
        List<String> mustList = Arrays.asList("2022/11/02,오후3시,2명,여성","2022/10/30,오전11시,4명,성별무관");
        List<String> titleList = Arrays.asList("붕짜할사람","다음주 붕짜하실 분");
        List<String> timeList = Arrays.asList("10/27 13:21","10/24 16:00");
        List<String> nameList = Arrays.asList("붕붕이","곽곽이");
        List<String> commentList = Arrays.asList("1","3");

        for(int i=0; i<mustList.size();i++) {
            boardData boardData = new boardData();
            boardData.setMustText(mustList.get(i));
            boardData.setWriteTitle(titleList.get(i));
            boardData.setWriteTime(timeList.get(i));
            boardData.setNickName(nameList.get(i));
            boardData.setCommentCount(commentList.get(i));

            adapter.addBoardItem(boardData);
        }
        adapter.notifyDataSetChanged();
    }
}