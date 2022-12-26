package com.example.graduproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class MypageFragment extends Fragment {
    ImageView myImage;
    Button editBtn;
    TextView myNickname, onlineText, myBreak, myFeature, noMyPost;
    ListView resultList;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseRef;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    ArrayList<String> titles = new ArrayList<String>();
    ArrayList<String> contents = new ArrayList<String>();
    ArrayList<String> times = new ArrayList<String>();
    ArrayList<String> boards = new ArrayList<String>();

    CustomAdapter ca;



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
        resultList = (ListView) view.findViewById(R.id.myPostlist);
        noMyPost = (TextView) view.findViewById(R.id.noMyPost);
        LinearLayout myPage = (LinearLayout) view.findViewById(R.id.myPage);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();


        ca = new CustomAdapter();
        resultList.setAdapter(ca);
        getUserPost();

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
                    Glide.with(getActivity()).load(R.drawable.no_profile_image).into(myImage);
                }
            });

        }

        //닉네임,공강,관심사,한줄소개 띄우기
        mDatabaseRef.child("userInfo").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile info = snapshot.getValue(userProfile.class);
                String nickName = info.getNickName();
                String introduce = info.getIntroduce();
                String emptyString = info.getEmptyTime();
                String interestString = info.getInterest();

                myNickname.setText(nickName);
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

    @Override
    public void onResume() {
        super.onResume();

        getUserPost();

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
                    Glide.with(getActivity()).load(R.drawable.no_profile_image).into(myImage);
                }
            });

        }

        //닉네임,공강,관심사,한줄소개 띄우기
        mDatabaseRef.child("userInfo").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile info = snapshot.getValue(userProfile.class);
                String nickName = info.getNickName();
                String introduce = info.getIntroduce();
                String emptyString = info.getEmptyTime();
                String interestString = info.getInterest();

                myNickname.setText(nickName);
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



    }


    class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.mypost_inlist,null);

            TextView title = (TextView) convertView.findViewById(R.id.title_inlist);
            TextView content = (TextView) convertView.findViewById(R.id.content_inlist);
            TextView time = (TextView) convertView.findViewById(R.id.time_inlist);
            TextView board = (TextView) convertView.findViewById(R.id.board_inlist);

            title.setText(titles.get(position));
            content.setText(contents.get(position));
            time.setText(times.get(position));
            board.setText(boards.get(position));

            return convertView;
        }
    }
    public void getUserPost() {

        mDatabaseRef.child("user-posts").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                titles.clear();
                contents.clear();
                times.clear();
                boards.clear();

                for(DataSnapshot ds : snapshot.getChildren()) {
                    if(ds == null) {
                        noMyPost.setVisibility(View.VISIBLE);
                        break;
                    }
                    resultList.setVisibility(View.VISIBLE);
                    noMyPost.setVisibility(View.GONE);

                    postModel postModel = ds.getValue(postModel.class);
                    titles.add(postModel.getPostTitle());
                    contents.add(postModel.getPostContent());
                    times.add(postModel.getWriteTime());
                    boards.add(postModel.getBoardName());

                }
                ca.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}