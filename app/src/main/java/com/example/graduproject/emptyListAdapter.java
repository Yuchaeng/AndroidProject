package com.example.graduproject;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;


 public class emptyListAdapter extends BaseAdapter {



    private TextView content;
    private ImageButton closeBtn;

    private ArrayList<emptyTimeList> emptyTimeLists = new ArrayList<emptyTimeList>();
    @Override
    public int getCount() {
        return emptyTimeLists.size();
    }

    @Override
    public Object getItem(int position) {
        return emptyTimeLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final int pos = position;
        final Context context = viewGroup.getContext();

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.emptytime_list,viewGroup,false);
        }

        content = (TextView) view.findViewById(R.id.content);
        closeBtn = (ImageButton) view.findViewById(R.id.closeBtn);

        emptyTimeList emptyTimeList = emptyTimeLists.get(position);

        content.setText(emptyTimeList.getText());


        return view;
    }

    public void addItem(String content) {
        emptyTimeList item = new emptyTimeList();

        item.setText(content);

        emptyTimeLists.add(item);
    }
    public ArrayList<emptyTimeList> getItemList() {
        return emptyTimeLists;
    }
    public void deleteItem(int position) {
        emptyTimeLists.remove(position);
    }

}
