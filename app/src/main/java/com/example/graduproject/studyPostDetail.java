package com.example.graduproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class studyPostDetail extends AppCompatActivity {
    CircleImageView detailImage;
    TextView detailWriter, detailTime, detailTitle, detailDateInput, detailPeopleInput, detailGenderInput,
            detailContent, typeStudy;
    ListView commentLists;
    EditText commentInput;
    Button commentRegister, detailIng, detailClose;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser mUser;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    commentAdapter ca;
    int check=1;


    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if(keycode ==KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(studyPostDetail.this, study.class));
            finish();
            return true;
        }
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_post_detail);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance(); //스토리지 인스턴스
        storageRef = storage.getReference(); //스토리지 참조

        detailImage =  findViewById(R.id.detailImageStudy);
        detailWriter = findViewById(R.id.detailWriterStudy);
        detailTime = findViewById(R.id.detailTimeStudy);
        detailTitle = findViewById(R.id.detailTitleStudy);
        detailDateInput = findViewById(R.id.detailDateInputStudy);
        detailPeopleInput = findViewById(R.id.detailPeopleInputStudy);
        detailGenderInput = findViewById(R.id.detailGenderInputStudy);
        detailContent = findViewById(R.id.detailContentStudy);
        commentLists = findViewById(R.id.commentListsStudy);
        commentInput = findViewById(R.id.commentInputStudy);
        commentRegister = findViewById(R.id.commentRegisterStudy);
        detailIng = findViewById(R.id.detailIngStudy);
        detailClose = findViewById(R.id.detailCloseStudy);
        typeStudy = findViewById(R.id.typeStudy);

        //키 받아옴
        Intent detailIntent = getIntent();
        String key = detailIntent.getStringExtra("key");

        ca = new commentAdapter();
        commentLists.setAdapter(ca);

        mDatabaseRef.child("allPosts").child("study").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //데이터 받아오기
                postModel postModel = snapshot.getValue(postModel.class);

                //모집중 버튼
                if(postModel.isRecruit()) {
                    detailIng.setVisibility(View.VISIBLE);
                    detailClose.setVisibility(View.GONE);
                    detailIng.setClickable(postModel.getWriterUid().equals(mUser.getUid()));
                }
                else {
                    detailIng.setVisibility(View.GONE);
                    detailClose.setVisibility(View.VISIBLE);
                    commentRegister.setVisibility(View.INVISIBLE);
                    commentInput.setFocusable(false);
                    commentInput.setClickable(false);
                    commentInput.setText("모집이 종료된 게시글입니다.");
                }


                //프로필사진
                StorageReference pathRef = storageRef.child("UserProfile").child(postModel.getWriterUid()+"_img");
                if(pathRef!=null) { //profile.getProfileImageUrl() != null
                    pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(studyPostDetail.this).load(uri).into(detailImage);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Glide.with(studyPostDetail.this).load(R.drawable.no_profile_image).into(detailImage);
                        }
                    });

                }

                //닉네임
                detailWriter.setText(postModel.getWriterName());

                //무슨공부
                typeStudy.setText(postModel.getType());

                //시간
                detailTime.setText(postModel.getWriteTime());

                //제목
                detailTitle.setText(postModel.getPostTitle());

                //날짜 시간
                detailDateInput.setText(postModel.getDate()+", "+postModel.getTime());

                //인원수
                detailPeopleInput.setText(postModel.getPeople());

                //성별
                detailGenderInput.setText(postModel.getGender());

                //내용
                detailContent.setText(postModel.getPostContent());


                mDatabaseRef.child("allPosts").child("study").child(key).child("comment").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            commentModel commentModel = dataSnapshot.getValue(commentModel.class);

                            if(postModel.getWriterUid().equals(commentModel.getCommentUid())) {
                                check=0;
                            }
                            ca.addItem(commentModel.getImageUri(),commentModel.getCommentName(),commentModel.getCommentContent(),commentModel.getCommentTime(),check);

                            ca.notifyDataSetChanged();


                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //모집완료
        detailIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(studyPostDetail.this);
                builder.setTitle("모집완료").setMessage("모집을 완료하시겠어요?").setCancelable(true);
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDatabaseRef.child("allPosts").child("study").child(key).child("recruit").setValue(false);
                        detailIng.setVisibility(View.GONE);
                        detailClose.setVisibility(View.VISIBLE);
                        commentRegister.setVisibility(View.INVISIBLE);
                        commentInput.setFocusable(false);
                        commentInput.setClickable(false);
                        commentInput.setText("모집이 종료된 게시글입니다.");

                    }
                });

                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });


        commentRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabaseRef.child("userInfo").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String comment = commentInput.getText().toString();
                        //현재 시간
                        long now = System.currentTimeMillis();
                        Date date = new Date(now);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
                        String writeTime = dateFormat.format(date);

                        userProfile userProfile = snapshot.getValue(userProfile.class);
                        String uri = userProfile.getProfileImageUrl();
                        String name = userProfile.getNickName();

                        commentModel commentModel = new commentModel();

                        commentModel.setImageUri(uri);
                        commentModel.setCommentUid(mUser.getUid());
                        commentModel.setCommentName(name);
                        commentModel.setCommentContent(comment);
                        commentModel.setCommentTime(writeTime);

                        mDatabaseRef.child("allPosts").child("study").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                postModel pm = snapshot.getValue(postModel.class);
                                if(pm.getWriterUid().equals(commentModel.getCommentUid())) {
                                    check=0;
                                }
                                ca.addItem(String.valueOf(uri),commentModel.getCommentName(),commentModel.getCommentContent(),commentModel.getCommentTime(),check);
                                ca.notifyDataSetChanged();
                                mDatabaseRef.child("allPosts").child("study").child(key).child("comment").push().setValue(commentModel);
                                mDatabaseRef.child("allPosts").child("study").child(key).child("commentCount").setValue(ca.getCount());

                                commentInput.setText("");
                                InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

    }
}