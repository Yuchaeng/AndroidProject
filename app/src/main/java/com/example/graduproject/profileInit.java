package com.example.graduproject;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class profileInit extends AppCompatActivity {
    TextInputEditText nameText;
    DatePicker birth;
    RadioGroup genderGroup;
    RadioButton selectFemale;
    RadioButton selectMale;
    Spinner dept;
    Spinner studentNum;
    Button saveBtn;
    String selectYear, selectMonth, selectDay;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    String birthString;


    private String[] selectDept = {"기계시스템디자인공학과","기계ㆍ자동차공학과","안전공학과","신소재공학과","건설시스템공학과", "건축학부(건축공학전공)","건축학부(건축학전공)",
        "[계약학과]건축기계설비공학과","전기정보공학과","전자IT미디어공학과","컴퓨터공학과","스마트ICT융합공학과","화공생명공학과","환경공학과","식품공학과","정밀화학과","스포츠과학과",
            "안경광학과","산업디자인과","시각디자인과","도예학과","금속공예디자인학과","조형예술학과",
            "행정학과","영어영문학과","문예창작학과","산업공학과","MSDE학과","경영학과","융합공학부(융합기계공학전공)","융합공학부(건설환경융합전공)","융합사회학부(헬스피트니스전공)",
            "융합사회학부(문화예술전공)","융합사회학부(영어전공)","융합사회학부(벤처경영전공)","인공지능응용학과","지능형반도체공학과","미래에너지융합학과"};
    private String[] selectStundetId = {"16학번","17학번","18학번","19학번","20학번","21학번","22학번","23학번"};

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if(keycode == android.view.KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(profileInit.this, signup.class));
            finish();
            return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_init);

        nameText = findViewById(R.id.nameText);
        birth = findViewById(R.id.birth);
        genderGroup = findViewById(R.id.genderGroup);
        selectFemale = findViewById(R.id.selectFemale);
        selectMale = findViewById(R.id.selectMale);
        dept = findViewById(R.id.dept);
        studentNum = findViewById(R.id.studentNum);
        saveBtn = findViewById(R.id.saveBtn);
        //데이터베이스로 연결
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        String initNickname = "곽곽"+(int)(Math.random()*1000);


        //생년월일을 문자 변수에 저장 -> db에 넘겨서 마이페이지에 띄울 것임
        birth.init(birth.getYear(), birth.getMonth(), birth.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                selectYear = Integer.toString(year);
                selectMonth = Integer.toString(month+1);
                selectDay = Integer.toString(day);

                birthString = selectYear + "-" + selectMonth + "-" + selectDay;
            }
        });

        //학과 스피너
        ArrayAdapter<String> deptAdapter = new ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,selectDept);
        deptAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        dept.setAdapter(deptAdapter);


        //학번 스피너
        ArrayAdapter<String> idAdapter = new ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,selectStundetId);
        idAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        studentNum.setAdapter(idAdapter);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser mUser = mAuth.getCurrentUser();
                userProfile info = new userProfile();
                info.setUid(mUser.getUid());
                info.setName(nameText.getText().toString());
                info.setBirth(birthString);

                int checkRadio = genderGroup.getCheckedRadioButtonId();
                RadioButton rb = findViewById(checkRadio);
                info.setGender(rb.getText().toString());

                String deptText = dept.getSelectedItem().toString();
                String idText = studentNum.getSelectedItem().toString();
                info.setDept(deptText);
                info.setStudentId(idText);

                info.setNickName(initNickname);
                info.setEmptyTime("공강시간을 설정해주세요.");
                info.setInterest("관심사 및 특징을 설정해주세요.");
                info.setIntroduce("한줄소개를 작성해주세요.");

                mDatabase.child("userInfo").child(mUser.getUid()).setValue(info);

                gotoMain(mUser);

            }
        });

    }

    private void gotoMain(FirebaseUser user) {
        if(user != null) {
            Toast.makeText(profileInit.this,"환영합니다 "+nameText.getText().toString()+"님",Toast.LENGTH_LONG).show();
            startActivity(new Intent(profileInit.this,loadMain.class));
            finish();
        }
    }
}