package com.example.graduproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder> {

    private ArrayList<boardData> boardList = new ArrayList<>();

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board,parent,false);
        return new BoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardAdapter.BoardViewHolder holder, int position) {
        holder.onBind(boardList.get(position));
    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }

    void addBoardItem(boardData boardData) {

        boardList.add(boardData);
    }

    class BoardViewHolder extends RecyclerView.ViewHolder {
        private Button keepBtn;
        private TextView mustText;
        private TextView writeTime;
        private TextView writeTitle;
        private TextView nickName;
        private TextView commentCount;

        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);

            keepBtn = itemView.findViewById(R.id.ingBtn);
            mustText = itemView.findViewById(R.id.boardContent);
            writeTime = itemView.findViewById(R.id.boardTime);
            writeTitle = itemView.findViewById(R.id.boardTitle);
            nickName = itemView.findViewById(R.id.boardName);
            commentCount = itemView.findViewById(R.id.commentCount);
        }

        public void onBind(boardData boardData) {
            mustText.setText(boardData.getMustText());
            writeTime.setText(boardData.getWriteTime());
            writeTitle.setText(boardData.getWriteTitle());
            nickName.setText(boardData.getNickName());
            commentCount.setText(boardData.getCommentCount());

        }
    }
}
