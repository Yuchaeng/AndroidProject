package com.example.graduproject;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class login extends AppCompatActivity {
    private TextView signupBtn;
    private Button loginBtn;
    private TextInputLayout userMail;
    private TextInputLayout userPw;
    private TextInputEditText mailEdit;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signupBtn = findViewById(R.id.signupBtn);
        loginBtn = findViewById(R.id.loginBtn);
        userMail = findViewById(R.id.userMail);
        userPw = findViewById(R.id.userPw);

        mailEdit = findViewById(R.id.mailEdit);

        mAuth = FirebaseAuth.getInstance();


        //이미 로그인 되어있으면 바로 홈화면으로
        if(mAuth.getCurrentUser() != null) {
            Toast.makeText(login.this,"자동로그인 되었습니다.",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }

        mailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(isvalidEmail(mailEdit.getText().toString())) {
                    userMail.setErrorEnabled(false);
                }
                else {
                    userMail.setError("서울과기대 웹메일만 입력가능합니다.");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //로그인 버튼
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = userMail.getEditText().toString();
                String userPassword = userPw.getEditText().toString();

                if(!userEmail.isEmpty() && !userPassword.isEmpty()) {
                    userLogin(userEmail,userPassword);

                }
                else {
                    Toast.makeText(login.this,"아이디와 비밀번호를 다시 확인하세요.",Toast.LENGTH_LONG).show();
                }
            }
        });




       //회원가입 화면으로 전환
        String signup = signupBtn.getText().toString();
        SpannableString spannableString = new SpannableString(signup);

        String word = "회원가입하기";
        int start = signup.indexOf(word);
        int end = start + word.length();

        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#ff0000")),start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        signupBtn.setText(spannableString);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, signup.class);
                startActivity(intent);
            }
        });
    }

    //이메일 유효성 검사
    private boolean isvalidEmail(String email) {
        Pattern patternMail = Pattern.compile("^[a-zA-Z0-9]+@(seoultech.ac.kr)$");
        Matcher matcherMail = patternMail.matcher(email);
        if(!email.isEmpty() && matcherMail.find()) {
            return true;
        }
        else return false;
    }

    //로그인함수
    private void userLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(login.this,"로그인 성공",Toast.LENGTH_LONG).show();
                            movePage(mAuth.getCurrentUser());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(login.this, "로그인 실패",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void movePage(FirebaseUser user) {
        if(user != null) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}