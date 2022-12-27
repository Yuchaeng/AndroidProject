package com.example.graduproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileListAdapter extends RecyclerView.Adapter<UserProfileListAdapter.MyViewHolder> {
    private ArrayList<userProfile> mDataset;
    String stMyEmail = "";
    private FirebaseStorage storage;
    private StorageReference storageRef;
    FirebaseAuth mAuth;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUser;
        public CircleImageView ivUser;
        public TextView tvInterest;
        public TextView tvIntroduce;
        public MyViewHolder(View v) {
            super(v);
            tvUser = v.findViewById(R.id.tvUser);
            ivUser = v.findViewById(R.id.ivUser);
            tvInterest = v.findViewById(R.id.tvInterest);
            tvIntroduce = v.findViewById(R.id.tvIntroduce);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);

    }

    public UserProfileListAdapter(ArrayList<userProfile> myDataset) {
        mDataset = myDataset;
    }

    @NonNull
    @Override
    public UserProfileListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_info_view, parent, false);


        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull UserProfileListAdapter.MyViewHolder holder, int position) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user.getUid().equals(mDataset.get(position).getUid())){

        } else{
            holder.tvUser.setText(mDataset.get(position).getNickName());
            holder.tvInterest.setText((mDataset.get(position).getInterest()));
            holder.tvIntroduce.setText(mDataset.get(position).getIntroduce());
            if (mDataset.get(position).getProfileImageUrl()!=null){
                Glide.with(holder.itemView).load(mDataset.get(position).getProfileImageUrl()).override(80,80).into(holder.ivUser);
            } else {
                Glide.with(holder.itemView).load(R.drawable.no_profile_image).override(80,80).into(holder.ivUser);
            }
        }
        


        //StorageReference pathRef = storageRef.child("userProfile").child(mDataset.get(position).getUid()+"_img");
        /*if(pathRef!=null) { //profile.getProfileImageUrl() != null

            pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(holder.itemView.getContext())
                            .load(uri)
                            .into(holder.ivUser);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Glide.with(holder.itemView.getContext()).load(R.drawable.cat_temp).into(holder.ivUser);
                }
            });

        }*/
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
