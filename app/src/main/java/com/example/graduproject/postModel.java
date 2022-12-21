package com.example.graduproject;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class postModel {
    String boardName;
    String writerUid;
    String writeTime;
    String writerName;

    String mustKeep;
    String postTitle;
    String postContent;
    int commentCount;
    boolean recruit;

    public postModel() {}

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getWriterUid() {
        return writerUid;
    }

    public void setWriterUid(String writerUid) {
        this.writerUid = writerUid;
    }

    public String getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(String writeTime) {
        this.writeTime = writeTime;
    }

    public String getMustKeep() {
        return mustKeep;
    }

    public void setMustKeep(String mustKeep) {
        this.mustKeep = mustKeep;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public boolean isRecruit() {
        return recruit;
    }

    public void setRecruit(boolean recruit) {
        this.recruit = recruit;
    }

//    public postModel(String boardName, String writerUid, String writeTime,
//                     String mustKeep, String postTitle, String postContent, boolean recruit) {
//        this.boardName = boardName;
//        this.writerUid = writerUid;
//
//        this.writeTime = writeTime;
//        this.mustKeep = mustKeep;
//        this.postTitle = postTitle;
//        this.postContent = postContent;
//        this.recruit = recruit;
//    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("boardName",boardName);
        result.put("writerUid",writerUid);

        result.put("writeTime",writeTime);
        result.put("mustKeep",mustKeep);
        result.put("postTitle",postTitle);
        result.put("postContent",postContent);
        result.put("recruit",recruit);

        return result;
    }
}
