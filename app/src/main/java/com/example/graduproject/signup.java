package com.example.graduproject;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signup extends AppCompatActivity {
    private FirebaseAuth mAuth; //파이어베이스 인증

    private TextInputLayout idInput;
    private TextInputLayout pwInput;
    private TextInputLayout pwCheck;
    private Button mailVerify;
    private TextInputEditText idEdittext;
    private TextInputEditText pwEditText;
    private TextInputEditText pwOKcheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        idInput = findViewById(R.id.idInput);
        pwInput = findViewById(R.id.pwInput);
        pwCheck = findViewById(R.id.pwcheck);
        mailVerify = (Button) findViewById(R.id.mailVerify);

        idEdittext = findViewById(R.id.idEdittext);
        pwEditText = findViewById(R.id.pwEditText);
        pwOKcheck = findViewById(R.id.pwOKedit);


        idEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isValidEmail(idEdittext.getText().toString())) {
                    idInput.setErrorEnabled(false);
                } else {
                    idInput.setError("필수입력/서울과기대 웹메일만 입력가능합니다.");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        pwEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isValidPw(pwEditText.getText().toString())) {
                    pwInput.setErrorEnabled(false);
                } else {
                    pwInput.setError("필수입력/영문,숫자,특수문자를 조합해 8~15자리로 입력하세요.");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        pwOKcheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (pwEditText.getText().toString().equals(pwOKcheck.getText().toString())) {
                    pwCheck.setErrorEnabled(false);
                } else {
                    pwCheck.setError("비밀번호가 일치하지 않습니다.");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        //회원가입 버튼 클릭 리스너
        mailVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = idEdittext.getText().toString();
                String password = pwEditText.getText().toString();
                String passwordOk = pwOKcheck.getText().toString();

                if (isValidEmail(email) && isValidPw(password) && password.equals(passwordOk)
                        && !passwordOk.isEmpty()) {
                    createUser(email, password);
                } else {
                    Toast.makeText(signup.this,"빈칸없이 조건에 맞게 입력하세요.",Toast.LENGTH_LONG).show();
                }
            }
        });

//        finish();
//        startActivity(new Intent(getApplicationContext(),MainActivity.class));


    } //oncreate 끝

    //메일 유효성 검사
    private boolean isValidEmail(String inputEmail) {
        Pattern patternMail = Pattern.compile("^[a-zA-Z0-9]+@(seoultech.ac.kr)$");
        Matcher matcherMail = patternMail.matcher(inputEmail);
        if (!inputEmail.isEmpty() && matcherMail.find()) {
            return true;
        } else return false;
    }


    //비밀번호 유효성 검사
    private boolean isValidPw(String inputPw) {
        Pattern patternPW = Pattern.compile("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@!%*#+?]).{8,15}.$");
        Matcher matcherPW = patternPW.matcher(inputPw);

        if (!inputPw.isEmpty() && matcherPW.find()) {
            return true;
        } else return false;
    }


    //회원가입
    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        //회원가입 성공
                        Log.d(TAG,"signInWithEmail:success");
                        Toast.makeText(signup.this, "인증 성공", Toast.LENGTH_LONG).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        gotoSetting(user);
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(signup.this, "회원가입 실패", Toast.LENGTH_LONG).show();

                    }
                });
    }

    private void gotoSetting(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(this, profileInit.class);
            startActivity(intent);
            finish();
        }
    }
}





//    private void sendVerificationEmail() {
//        FirebaseUser user = mAuth.getCurrentUser();
//        user.sendEmailVerification()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()) {
//
//                        }
//                        else {
//
//                        }
//                    }
//                });
//    }

