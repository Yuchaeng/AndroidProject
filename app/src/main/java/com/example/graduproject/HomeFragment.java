package com.example.graduproject;

import android.content.Intent;
import android.media.MediaCodec;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;


public class HomeFragment extends Fragment {
    Button boongzzaBtn, mealBtn, exerciseBtn, studyBtn, freeBtn, recommentClub;
    ViewFlipper v_fllipper;
    TextView noThunder;
    LinearLayout thunderPost;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

       int images[] = {R.drawable.banner_temp_1, R.drawable.banner_temp_2, R.drawable.banner_3};
       v_fllipper = (ViewFlipper) view.findViewById(R.id.pager);
       for(int image : images) {
           fllipperImages(image);
       }

        boongzzaBtn = (Button)view.findViewById(R.id.boongzzaBtn);
        mealBtn = (Button) view.findViewById(R.id.mealBtn);
        exerciseBtn = (Button)view.findViewById(R.id.exerciseBtn);
        studyBtn = (Button) view.findViewById(R.id.studyBtn);
        freeBtn = (Button) view.findViewById(R.id.freeBtn);
        noThunder = (TextView) view.findViewById(R.id.noThunder);
        recommentClub = (Button) view.findViewById(R.id.recommendClub);
        thunderPost = view.findViewById(R.id.thunderPost);

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View viewThunder = layoutInflater.inflate(R.layout.board,null);



        boongzzaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), boongzza.class); //fragment라서 activity intent와는 다른 방식
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        mealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), meal.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        exerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), exercise.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            }
        });

        studyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), study.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            }
        });

        freeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), free.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            }
        });

        mDatabaseRef.child("allPosts").child("free").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> keys = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    postModel postModel = dataSnapshot.getValue(com.example.graduproject.postModel.class);
                    Log.d("thunder",postModel.getType());
                    if (postModel.getType().equals("번개") && postModel.isRecruit()) {  //번개이고 모집중인 경우 키 넣기
                        keys.add(dataSnapshot.getKey());
                    }
                }

                Log.d("keys", String.valueOf(keys.size()));

                if(keys.size()==0) { //번개이고 모집중인 글 없을 때
                    noThunder.setVisibility(View.VISIBLE);
                }
                else {
                    noThunder.setVisibility(View.GONE);
                    Random rand = new Random();
                    int ranNum = rand.nextInt(keys.size()); //게시글 중 랜덤으로 고르기
                    String randomKey = keys.get(ranNum);
                    mDatabaseRef.child("allPosts").child("free").child(randomKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //게시글 정보 가져오기
                            postModel postModel = snapshot.getValue(com.example.graduproject.postModel.class);
                            ((Button)viewThunder.findViewById(R.id.ingBtn)).setVisibility(View.VISIBLE);
                            ((TextView) viewThunder.findViewById(R.id.boardTime)).setText(postModel.getWriteTime());
                            Log.d("name",postModel.getWriterName());
                            ((TextView) viewThunder.findViewById(R.id.boardMust)).setText(postModel.getType());
                            ((TextView) viewThunder.findViewById(R.id.boardTitle)).setText(postModel.getPostTitle());
                            ((TextView) viewThunder.findViewById(R.id.boardContent)).setText(postModel.getPostContent());
                            ((TextView) viewThunder.findViewById(R.id.writerName)).setText(postModel.getWriterName());
                            ((TextView) viewThunder.findViewById(R.id.commentCount)).setText(" "+postModel.getCommentCount());

                            thunderPost.addView(viewThunder);

                            thunderPost.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent postIntent = new Intent(getActivity(), freePostDetail.class);
                                    postIntent.putExtra("key", randomKey);
                                    startActivity(postIntent);
                                }
                            });
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


        noThunder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), free.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        recommentClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), clubLoading.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });




        return view;

    }


    public void fllipperImages(int image) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(image);

        v_fllipper.addView(imageView);      // 이미지 추가
        v_fllipper.setFlipInterval(2500);       // 자동 이미지 슬라이드 딜레이시간(1000 당 1초)
        v_fllipper.setAutoStart(true);          // 자동 시작 유무 설정

        // animation
        v_fllipper.setInAnimation(getContext(),android.R.anim.slide_in_left);
        v_fllipper.setOutAnimation(getContext(),android.R.anim.slide_out_right);

    }
}
