package com.example.graduproject;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class commentAdapter extends BaseAdapter {
    ArrayList<commentList> comments = new ArrayList<commentList>();
    TextView commentMe;

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.comment_layout,viewGroup,false);
        }

        CircleImageView commentImage = view.findViewById(R.id.commentImage);
        TextView commentWriter = view.findViewById(R.id.commentWriter);
        commentMe = view.findViewById(R.id.commentMe);
        TextView commentContent = view.findViewById(R.id.commentContent);
        TextView commentTime = view.findViewById(R.id.commentTime);

        commentList commentList = comments.get(position);

        if(commentList.getImageUri()==null) {
            Glide.with(view).load(R.drawable.no_profile_image).into(commentImage);
        }
        else{
            Glide.with(view).load(Uri.parse(commentList.getImageUri())).into(commentImage);
        }

        commentWriter.setText(commentList.getCommentWriter());
        commentContent.setText(commentList.getCommentContent());
        commentTime.setText(commentList.getCommentTime());

        if(commentList.getCheck()==0) {
            commentMe.setVisibility(View.VISIBLE);
        }

        return view;
    }
    public void addItem(String imageUri, String commentWriter, String commentContent, String commentTime,int check) {
        commentList item = new commentList();

        item.setImageUri(imageUri);
        item.setCommentWriter(commentWriter);
        item.setCommentContent(commentContent);
        item.setCommentTime(commentTime);
        item.setCheck(check);


        comments.add(item);

    }


}
