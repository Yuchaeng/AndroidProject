package com.example.graduproject;



public class boardList {

    private String mustText;
    private String writeTime;
    private String writeTitle;
    private String writeContent;
    private String nickName;
    private int commentCount;


    public String getWriteContent() {
        return writeContent;
    }

    public void setWriteContent(String writeContent) {
        this.writeContent = writeContent;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getMustText() {
        return mustText;
    }

    public void setMustText(String mustText) {
        this.mustText = mustText;
    }

    public String getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(String writeTime) {
        this.writeTime = writeTime;
    }

    public String getWriteTitle() {
        return writeTitle;
    }

    public void setWriteTitle(String writeTitle) {
        this.writeTitle = writeTitle;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

}
