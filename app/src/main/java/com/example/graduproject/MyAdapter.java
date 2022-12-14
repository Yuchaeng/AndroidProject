package com.example.graduproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.awt.font.TextAttribute;
import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Chat> mDataset;
    private String myNickName = "";
    private String urNickName = "";

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNickName;
        public TextView tvChat;
        public MyViewHolder(View v) {
            super(v);
            tvNickName = v.findViewById(R.id.tvNickName);
            tvChat = v.findViewById(R.id.tvChat);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        if(mDataset.get(position).nickname.equals(myNickName)){
            return 1;
        } else{
            return 2;
        }
    }

    //adapter 처음 실행
    //recyclerview list에 나타나는 데이터
    public MyAdapter(ArrayList<Chat> myDataset, String nick, String nick2) {
        mDataset = myDataset;
        this.myNickName = nick;
        this.urNickName = nick2;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);

        if(viewType == 1){
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.right_text_view, parent, false);
        }
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    //data transfer object
    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        /*holder.tvNickName.setText(mDataset.get(position).getNickname());
        holder.tvChat.setText(mDataset.get(position).getMsg());*/

        if (mDataset.get(position).getNickname().equals(urNickName)){
            holder.tvNickName.setText(mDataset.get(position).getNickname());
            holder.tvChat.setText(mDataset.get(position).getMsg());
        } else if (mDataset.get(position).getNickname().equals(myNickName)) {
            holder.tvNickName.setText(mDataset.get(position).getNickname());
            holder.tvChat.setText(mDataset.get(position).getMsg());
        } else {
            holder.tvNickName.setVisibility(View.GONE);
            holder.tvNickName.setLayoutParams(new RecyclerView.LayoutParams(0,0));
            holder.tvChat.setVisibility(View.GONE);
            holder.tvChat.setLayoutParams(new RecyclerView.LayoutParams(0,0));
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}

