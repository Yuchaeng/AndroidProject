package com.example.graduproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class boongzzawrite extends AppCompatActivity {

    private TextView dateText;
    private DatePickerDialog datePickerDialog;
    private RadioButton selectDate,noSelect;
    private Spinner timeSpinner, peopleSpinner, mfSpinner;
    private String[] timeItem = {"오전","오후"};
    private String[] people = {"2명","3명","4명","5명","6명이상","무관"};
    private String[] mfItem = {"무관","여성","남성"};

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

        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,timeItem);
        timeAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        //스피너 객체에 어댑터 넣어주기
        timeSpinner.setAdapter(timeAdapter);
//        timeSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//        });
        ArrayAdapter<String> peopleAdapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,people);
        peopleAdapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item);
        peopleSpinner.setAdapter(peopleAdapter);

        ArrayAdapter<String> mfAdapter = new ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,mfItem);
        mfAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        mfSpinner.setAdapter(mfAdapter);

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

    }



}