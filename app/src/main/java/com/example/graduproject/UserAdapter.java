package com.example.graduproject;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private ArrayList<userProfile> mDataset;
    String stMyEmail = "";
    private FirebaseStorage storage;
    private StorageReference storageRef;
    FirebaseAuth mAuth;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUser;
        public ImageView ivUser;
        public TextView goChat;
        public MyViewHolder(View v) {
            super(v);
            tvUser = v.findViewById(R.id.tvUser);
            ivUser = v.findViewById(R.id.ivUser);
            goChat = v.findViewById(R.id.goChat);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);

    }

    public UserAdapter(ArrayList<userProfile> myDataset) {
        mDataset = myDataset;
    }

    @NonNull
    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item_view, parent, false);


        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.MyViewHolder holder, int position) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        holder.tvUser.setText(mDataset.get(position).getNickName());
        //프로필사진
        if (mDataset.get(position).getProfileImageUrl()!=null){
            Glide.with(holder.itemView).load(mDataset.get(position).getProfileImageUrl()).override(100,100).into(holder.ivUser);
        } else {
            Glide.with(holder.itemView).load(R.drawable.no_profile_image).override(100,100).into(holder.ivUser);
        }




        holder.goChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
