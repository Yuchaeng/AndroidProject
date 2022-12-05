package com.example.graduproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
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


public class MypageFragment extends Fragment {
    ImageView myImage;
    Button editBtn;
    TextView myNickname, onlineText, myBreak, myFeature;
    RecyclerView recyclerView;
    Adapter adapter;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseRef;
    private FirebaseStorage storage;
    private StorageReference storageRef;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        editBtn = (Button)view.findViewById(R.id.editBtn);
        myImage = (ImageView) view.findViewById(R.id.myImage);
        myNickname = (TextView) view.findViewById(R.id.myNickname);
        onlineText = (TextView) view.findViewById(R.id.onlineText);
        myBreak = (TextView) view.findViewById(R.id.myBreak);
        myFeature = (TextView) view.findViewById(R.id.myfeature);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance(); //스토리지 인스턴스
        storageRef = storage.getReference(); //스토리지 참조

        //프로필사진 띄우기
        StorageReference imgRef = storageRef.child("UserProfile").child(mUser.getUid()+"_img");
        if(imgRef!=null) { //profile.getProfileImageUrl() != null
            imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getActivity())
                            .load(uri)
                            .into(myImage);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Glide.with(getActivity()).load(R.drawable.cat_temp).into(myImage);
                }
            });

        }

        //닉네임,공강,관심사,한줄소개 띄우기
        mDatabaseRef.child("userInfo").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile info = snapshot.getValue(userProfile.class);
                String nickName = info.getNickName();
                String introduce = info.getIntroduce();
                String emptyString = info.getEmptyTime();
                String interestString = info.getInterest();

                if(nickName.equals("닉네임을 설정해주세요.")) {
                    myNickname.setText("닉네임 없음");
                }
                else {
                    myNickname.setText(nickName);
                }
                onlineText.setText(introduce);
                myBreak.setText(emptyString);
                myFeature.setText(interestString);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //프로필 편집 버튼
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), myChange.class); //fragment라서 activity intent와는 다른 방식
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            }
        });

        return view;
    }
}