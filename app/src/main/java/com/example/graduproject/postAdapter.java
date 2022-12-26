package com.example.graduproject;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class postAdapter extends BaseAdapter {
    ArrayList<boardList> board = new ArrayList<boardList>();

    @Override
    public int getCount() {
        return board.size();
    }

    @Override
    public Object getItem(int position) {
        return board.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.board,parent,false);
        }

        Button ingBtn = convertView.findViewById(R.id.ingBtn);
        Button closeBtn = convertView.findViewById(R.id.ingClose);
        TextView boardMust = convertView.findViewById(R.id.boardMust);
        TextView boardTitle = convertView.findViewById(R.id.boardTitle);
        TextView boardContent = convertView.findViewById(R.id.boardContent);
        TextView writerName = convertView.findViewById(R.id.writerName);
        TextView boardTime = convertView.findViewById(R.id.boardTime);
        TextView commentCount = convertView.findViewById(R.id.commentCount);
        TextView whatType = convertView.findViewById(R.id.whatType);

        boardList boardList = board.get(position);



        if(boardList.getWhatType()!=null) {
            whatType.setVisibility(View.VISIBLE);
            whatType.setText(boardList.getWhatType());
            whatType.setTextColor(Color.parseColor("#F578CF"));
        }
        else{
            whatType.setVisibility(View.GONE);
        }

        boardMust.setText(boardList.getMustText());
        boardTitle.setText(boardList.getWriteTitle());
        boardContent.setText(boardList.getWriteContent());
        writerName.setText(boardList.getNickName());
        boardTime.setText(boardList.getWriteTime());
        commentCount.setText(" "+boardList.getCommentCount());

        if(boardList.getRecruitCheck() == 1) {
            ingBtn.setVisibility(View.GONE);
            closeBtn.setVisibility(View.VISIBLE);
        }
        else{
            ingBtn.setVisibility(View.VISIBLE);
            closeBtn.setVisibility(View.GONE);
        }

        return convertView;
    }
    public void addItem(String title, String content, String mustText, String nickName, String writeTime, Integer commentCount, Integer check, String type) {
        boardList item = new boardList();

        item.setWriteTitle(title);
        item.setWriteContent(content);
        item.setMustText(mustText);
        item.setNickName(nickName);
        item.setWriteTime(writeTime);
        item.setCommentCount(commentCount);
        item.setRecruitCheck(check);
        item.setWhatType(type);

        board.add(0,item);
    }


}
