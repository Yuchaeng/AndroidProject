package com.example.graduproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class boongWriteDetail extends AppCompatActivity {
    CircleImageView detailImage;
    TextView detailWriter, detailTime, detailTitle, detailDateInput, detailPeopleInput, detailGenderInput,
    detailContent;
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
            startActivity(new Intent(boongWriteDetail.this, boongzza.class));
            finish();
            return true;
        }

        return false;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boong_write_detail);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance(); //???????????? ????????????
        storageRef = storage.getReference(); //???????????? ??????

        detailImage =  findViewById(R.id.detailImage);
        detailWriter = findViewById(R.id.detailWriter);
        detailTime = findViewById(R.id.detailTime);
        detailTitle = findViewById(R.id.detailTitle);
        detailDateInput = findViewById(R.id.detailDateInput);
        detailPeopleInput = findViewById(R.id.detailPeopleInput);
        detailGenderInput = findViewById(R.id.detailGenderInput);
        detailContent = findViewById(R.id.detailContent);
        commentLists = findViewById(R.id.commentLists);
        commentInput = findViewById(R.id.commentInput);
        commentRegister = findViewById(R.id.commentRegister);
        detailIng = findViewById(R.id.detailIng);
        detailClose = findViewById(R.id.detailClose);

        //??? ?????????
        Intent detailIntent = getIntent();
        String key = detailIntent.getStringExtra("key");

        ca = new commentAdapter();
        commentLists.setAdapter(ca);

        mDatabaseRef.child("allPosts").child("boongzza").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //????????? ????????????
                postModel postModel = snapshot.getValue(postModel.class);

                //????????? ??????
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
                    commentInput.setText("????????? ????????? ??????????????????.");
                }


                //???????????????
                StorageReference pathRef = storageRef.child("UserProfile").child(postModel.getWriterUid()+"_img");
                if(pathRef!=null) { //profile.getProfileImageUrl() != null
                    pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(boongWriteDetail.this).load(uri).into(detailImage);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Glide.with(boongWriteDetail.this).load(R.drawable.no_profile_image).into(detailImage);
                        }
                    });

                }

                //?????????
                detailWriter.setText(postModel.getWriterName());

                //??????
                detailTime.setText(postModel.getWriteTime());

                //??????
                detailTitle.setText(postModel.getPostTitle());

                //?????? ??????
                detailDateInput.setText(postModel.getDate()+", "+postModel.getTime());

                //?????????
                detailPeopleInput.setText(postModel.getPeople());

                //??????
                detailGenderInput.setText(postModel.getGender());

                //??????
                detailContent.setText(postModel.getPostContent());


                mDatabaseRef.child("allPosts").child("boongzza").child(key).child("comment").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            commentModel commentModel = dataSnapshot.getValue(commentModel.class);

                            if(postModel.getWriterUid().equals(commentModel.getCommentUid())) {
                                check=0;
                            }
                            else{
                                check=1;
                            }
                            Log.d("check", String.valueOf(check));
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

        //????????????
        detailIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(boongWriteDetail.this);
                builder.setTitle("????????????").setMessage("????????? ??????????????????????").setCancelable(true);
                builder.setPositiveButton("???", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDatabaseRef.child("allPosts").child("boongzza").child(key).child("recruit").setValue(false);
                        detailIng.setVisibility(View.GONE);
                        detailClose.setVisibility(View.VISIBLE);
                        commentRegister.setVisibility(View.INVISIBLE);
                        commentInput.setFocusable(false);
                        commentInput.setClickable(false);
                        commentInput.setText("????????? ????????? ??????????????????.");

                    }
                });

                builder.setNegativeButton("?????????", new DialogInterface.OnClickListener() {
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
                        //?????? ??????
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

                        mDatabaseRef.child("allPosts").child("boongzza").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                postModel pm = snapshot.getValue(postModel.class);
                                if(pm.getWriterUid().equals(commentModel.getCommentUid())) {
                                    check=0;
                                }
                                else{
                                    check=1;
                                }
                                Log.d("check", String.valueOf(check));
                                ca.addItem(uri,commentModel.getCommentName(),commentModel.getCommentContent(),commentModel.getCommentTime(),check);
                                ca.notifyDataSetChanged();
                                mDatabaseRef.child("allPosts").child("boongzza").child(key).child("comment").push().setValue(commentModel);
                                mDatabaseRef.child("allPosts").child("boongzza").child(key).child("commentCount").setValue(ca.getCount());

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

        commentLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mDatabaseRef.child("allPosts").child("boongzza").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        postModel postModel = snapshot.getValue(com.example.graduproject.postModel.class);
                        if(!postModel.getWriterUid().equals(mUser.getUid()) && postModel.isRecruit()) { //???????????? ???????????? ?????? ??? ???????????? ??????
                            AlertDialog.Builder builder = new AlertDialog.Builder(boongWriteDetail.this);
                            builder.setTitle("??????").setMessage("???????????? ??????????????? ????????? ?????? ??? ?????????.").setCancelable(true);
                            builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        }
                        else if(!postModel.isRecruit()) {  //??????????????? ?????? ??????????????? ??????

                        }
                        else{ //??? ??????????????? ?????????
                            ArrayList<String> commentKey = new ArrayList<>();

                            mDatabaseRef.child("allPosts").child("boongzza").child(key).child("comment").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    commentKey.clear();
                                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        commentModel commentModel = dataSnapshot.getValue(com.example.graduproject.commentModel.class);
                                        commentKey.add(commentModel.getCommentUid());
                                    }
                                    String destUid = commentKey.get(i);  //????????? ????????? ?????? ?????? ??????????????? uid??? ?????????

                                    if(destUid.equals(postModel.getWriterUid())) { //?????????=???????????????
                                        AlertDialog.Builder builder = new AlertDialog.Builder(boongWriteDetail.this);
                                        builder.setTitle("??????").setMessage("???????????? ????????? ?????? ??? ?????????.").setCancelable(true);
                                        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        });
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                    }
                                    else { //??? ??????????????? ??????????????? ???????????? ??? ???????????? ?????? ???
                                        mDatabaseRef.child("userInfo").child(destUid).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                userProfile userProfile = snapshot.getValue(com.example.graduproject.userProfile.class);

                                                AlertDialog.Builder builder = new AlertDialog.Builder(boongWriteDetail.this);
                                                builder.setTitle("???????????????").setMessage(userProfile.getNickName()+"????????? ????????? ???????????????????").setCancelable(true);
                                                builder.setPositiveButton("???", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent intent = new Intent(boongWriteDetail.this, ChatFunction.class);
                                                        intent.putExtra("destUid",destUid);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });

                                                builder.setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                });

                                                AlertDialog alertDialog = builder.create();
                                                alertDialog.show();

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}