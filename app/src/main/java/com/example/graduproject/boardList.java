package com.example.graduproject;



public class boardList {

    private String whatType;
    private String mustText;
    private String writeTime;
    private String writeTitle;
    private String writeContent;
    private String nickName;
    private int commentCount;
    private int recruitCheck;

    public String getWhatType() {
        return whatType;
    }

    public void setWhatType(String whatType) {
        this.whatType = whatType;
    }

    public int getRecruitCheck() {
        return recruitCheck;
    }

    public void setRecruitCheck(int recruitCheck) {
        this.recruitCheck = recruitCheck;
    }

    public String getWriteContent() {
        return writeContent;
    }

    public void setWriteContent(String writeContent) {
        this.writeContent = writeContent;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
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
