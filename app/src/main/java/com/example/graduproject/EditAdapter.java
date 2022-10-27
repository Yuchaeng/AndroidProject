package com.example.graduproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EditAdapter extends RecyclerView.Adapter<EditAdapter.ItemViewHolder>{

    private ArrayList<profileData> listData= new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_edit,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditAdapter.ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    void addItem(profileData profileData) {

        listData.add(profileData);
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView content;
        private TextView open;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.profile_title);
            content = itemView.findViewById(R.id.profile_content);
            open = itemView.findViewById(R.id.profile_open);
        }

        public void onBind(profileData profileData) {
            title.setText(profileData.getTitle());
            content.setText(profileData.getContent());
            open.setText(profileData.getOpen());
        }
    }
}
