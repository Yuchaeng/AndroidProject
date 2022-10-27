package com.example.graduproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class myChange extends AppCompatActivity {

    private EditAdapter adapter;
    TextView picChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mychage);

        picChange = (TextView)findViewById(R.id.picChange);
        //프로필 사진 변경
        picChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        init();
        getData();


    }

    private void init() {

        RecyclerView edit_recycle = findViewById(R.id.edit_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        edit_recycle.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
        edit_recycle.addItemDecoration(dividerItemDecoration);
        adapter = new EditAdapter();
        edit_recycle.setAdapter(adapter);

    }

    private void getData() {
        List<String> listTitle = Arrays.asList("닉네임", "공강시간", "관심사/특징", "한줄소개",
                "이름", "성별", "생년월일", "학과/학번");
        List<String> listContent = Arrays.asList("고양이", "월 13시~15시", "enfp,디즈니", "잘 부탁해용",
                "김ㅇㅇ", "여성", "1999.09.01", "컴퓨터공학과/20학번");
        List<String> listOpen = Arrays.asList(">",">",">",">"," "," "," "," ");

        for(int i=0; i<listTitle.size(); i++) {
            profileData profileData = new profileData();
            profileData.setTitle(listTitle.get(i));
            profileData.setContent(listContent.get(i));
            profileData.setOpen(listOpen.get(i));

            adapter.addItem(profileData);
        }

        adapter.notifyDataSetChanged();
    }

}