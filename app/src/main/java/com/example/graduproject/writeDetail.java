package com.example.graduproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class writeDetail extends AppCompatActivity {
    CircleImageView detailImage;
    TextView detailWriter, detailTime, detailTitle, detailDateInput, detailPeopleInput, detailGenderInput,
    detailContent;
    ListView commentList;
    EditText commentInput;
    Button commentRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_detail);

        detailImage =  findViewById(R.id.detailImage);
        detailWriter = findViewById(R.id.detailWriter);
        detailTime = findViewById(R.id.detailTime);
        detailTitle = findViewById(R.id.detailTitle);
        detailDateInput = findViewById(R.id.detailDateInput);
        detailPeopleInput = findViewById(R.id.detailPeopleInput);
        detailGenderInput = findViewById(R.id.detailGenderInput);
        detailContent = findViewById(R.id.detailContent);
        commentList = findViewById(R.id.commentList);
        commentInput = findViewById(R.id.commentInput);
        commentRegister = findViewById(R.id.commentRegister);
    }
}